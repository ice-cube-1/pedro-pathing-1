package org.firstinspires.ftc.teamcode.tuning;

import static org.firstinspires.ftc.teamcode.robotParts.RobotConstants.HOOD_ANGLE;
import static org.firstinspires.ftc.teamcode.robotParts.RobotConstants.STOP_DOWN;
import static org.firstinspires.ftc.teamcode.robotParts.RobotConstants.STOP_UP;
import static org.firstinspires.ftc.teamcode.robotParts.RobotConstants.TURRET_KP;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;

@TeleOp(name = "zero components", group = "tuning")
public class Zeroing extends LinearOpMode {
    public void runOpMode() {
        waitForStart();
        telemetry.addLine("DPAD LEFT for TURRET TEST, RIGHT for ZERO HOOD, UP for CALIBRATE HARD STOP");
        telemetry.update();
        ServoImplEx[] turret = new ServoImplEx[] {
                hardwareMap.get(ServoImplEx.class, "t1"),
                hardwareMap.get(ServoImplEx.class, "t2")
        };
        turret[0].setPwmRange(new PwmControl.PwmRange(500.0, 2500.0));
        turret[1].setPwmRange(new PwmControl.PwmRange(500.0, 2500.0));
        Servo hood = hardwareMap.get(ServoImplEx.class, "hood");
        Servo stop = hardwareMap.get(ServoImplEx.class, "stop");
        double sPos =0.0;
        while (opModeIsActive()) {
            if (gamepad1.dpad_left) {
                turret[0].setPosition(TURRET_KP);
                turret[1].setPosition(TURRET_KP);
                telemetry.addData("position",TURRET_KP);
                telemetry.update();
            } if (gamepad1.dpad_right) {
                hood.setPosition(sPos);
                if (gamepad1.left_bumper) {sPos = HOOD_ANGLE; }
                if (gamepad1.right_bumper) {sPos = 0.0; }
                telemetry.addData("hood angle",sPos);
                telemetry.update();
            } if (gamepad1.dpad_down) {
                stop.setPosition(sPos);
                if (gamepad1.left_bumper) {sPos = STOP_DOWN; }
                if (gamepad1.right_bumper) {sPos = STOP_UP; }
                telemetry.addData("stop angle",sPos);
                telemetry.update();
            }
        }
    }
}
