package org.firstinspires.ftc.teamcode.robotParts;

import static org.firstinspires.ftc.teamcode.robotParts.RobotConstants.OFFSET;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.robotcore.external.navigation.Position;

public class Limelight {
    private final Limelight3A ll;
    private final int tagID;
    double lastDist = 0.0;
    public double lastAngle = 0.0;
    double mostRecent = -1000.0;
    private final ElapsedTime timer = new ElapsedTime();

    public Limelight(HardwareMap hardwareMap, int tagID) {
        ll = hardwareMap.get(Limelight3A.class, "limelight");
        ll.setPollRateHz(100);
        ll.start();
        ll.pipelineSwitch(0);
        this.tagID = tagID;
    }
    public void update() {
        LLResult result = ll.getLatestResult();
        if (result == null) return;
        if (!result.isValid()) return;
        for (LLResultTypes.FiducialResult tag : result.getFiducialResults()) {
            if (tag.getFiducialId() == tagID) {
                mostRecent = timer.milliseconds() - result.getStaleness();
                Pose3D pose = tag.getTargetPoseCameraSpace();
                Position pos = pose.getPosition();
                double yaw = pose.getOrientation().getYaw(AngleUnit.RADIANS);
                double targetX = pos.x - OFFSET * Math.sin(yaw);
                double targetZ = pos.z - OFFSET * Math.cos(yaw);
                lastDist = Math.sqrt(targetX * targetX + targetZ * targetZ);
                lastAngle = Math.toDegrees(Math.atan2(targetX, targetZ));
            }
        }
    }
}
