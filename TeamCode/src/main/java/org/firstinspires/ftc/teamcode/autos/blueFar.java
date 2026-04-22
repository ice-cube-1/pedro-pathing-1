package org.firstinspires.ftc.teamcode.autos;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Auto12;
import org.firstinspires.ftc.teamcode.AutoFar;

@Autonomous(name = "Auto far - Blue")
public class blueFar extends AutoFar {
    public blueFar() {
        super(0.0, -1.0, 20);
    }
}