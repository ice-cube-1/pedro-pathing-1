package org.firstinspires.ftc.teamcode.robotParts;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.geometry.Pose;

@Configurable
public class RobotState {
    public static RobotConstants.Alliance ALLIANCE_COLOUR = RobotConstants.Alliance.BLUE;
    public static Pose AUTO_END_POSE = new Pose(Debug.startPoseX, Debug.startPoseY, Debug.startPoseThetaDeg);
    public static int NUM_TO_ATTEMPT = 0;
    public static String displayNumToAttempt(int n, RobotConstants.ShootMode shootMode) {
        return switch (n) {
            case -1 -> displayNumToAttempt(NUM_TO_ATTEMPT, shootMode);
            case 0 -> "3 BALLS - NO SPIKE";
            case 1 -> "6 BALLS - 1 SPIKE";
            case 2 -> shootMode == RobotConstants.ShootMode.NEAR ? "9 BALLS - 2 SPIKES" : "9 BALLS - 1 SPIKE, 1 HP ZONE";
            case 3 -> shootMode == RobotConstants.ShootMode.NEAR ? "12 BALLS - 3 SPIKES" : "INVALID OPTION";
            default -> "ERROR IN WHAT TO ATTEMPT";
        };
    }
}
