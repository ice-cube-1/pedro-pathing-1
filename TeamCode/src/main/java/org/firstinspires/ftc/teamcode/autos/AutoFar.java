package org.firstinspires.ftc.teamcode.autos;

import static org.firstinspires.ftc.teamcode.robotParts.RobotConstants.ROBOT_LENGTH_CM;
import static org.firstinspires.ftc.teamcode.robotParts.RobotConstants.ROBOT_WIDTH_CM;
import static java.lang.Double.max;
import static java.lang.Math.min;
import static java.lang.Math.toRadians;

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

import java.util.ArrayList;

public class AutoFar extends LinearOpMode {
    private Follower follower;
    private Shooter shooter;
    private TransferIntake transferIntake;
    private final ElapsedTime timer = new ElapsedTime();
    private final RobotConstants.Alliance alliance;
    private final int attemptCount;

    public AutoFar(RobotConstants.Alliance alliance, int attemptCount) {
        this.attemptCount = attemptCount;
        this.alliance = alliance;
    }
    public void runOpMode()  {
        ArrayList<Pose> poses = new ArrayList<>();
        // all of these need checking icl
        poses.add(new Pose(alliance.mirrorX(48+ROBOT_WIDTH_CM/(2.54*2)), ROBOT_LENGTH_CM/(2.54),toRadians(270))); // start
        poses.add(new Pose(alliance.mirrorX(48+ROBOT_WIDTH_CM/(2.54*2)),ROBOT_LENGTH_CM/(2.54)+6, toRadians(270))); // shoot here
        poses.add(new Pose(alliance.mirrorX(60.0),84.0-24.0-20.0,toRadians(alliance.mirrorAngle(90)))); // start intaking here
        poses.add(new Pose(alliance.mirrorX(20.0), 84.0-24.0-20.0, toRadians(alliance.mirrorAngle(90)))); // stop intaking here
        poses.add(new Pose(alliance.mirrorX(48+ROBOT_WIDTH_CM/(2.54*2)), ROBOT_LENGTH_CM/(2.54)+6,toRadians(270))); // shoot here
        poses.add(new Pose(alliance.mirrorX(9.0),84.0-24.0-6.0, toRadians(270))); // start getting 3 from edge
        poses.add(new Pose(alliance.mirrorX(9.0),ROBOT_LENGTH_CM/(2.54), toRadians(270))); // go into far corner
        poses.add(new Pose(alliance.mirrorX(48+ROBOT_WIDTH_CM/(2.54*2)),ROBOT_LENGTH_CM/(2.54)+6, toRadians(270))); // shoot here
        poses.add(new Pose(alliance.mirrorX(48+ROBOT_WIDTH_CM/(2.54*2)), ROBOT_LENGTH_CM/(2.54)+24,toRadians(270))); // park
        ArrayList<Path> paths = new ArrayList<>();
        for (int i = 1; i< poses.size(); i++) {
            Path path = new Path(new BezierLine(poses.get(i - 1), poses.get(i)));
            path.setLinearHeadingInterpolation(poses.get(i - 1).getHeading(), poses.get(i).getHeading());
            paths.add(path);
        }
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(poses.get(0));
        shooter = new Shooter(hardwareMap, alliance, RobotConstants.ShootMode.FAR, false); // also hopefully make true
        shooter.setSubRange(min(alliance.direction*90.0, 0.0), max(alliance.direction*90.0, 0.0));
        transferIntake = new TransferIntake(hardwareMap);
        while (!isStarted() && !isStopRequested()) { updateTelemetry(); }
        transferIntake.prepShooter();
        shooter.turnOnShooter();
        follow(paths.get(0));
        shoot();
        for (int i = 0; i<attemptCount; i++) {
            follow(paths.get(i*3+1));
            transferIntake.intake(1.0);
            follow(paths.get(i*3+2));
            transferIntake.intake(0.0);
            follow(paths.get(i*3+3));
            transferIntake.prepShooter();
            shoot();
        }
        follow(paths.get(7));
        RobotState.ALLIANCE_COLOUR = alliance;
        RobotState.AUTO_END_POSE = follower.getPose();
    }
    private void follow(Path path) {
        follower.followPath(path);
        while (opModeIsActive() && follower.isBusy()) {updateAll();}
    }
    private void shoot() {
        shooter.relocaliseLL(); // delete if behaving weirdly
        timer.reset();
        while (opModeIsActive() && timer.milliseconds() < 3000) {
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
        telemetry.addLine(alliance.toString());
        telemetry.addLine(RobotState.displayNumToAttempt(attemptCount, RobotConstants.ShootMode.FAR));
        telemetry.addLine(shooter.toString());
        telemetry.addLine(transferIntake.toString());
        telemetry.addLine(follower.getPose().toString());
        telemetry.update();
    }
}