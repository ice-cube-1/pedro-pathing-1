package org.firstinspires.ftc.teamcode.robotParts;

public class RobotConstants {
   public static double MANUAL_MULTIPLIER = 1.0;

    public static double TURRET_KP = 0.1;
    public static double TURRET_STEP = 1.8;

    public static double STOP_DOWN = 0.32;
    public static double STOP_UP = 1.0;

    public static double INTAKE_POWER = 0.6;
    public static double TRANSFER_POWER = 0.6;

    public static double KP_SHOOTER = 0.02;
    public static double HOOD_ANGLE = 1.0;

    public static double TURRET_MAX_DEGREES =
            ((35.0 / 15.0) * (20.0 / 82.0)) * 360.0;

    public static double TURRET_ZERO_DEG =
            ((35.0 / 15.0) * (20.0 / 82.0)) * 180.0;

    public static int SHOOTER_IDLE_VELOCITY = 400;
    public static double ROBOT_WIDTH_CM = 39.0;
    public static double ROBOT_LENGTH_CM = 46.0;

    public static double K_FF = 0.0004076634;
    public static double KFF_INTERCEPT = 0.0652076247;

    public static double OFFSET = 0.3;
}