package org.firstinspires.ftc.teamcode.robotParts;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Wheel {

    private final DcMotorEx drive;

    public Wheel(String name, HardwareMap hardwareMap, DcMotorSimple.Direction direction) {

        drive = hardwareMap.get(DcMotorEx.class, name);

        drive.setDirection(direction);

        drive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        drive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void setPower(double power) {
        if (power > 1.0) power = 1.0;
        if (power < -1.0) power = -1.0;

        drive.setPower(power);
    }

    public double getVelocity() {
        return drive.getVelocity();
    }
}