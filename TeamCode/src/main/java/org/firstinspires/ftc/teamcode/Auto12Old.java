package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.robotParts.RobotConstants.ROBOT_LENGTH_CM;
import static org.firstinspires.ftc.teamcode.robotParts.RobotConstants.ROBOT_WIDTH_CM;
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
import org.firstinspires.ftc.teamcode.robotParts.Shooter;
import org.firstinspires.ftc.teamcode.robotParts.TransferIntake;

import java.util.ArrayList;
import java.util.Arrays;
@Configurable
public class Auto12Old extends LinearOpMode {
    private Follower follower;
    private Shooter shooter;
    private TransferIntake transferIntake;
    private final ElapsedTime timer = new ElapsedTime();
    private final double offset;
    private final double direction;
    private final int tagID;
    private final int attempt;
    public static double shooter_y = 90.0;
    public static double[] intakePositions = new double[] {84.0,60.0,36.0};
    public static double intake_far_x = 34.0;
    public Auto12Old(double offset, double direction, int numToAttempt, int tagID) {
        this.offset = offset;
        this.direction = direction;
        this.tagID = tagID;
        this.attempt = numToAttempt;
    }
    @Override
    public void runOpMode() {
        ArrayList<Pose> poses = new ArrayList<>();
        poses.add(new Pose(offset - direction*(24+ROBOT_WIDTH_CM/(2.54*2)),144.0-ROBOT_LENGTH_CM/(2.54), toRadians(270)));
        poses.add(new Pose(offset - direction * 60.0, shooter_y, toRadians(270))); // SHOOT AFTER THIS
        for (int i = 0; i<3; i++) {
            poses.add(new Pose(offset - direction * 60.0, intakePositions[i], toRadians(270)));
            poses.add(new Pose(offset - direction * 60.0, intakePositions[i], toRadians(90*(1-direction)))); // INTAKE AFTER THIS
            poses.add(new Pose(offset - direction * intake_far_x, intakePositions[i],toRadians(90*(1-direction)))); // STOP INTAKING 1 HERE
            poses.add(new Pose(offset - direction * 60.0, shooter_y, toRadians(90*(1-direction))));
            poses.add(new Pose(offset - direction * 60.0, shooter_y, toRadians(270)));
        }
        poses.add(new Pose(offset - direction * 60.0, 60.0, toRadians(90*(1-direction))));
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
        shooter.turnOnShooter();
        transferIntake.prepShooter();
        follow(paths.get(0));
        shoot();
        for (int i = 0; i<attempt; i++) {
            follow(paths.get(i*5+1));
            transferIntake.intake(1.0);
            follow(paths.get(i*5+2));
            follow(paths.get(i*5+3));
            transferIntake.intake(0.0);
            follow(paths.get(i*5+4));
            transferIntake.prepShooter();
            follow(paths.get(i*5+5));
            shoot();
        }
        follow(paths.get(16));
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
        shooter.moveTurret();
        shooter.spin();
        transferIntake.update();
        follower.update();
        telemetry.addLine(shooter.getData());
        telemetry.addLine(transferIntake.getData());
        telemetry.addLine(Arrays.toString(follower.debug()));
        telemetry.update();
    }
}
