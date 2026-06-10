package org.firstinspires.ftc.teamcode.robotParts;

import static org.firstinspires.ftc.teamcode.robotParts.RobotConstants.OFFSET;

import androidx.annotation.NonNull;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.robotcore.external.navigation.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

public class Limelight {
    private final Limelight3A ll;
    double lastDist = 0.0;
    public double lastAngle = 0.0;
    double mostRecent = -1000.0;
    private final ElapsedTime timer = new ElapsedTime();
    private final List<Pose> relocalisationPoses = new ArrayList<>();
    public boolean tryRelocalise = false;
    public Pose latest = new Pose();

    public Limelight(HardwareMap hardwareMap) {
        ll = hardwareMap.get(Limelight3A.class, "limelight");
        ll.setPollRateHz(100);
        ll.start();
        ll.pipelineSwitch(0);
    }
    public Optional<Double> update(Follower follower) {
        LLResult result = ll.getLatestResult();
        if (result == null || !result.isValid()) return Optional.empty();
        relocaliseTranslation(result, follower);
        return findDistAngle(result);
    }
    private Optional<Double> findDistAngle(LLResult result) {
        for (LLResultTypes.FiducialResult tag : result.getFiducialResults()) {
            if (tag.getFiducialId() == RobotState.ALLIANCE_COLOUR.tagID) {
                mostRecent = timer.milliseconds() - result.getStaleness();
                Pose3D pose = tag.getTargetPoseCameraSpace();
                Position pos = pose.getPosition();
                double yaw = pose.getOrientation().getYaw(AngleUnit.RADIANS);
                double targetX = pos.x - OFFSET * Math.sin(yaw);
                double targetZ = pos.z - OFFSET * Math.cos(yaw);
                lastDist = Math.sqrt(targetX * targetX + targetZ * targetZ);
                lastAngle = Math.toDegrees(Math.atan2(targetX, targetZ));
                return Optional.of(Math.atan2(targetX, targetZ));
            }
        }
        return Optional.empty();
    }
    private void relocaliseTranslation(LLResult result, Follower follower) {
        if (tryRelocalise || (Debug.debugMode && Debug.tryRelocalise)) {
            if (Math.abs(follower.getAngularVelocity()) > 1) {
                tryRelocalise = false;
                relocalisationPoses.clear();
            }
            relocalisationPoses.add(convertLLToPose(result.getBotpose(), follower));
        }
        if (relocalisationPoses.size() >= 50) {
            tryRelocalise = false;
            List<Pose> filteredPoses = filterPoses(relocalisationPoses);
            latest = averagePoses(filteredPoses, follower);
            if (!filteredPoses.isEmpty()) follower.setPose(latest);
            relocalisationPoses.clear();
        }
    }
    private Pose convertLLToPose(Pose3D limelightPose, Follower follower) {
        double xInches = limelightPose.getPosition().x * 39.3701;
        double yInches = limelightPose.getPosition().y * 39.3701;
        return new Pose(yInches + 72, -xInches + 72, follower.getHeading());
    }
    private List<Pose> filterPoses(List<Pose> relocalisationPoses) {
        int mid = relocalisationPoses.size() / 2;
        double[] xs = relocalisationPoses.stream()
                .mapToDouble(Pose::getX)
                .sorted()
                .toArray();
        double[] ys = relocalisationPoses.stream()
                .mapToDouble(Pose::getY)
                .sorted()
                .toArray();
        Pose medianPose = new Pose(xs[mid], ys[mid]);
        return relocalisationPoses.stream()
                .filter(p -> p.distanceFrom(medianPose) < 2)
                .collect(Collectors.toList());
    }
    private Pose averagePoses(List<Pose> filteredPoses, Follower follower) {
        return new Pose(
                filteredPoses.stream().mapToDouble(Pose::getX).average().orElse(10000000),
                filteredPoses.stream().mapToDouble(Pose::getY).average().orElse(10000000),
                follower.getHeading());
    }
    @NonNull
    public String toString() {
        return String.format(Locale.UK, "---LIMELIGHT---\nDistance: %.3f, Angle: %.3f, Detection age: %.3f",lastDist, lastAngle, mostRecent);
    }
}
