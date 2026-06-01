package org.firstinspires.ftc.teamcode.old;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.autos.Auto12;
import org.firstinspires.ftc.teamcode.robotParts.RobotConstants;

@Disabled
@Autonomous(name = "Auto 9 - Blue")
public class b9 extends Auto12 {
    public b9() {
        super(RobotConstants.Alliance.BLUE, 2);
    }
}