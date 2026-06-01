package org.firstinspires.ftc.teamcode.old;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.autos.AutoFar;
import org.firstinspires.ftc.teamcode.robotParts.RobotConstants;

@Disabled
@Autonomous(name = "Far auto, RED 6")
public class RedFar6 extends AutoFar { public RedFar6() {
    super(RobotConstants.Alliance.RED,1);
}}