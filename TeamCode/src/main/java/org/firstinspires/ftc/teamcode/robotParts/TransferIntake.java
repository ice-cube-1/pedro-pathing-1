package org.firstinspires.ftc.teamcode.robotParts;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import static org.firstinspires.ftc.teamcode.robotParts.RobotConstants.INTAKE_POWER;
import static org.firstinspires.ftc.teamcode.robotParts.RobotConstants.STOP_DOWN;
import static org.firstinspires.ftc.teamcode.robotParts.RobotConstants.STOP_UP;
import static org.firstinspires.ftc.teamcode.robotParts.RobotConstants.TRANSFER_POWER;

import androidx.annotation.NonNull;

import java.util.Locale;

public class TransferIntake {
    public enum IntakeStates {INTAKE, SHOOTING}
    private double intakePower = 0.0;
    public DcMotorEx intake;
    public DcMotorEx transfer;
    public Servo stop;
    private IntakeStates intakeState = IntakeStates.INTAKE;
    public TransferIntake(HardwareMap hardwareMap) {
        intake = hardwareMap.get(DcMotorEx.class, "intake");
        intake.setDirection(DcMotorSimple.Direction.REVERSE);
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        transfer = hardwareMap.get(DcMotorEx.class, "transfer");
        transfer.setDirection(DcMotorSimple.Direction.FORWARD);
        transfer.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        stop = hardwareMap.get(Servo.class, "stop");
        stop.setPosition(STOP_DOWN);
    }
    public void prepShooter() { stop.setPosition(STOP_UP); }

    public void shoot(boolean s) {
        if (s) {
            stop.setPosition(STOP_UP);
            transfer.setPower(0.0);
            intake.setPower(0.0);
            intakeState = IntakeStates.SHOOTING;
        } else {
            stop.setPosition(STOP_DOWN);
            transfer.setPower(0.0);
            intake.setPower(0.0);
            intakeState = IntakeStates.INTAKE;
        }
    }

    @NonNull
    public String toString() {
        return String.format(Locale.UK, "---INTAKE---\nState: %s, transfer power %.3f, intake power %.3f", intakeState, transfer.getPower(), intake.getPower());
    }
    public void intake(Double i) {intakePower = i;}
    public void update() {
        switch (intakeState) {
            case SHOOTING:
                transfer.setPower(TRANSFER_POWER);
                intake.setPower(INTAKE_POWER);
                break;
            case INTAKE:
                intake.setPower(intakePower);
                transfer.setPower(0.0);
                break;
        }
    }
}