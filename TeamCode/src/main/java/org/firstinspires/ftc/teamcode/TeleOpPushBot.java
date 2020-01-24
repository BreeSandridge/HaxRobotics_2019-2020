package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="Push Bot Controller")
public class TeleOpPushBot extends OpMode {

    // declare motors
    public DcMotor FrontLeftDrive = null;
    public DcMotor FrontRightDrive = null;
    public DcMotor BackLeftDrive = null;
    public DcMotor BackRightDrive = null;

    public double x_speed;
    public double y_speed;
    public double w_speed;

    public void init() {
        // initialize the motors
        FrontLeftDrive  = hardwareMap.get(DcMotor.class, "FrontLeftDrive");
        FrontRightDrive = hardwareMap.get(DcMotor.class, "FrontRightDrive");
        BackLeftDrive  = hardwareMap.get(DcMotor.class, "BackLeftDrive");
        BackRightDrive = hardwareMap.get(DcMotor.class, "BackRightDrive");

        // Reverse directions on the right motors
        // so that "forward" and "backward" are the same number for both sides
        FrontLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        FrontRightDrive.setDirection(DcMotor.Direction.REVERSE);
        BackLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        BackRightDrive.setDirection(DcMotor.Direction.REVERSE);

        // speed variables
        x_speed = .75;
        y_speed = .40;
        w_speed = .45;
    }

    public void loop() {
        drive(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x);
    }

    // Mechanum wheel implementation
    // Accepts amount to move left/right (x), move up/down (y), and rotate (w)

    public void drive(double x, double y, double w) {
        FrontLeftDrive.setPower((y_speed * y) * 1 - (x_speed * x) * 1 + (w_speed * w));
        FrontRightDrive.setPower((y_speed * y) * 1 + (x_speed * x) * 1 - (w_speed * w));
        BackLeftDrive.setPower((y_speed * y) * 1 + (x_speed * x) * 1 + (w_speed * w));
        BackRightDrive.setPower((y_speed * y) * 1 - (x_speed * x) * 1 - (w_speed * w));
    }
}
