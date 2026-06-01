package org.firstinspires.ftc.teamcode.manuals;

import static org.firstinspires.ftc.teamcode.robotParts.RobotState.ALLIANCE_COLOUR;
import static org.firstinspires.ftc.teamcode.robotParts.RobotState.AUTO_END_POSE;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "MANUAL - FOLLOW FROM AUTO (or set start config in panels)")
public class MainManual extends Manual { public MainManual() {super(AUTO_END_POSE, ALLIANCE_COLOUR, true);}}