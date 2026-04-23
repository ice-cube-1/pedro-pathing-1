package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.robotParts.RobotConstants.ROBOT_LENGTH_CM;
import static org.firstinspires.ftc.teamcode.robotParts.RobotConstants.ROBOT_WIDTH_CM;
import static java.lang.Double.max;
import static java.lang.Math.min;
import static java.lang.Math.toRadians;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
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

import kotlin.Triple;

@Configurable
public class Auto12 extends LinearOpMode {
    private Follower follower;
    private Shooter shooter;
    private TransferIntake transferIntake;
    private final ElapsedTime timer = new ElapsedTime();
    private final double offset;
    private final double direction;
    private final int tagID;
    private final int attempt;
    public static double shooter_y = 80.0;
    public Auto12(double offset, double direction, int numToAttempt, int tagID) {
        this.offset = offset;
        this.direction = direction;
        this.tagID = tagID;
        this.attempt = numToAttempt;
    }
    @Override
    public void runOpMode() {
        double[][] intakePositions = new double[][] {
                {84.0-15.0, 24.0, toRadians(90 * (1 - direction))},
                {60.0-15.0, 20.0, toRadians(90 - 90 * direction)},
                {36.0-15.0, 20.0, toRadians(90 - 90 * direction)}
        };
        ArrayList<Pose> poses = new ArrayList<>();
        poses.add(new Pose(offset - direction*(24+ROBOT_WIDTH_CM/(2.54*2)),144.0-ROBOT_LENGTH_CM/(2.54), toRadians(270)));
        poses.add(new Pose(offset - direction * 60.0, shooter_y, toRadians(270))); // SHOOT AFTER THIS
        for (int i = 0; i<3; i++) {
            poses.add(new Pose(offset - direction * 60.0, intakePositions[i][0], intakePositions[i][2])); // INTAKE AFTER THIS
            poses.add(new Pose(offset - direction * intakePositions[i][1], intakePositions[i][0],intakePositions[i][2])); // STOP INTAKING 1 HERE
            poses.add(new Pose(offset - direction * 60.0, shooter_y, toRadians(270)));
        }
        poses.add(new Pose(offset - direction * 60.0, 60.0-12.0, toRadians(270)));
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
        for (int i = 0; i<attempt; i++) {
            follow(paths.get(i*3+1));
            transferIntake.intake(1.0);
            follow(paths.get(i*3+2));
            transferIntake.intake(0.0);
            if (i != 2) {
                transferIntake.prepShooter();
                follow(paths.get(i*3+3));
                shoot();
            }
        }
        follow(paths.get(10));
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
        shooter.moveTurret(1.0);
        shooter.spin(false, false);
        transferIntake.update();
        follower.update();
        telemetry.addLine(shooter.getData());
        telemetry.addLine(transferIntake.getData());
        telemetry.addLine(Arrays.toString(follower.debug()));
        telemetry.update();
    }
}
