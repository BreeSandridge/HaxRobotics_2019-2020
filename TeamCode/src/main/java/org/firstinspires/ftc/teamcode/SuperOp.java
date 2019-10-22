package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.AreshPourkavoos.Accel_Drive;

// extend OpMode so future classes will extend SuperOp Instead
// implements is for interfaces

/*
SuperOp class
The SuperOp class is the OpMode of the robot,
containing the motors and an AccelDrive through which
the robot can accelerate and decelerate
in a trapezoid drive pattern.
(See AreshPourkavoos/Accel_Drive.java)
 */

public abstract class SuperOp extends OpMode implements SuperOp_Interface {

    DcMotor FrontLeftDrive = null;
    DcMotor FrontRightDrive = null;
    DcMotor BackLeftDrive = null;
    DcMotor BackRightDrive = null;
    protected Accel_Drive accelDrive;
    boolean encoder;



    static final double COUNTS_PER_MOTOR_REV = 1440;            // eg: TETRIX Motor Encoder
    static final double     DRIVE_GEAR_REDUCTION    = 2.0 ;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double     DRIVE_SPEED             = 0.6;
    static final double     TURN_SPEED              = 0.5;

    @Override
    public void init() {
        // Initialize the hardware variables
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

        // Pass the motors to the AccelDrive so it can access them
        // (may later be bundled into a class or lookup table)
        accelDrive = new Accel_Drive(FrontLeftDrive, FrontRightDrive,
                                      BackLeftDrive,  BackRightDrive);

        encoder = false;
    }

    // Mechanum wheel implementation
    // Accepts amount to move left/right (x), move up/down (y), and rotate (w)
    @Override
    public void drive(double x, double y, double w) {
        FrontLeftDrive.setPower(x+y+w);
        FrontRightDrive.setPower(x+y-w);
        BackLeftDrive.setPower(-x+y+w);
        BackRightDrive.setPower(x+y-w);
    }

    // Wait for a given number of seconds (t)
    // Is currently deprecated and will likely remain that way,
    // as this function would halt all data collection, etc.
    // until the wait time is over
    public void sleep_secs(double t){
        try {
            Thread.sleep((long)(t*1000));
        }
        catch(InterruptedException e){
        }
    }

    // Perform a trapezoid drive with a max translational velocity of (x, y)
    // and max rotation of w lasting t seconds
    // Would be called by implementation, is not yet
    @Override
    public void t_drive(double x, double y, double w, double t) {
        DriveParams newParams = new DriveParams(x, y, w, t);
        accelDrive.pushCommand(newParams);
    }


    /*public void setEncoder(boolean on) {
        encoder = on;
        if (on) {

            telemetry.addData("Setting encoders", 0);
            FrontLeftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            FrontRightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            BackLeftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            BackRightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);



            telemetry.addData("Set encoders", 0);
        }
    }*/

    public void basicEncoderDrive(double straightInches, double strafeInches) {
        int newFrontLeftTarget, newFrontRightTarget, newBackLeftTarget, newBackRightTarget;

        // Determine new target position, and pass to motor controller
        newFrontLeftTarget = FrontLeftDrive.getCurrentPosition() - (int) (straightInches * COUNTS_PER_INCH);
        newFrontRightTarget = FrontRightDrive.getCurrentPosition() + (int) (straightInches * COUNTS_PER_INCH);
        newBackLeftTarget = BackLeftDrive.getCurrentPosition() - (int) (straightInches * COUNTS_PER_INCH);
        newBackRightTarget = BackRightDrive.getCurrentPosition() + (int) (straightInches * COUNTS_PER_INCH);

        FrontLeftDrive.setTargetPosition(newFrontLeftTarget);
        BackLeftDrive.setTargetPosition(newBackLeftTarget);
        FrontRightDrive.setTargetPosition(newFrontRightTarget);
        BackRightDrive.setTargetPosition(newBackRightTarget);

        BackRightDrive.setPower(DRIVE_SPEED);
        BackLeftDrive.setPower(DRIVE_SPEED);
        FrontRightDrive.setPower(DRIVE_SPEED);
        FrontLeftDrive.setPower(DRIVE_SPEED);

        FrontLeftDrive.setMode (DcMotor.RunMode.RUN_TO_POSITION);
        FrontRightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        BackLeftDrive.setMode  (DcMotor.RunMode.RUN_TO_POSITION);
        BackRightDrive.setMode (DcMotor.RunMode.RUN_TO_POSITION);

        // Determine new target position, and pass to motor controller
        double strafeConstant = 1.3;

        if(strafeInches > 0) {
            newFrontLeftTarget = FrontLeftDrive.getCurrentPosition() - (int) (strafeConstant * strafeInches * COUNTS_PER_INCH);
            newFrontRightTarget = FrontRightDrive.getCurrentPosition() - (int) (strafeConstant * strafeInches * COUNTS_PER_INCH);
            newBackLeftTarget = BackLeftDrive.getCurrentPosition() + (int) (strafeConstant * strafeInches * COUNTS_PER_INCH);
            newBackRightTarget = BackRightDrive.getCurrentPosition() + (int) (strafeConstant * strafeInches * COUNTS_PER_INCH);
        } else {
            newFrontLeftTarget = FrontLeftDrive.getCurrentPosition() + (int) (strafeConstant * strafeInches * COUNTS_PER_INCH);
            newFrontRightTarget = FrontRightDrive.getCurrentPosition() + (int) (strafeConstant * strafeInches * COUNTS_PER_INCH);
            newBackLeftTarget = BackLeftDrive.getCurrentPosition() - (int) (strafeConstant * strafeInches * COUNTS_PER_INCH);
            newBackRightTarget = BackRightDrive.getCurrentPosition() - (int) (strafeConstant * strafeInches * COUNTS_PER_INCH);
        }

        FrontLeftDrive.setTargetPosition(newFrontLeftTarget);
        BackLeftDrive.setTargetPosition(newBackLeftTarget);
        FrontRightDrive.setTargetPosition(newFrontRightTarget);
        BackRightDrive.setTargetPosition(newBackRightTarget);

        BackRightDrive.setPower(DRIVE_SPEED);
        BackLeftDrive.setPower(DRIVE_SPEED);
        FrontRightDrive.setPower(DRIVE_SPEED);
        FrontLeftDrive.setPower(DRIVE_SPEED);

        FrontLeftDrive.setMode (DcMotor.RunMode.RUN_TO_POSITION);
        FrontRightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        BackLeftDrive.setMode  (DcMotor.RunMode.RUN_TO_POSITION);
        BackRightDrive.setMode (DcMotor.RunMode.RUN_TO_POSITION);
    }
}
