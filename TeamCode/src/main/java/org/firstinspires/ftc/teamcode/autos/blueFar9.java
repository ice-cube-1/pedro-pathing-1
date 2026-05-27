package org.firstinspires.ftc.teamcode.autos;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.AutoFar;
import org.firstinspires.ftc.teamcode.robotParts.RobotConstants;

@Autonomous(name = "Auto far 9 - Blue")
public class blueFar9 extends AutoFar {
    public blueFar9() {
        super(RobotConstants.Alliance.BLUE,2);
    }
}