package org.firstinspires.ftc.teamcode.tuning;

import static org.firstinspires.ftc.teamcode.robotParts.RobotConstants.STOP_DOWN;
import static org.firstinspires.ftc.teamcode.robotParts.RobotConstants.STOP_UP;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.robotParts.TransferIntake;


@TeleOp(name = "transfer intake test", group = "tuning")
public class TransferTest extends LinearOpMode {
    @Override
    public void runOpMode() {
        waitForStart();
        TransferIntake transferIntake = new TransferIntake(hardwareMap);

        while (opModeIsActive()) {
            transferIntake.intake.setPower(gamepad1.left_trigger - gamepad1.right_trigger);
            transferIntake.transfer.setPower(gamepad1.left_stick_x);
            if (gamepad1.dpad_up) {transferIntake.stop.setPosition(STOP_UP);}
            if (gamepad1.dpad_down) {transferIntake.stop.setPosition(STOP_DOWN);}
        }
    }
}