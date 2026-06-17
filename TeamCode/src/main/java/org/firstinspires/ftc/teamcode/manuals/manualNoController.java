package org.firstinspires.ftc.teamcode.manuals;

import static org.firstinspires.ftc.teamcode.pedroPathing.Constants.createFollower;

import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.robotParts.Debug;
import org.firstinspires.ftc.teamcode.robotParts.RobotConstants;
import org.firstinspires.ftc.teamcode.robotParts.Shooter;
import org.firstinspires.ftc.teamcode.robotParts.TransferIntake;
import org.firstinspires.ftc.teamcode.robotParts.RobotState;

@Configurable
@TeleOp(name = "MANUAL - NO CONTROLLER")
public class manualNoController extends LinearOpMode {
    public static double powerX = 0;
    public static double powerY = 0;
    public static double powerYaw = 0;
    public static double driveTimeLeft = 0;
    public static boolean shootFullTest = false;
    public static boolean turretTest = false;
    public static double transferPower = 0;
    public static double intakePower = 0;


    private final Pose position = RobotState.AUTO_END_POSE;
    private final RobotConstants.Alliance alliance = RobotState.ALLIANCE_COLOUR;
    private Shooter shooter;
    private TransferIntake transferIntake;
    private Follower follower;

    private final ElapsedTime timer = new ElapsedTime();
    private final ElapsedTime driveTime = new ElapsedTime();
    TelemetryManager panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();


    @Override
    public void runOpMode() {
        panelsTelemetry.addLine("""
                -----TESTS-----
                DRIVETRAIN TEST
                change power x,y,z for forward, strafe, turn, and timer for how long to do it for
                when the timer is updated the action will start, timer is in MILLISECONDS
                this is robot centric, but just a boolean, so change if you want
                SHOOTER TEST
                change debug mode to true (should be under debug) to test the shooter - it's off otherwise in this mode for noise reasons
                TRANSFER INTAKE TEST
                set transfer/intake to true/false
                TURRET TEST
                once you're sure the turret gears are aligned set this to true and put an apriltag in front of it on a phone screen or smth
                FULL SHOOT TEST
                set this to true and it should just do it once
                """);
        panelsTelemetry.update();
        follower = createFollower(hardwareMap);
        follower.setStartingPose(Debug.debugMode ? Debug.startPose() : position);
        follower.startTeleopDrive();
        shooter = new Shooter(hardwareMap, RobotConstants.ShootMode.NEAR);
        transferIntake = new TransferIntake(hardwareMap);
        while (!isStarted() && !isStopRequested()) { updateTelemetry(); }
        while (opModeIsActive()) {
            follower.update();
            if (driveTimeLeft > 0) {
                driveTimeLeft -= driveTime.milliseconds();
                driveTime.reset();
                follower.setTeleOpDrive(-powerY, -powerX, -powerYaw * 0.7, true);
            } else {
                follower.setTeleOpDrive(0,0,0);
            }
            transferIntake.transfer.setPower(transferPower);
            transferIntake.intake.setPower(intakePower);

            if (turretTest) {shooter.moveTurret(follower);}

            if (shootFullTest) {
                transferIntake.prepShooter();
                timer.reset();
                while (opModeIsActive() && timer.milliseconds() < 500) { updateAll(); }
                timer.reset();
                while (opModeIsActive() && timer.milliseconds() < 4000) {
                    updateAll();
                    if (shooter.atSpeed) {
                        transferIntake.shoot(true);
                        break;
                    }
                }
                timer.reset();
                while (opModeIsActive() && timer.milliseconds() < 2000) {updateAll();}
                transferIntake.shoot(false);
            }
            shootFullTest = false;
        }
    }


    public void updateTelemetry() {
        telemetry.addLine(alliance.toString());
        telemetry.addLine(shooter.toString());
        telemetry.addLine(transferIntake.toString());
        telemetry.addLine(follower.getPose().toString());
        telemetry.update();
    }
    private void updateAll() {
        shooter.moveTurret(follower);
        shooter.spin();
        transferIntake.update();
        follower.update();
        updateTelemetry();
    }
}
