package org.firstinspires.ftc.teamcode.autos;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.AutoFar;
import org.firstinspires.ftc.teamcode.robotParts.RobotConstants;

@Autonomous(name = "Auto far 6 - Blue")
public class blueFar6 extends AutoFar {
    public blueFar6() {
        super(RobotConstants.Alliance.BLUE,1);
    }
}