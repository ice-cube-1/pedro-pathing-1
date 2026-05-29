package org.firstinspires.ftc.teamcode.robotParts;

import static java.lang.Math.toRadians;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.geometry.Pose;

@Configurable
public class Debug {
    public static boolean debugMode = false;
    public static int shooterVelocity = 0;
    public static double hoodAngle = 1.0;
    public static boolean tryRelocalise = false;
    public static boolean usePinpointLoc = false;
    public static double startPoseX = 0.0;
    public static double startPoseY = 0.0;
    public static double startPoseThetaDeg = 0.0;
    public static Pose startPose() { return new Pose(startPoseX, startPoseY, toRadians(startPoseThetaDeg)); }
}
