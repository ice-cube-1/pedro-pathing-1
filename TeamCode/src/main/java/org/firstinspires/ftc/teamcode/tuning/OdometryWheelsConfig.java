package org.firstinspires.ftc.teamcode.tuning;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp
public class OdometryWheelsConfig extends LinearOpMode {

    @Override
    public void runOpMode() {
        waitForStart();

        DcMotorEx[] wheels = new DcMotorEx[] {
                initOdoWheel("lf", DcMotorSimple.Direction.REVERSE),
                initOdoWheel("rf", DcMotorSimple.Direction.FORWARD),
                initOdoWheel("lb", DcMotorSimple.Direction.REVERSE),
                initOdoWheel("rb", DcMotorSimple.Direction.FORWARD)
        };

        while (opModeIsActive()) {

            if (gamepad1.left_trigger > 0.5) {
                wheels[0].setPower(0.5);
            } else {
                wheels[0].setPower(0.0);
            }

            if (gamepad1.right_trigger > 0.5) {
                wheels[1].setPower(0.5);
            } else {
                wheels[1].setPower(0.0);
            }

            if (gamepad1.left_bumper) {
                wheels[2].setPower(0.5);
            } else {
                wheels[2].setPower(0.0);
            }

            if (gamepad1.right_bumper) {
                wheels[3].setPower(0.5);
            } else {
                wheels[3].setPower(0.0);
            }

            telemetry.addLine("LT - LF, RT - RF, LB - LB, RB - RB");
            telemetry.addData("LF encoder", wheels[0].getCurrentPosition());
            telemetry.addData("RF encoder", wheels[1].getCurrentPosition());
            telemetry.addData("LB encoder", wheels[2].getCurrentPosition());
            telemetry.addData("RB encoder", wheels[3].getCurrentPosition());
            telemetry.update();
        }
    }

    private DcMotorEx initOdoWheel(String name, DcMotorSimple.Direction direction) {
        DcMotorEx motor = hardwareMap.get(DcMotorEx.class, name);

        motor.setDirection(direction);
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        return motor;
    }
}