package org.firstinspires.ftc.teamcode.autos;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Auto12;
import org.firstinspires.ftc.teamcode.robotParts.RobotState;

@Autonomous(name = "NEAR - USE NEAR SELECTOR")
public class GeneralNear extends Auto12 {
    public GeneralNear() {
        super(RobotState.ALLIANCE_COLOUR, RobotState.NUM_TO_ATTEMPT);
    }
}