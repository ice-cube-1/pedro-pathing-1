package org.firstinspires.ftc.teamcode;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;

@Configurable
@TeleOp(name="shooter velocity tuner", group="Linear OpMode")
public class shooterVelocityTuner extends LinearOpMode {
    DcMotorEx[] motors;
    public static double targetV = 200;
    public static float kP = 0.0005F;
    double power = 0;
    int time = 0;
    ElapsedTime timer = new ElapsedTime();
    AprilTagProcessor aprilTag;
    @Override
    public void runOpMode() {
        //aprilTag = AprilTagProcessor.easyCreateWithDefaults();
        //VisionPortal.easyCreateWithDefaults(hardwareMap.get(WebcamName.class, "Webcam 1"), aprilTag);
        boolean motor_running = false;
        motors = new DcMotorEx[]{init_motor("m1", DcMotorSimple.Direction.REVERSE),
                init_motor("m2", DcMotorSimple.Direction.FORWARD)};
        CRServo s = hardwareMap.get(CRServo.class, "s");
        waitForStart();
        while (opModeIsActive()) {
            if (gamepad1.aWasPressed()) {
                motor_running = !motor_running;
                timer.reset();
                for (DcMotorEx motorEx: motors) {
                    motorEx.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    motorEx.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                }
            }
            if (motor_running) { integralVelocity(); }
            else { for (DcMotorEx motorEx: motors) { motorEx.setPower(0); } }
            if (gamepad1.dpadUpWasPressed()) {
                s.setPower(-0.2);
                sleep(200);
                time += 200;
            } else if (gamepad1.dpadDownWasPressed()) {
                s.setPower(0.2);
                sleep(200);
                time -= 200;
            } else { s.setPower(0); }
            telemetry.addLine(String.valueOf(time));
            //detectAprilTags();
            telemetry.update();
        }
    }
    void integralVelocity() {
        double error = targetV - getVelocity();
        power = Math.max(0, Math.min(1, power + kP * timer.seconds() * error));
        timer.reset();
        for (DcMotorEx m : motors) { m.setPower(power); }
        telemetry.addData("Error", error);
        telemetry.addData("Power", power);
    }
    double getVelocity() {
        double ans = 0;
        for (DcMotorEx motor: motors) {
            ans += motor.getVelocity();
        }
        return ans/2;
    }
    DcMotorEx init_motor(String name, DcMotorSimple.Direction direction) {
        DcMotorEx out = hardwareMap.get(DcMotorEx.class, name);
        out.setDirection(direction);
        out.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        out.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        out.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        return out;
    }
    void detectAprilTags() {
        List<AprilTagDetection> currentDetections = aprilTag.getDetections();
        telemetry.addData("no. tags", currentDetections.size());
        for (AprilTagDetection tag: currentDetections) {
            if (tag.id == 20) {
                telemetry.addLine(tag.ftcPose.bearing + " - "+tag.ftcPose.range);
                if (tag.ftcPose.bearing > 5) {
                    telemetry.addLine("Rotate clockwise");
                } else if (tag.ftcPose.bearing < -5) {
                    telemetry.addLine("Rotate counterclockwise");
                } else {
                    telemetry.addLine("ready to shoot - " + tag.ftcPose.range);
                }
            }
        }
    }
}
