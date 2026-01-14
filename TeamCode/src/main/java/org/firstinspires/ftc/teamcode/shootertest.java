package org.firstinspires.ftc.teamcode;

import static java.lang.Double.max;
import static java.lang.Double.min;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name="shooter test", group="Linear OpMode")
public class shootertest extends LinearOpMode {
    DcMotorEx init_motor(String name, DcMotorSimple.Direction direction) {
        DcMotorEx out = hardwareMap.get(DcMotorEx.class, name);
        out.setDirection(direction);
        out.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        out.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        out.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        return out;
    }
    @Override
    public void runOpMode() {
        DcMotorEx[] motors = {init_motor("m1", DcMotorSimple.Direction.FORWARD),
                init_motor("m2", DcMotorSimple.Direction.REVERSE)};
        CRServo s = hardwareMap.get(CRServo.class, "s");
        double speed = 0.1;
        waitForStart();
        while (opModeIsActive()) {
            if (gamepad1.left_bumper) {
                speed = min(speed-0.05, 0.1);
            } if (gamepad1.right_bumper) {
                speed = max(speed+0.05,1);
            } if (gamepad1.b) {
                for (DcMotorEx motor: motors) {motor.setPower(speed);}
            } else if (gamepad1.x || gamepad1.y) {
                for (DcMotorEx motor: motors) {motor.setPower(-speed);}
            } else {
                for (DcMotorEx motor: motors) {motor.setPower(0);}
            }
            if (gamepad1.dpad_up) {
                s.setPower(-0.2);
            } else if (gamepad1.dpad_down) {
                s.setPower(0.2);
            } else {
                s.setPower(0);
            }
            telemetry.addLine(String.valueOf(speed));
            for (DcMotorEx motor: motors) {
                telemetry.addLine(String.valueOf(motor.getVelocity()));
            }
            telemetry.update();
        }
    }
}
