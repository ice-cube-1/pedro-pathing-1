package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.pedroPathing.Constants.createFollower;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;


class Robot {
    enum RobotState {START,TO_POINT,POINT,TO_PARK,PARK}
    Pose current_pos = new Pose(0,0,0);
    DcMotor elevator;
    Servo claw;
    Follower follower;
    RobotState state;
    ElapsedTime timer = new ElapsedTime();

    Robot(HardwareMap hardwareMap) {
        elevator = hardwareMap.get(DcMotor.class, "elevator");
        claw = hardwareMap.get(Servo.class,"claw");
        state = RobotState.START;
        follower = createFollower(hardwareMap);
        follower.setStartingPose(current_pos);
    }

    private void resetMotor(DcMotor motor) {
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setTargetPosition(0);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    private void gotoPos(Pose new_pos) {
        Path path = new Path(new BezierLine(current_pos, new_pos));
        follower.followPath(path);
        current_pos = new_pos;
    }

    public void tick() {
        switch (state) {
            case START:
                resetMotor(elevator);
                elevator.setTargetPosition(100);
                elevator.setPower(0.5);
                claw.setPosition(0.5);
                gotoPos(new Pose(30,0,45));
                state = RobotState.TO_POINT;
                break;
            case TO_POINT:
                if (!(follower.isBusy() || elevator.isBusy())) {
                    timer.reset();
                    state = RobotState.POINT;
                }
                break;
            case POINT:
                if (timer.seconds() < 1.0) claw.setPosition(1);
                else if (timer.seconds() < 3.0) claw.setPosition(0.5);
                else {
                    elevator.setTargetPosition(0);
                    gotoPos(new Pose(30,30,0));
                    state = RobotState.TO_PARK;
                }
                break;
            case TO_PARK:
                if (!(follower.isBusy() || elevator.isBusy())) {
                    state = RobotState.PARK;
                }
                break;
        }
    }
}

@TeleOp(name="Sample", group="Linear OpMode")
public class Sample extends LinearOpMode {
    Robot robot;
    @Override
    public void runOpMode() {
        robot = new Robot(hardwareMap);
        waitForStart();
        if (gamepad1.dpad_down) {

        }
        while (opModeIsActive()) {
            robot.tick();
        }
    }
}
