package org.firstinspires.ftc.teamcode.old;

import static java.lang.Math.PI;

import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.manuals.Manual;
import org.firstinspires.ftc.teamcode.robotParts.RobotConstants;

@Disabled
@TeleOp(name = "Manual, BLUE, intake TOWARDS")
public class Blue1 extends Manual { public Blue1() {super(new Pose(0,0,PI), RobotConstants.Alliance.BLUE, false);}}