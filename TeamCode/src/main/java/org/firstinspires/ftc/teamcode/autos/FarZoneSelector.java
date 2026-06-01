package org.firstinspires.ftc.teamcode.autos;

import static org.firstinspires.ftc.teamcode.robotParts.RobotState.ALLIANCE_COLOUR;
import static org.firstinspires.ftc.teamcode.robotParts.RobotState.NUM_TO_ATTEMPT;
import static java.lang.Math.max;
import static java.lang.Math.min;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.robotParts.RobotConstants;
import org.firstinspires.ftc.teamcode.robotParts.RobotState;

@TeleOp(name = "FAR ZONE SELECTOR", group = "selectors")
public class FarZoneSelector extends LinearOpMode {
    @Override
    public void runOpMode() {
        ElapsedTime timer = new ElapsedTime();
        waitForStart();
        while (opModeIsActive()) {
            for (int i = 0; i<=2; i++) {
                telemetry.addLine((NUM_TO_ATTEMPT == i ? "> ": "  ") + RobotState.displayNumToAttempt(i, RobotConstants.ShootMode.FAR));
            }
            telemetry.addLine("DPAD LEFT FOR BLUE ALLIANCE, RIGHT FOR RED ALLIANCE");
            telemetry.addLine("ALLIANCE SELECTED: "+ALLIANCE_COLOUR);
            telemetry.update();
            if (gamepad1.dpad_down && timer.milliseconds() > 200) {
                NUM_TO_ATTEMPT = min(NUM_TO_ATTEMPT + 1, 3);
                timer.reset();
            } else if (gamepad1.dpad_up && timer.milliseconds() > 200) {
                NUM_TO_ATTEMPT = max(NUM_TO_ATTEMPT - 1, 0);
                timer.reset();
            }
            if (gamepad1.dpad_left) ALLIANCE_COLOUR = RobotConstants.Alliance.BLUE;
            if (gamepad1.dpad_right) ALLIANCE_COLOUR = RobotConstants.Alliance.RED;
        }
    }
}