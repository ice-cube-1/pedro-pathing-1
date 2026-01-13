package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;

@TeleOp(name="servo test", group="Linear OpMode")
public class servoTest extends LinearOpMode {
    @Override
    public void runOpMode() {
        CRServo s = hardwareMap.get(CRServo.class, "spindex");
        waitForStart();
        while (opModeIsActive()) {
            if (gamepad1.left_bumper) {
                s.setPower(0.5);
            } else if (gamepad1.right_bumper) {
                s.setPower(-0.5);
            } else {
                s.setPower(0);
            }
        }
    }
}
