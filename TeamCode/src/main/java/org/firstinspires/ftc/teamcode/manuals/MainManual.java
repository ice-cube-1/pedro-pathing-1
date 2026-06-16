package org.firstinspires.ftc.teamcode.manuals;

import static org.firstinspires.ftc.teamcode.robotParts.RobotState.ALLIANCE_COLOUR;
import static org.firstinspires.ftc.teamcode.robotParts.RobotState.AUTO_END_POSE;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "MANUAL - FOLLOW FROM AUTO")
public class MainManual extends Manual { public MainManual() {super(AUTO_END_POSE, ALLIANCE_COLOUR);}}

// you can live update auto end pose if you need reloc in the middle of a match
// also you can create these if follow through doesn't work, but they al need to be in different file