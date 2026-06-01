package org.firstinspires.ftc.teamcode.old;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.autos.AutoFar;
import org.firstinspires.ftc.teamcode.robotParts.RobotConstants;

@Disabled
@Autonomous(name = "Auto far 9 - Blue")
public class blueFar9 extends AutoFar {
    public blueFar9() {
        super(RobotConstants.Alliance.BLUE,2);
    }
}