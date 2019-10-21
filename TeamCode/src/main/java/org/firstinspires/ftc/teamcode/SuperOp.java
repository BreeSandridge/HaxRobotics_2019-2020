package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

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



    static final double COUNTS_PER_MOTOR_REV = 1440;            // eg: TETRIX Motor Encoder
    static final double     DRIVE_GEAR_REDUCTION    = 2.0 ;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double     DRIVE_SPEED             = 0.6;
    static final double     TURN_SPEED              = 0.5;


    boolean encoder;

    @Override
    public void init() {
        encoder = false;
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
    }

    public void setEncoder(boolean on) {
        encoder = on;
        if (on) {

            telemetry.addData("Setting encoders", 0);
            FrontLeftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            FrontRightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            BackLeftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            BackRightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            FrontLeftDrive.setMode (DcMotor.RunMode.RUN_TO_POSITION);
            FrontRightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            BackLeftDrive.setMode  (DcMotor.RunMode.RUN_TO_POSITION);
            BackRightDrive.setMode (DcMotor.RunMode.RUN_TO_POSITION);

            telemetry.addData("Set encoders", 0);
        }
    }


    // Mechanum wheel implementation
    // Accepts amount to move left/right (x), move up/down (y), and rotate (w)
    @Override
    public void drive(double x, double y, double w) {
        if (!encoder) {
            FrontLeftDrive.setPower(x + y + w);
            FrontRightDrive.setPower(x + y - w);
            BackLeftDrive.setPower(-x + y + w);
            BackRightDrive.setPower(x + y - w);
        } else {

        }
    }



    public void basicEncoderDrive(double speed, double leftInches, double rightInches) {
        int newFrontLeftTarget, newFrontRightTarget, newBackLeftTarget, newBackRightTarget;



        // Determine new target position, and pass to motor controller
        newFrontLeftTarget = FrontLeftDrive.getCurrentPosition() + (int) (leftInches * COUNTS_PER_INCH);
        newFrontRightTarget = FrontRightDrive.getCurrentPosition() + (int) (rightInches * COUNTS_PER_INCH);
        newBackLeftTarget = BackLeftDrive.getCurrentPosition() + (int) (leftInches * COUNTS_PER_INCH);
        newBackRightTarget = BackRightDrive.getCurrentPosition() + (int) (rightInches * COUNTS_PER_INCH);

        FrontLeftDrive.setTargetPosition(newFrontLeftTarget);
        BackLeftDrive.setTargetPosition(newBackLeftTarget);
        FrontRightDrive.setTargetPosition(newFrontRightTarget);
        BackRightDrive.setTargetPosition(newBackRightTarget);



        BackRightDrive.setPower(speed);
        BackLeftDrive.setPower(speed);
        FrontRightDrive.setPower(speed);
        FrontLeftDrive.setPower(speed);



    }

    public void encoderDrive(double strafeInches, double rotationDegrees, double straightInches) {
        /**
        * uses variable straightInches to perform straight encoder rotations
         */

        // declares 3 variables to store desired location for encoders to move
        int newFrontLeftTarget, newFrontRightTarget, newBackLeftTarget, newBackRightTarget;

        // figures out how many encoder rotations are required to move straightInches
        newFrontLeftTarget = FrontLeftDrive.getCurrentPosition() + (int) (straightInches * COUNTS_PER_INCH);
        newFrontRightTarget = FrontRightDrive.getCurrentPosition() + (int) (straightInches * COUNTS_PER_INCH);
        newBackLeftTarget = BackLeftDrive.getCurrentPosition() + (int) (straightInches * COUNTS_PER_INCH);
        newBackRightTarget = BackRightDrive.getCurrentPosition() + (int) (straightInches * COUNTS_PER_INCH);

        // sets desired position relative to the robot for encoders to rotate
        FrontLeftDrive.setTargetPosition(newFrontLeftTarget);
        BackLeftDrive.setTargetPosition(newBackLeftTarget);
        FrontRightDrive.setTargetPosition(newFrontRightTarget);
        BackRightDrive.setTargetPosition(newBackRightTarget);

        // sets speed of motors for travel
        BackRightDrive.setPower(DRIVE_SPEED);
        BackLeftDrive.setPower(DRIVE_SPEED);
        FrontRightDrive.setPower(DRIVE_SPEED);
        FrontLeftDrive.setPower(DRIVE_SPEED);


        /**
         * uses variable strafeInches to perform strafe movements
         */

        // declares constant that converts strafeInches to rotation in the motors
        int x = 5;

        // figures out how many encoder rotations are required to move strafeInches
        if (strafeInches >= 0) {
            newFrontLeftTarget = FrontLeftDrive.getCurrentPosition() + (int) (x * strafeInches * COUNTS_PER_INCH);
            newFrontRightTarget = FrontRightDrive.getCurrentPosition() + (int) (-x * strafeInches * COUNTS_PER_INCH);
            newBackLeftTarget = BackLeftDrive.getCurrentPosition() + (int) (-x * strafeInches * COUNTS_PER_INCH);
            newBackRightTarget = BackRightDrive.getCurrentPosition() + (int) (x * strafeInches * COUNTS_PER_INCH);
        } else {
            newFrontLeftTarget = FrontLeftDrive.getCurrentPosition() + (int) (-x * strafeInches * COUNTS_PER_INCH);
            newFrontRightTarget = FrontRightDrive.getCurrentPosition() + (int) (x * strafeInches * COUNTS_PER_INCH);
            newBackLeftTarget = BackLeftDrive.getCurrentPosition() + (int) (x * strafeInches * COUNTS_PER_INCH);
            newBackRightTarget = BackRightDrive.getCurrentPosition() + (int) (-x * strafeInches * COUNTS_PER_INCH);
        }

        // sets desired position relative to the robot for encoders to rotate
        FrontLeftDrive.setTargetPosition(newFrontLeftTarget);
        BackLeftDrive.setTargetPosition(newBackLeftTarget);
        FrontRightDrive.setTargetPosition(newFrontRightTarget);
        BackRightDrive.setTargetPosition(newBackRightTarget);

        // sets speed of motors for travel
        BackRightDrive.setPower(DRIVE_SPEED);
        BackLeftDrive.setPower(DRIVE_SPEED);
        FrontRightDrive.setPower(DRIVE_SPEED);
        FrontLeftDrive.setPower(DRIVE_SPEED);
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
}
