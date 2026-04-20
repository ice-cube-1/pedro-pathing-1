package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.robotParts.RobotConstants.ROBOT_LENGTH_CM;
import static org.firstinspires.ftc.teamcode.robotParts.RobotConstants.ROBOT_WIDTH_CM;

import static java.lang.Math.toRadians;

import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.robotParts.Shooter;
import org.firstinspires.ftc.teamcode.robotParts.TransferIntake;

import com.pedropathing.follower.Follower;

import java.util.Arrays;

public class Auto9Pedro extends LinearOpMode {
    private Follower follower;
    private Shooter shooter;
    private TransferIntake transferIntake;
    private final ElapsedTime timer = new ElapsedTime();
    private final double offset;
    private final double direction;
    private final int tagID;
    public Auto9Pedro(double offset, double direction, int tagID) {
        this.offset = offset;
        this.direction = direction;
        this.tagID = tagID;
    }
    private Pose start, shoot, intake1Start, intake1End, intake2Start, intake2End, park;
    private Path toShoot1,toIntake1,intake1,toShoot2,toIntake2,intake2,toShoot3,toPark;

    @Override
    public void runOpMode() {
        follower = Constants.createFollower(hardwareMap);
        buildPoses();
        follower.setStartingPose(start);
        buildPaths();
        shooter = new Shooter(hardwareMap, tagID);
        shooter.setSubRange(direction*90.0, 0.0);
        transferIntake = new TransferIntake(hardwareMap);
        waitForStart();
        transferIntake.prepShooter();
        shooter.turnOnShooter();
        follow(toShoot1);
        shoot();
        follow(toIntake1);
        transferIntake.intake(1.0);
        follow(intake1);
        transferIntake.intake(0.0);
        transferIntake.prepShooter();
        follow(toShoot2);
        shoot();
        follow(toIntake2);
        transferIntake.intake(1.0);
        follow(intake2);
        transferIntake.intake(0.0);
        transferIntake.prepShooter();
        follow(toShoot3);
        shoot();
        follow(toPark);
    }

    private void buildPoses() {
        start = new Pose(
                offset - direction*(24+ROBOT_WIDTH_CM/(2.54*2)),
                144.0-ROBOT_LENGTH_CM/(2.54*2),
                toRadians(270)
        );
        shoot = new Pose(
                offset - direction * 60.0,
                84.0,
                toRadians(270)
        );
        intake1Start = new Pose(
                offset - direction * 60.0,
                84.0,
                toRadians(90-90*direction)
        );

        intake1End = new Pose(
                offset - direction * 18.0,
                84.0,
                toRadians(90-90*direction)
        );
        intake2Start = new Pose(
                offset - direction * 60.0,
                60.0,
                toRadians(90-90*direction)
        );

        intake2End = new Pose(
                offset - direction * 18.0,
                60.0,
                toRadians(90-90*direction)
        );

        park = new Pose(
                offset - direction * 60.0,
                60.0,
                toRadians(270)
        );
    }

    public void buildPaths() {
        toShoot1 = new Path(new BezierLine(start, shoot));
        toShoot1.setLinearHeadingInterpolation(start.getHeading(), shoot.getHeading());

        toIntake1 = new Path(new BezierLine(shoot, intake1Start));
        toIntake1.setLinearHeadingInterpolation(shoot.getHeading(), intake1Start.getHeading());

        intake1 = new Path(new BezierLine(intake1Start, intake1End));
        intake1.setLinearHeadingInterpolation(intake1Start.getHeading(), intake1End.getHeading());

        toShoot2 = new Path(new BezierLine(intake1End, shoot));
        toShoot2.setLinearHeadingInterpolation(intake1End.getHeading(), shoot.getHeading());

        toIntake2 = new Path(new BezierLine(shoot, intake2Start));
        toIntake2.setLinearHeadingInterpolation(shoot.getHeading(), intake2Start.getHeading());

        intake2 = new Path(new BezierLine(intake2Start, intake2End));
        intake2.setLinearHeadingInterpolation(intake2Start.getHeading(), intake2End.getHeading());

        toShoot3 = new Path(new BezierLine(intake2End, shoot));
        toShoot3.setLinearHeadingInterpolation(intake2End.getHeading(), shoot.getHeading());

        toPark = new Path(new BezierLine(shoot, park));
        toPark.setLinearHeadingInterpolation(shoot.getHeading(), park.getHeading());
    }

    private void follow(Path path) {
        follower.followPath(path);

        while (opModeIsActive() && follower.isBusy()) {
            follower.update();
            updateAll();
        }
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
        telemetry.addLine(shooter.getData());
        telemetry.addLine(transferIntake.getData());
        telemetry.addLine(Arrays.toString(follower.debug()));
        telemetry.update();
    }
}