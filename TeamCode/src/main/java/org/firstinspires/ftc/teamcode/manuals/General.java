package org.firstinspires.ftc.teamcode.manuals;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Manual;
import org.firstinspires.ftc.teamcode.robotParts.RobotConstants;
import org.firstinspires.ftc.teamcode.robotParts.RobotState;

@TeleOp(name = "MANUAL - FOLLOW FROM AUTO")
public class General extends Manual { public General() {super(RobotState.AUTO_END_POSE, RobotState.ALLIANCE_COLOUR, true);}}