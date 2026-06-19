package org.firstinspires.ftc.teamcode.robotParts;

import androidx.annotation.NonNull;

import com.bylazar.configurables.annotations.Configurable;

@Configurable
public class RobotConstants {
   public static double MANUAL_MULTIPLIER = 1.0;

    public static double TURRET_KP = 0.1;
    public static double TURRET_STEP = 1.8;

    public static double STOP_DOWN = 0.32;
    public static double STOP_UP = 1.0;

    public static double INTAKE_POWER = 1.0;
    public static double TRANSFER_POWER = 1.0;

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
    public static double FAR_ZONE_MULTIPLIER = 0.5;

    public enum Alliance {
        RED(144.0, 1.0, 24, "RED ALLIANCE"),
        BLUE(0.0, -1.0, 20, "BLUE ALLIANCE");
        private final double offset;
        public final double direction;
        public final int tagID;
        public final String str;
        Alliance(double offset, double direction, int tagID, String str) {
            this.offset = offset;
            this.direction = direction;
            this.tagID = tagID;
            this.str = str;
        }
        public double mirrorAngle(double angle) { return angle * (1 - direction); }
        public double mirrorX(double x) { return offset - direction * x; }
        @NonNull
        public String toString() { return this.str; }
    }
    public enum ShootMode {
        NEAR(0.02,TURRET_STEP, 0.0,1.0, "NEAR ZONE"),
        FAR(0.015, TURRET_STEP * FAR_ZONE_MULTIPLIER, 0.8, 0.85, "FAR ZONE");
        final double detectedKP;
        final double wrappingStep;
        final double distanceOffset;
        final double hoodPos;
        final String str;
        ShootMode(double detectedKP, double wrappingStep, double distanceOffset, double hoodPos, String str) {
            this.detectedKP = detectedKP;
            this.wrappingStep = wrappingStep;
            this.distanceOffset = distanceOffset;
            this.hoodPos = hoodPos;
            this.str = str;
        }
        @NonNull
        public String toString() { return this.str; }
    }
    public enum TurretState {
        DETECTED("tag detected"),
        WRAPPING("looking for tag");
        private final String str;
        TurretState(String str) {
            this.str = str;
        }
        @NonNull
        public String toString() { return this.str; }
    }
}