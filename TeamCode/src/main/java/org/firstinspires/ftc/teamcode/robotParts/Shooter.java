package org.firstinspires.ftc.teamcode.robotParts;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.robotcore.external.navigation.Position;

import static org.firstinspires.ftc.teamcode.robotParts.RobotConstants.*;

import java.util.Arrays;

public class Shooter {

    public enum TurretState {
        DETECTED,
        WRAPPING
    }

    private final Limelight3A limelight;

    private final ServoImplEx[] turret = new ServoImplEx[2];
    private final Servo hoodAngle;

    private final Wheel[] motors;

    private double lastDist = 0.0;
    private double lastAngle = 0.0;

    private double power = 0.0;
    private double mostRecent = -1000.0;

    private final ElapsedTime timer = new ElapsedTime();

    private TurretState turretState = TurretState.WRAPPING;

    private boolean shooterOn = false;
    private int targetV = SHOOTER_IDLE_VELOCITY;

    private double nextPos = 0.0;

    public boolean atSpeed = true;
    public double distanceOffset = 0.0;

    private double turretMin = -TURRET_ZERO_DEG;
    private double turretMax = TURRET_MAX_DEGREES - TURRET_ZERO_DEG;

    private double gotoPos = turretMin;

    private final int tagID;
    public boolean paused = false;

    public Shooter(HardwareMap hardwareMap, int tagID) {

        this.tagID = tagID;

        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.setPollRateHz(100);
        limelight.start();
        limelight.pipelineSwitch(0);

        turret[0] = hardwareMap.get(ServoImplEx.class, "t1");
        turret[0].setPosition(0.5);

        turret[1] = hardwareMap.get(ServoImplEx.class, "t2");
        turret[1].setPosition(0.5);

        hoodAngle = hardwareMap.get(Servo.class, "hood");
        hoodAngle.setPosition(HOOD_ANGLE);

        motors = new Wheel[]{
                new Wheel("m1", hardwareMap, DcMotorSimple.Direction.REVERSE),
                new Wheel("m2", hardwareMap, DcMotorSimple.Direction.FORWARD)
        };
    }

    public boolean canShoot() {
        return Math.abs(lastAngle) < 4 && turretState == TurretState.DETECTED || paused;
    }

    private int getTargetVelocity(Boolean far) {
        if (far) hoodAngle.setPosition(0.85);
        else hoodAngle.setPosition(1.0);
        if (shooterOn) {
            if (far) {
                return (int) (177.92 * (lastDist+0.8) + 937.31);
            }
            return (int) (177.92 * lastDist + 937.31);
        } else {
            return SHOOTER_IDLE_VELOCITY;
        }
    }

    private void lookForTag() {
        LLResult result = limelight.getLatestResult();

        if (result == null) return;
        if (!result.isValid()) return;

        for (LLResultTypes.FiducialResult tag : result.getFiducialResults()) {
            if (tag.getFiducialId() == tagID) {
                mostRecent = timer.milliseconds() - result.getStaleness();
                Pose3D pose = tag.getTargetPoseCameraSpace();
                Position pos = pose.getPosition();
                double yaw = pose.getOrientation().getYaw(AngleUnit.RADIANS);
                double targetX = pos.x - OFFSET * Math.sin(yaw);
                double targetZ = pos.z - OFFSET * Math.cos(yaw);
                lastDist = Math.sqrt(targetX * targetX + targetZ * targetZ) + distanceOffset;
                lastAngle = Math.toDegrees(Math.atan2(targetX, targetZ));
            }
        }
    }

    public void moveTurret(double farZoneMultiplier) {
        lookForTag();
        switch (turretState) {
            case DETECTED:
                if (timer.milliseconds() > mostRecent + 500) {
                    turretState = TurretState.WRAPPING;
                } else if (Math.abs(lastAngle) > 1.0) {
                    if (farZoneMultiplier == 1) {
                        nextPos = lastAngle * 0.02 + getTurretAngle();
                    } else {
                        nextPos = lastAngle * 0.015 + getTurretAngle();
                    }
                }
                break;
            case WRAPPING:
                if (timer.milliseconds() < mostRecent + 500) {
                    turretState = TurretState.DETECTED;
                } else {
                    nextPos = (gotoPos - getTurretAngle() > 0 ? TURRET_STEP*farZoneMultiplier : -TURRET_STEP*farZoneMultiplier)
                            + getTurretAngle();
                }
                break;
        }
        if (!paused) {
            if (nextPos >= turretMax) {
                gotoPos = turretMin;
            } else if (nextPos <= turretMin) {
                gotoPos = turretMax;
            } else {
                setTurretPos(nextPos);
            }
        }
    }

    public void reverseGoto() {
        gotoPos = (gotoPos > 0) ? turretMin : turretMax;
    }
    public void pauseTurret(){
        paused = !paused;
    }
    private double getTurretAngle() {
        return ((turret[0].getPosition() + turret[1].getPosition()) / 2.0)
                * TURRET_MAX_DEGREES - TURRET_ZERO_DEG;
    }

    private void setTurretPos(double pos) {
        double clamped = Math.min(1.0,
                Math.max((pos + TURRET_ZERO_DEG) / TURRET_MAX_DEGREES, 0.0));

        turret[0].setPosition(clamped);
        turret[1].setPosition(clamped);
    }

    public void setSubRange(double min, double max) {
        turretMin = min;
        turretMax = max;
    }

    public void turnOnShooter() {
        targetV = (int) Arrays.stream(motors)
                .mapToDouble(Wheel::getVelocity)
                .average()
                .orElse(0.0);

        shooterOn = true;
    }

    public void turnOffShooter() {
        shooterOn = false;
        targetV = SHOOTER_IDLE_VELOCITY;

        motors[0].setPower(0.0);
        motors[1].setPower(0.0);
    }

    public void spin(Boolean far) {
        targetV = getTargetVelocity(far);
        double currentV = Arrays.stream(motors)
                .filter(i -> i.getVelocity() != 0)
                .mapToDouble(Wheel::getVelocity)
                .average()
                .orElse(0.0);
        double errorV = targetV - currentV;

        power = Math.max(0.0,
                Math.min(1.0,
                        KP_SHOOTER * errorV + targetV * K_FF + KFF_INTERCEPT));

        for (Wheel m : motors) {m.setPower(power);}

        atSpeed = Math.abs(errorV) < 40;
    }

    public String getData() {
        return "Turret state: " + turretState +
                ", last tag range = " + lastDist +
                ", bearing = " + lastAngle + "\n" +
                "Turret current position " + getTurretAngle() +
                ", going to " + gotoPos + "\n" +
                "Shooter target: " + targetV +
                "power " + power + "\n" +
                "actually going at " +
                motors[0].getVelocity() + ", " + motors[1].getVelocity() + "\n" +
                "OFFSET - " + distanceOffset + " m\nservo position "+hoodAngle.getPosition();
    }
}