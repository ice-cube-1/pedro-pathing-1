package org.firstinspires.ftc.teamcode.tuning;

import static org.firstinspires.ftc.teamcode.robotParts.RobotConstants.STOP_DOWN;
import static org.firstinspires.ftc.teamcode.robotParts.RobotConstants.STOP_UP;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;

import java.util.Objects;
/*
@TeleOp(name = "zero components - no controller", group = "tuning")
public class ZeroNoController extends LinearOpMode {
    public static boolean zeroTurret = false;
    public static boolean zeroHood = false;
    public static boolean zeroHardStop = false;
    public static String hardStop = "down";
    public static double hoodAngle = 1.0;
    public static double turretAngle = 0.5;
    TelemetryManager panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();
    public void runOpMode() {
        panelsTelemetry.addLine("""
                TURRET - same as usual, but the value needs to be 0.5 for when the shooter faces directly forwards
                HOOD - just change the value, it should be 0 when the hood is taken out, and then 1 when it is all the way down
                (so put it in and then change to 1)
                HARD STOP - j write "down" or "up" and it'll switch between the 2
                """);
        panelsTelemetry.update();
        waitForStart();
        ServoImplEx[] turret = new ServoImplEx[] {
                hardwareMap.get(ServoImplEx.class, "t1"),
                hardwareMap.get(ServoImplEx.class, "t2")
        };
        turret[0].setPwmRange(new PwmControl.PwmRange(500.0, 2500.0));
        turret[1].setPwmRange(new PwmControl.PwmRange(500.0, 2500.0));
        Servo hood = hardwareMap.get(ServoImplEx.class, "hood");
        Servo stop = hardwareMap.get(ServoImplEx.class, "stop");
        while (opModeIsActive()) {
            if (zeroTurret) {
                turret[0].setPosition(turretAngle);
                turret[1].setPosition(1-turretAngle);
            }
            if (zeroHood) {
                hood.setPosition(hoodAngle);
            }
            if (zeroHardStop) {
                if (Objects.equals(hardStop, "down")) {
                    stop.setPosition(STOP_DOWN);
                } else {
                    stop.setPosition(STOP_UP);
                }
            }
        }
    }
}
*/