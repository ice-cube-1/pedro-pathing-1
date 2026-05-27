package org.firstinspires.ftc.teamcode.autos;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Auto12;
import org.firstinspires.ftc.teamcode.robotParts.RobotConstants;

@Autonomous(name = "Auto 6 - Red")
public class r6 extends Auto12 {
    public r6() {
        super(RobotConstants.Alliance.RED, 1);
    }
}