package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;


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
    //stopwatch class
    public ElapsedTime timer;


    public DcMotor FrontLeftDrive = null;
    public DcMotor FrontRightDrive = null;
    public DcMotor BackLeftDrive = null;
    public DcMotor BackRightDrive = null;
    public DcMotor LeftStoneRamp = null;
    public DcMotor RightStoneRamp = null;
    public DcMotor LatchMotor = null;
    public DcMotor FlipperMotor = null;
    public DcMotor SlideMotor = null;

    public Servo Gripper = null;
    public CRServo Extension  = null;

    // enums used in build/player autonomi
    public enum BUILDSTATUS {FLIPPER, TOFOUNDATION, DRAG, AROUND, MOVE, PARKY, PARKW, STOP}
    public enum PLAYERSTATUS {FLIPPER, TOBLOCK, AWAY, AGAIN, AWAY2, DECISION, PARKY, PARKW, STOP}

    public Accel_Drive accelDrive;
    public int startPoint = 1;
    public double x_speed;
    public double y_speed;
    public double w_speed;
    public double auto_x_speed;
    public double auto_y_speed;
    public double auto_w_speed;

    public double deadZone;

    public double leftSpeedMultiplier = 1;
    public double rightSpeedMultiplier = 1;
    public int tfodMonitorViewId;

    static final double COUNTS_PER_MOTOR_REV = 1440;            // eg: TETRIX Motor Encoder
    static final double     DRIVE_GEAR_REDUCTION    = 2.0 ;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double     DRIVE_SPEED             = 0.6;
    static final double     TURN_SPEED              = 0.5;

    //these allow the motor's
    //encoder values to be averaged
    //they represent the motor's encoder values
    public int fl = 0;
    public int fr = 0;
    public int bl = 0;
    public int br = 0;

    //averages of the motors go into these integers
    public int tp_avg = 0;
    public int tn_avg = 0;
    public int p_avg = 0;
    public int n_avg = 0;

    public boolean TOBLOCK = true;
    public boolean BUILD = false;


    @Override
    public void init() {

        // Initialize the hardware variables
        FrontLeftDrive  = hardwareMap.get(DcMotor.class, "FrontLeftDrive");
        FrontRightDrive = hardwareMap.get(DcMotor.class, "FrontRightDrive");
        BackLeftDrive  = hardwareMap.get(DcMotor.class, "BackLeftDrive");
        BackRightDrive = hardwareMap.get(DcMotor.class, "BackRightDrive");

        // autonomous arm
        LatchMotor = hardwareMap.get(DcMotor.class, "LatchMotor");

        // Intake
        LeftStoneRamp = hardwareMap.get(DcMotor.class, "LeftStoneRamp");
        RightStoneRamp = hardwareMap.get(DcMotor.class, "RightStoneRamp");

        // Basket (no longer used)
        //FlipperMotor = hardwareMap.get (DcMotor.class, "FlipperMotor");

        // Linear slide
        SlideMotor = hardwareMap.get(DcMotor.class, "SlideMotor");

        // System to move the gripper out
        Extension = hardwareMap.get(CRServo.class, "Extension");

        // gripper on arm
        Gripper = hardwareMap.get(Servo.class, "Gripper");




        tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());

        // Reverse directions on the right motors
        // so that "forward" and "backward" are the same number for both sides
        FrontLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        FrontRightDrive.setDirection(DcMotor.Direction.REVERSE);
        BackLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        BackRightDrive.setDirection(DcMotor.Direction.FORWARD);


        /**changed values **/
        x_speed = .75;
        y_speed = .40;
        w_speed = .45;

        timer = new ElapsedTime();

        auto_x_speed = .8;
        auto_y_speed = .6;
        auto_w_speed = .6;

        accelDrive = new Accel_Drive();

        deadZone = .05;
    }

    public void setMode(DcMotor.RunMode mode){
        FrontRightDrive.setMode(mode);
        FrontLeftDrive.setMode(mode);
        BackLeftDrive.setMode(mode);
        BackRightDrive.setMode(mode);

        // Pass the motors to the AccelDrive so it can access them
        // (may later be bundled into a class or lookup table)
        //accelDrive = new Accel_Drive();

    }


    // Mechanum wheel implementation
    // Accepts amount to move left/right (x), move up/down (y), and rotate (w)
    /*
    public void drive(double x, double y, double w) {
        FrontLeftDrive.setPower((auto_y_speed * y) * startPoint - (auto_x_speed * x) * startPointBuild + (auto_w_speed* w));
        FrontRightDrive.setPower((auto_y_speed * y) * startPoint + (auto_x_speed * x) * startPointBuild - (auto_w_speed * w));
        BackLeftDrive.setPower((auto_y_speed * y) * startPoint + (auto_x_speed * x) * startPointBuild + (auto_w_speed * w));
        BackRightDrive.setPower((auto_y_speed * y) * startPoint - (auto_x_speed * x) * startPointBuild - (auto_w_speed * w));
    }

    public void teleDrive(double x, double y, double w) {
        FrontLeftDrive.setPower((y_speed * y) - (x_speed * x)+ (w_speed* w));
        FrontRightDrive.setPower((yb_speed * y) + (x_speed * x) - (w_speed * w));
        BackLeftDrive.setPower((y_speed * y) + (x_speed * x) + (w_speed * w));
        BackRightDrive.setPower((y_speed * y) - (x_speed * x) - (w_speed * w));
    }
    */

    public void updateMotors(){
        FrontLeftDrive.setPower(accelDrive.motorPowers[0]);
        FrontRightDrive.setPower(accelDrive.motorPowers[1]);
        BackLeftDrive.setPower(accelDrive.motorPowers[2]);
        BackRightDrive.setPower(accelDrive.motorPowers[3]);
    }

    public void updateAndDrive(){
        accelDrive.update();
        updateMotors();
    }

    public void drive(double x, double y, double w){
        accelDrive.drive(
                auto_x_speed*x,
                auto_y_speed*y* startPoint,
                auto_w_speed*w);
        updateMotors();
    }

    public void teleDrive(double x, double y, double w){
        FrontLeftDrive.setPower((y_speed * y) - (x_speed * x)+ (w_speed* w));
        FrontRightDrive.setPower((y_speed * y) + (x_speed * x) - (w_speed * w));
        BackLeftDrive.setPower((y_speed * y) + (x_speed * x) + (w_speed * w));
        BackRightDrive.setPower((y_speed * y) - (x_speed * x) - (w_speed * w));
    }


    /**
     * Uses gamepad1 to use
     */
    public void c_driveDebug(){
        c_drive();

        telemetry.addData("> x: ", gamepad1.left_stick_x);
        telemetry.addData("> y: ", gamepad1.left_stick_y);
        telemetry.addData("> w: ", gamepad1.right_stick_x);

    }

    public void c_drive(){
        double x = (gamepad1.left_stick_x > .05 || gamepad1.left_stick_x < .05) ?
                -gamepad1.left_stick_x : 0;
        double y = (gamepad1.left_stick_y > .05 || gamepad1.left_stick_y < .05) ?
                -gamepad1.left_stick_y : 0;
        double w = (gamepad1.right_stick_x > .05 || gamepad1.right_stick_x < .05) ?
                gamepad1.right_stick_x : 0;

        teleDrive(x, y, w);
    }

    /*
    public void drive (double[] motorVals){
        double x = motorVals[0];
        double y = motorVals[1];
        double w = motorVals[2];
        drive(x, y, w);
    }
     */


    public void drive(double frontleft, double frontright, double backleft, double backright){
        FrontLeftDrive.setPower(frontleft);
        FrontRightDrive.setPower(frontright);
        BackLeftDrive.setPower(backleft);
        BackRightDrive.setPower(backright);
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
        Accel_Drive.DriveCommand newParams = new Accel_Drive.DriveCommand(x, y, w, t);
        accelDrive.pushCommand(newParams);
    }

    /**
     * This method allows forwards and backwards movement for the robot by running the motors
     * until a certain encoder value is reached
     * @param speed (the speed [-1, 1], at which the robot's wheels will turn)
     * @param desired the distance the robot will travel (positive for forwards, negative for backwards)
     */


    /*
    Why does Waldo wear stripes?

    Because he doesnt want to be spotted
     */

}

