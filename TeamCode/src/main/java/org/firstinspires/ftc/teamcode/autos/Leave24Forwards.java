package org.firstinspires.ftc.teamcode.autos;

import static java.lang.Math.PI;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "Auto - drive 24in forwards")
public class Leave24Forwards extends LinearOpMode {
    private Follower follower;
    @Override
    public void runOpMode() {
        follower.setStartingPose(new Pose(0,0,PI/2));
        follower.followPath(new Path(new BezierLine(new Pose(0.0,0.0,PI/2), new Pose(0.0,24.0, PI/2))));
    }
}