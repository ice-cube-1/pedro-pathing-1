package org.firstinspires.ftc.teamcode.autos;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.robotParts.RobotState;

@Autonomous(name = "FAR - USE FAR SELECTOR")
public class GeneralFar extends AutoFar {
    public GeneralFar() {
        super(RobotState.ALLIANCE_COLOUR, RobotState.NUM_TO_ATTEMPT);
    }
}