package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.robotParts.RobotConstants.ROBOT_LENGTH_CM;
import static org.firstinspires.ftc.teamcode.robotParts.RobotConstants.ROBOT_WIDTH_CM;

import static java.lang.Math.hypot;
import static java.lang.Math.toRadians;

import com.bylazar.configurables.annotations.Configurable;
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
@Configurable
public class Auto9Pedro extends LinearOpMode {
    private Follower follower;
    private Shooter shooter;
    private TransferIntake transferIntake;
    private final ElapsedTime timer = new ElapsedTime();
    private final double offset;
    private final double direction;
    private final int tagID;
    public static float MULTIPLIER = 0F;
    public Auto9Pedro(double offset, double direction, int tagID) {
        this.offset = offset;
        this.direction = direction;
        this.tagID = tagID;
    }
    private Pose start, shoot1, intake1Start, intake1End, intake2Start, intake2End, intake3Start, intake3End, park, shoot4, shoot2, shoot3;
    private Path toShoot1,toIntake1,intake1,toShoot2,toIntake2,intake2, backIntake2,toShoot3,toIntake3, intake3, toShoot4, toPark;
    private double multiplier(double value) {
        return value * MULTIPLIER;
    }

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
        follow(backIntake2);
        transferIntake.intake(0.0);
        transferIntake.prepShooter();
        follow(toShoot3);
        shoot();
        follow(toIntake3);
        transferIntake.intake(1.0);
        follow(intake3);
        transferIntake.intake(0.0);
        transferIntake.prepShooter();
        follow(toShoot4);
        shoot();
        follow(toPark);
    }

    private void buildPoses() {
        double disp = 0.0;

        start = new Pose(
                offset - direction*(24+ROBOT_WIDTH_CM/(2.54*2)),
                144.0-ROBOT_LENGTH_CM/(2.54),
                toRadians(270)
        );

        // start -> shoot1
        disp += Math.hypot(
                144.0-ROBOT_LENGTH_CM/(2.54)-90.0,
                60-(24+ROBOT_WIDTH_CM/(2.54*2))
        );

        shoot1 = new Pose(
                offset - direction * 60.0,
                90.0,
                toRadians(270) + multiplier(disp)
        );

        // shoot1 -> intake1Start
        disp += Math.abs(84.0 - 90.0);

        intake1Start = new Pose(
                offset - direction * 60.0,
                84.0,
                toRadians(90 - 90*direction) + multiplier(disp)
        );

        // intake1Start -> intake1End
        disp += Math.abs(60.0 - 34.0);

        intake1End = new Pose(
                offset - direction * 34.0,
                84.0,
                toRadians(90 - 90*direction) + multiplier(disp)
        );

        disp += hypot(60-34,84-90);
        shoot2 = new Pose(
                offset - direction * 60.0,
                90.0,
                toRadians(270) + multiplier(disp)
        );

        disp += Math.abs(61.0 - 90.0);

        intake2Start = new Pose(
                offset - direction * 60.0,
                61.0,
                toRadians(90 - 90*direction) + multiplier(disp)
        );

        // intake2Start -> intake2End
        disp += Math.abs(60.0 - 34.0);

        intake2End = new Pose(
                offset - direction * 34.0,
                61.0,
                toRadians(90 - 90*direction) + multiplier(disp)
        );

        disp += hypot(60-34,90-61);

        shoot3 = new Pose(
                offset - direction * 60.0,
                90.0,
                toRadians(270) + multiplier(disp)
        );

        disp += Math.abs(35.0 - 90.0);

        intake3Start = new Pose(
                offset - direction * 60.0,
                38.0,
                toRadians(90 - 90*direction) + multiplier(disp)
        );

        // intake3Start -> intake3End
        disp += Math.abs(66.0 - 38.0);

        intake3End = new Pose(
                offset - direction * 34.0,
                38.0,
                toRadians(90 - 90*direction) + multiplier(disp)
        );

        disp += hypot(60-34,90-35);

        shoot4 = new Pose(
                offset - direction * 60.0,
                90.0,
                toRadians(270) + multiplier(disp)
        );

        disp += Math.abs(75.0 - 90.0);

        park = new Pose(
                offset - direction * 60.0,
                75.0,
                toRadians(270) + multiplier(disp)
        );
    }
    public void buildPaths() {
        toShoot1 = new Path(new BezierLine(start, shoot1));
        toShoot1.setLinearHeadingInterpolation(start.getHeading(), shoot1.getHeading());

        toIntake1 = new Path(new BezierLine(shoot1, intake1Start));
        toIntake1.setLinearHeadingInterpolation(shoot1.getHeading(), intake1Start.getHeading());

        intake1 = new Path(new BezierLine(intake1Start, intake1End));
        intake1.setLinearHeadingInterpolation(intake1Start.getHeading(), intake1End.getHeading());

        toShoot2 = new Path(new BezierLine(intake1End, shoot2));
        toShoot2.setLinearHeadingInterpolation(intake1End.getHeading(), shoot2.getHeading());

        toIntake2 = new Path(new BezierLine(shoot2, intake2Start));
        toIntake2.setLinearHeadingInterpolation(shoot2.getHeading(), intake2Start.getHeading());

        intake2 = new Path(new BezierLine(intake2Start, intake2End));
        intake2.setLinearHeadingInterpolation(intake2Start.getHeading(), intake2End.getHeading());

        backIntake2 = new Path(new BezierLine(intake2End, intake2Start));
        backIntake2.setLinearHeadingInterpolation(intake2End.getHeading(), intake2Start.getHeading());

        toShoot3 = new Path(new BezierLine(intake2End, shoot3));
        toShoot3.setLinearHeadingInterpolation(intake2End.getHeading(), shoot3.getHeading());

        toIntake3 = new Path(new BezierLine(shoot3, intake3Start));
        toIntake3.setLinearHeadingInterpolation(shoot3.getHeading(), intake3Start.getHeading());

        intake3 = new Path(new BezierLine(intake3Start, intake3End));
        intake3.setLinearHeadingInterpolation(intake3Start.getHeading(), intake3End.getHeading());

        toShoot4 = new Path(new BezierLine(intake3End, shoot4));
        toShoot4.setLinearHeadingInterpolation(intake3End.getHeading(), shoot4.getHeading());

        toPark = new Path(new BezierLine(shoot4, park));
        toPark.setLinearHeadingInterpolation(shoot4.getHeading(), park.getHeading());
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
        telemetry.addLine("stuck here?");
        telemetry.update();
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