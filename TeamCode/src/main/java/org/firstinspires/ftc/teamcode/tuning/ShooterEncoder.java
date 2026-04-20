package org.firstinspires.ftc.teamcode.tuning;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp
class ShooterEncoder extends LinearOpMode {
     public void runOpMode() {
        DcMotor[] motors = new DcMotor[] {hardwareMap.get(DcMotor.class, "m1"), hardwareMap.get(DcMotor.class, "m2")};
        waitForStart();
        while (opModeIsActive()) {
            telemetry.addData("m1", motors[0].getCurrentPosition());
            telemetry.addData("m2",motors[1].getCurrentPosition());
            telemetry.update();
        }
    }
}