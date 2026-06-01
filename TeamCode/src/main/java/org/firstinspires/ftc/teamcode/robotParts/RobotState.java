package org.firstinspires.ftc.teamcode.robotParts;

import com.pedropathing.geometry.Pose;

public class RobotState {
    public static RobotConstants.Alliance ALLIANCE_COLOUR = RobotConstants.Alliance.BLUE;
    public static Pose AUTO_END_POSE = new Pose(Debug.startPoseX, Debug.startPoseY, Debug.startPoseThetaDeg);
    public static int NUM_TO_ATTEMPT = 0;
    public static String displayNumToAttempt(int n) {
        switch (n) {
            case -1: displayNumToAttempt(NUM_TO_ATTEMPT);
            case 0: return "3 BALLS - NO SPIKE";
            case 1: return "6 BALLS - 1 SPIKE";
            case 2: return "9 BALLS - 2 SPIKES";
            case 3: return "12 BALLS - 3 SPIKES";
            default: return "ERROR IN WHAT TO ATTEMPT";
        }
    }
}
