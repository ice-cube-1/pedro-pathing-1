package org.firstinspires.ftc.teamcode.manuals;

import static java.lang.Math.PI;

import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Manual;
import org.firstinspires.ftc.teamcode.robotParts.RobotConstants;

@TeleOp(name = "Manual, RED , intake RIGHT")
public class Red3 extends Manual { public Red3() {
    super(new Pose(0,0,-PI/2), RobotConstants.Alliance.RED, false);
}}