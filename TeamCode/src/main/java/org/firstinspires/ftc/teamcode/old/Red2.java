package org.firstinspires.ftc.teamcode.old;

import static java.lang.Math.PI;

import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.manuals.Manual;
import org.firstinspires.ftc.teamcode.robotParts.RobotConstants;

@Disabled
@TeleOp(name = "Manual, RED, intake LEFT")
public class Red2 extends Manual { public Red2() {
    super(new Pose(0,0,PI/2), RobotConstants.Alliance.RED, false);
}}