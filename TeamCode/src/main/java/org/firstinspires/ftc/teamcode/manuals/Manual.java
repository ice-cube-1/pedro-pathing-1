package org.firstinspires.ftc.teamcode.manuals;

import static org.firstinspires.ftc.teamcode.pedroPathing.Constants.createFollower;
import static org.firstinspires.ftc.teamcode.robotParts.RobotConstants.MANUAL_MULTIPLIER;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.robotParts.Debug;
import org.firstinspires.ftc.teamcode.robotParts.RobotConstants;
import org.firstinspires.ftc.teamcode.robotParts.Shooter;
import org.firstinspires.ftc.teamcode.robotParts.TransferIntake;

public abstract class Manual extends LinearOpMode {

    enum RobotState {
        INTAKE,
        SHOOTER_SPIN_UP,
        SHOOTER_ON
    }

    private final Pose position;
    private final RobotConstants.Alliance alliance;
    private final boolean useLoc;

    private Shooter shooter;
    private TransferIntake transferIntake;
    private Follower follower;

    private RobotState robotState = RobotState.INTAKE;

    private double timeToEnd = 0.0;

    private final ElapsedTime timer = new ElapsedTime();
    private final ElapsedTime atSpeed = new ElapsedTime();
    private final ElapsedTime dpadTimer = new ElapsedTime();
    private final ElapsedTime end = new ElapsedTime();

    public Manual(Pose start, RobotConstants.Alliance alliance, boolean useLoc) {
        this.useLoc = useLoc;
        this.position = start;
        this.alliance = alliance;
    }

    @Override
    public void runOpMode() {
        follower = createFollower(hardwareMap);
        follower.setStartingPose(Debug.debugMode ? Debug.startPose() : position);
        follower.startTeleopDrive();
        shooter = new Shooter(hardwareMap, RobotConstants.ShootMode.NEAR);
        transferIntake = new TransferIntake(hardwareMap);
        end.reset();
        while (!isStarted() && !isStopRequested()) { updateTelemetry(); }
        while (opModeIsActive()) {
            if (end.seconds() > 150) break;
            follower.update();
            if (alliance == RobotConstants.Alliance.BLUE) {
                follower.setTeleOpDrive(
                        -gamepad1.left_stick_y * MANUAL_MULTIPLIER,
                        -gamepad1.left_stick_x * MANUAL_MULTIPLIER,
                        -gamepad1.right_stick_x * MANUAL_MULTIPLIER * 0.7,
                        false
                );
            } else {
                follower.setTeleOpDrive(
                        gamepad1.left_stick_y * MANUAL_MULTIPLIER,
                        gamepad1.left_stick_x * MANUAL_MULTIPLIER,
                        -gamepad1.right_stick_x * MANUAL_MULTIPLIER * 0.7,
                        false
                );
            }
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
                shooter.distanceOffset += 0.1F;
                dpadTimer.reset();
            }
            if (gamepad1.dpad_down && dpadTimer.milliseconds() > 200) {
                shooter.distanceOffset -= 0.1F;
                dpadTimer.reset();
            }
            if (gamepad1.a && dpadTimer.milliseconds() > 200) { // reverse turret direction
                shooter.reverseTurret();
                dpadTimer.reset();
            }
            if (gamepad1.x && dpadTimer.milliseconds() > 200) { // switch between near and far zone
                shooter.toggleFromFar();
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
            shooter.moveTurret(follower);
            shooter.spin();
            updateTelemetry();
        }
    }
    public void updateTelemetry() {
        telemetry.addLine(alliance.toString());
        telemetry.addLine(shooter.toString());
        telemetry.addLine(transferIntake.toString());
        telemetry.addLine(follower.getPose().toString());
        telemetry.update();
    }
}