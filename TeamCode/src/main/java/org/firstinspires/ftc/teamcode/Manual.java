package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.pedroPathing.Constants.createFollower;
import static org.firstinspires.ftc.teamcode.robotParts.RobotConstants.FAR_ZONE_MULTIPLIER;
import static org.firstinspires.ftc.teamcode.robotParts.RobotConstants.MANUAL_MULTIPLIER;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.robotParts.Shooter;
import org.firstinspires.ftc.teamcode.robotParts.TransferIntake;

public abstract class Manual extends LinearOpMode {

    enum RobotState {
        INTAKE,
        SHOOTER_SPIN_UP,
        SHOOTER_ON
    }

    private final double orientation;
    private final int tag;

    private Shooter shooter;
    private TransferIntake transferIntake;
    private Follower follower;

    private RobotState robotState = RobotState.INTAKE;

    private double timeToEnd = 0.0;

    private final ElapsedTime timer = new ElapsedTime();
    private final ElapsedTime atSpeed = new ElapsedTime();
    private final ElapsedTime dpadTimer = new ElapsedTime();
    private final ElapsedTime end = new ElapsedTime();
    private boolean shootFromFar = false;

    public Manual(double orientation, int tag) {
        this.orientation = orientation;
        this.tag = tag;
    }

    @Override
    public void runOpMode() {
        follower = createFollower(hardwareMap);
        follower.setStartingPose(new Pose(0,0,orientation));
        waitForStart();
        follower.startTeleopDrive();
        shooter = new Shooter(hardwareMap, tag);
        transferIntake = new TransferIntake(hardwareMap);
        end.reset();
        while (opModeIsActive()) {
            if (end.seconds() > 150) break;
            follower.update();
            follower.setTeleOpDrive(
                    -gamepad1.left_stick_y * MANUAL_MULTIPLIER,
                    -gamepad1.left_stick_x * MANUAL_MULTIPLIER,
                    -gamepad1.right_stick_x * MANUAL_MULTIPLIER * 0.7,
                    false
            );
            if (gamepad1.left_bumper && timer.milliseconds() > timeToEnd) {
                robotState = RobotState.SHOOTER_SPIN_UP;
                transferIntake.prepShooter();
                shooter.turnOnShooter();
                timeToEnd = timer.milliseconds() + 500;
            }
            if (gamepad1.right_bumper && timer.milliseconds() > timeToEnd) {
                robotState = RobotState.INTAKE;
                transferIntake.shoot(false);
                shooter.turnOffShooter();
                timeToEnd = timer.milliseconds() + 200;
            }
            if (gamepad1.dpad_up && dpadTimer.milliseconds() > 200) {
                shooter.distanceOffset += 0.1;
                dpadTimer.reset();
            }
            if (gamepad1.dpad_down && dpadTimer.milliseconds() > 200) {
                shooter.distanceOffset -= 0.1;
                dpadTimer.reset();
            }
            if (gamepad1.a && dpadTimer.milliseconds() > 200) {
                shooter.reverseGoto();
                dpadTimer.reset();
            }
            if (gamepad1.x && dpadTimer.milliseconds() > 200) {
                shootFromFar = !shootFromFar;
                dpadTimer.reset();
            }
            if (gamepad1.y && dpadTimer.milliseconds() > 200) { //pause turret movement
                shooter.pauseTurret();
                dpadTimer.reset();
            }
            if (robotState == RobotState.INTAKE) {
                transferIntake.intake((double) (gamepad1.left_trigger - gamepad1.right_trigger));
            }
            if (!shooter.atSpeed) {
                atSpeed.reset();
            }
            if (atSpeed.milliseconds() > 500
                    && robotState == RobotState.SHOOTER_SPIN_UP
                    && shooter.canShoot()
                    && timer.milliseconds() > timeToEnd) {

                robotState = RobotState.SHOOTER_ON;
                transferIntake.shoot(true);
            }
            transferIntake.update();
            shooter.moveTurret(shootFromFar ? FAR_ZONE_MULTIPLIER : 1.0);
            telemetry.addData("shooting from far:", shootFromFar);
            shooter.spin(shootFromFar);
            telemetry.addLine(shooter.getData());
            telemetry.addLine(transferIntake.getData());
            telemetry.update();
        }
    }
}