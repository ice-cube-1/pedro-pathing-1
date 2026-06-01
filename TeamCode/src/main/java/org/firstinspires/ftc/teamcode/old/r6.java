package org.firstinspires.ftc.teamcode.old;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.autos.Auto12;
import org.firstinspires.ftc.teamcode.robotParts.RobotConstants;

@Disabled
@Autonomous(name = "Auto 6 - Red")
public class r6 extends Auto12 {
    public r6() {
        super(RobotConstants.Alliance.RED, 1);
    }
}