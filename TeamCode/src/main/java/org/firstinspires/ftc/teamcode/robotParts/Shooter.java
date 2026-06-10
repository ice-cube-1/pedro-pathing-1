package org.firstinspires.ftc.teamcode.robotParts;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.qualcomm.robotcore.util.ElapsedTime;

import static org.firstinspires.ftc.teamcode.robotParts.RobotConstants.*;

import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.toDegrees;
import static java.lang.Math.toRadians;

import androidx.annotation.NonNull;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

@Configurable
public class Shooter {
    private final Limelight limelight;
    private final ServoImplEx[] turret = new ServoImplEx[2];
    private final Servo hoodAngle;
    private final DcMotorEx[] motors;
    private final ElapsedTime timer = new ElapsedTime();
    private TurretState turretState = TurretState.WRAPPING;
    private boolean shooterOn = false;
    private int targetV = SHOOTER_IDLE_VELOCITY;
    private double power = 0.0;
    private double nextPos = 0.0;
    public boolean atSpeed = true;
    private double turretMin = -TURRET_ZERO_DEG;
    private double turretMax = TURRET_MAX_DEGREES - TURRET_ZERO_DEG;
    private double gotoPos = turretMin;
    public boolean paused = false;
    public ShootMode shootMode;
    public float distanceOffset;

    public Shooter(HardwareMap hardwareMap, ShootMode shootMode) {
        limelight = new Limelight(hardwareMap);
        this.shootMode = shootMode;
        turret[0] = hardwareMap.get(ServoImplEx.class, "t1");
        turret[0].setPosition(0.5);
        turret[1] = hardwareMap.get(ServoImplEx.class, "t2");
        turret[1].setPosition(0.5);
        hoodAngle = hardwareMap.get(Servo.class, "hood");
        hoodAngle.setPosition(HOOD_ANGLE);
        motors = new DcMotorEx[]{
                init_motor("m1", hardwareMap, DcMotorSimple.Direction.REVERSE),
                init_motor("m2", hardwareMap, DcMotorSimple.Direction.FORWARD)
        };
    }

    public boolean canShoot() {
        return (abs(limelight.lastAngle) < 4 && turretState == TurretState.DETECTED) || paused;
    }

    private int getTargetVelocity() {
        if (Debug.debugMode) {
            hoodAngle.setPosition(Debug.hoodAngle);
            return Debug.shooterVelocity;
        }
        hoodAngle.setPosition(shootMode.hoodPos);
        if (shooterOn) {
            return (int) (177.92 * (limelight.lastDist + shootMode.distanceOffset + distanceOffset) + 937.31);
        }
        return SHOOTER_IDLE_VELOCITY;
    }
    public static double calculateAngleToPose(Pose robot, Pose target) {
        double dx = target.getX() - robot.getX();
        double dy = target.getY() - robot.getY();
        double rawDelta = Math.atan2(dy, dx) - robot.getHeading();
        return -Math.atan2(Math.sin(rawDelta), Math.cos(rawDelta));
    }
    public void moveTurret(Follower follower) {
        double angle = calculateAngleToPose(follower.getPose(), new Pose(RobotState.ALLIANCE_COLOUR.mirrorX(12), 132,0));
        limelight.update(follower);
        switch (turretState) {
            case DETECTED:
                if (timer.milliseconds() > limelight.mostRecent + 500) {
                    gotoPos = (toDegrees(angle) - getTurretAngle() > 0) ? turretMin : turretMax;
                    turretState = TurretState.WRAPPING;
                } else if (abs(limelight.lastAngle) > 1.0) {
                    nextPos = limelight.lastAngle * shootMode.detectedKP + getTurretAngle();
                }
                break;
            case WRAPPING:
                if (timer.milliseconds() < limelight.mostRecent + 500) {
                    turretState = TurretState.DETECTED;
                } else {
                    nextPos = (gotoPos - getTurretAngle() > 0 ? shootMode.wrappingStep : -shootMode.wrappingStep) + getTurretAngle();
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
    public void pauseTurret(){ paused = !paused; }
    private double getTurretAngle() {
        return ((turret[0].getPosition() + turret[1].getPosition()) / 2.0) * TURRET_MAX_DEGREES - TURRET_ZERO_DEG;
    }

    private void setTurretPos(double pos) {
        double clamped = Math.min(1.0, Math.max((pos + TURRET_ZERO_DEG) / TURRET_MAX_DEGREES, 0.0));
        turret[0].setPosition(clamped);
        turret[1].setPosition(clamped);
    }

    public void setSubRange(double min, double max) {
        turretMin = min;
        turretMax = max;
    }

    public void reverseTurret() {
        if (gotoPos == turretMin) {
            gotoPos = turretMax;
        } else gotoPos = turretMin;
    }

    public void toggleFromFar() {
        if (shootMode == ShootMode.NEAR) { shootMode = ShootMode.FAR; }
        else shootMode = ShootMode.NEAR;
    }

    public void turnOnShooter() {
        targetV = (int) Arrays.stream(motors)
                .filter(i -> i.getVelocity() != 0)
                .mapToDouble(DcMotorEx::getVelocity)
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

    public void spin() {
        targetV = getTargetVelocity();
        double currentV = Arrays.stream(motors)
                .filter(i -> i.getVelocity() != 0)
                .mapToDouble(DcMotorEx::getVelocity)
                .average()
                .orElse(0.0);
        double errorV = targetV - currentV;
        power = Math.max(0.0, Math.min(1.0, KP_SHOOTER * errorV + targetV * K_FF + KFF_INTERCEPT));
        for (DcMotorEx m : motors) { m.setPower(power); }
        atSpeed = abs(errorV) < 40;
    }
    @NonNull
    public String toString() {
        return String.format(Locale.UK, "---TURRET---\nCurrently %s\nAt position %.3f, going to %.3f", turretState, getTurretAngle(), nextPos) +
                String.format(Locale.UK, "---SHOOTER---\nCurrently %s, mode: %s\nTarget power: %.0f, encoder readings: (%.0f, %.0f)\nOffset: %.3f(mode) + %.3f(manual)",
                        shooterOn ? "on" : "off", shootMode, power, motors[0].getVelocity(), motors[1].getVelocity(), shootMode.distanceOffset, distanceOffset) + limelight +"\n";
    }
    public DcMotorEx init_motor(String name, HardwareMap hardwareMap, DcMotorSimple.Direction direction) {
        DcMotorEx drive = hardwareMap.get(DcMotorEx.class, name);
        drive.setDirection(direction);
        drive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        drive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        return drive;
    }
}