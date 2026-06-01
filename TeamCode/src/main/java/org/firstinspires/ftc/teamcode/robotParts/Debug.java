package org.firstinspires.ftc.teamcode.robotParts;

import static java.lang.Math.toRadians;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.geometry.Pose;

@Configurable
public class Debug {
    public static boolean debugMode = true;
    public static int shooterVelocity = 0;
    public static double hoodAngle = 1.0;
    public static boolean tryRelocalise = true;
    public static boolean usePinpointLoc = true;
    public static double startPoseX = 72.0;
    public static double startPoseY = 72.0;
    public static double startPoseThetaDeg = 90.0;
    // in code everything assumes blue alliance - do 144-[found x position] if testing red alliance
    public static Pose startPose() { return new Pose(startPoseX, startPoseY, toRadians(startPoseThetaDeg)); }
}
