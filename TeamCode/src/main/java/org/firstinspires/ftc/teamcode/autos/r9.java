package org.firstinspires.ftc.teamcode.autos;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Auto12;
import org.firstinspires.ftc.teamcode.robotParts.RobotConstants;

@Autonomous(name = "Auto 9 - RED")
public class r9 extends Auto12 {
    public r9() {
        super(RobotConstants.Alliance.RED, 2);
    }
}