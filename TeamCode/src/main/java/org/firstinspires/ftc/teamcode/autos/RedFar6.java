package org.firstinspires.ftc.teamcode.autos;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.AutoFar;
import org.firstinspires.ftc.teamcode.robotParts.RobotConstants;

@Autonomous(name = "Far auto, RED 6")
public class RedFar6 extends AutoFar { public RedFar6() {
    super(RobotConstants.Alliance.RED,1);
}}