package org.firstinspires.ftc.teamcode;

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
import org.firstinspires.ftc.teamcode.robotParts.Shooter;
import org.firstinspires.ftc.teamcode.robotParts.TransferIntake;

import java.util.ArrayList;
import java.util.Arrays;

public class AutoFar extends LinearOpMode {
    private Follower follower;
    private Shooter shooter;
    private TransferIntake transferIntake;
    private final ElapsedTime timer = new ElapsedTime();
    private final double offset;
    private final double direction;
    private final int tagID;

    public AutoFar(double offset, double direction, int tagID) {
        this.offset = offset;
        this.direction = direction;
        this.tagID = tagID;
    }
    public void runOpMode()  {
        ArrayList<Pose> poses = new ArrayList<>();
        poses.add(new Pose(offset - direction*(48+ROBOT_WIDTH_CM/(2.54*2)), ROBOT_LENGTH_CM/(2.54),toRadians(270)));
        poses.add(new Pose(offset - direction*(48+ROBOT_WIDTH_CM/(2.54*2)),ROBOT_LENGTH_CM/(2.54)+6, toRadians(270))); // shoot here
        poses.add(new Pose(offset-direction*60.0,84.0-24.0-18.0,toRadians(90 - 90 * direction))); // start intaking here
        poses.add(new Pose(offset-direction*26.0, 84.0-24.0-18.0, 90 - 90*direction)); // stop intaking here
        poses.add(new Pose(offset - direction*(48+ROBOT_WIDTH_CM/(2.54*2)), ROBOT_LENGTH_CM/(2.54)+6,toRadians(270))); // shoot here
        poses.add(new Pose(offset - direction*(48+ROBOT_WIDTH_CM/(2.54*2)), ROBOT_LENGTH_CM/(2.54)+24,toRadians(270))); // park
        ArrayList<Path> paths = new ArrayList<>();
        for (int i = 1; i< poses.size(); i++) {
            Path path = new Path(new BezierLine(poses.get(i - 1), poses.get(i)));
            path.setLinearHeadingInterpolation(poses.get(i - 1).getHeading(), poses.get(i).getHeading());
            paths.add(path);
        }
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(poses.get(0));
        shooter = new Shooter(hardwareMap, tagID);
        shooter.setSubRange(min(direction*90.0, 0.0), max(direction*90.0, 0.0));
        transferIntake = new TransferIntake(hardwareMap);
        waitForStart();
        transferIntake.prepShooter();
        shooter.turnOnShooter();
        follow(paths.get(0));
        shoot();
        follow(paths.get(1));
        transferIntake.intake(1.0);
        follow(paths.get(2));
        transferIntake.intake(0.0);
        follow(paths.get(3));
        transferIntake.prepShooter();
        shoot();
        follow(paths.get(4));
    }
    private void follow(Path path) {
        follower.followPath(path);
        while (opModeIsActive() && follower.isBusy()) {updateAll();}
    }
    private void shoot() {
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
        shooter.moveTurret(1.0);
        shooter.spin(true);
        transferIntake.update();
        follower.update();
        telemetry.addLine(shooter.getData());
        telemetry.addLine(transferIntake.getData());
        telemetry.addLine(Arrays.toString(follower.debug()));
        telemetry.update();
    }
}