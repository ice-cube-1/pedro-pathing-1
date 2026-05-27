package org.firstinspires.ftc.teamcode.autos;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Auto12;
import org.firstinspires.ftc.teamcode.robotParts.RobotConstants;

@Autonomous(name = "Auto 6 - Blue")
public class b6 extends Auto12 {
    public b6() {
        super(RobotConstants.Alliance.BLUE, 1);
    }
}