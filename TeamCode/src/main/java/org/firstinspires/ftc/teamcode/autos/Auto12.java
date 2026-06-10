package org.firstinspires.ftc.teamcode.autos;

import static org.firstinspires.ftc.teamcode.robotParts.RobotConstants.ROBOT_LENGTH_CM;
import static org.firstinspires.ftc.teamcode.robotParts.RobotConstants.ROBOT_WIDTH_CM;
import static org.firstinspires.ftc.teamcode.robotParts.RobotState.ALLIANCE_COLOUR;
import static java.lang.Double.max;
import static java.lang.Math.min;
import static java.lang.Math.toRadians;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.robotParts.RobotConstants;
import org.firstinspires.ftc.teamcode.robotParts.RobotState;
import org.firstinspires.ftc.teamcode.robotParts.Shooter;
import org.firstinspires.ftc.teamcode.robotParts.TransferIntake;

@Configurable
public class Auto12 extends LinearOpMode {
    private Follower follower;
    private Shooter shooter;
    private TransferIntake transferIntake;
    private final ElapsedTime timer = new ElapsedTime();
    private final int attempt;
    public static double shooter_y = 80.0;
    public static double park_y = 105.0 - 12.0;
    public Auto12(int numToAttempt) {
        this.attempt = numToAttempt;
    }
    private Pose curPos;

    private void move(Pose newPos){
        Path path = new Path(new BezierLine(curPos,newPos));
        path.setLinearHeadingInterpolation(curPos.getHeading(), newPos.getHeading());
        follow(path);
        curPos=newPos;
    }
    @Override
    public void runOpMode() {
        double[][] intakePositions = new double[][] {
                // FAR IN point of each spike
                {ALLIANCE_COLOUR.mirrorX(24.0), 84.0-15.0, toRadians(ALLIANCE_COLOUR.mirrorAngle(90))},
                {ALLIANCE_COLOUR.mirrorX(20.0), 60.0-15.0, toRadians(ALLIANCE_COLOUR.mirrorAngle(90))},
                {ALLIANCE_COLOUR.mirrorX(20.0), 36.0-15.0, toRadians(ALLIANCE_COLOUR.mirrorAngle(90))}
        };
        Pose shootPos = new Pose(ALLIANCE_COLOUR.mirrorX(60.0), shooter_y, toRadians(270));
        Pose parkPos = new Pose(ALLIANCE_COLOUR.mirrorX(60.0), park_y, toRadians(270));
        curPos = new Pose(ALLIANCE_COLOUR.mirrorX(24+ROBOT_WIDTH_CM/(2.54*2)),144.0-ROBOT_LENGTH_CM/(2.54), toRadians(270));
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(curPos);
        shooter = new Shooter(hardwareMap, RobotConstants.ShootMode.NEAR);
        shooter.setSubRange(min(ALLIANCE_COLOUR.direction*90.0, 0.0), max(ALLIANCE_COLOUR.direction*90.0, 0.0));
        transferIntake = new TransferIntake(hardwareMap);
        while (!isStarted() && !isStopRequested()) { updateTelemetry(); }
        shooter.turnOnShooter();
        transferIntake.prepShooter();
        move(shootPos);
        shoot();
        for (int i=0;i<attempt;i++){
            move(new Pose(ALLIANCE_COLOUR.mirrorX(60.0), intakePositions[i][1], intakePositions[i][2]));
            transferIntake.intake(1.0);
            move(new Pose(intakePositions[i][0], intakePositions[i][1],intakePositions[i][2]));
            transferIntake.intake(0.0);
            transferIntake.prepShooter();
            if (i == attempt-1) { move(parkPos); }
            else { move(shootPos); }
            shoot();
        }
        move(parkPos);
        RobotState.AUTO_END_POSE = follower.getPose();
    }
    private void follow(Path path) {
        timer.reset();
        follower.followPath(path);
        while (opModeIsActive() && follower.isBusy() && timer.milliseconds() < 4000) {updateAll();}
    }
    private void shoot() {
        timer.reset();
        while (opModeIsActive() && timer.milliseconds() < 4000) {
            updateAll();
            if (shooter.atSpeed && shooter.canShoot()) {
                transferIntake.shoot(true);
                break;
            }
        }
        timer.reset();
        while (opModeIsActive() && timer.milliseconds() < 2000) {updateAll();}
        transferIntake.shoot(false);
    }
    private void updateAll() {
        shooter.moveTurret(follower);
        shooter.spin();
        transferIntake.update();
        follower.update();
        updateTelemetry();
    }
    public void updateTelemetry() {
        telemetry.addLine(ALLIANCE_COLOUR.toString());
        telemetry.addLine(RobotState.displayNumToAttempt(attempt, RobotConstants.ShootMode.NEAR));
        telemetry.addLine(shooter.toString());
        telemetry.addLine(transferIntake.toString());
        telemetry.addLine(follower.getPose().toString());
        telemetry.update();
    }
}