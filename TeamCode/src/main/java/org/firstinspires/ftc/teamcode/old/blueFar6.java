package org.firstinspires.ftc.teamcode.old;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.autos.AutoFar;
import org.firstinspires.ftc.teamcode.robotParts.RobotConstants;

@Disabled
@Autonomous(name = "Auto far 6 - Blue")
public class blueFar6 extends AutoFar {
    public blueFar6() {
        super(RobotConstants.Alliance.BLUE,1);
    }
}