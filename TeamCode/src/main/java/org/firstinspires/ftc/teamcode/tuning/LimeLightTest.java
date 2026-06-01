package org.firstinspires.ftc.teamcode.tuning;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.robotcore.external.navigation.Position;

import static java.lang.Math.atan;
import static java.lang.Math.sqrt;
import static java.lang.Math.toDegrees;

import java.util.List;

@TeleOp(name = "testing limelight", group = "tuning")
public class LimeLightTest extends LinearOpMode {

    private Limelight3A limelight;

    @Override
    public void runOpMode() {

        waitForStart();

        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.setPollRateHz(100);
        limelight.start();
        limelight.pipelineSwitch(0);

        while (opModeIsActive()) {

            LLResult result = limelight.getLatestResult();

            if (result != null && result.isValid()) {

                List<LLResultTypes.FiducialResult> fiducials = result.getFiducialResults();

                for (LLResultTypes.FiducialResult fiducial : fiducials) {

                    Pose3D pose = fiducial.getTargetPoseCameraSpace();
                    Position pos = pose.getPosition();

                    double range = sqrt(pos.x * pos.x + pos.z * pos.z);
                    double bearing = toDegrees(atan(pos.x / pos.z));

                    telemetry.addData("Tag ID", fiducial.getFiducialId());
                    telemetry.addData("Range (m)", range);
                    telemetry.addData("bearing", bearing);
                }

                telemetry.update();
            }
        }
    }
}