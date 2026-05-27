package org.firstinspires.ftc.teamcode.autos;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Auto12;
import org.firstinspires.ftc.teamcode.robotParts.RobotConstants;

@Autonomous(name = "Auto 12 - Red")
public class r12 extends Auto12 {
    public r12() {
        super(RobotConstants.Alliance.RED, 3);
    }
}