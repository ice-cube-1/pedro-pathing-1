package org.firstinspires.ftc.teamcode.autos;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.AutoFar;
import org.firstinspires.ftc.teamcode.robotParts.RobotConstants;

@Autonomous(name = "Far auto, RED 9")
public class RedFar9 extends AutoFar { public RedFar9() {
    super(RobotConstants.Alliance.RED,2);
}}