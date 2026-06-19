package org.firstinspires.ftc.teamcode.autos;

import static org.firstinspires.ftc.teamcode.robotParts.RobotConstants.ROBOT_LENGTH_CM;
import static org.firstinspires.ftc.teamcode.robotParts.RobotConstants.ROBOT_WIDTH_CM;
import static org.firstinspires.ftc.teamcode.robotParts.RobotState.ALLIANCE_COLOUR;
import static java.lang.Double.max;
import static java.lang.Math.min;
import static java.lang.Math.toRadians;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.robotParts.RobotConstants;
import org.firstinspires.ftc.teamcode.robotParts.RobotState;
import org.firstinspires.ftc.teamcode.robotParts.Shooter;
import org.firstinspires.ftc.teamcode.robotParts.TransferIntake;

import java.util.ArrayList;

@Autonomous
public class AutoFarLeave extends LinearOpMode {
    private Follower follower;

    public void runOpMode()  {
        waitForStart();
        ArrayList<Pose> poses = new ArrayList<>();
        poses.add(new Pose(ALLIANCE_COLOUR.mirrorX(48+ROBOT_WIDTH_CM/(2.54*2)), ROBOT_LENGTH_CM/(2.54),toRadians(270))); // start
        poses.add(new Pose(ALLIANCE_COLOUR.mirrorX(25.0), ROBOT_LENGTH_CM/(2.54)+2, toRadians(270))); // stop intaking here
        ArrayList<Path> paths = new ArrayList<>();
        for (int i = 1; i< poses.size(); i++) {
            Path path = new Path(new BezierLine(poses.get(i - 1), poses.get(i)));
            path.setLinearHeadingInterpolation(poses.get(i - 1).getHeading(), poses.get(i).getHeading());
            paths.add(path);
        }
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(poses.get(0));
        follow(paths.get(0));
        RobotState.AUTO_END_POSE = follower.getPose();
    }
    private void follow(Path path) {
        follower.followPath(path);
        while (opModeIsActive() && follower.isBusy()) {updateAll();}
    }
    private void updateAll() {
        follower.update();
        updateTelemetry();
    }
    public void updateTelemetry() {
        telemetry.addLine(ALLIANCE_COLOUR.toString());
        telemetry.addLine(follower.getPose().toString());
        telemetry.update();
    }
}