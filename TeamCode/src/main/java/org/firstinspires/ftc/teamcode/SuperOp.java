package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.teamcode.Autonomous.VisionOpModes.CVCamera;
import org.firstinspires.ftc.teamcode.Autonomous.VisionOpModes.CVTest;

import java.util.Queue;


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

    public static final String VUFORIA_KEY =
            "AUAq88//////AAABmU+bO6dpUU4BreRJC5efYI1U4Fc5EvLiP5eGiT94wpCspMiACoccxAAVAgEOcCw87pTuHz671RvMDs3dtUBYrJNGI/x/bm60AsIdy3J7prt5EP8xeJuiKjWX32EoIhEsRnqZPpQOmCh11Q5vboZhsCNkNGMNWUIufrVa2g4SKwkSAjaAdOla8w/LwPKbiQBYvwbikpCb01LQg8iVYzWJHBfWLbQcXbuEBQIG9VSgGzyz4RStzgfG5mCTO4UZQbs7P3b/oJIf2rSzd7Ng1HmpHjldX8uFnLMuvIjgG/mJENP/edAw51wRui/21dV8QNdhV8KwP+KBdgpyVBMj44+OlN4ZrGGRkxYDNzd7yptjiGfe";

    public DcMotor FrontLeftDrive = null;
    public DcMotor FrontRightDrive = null;
    public DcMotor BackLeftDrive = null;
    public DcMotor BackRightDrive = null;
    public DcMotor LeftStoneRamp = null;
    public DcMotor RightStoneRamp = null;
    public DcMotor LatchMotor = null;
    public DcMotor SlideMotor = null;

    public Servo Gripper = null;
    public Servo ExtensionRight = null;
    public Servo ExtensionLeft  = null;
    public Servo Foundation = null;

    // enums used in build/player autonomi
    public enum BUILDSTATUS {FLIPPER, TOFOUNDATION, DRAG, AROUND, MOVE, PARK, STOP}
    public enum PLAYERSTATUS {FLIPPER, GRAB, TOBLOCK, TOBLOCK2, AWAY, AGAIN, AWAY2, DECISION, PARK, STOP}
    public enum CamType{INTERNAL, WEBCAM}

    public Accel_Drive accelDrive;

    public int startPoint = 1;
    private static final String TFOD_MODEL_ASSET = "Skystone.tflite";
    private static final String LABEL_FIRST_ELEMENT = "Stone";
    private static final String LABEL_SECOND_ELEMENT = "Skystone";
    public double multi = 1;
    public double x_speed;
    public double y_speed;
    public double w_speed;
    public double auto_x_speed;
    public double auto_y_speed;
    public double auto_w_speed;
    public double deadZone;
    public int tfodMonitorViewId;

    static final double COUNTS_PER_MOTOR_REV = 1440;            // eg: TETRIX Motor Encoder
    static final double     DRIVE_GEAR_REDUCTION    = 2.0 ;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double     DRIVE_SPEED             = 0.6;
    static final double     TURN_SPEED              = 0.5;
    static final double dead_zone = 0.05;
    @Override
    public void init() {
        // Initialize the hardware variables
        FrontLeftDrive  = hardwareMap.get(DcMotor.class, "FrontLeftDrive");
        FrontRightDrive = hardwareMap.get(DcMotor.class, "FrontRightDrive");
        BackLeftDrive  = hardwareMap.get(DcMotor.class, "BackLeftDrive");
        BackRightDrive = hardwareMap.get(DcMotor.class, "BackRightDrive");
        LatchMotor = hardwareMap.get(DcMotor.class, "LatchMotor");
        LeftStoneRamp = hardwareMap.get(DcMotor.class, "LeftStoneRamp");
        RightStoneRamp = hardwareMap.get(DcMotor.class, "RightStoneRamp");
        SlideMotor = hardwareMap.get(DcMotor.class, "SlideMotor");

        ExtensionLeft = hardwareMap.get(Servo.class, "ExtensionLeft");
        ExtensionRight = hardwareMap.get(Servo.class, "ExtensionRight");
        Gripper = hardwareMap.get(Servo.class, "Gripper");
        Foundation = hardwareMap.get(Servo.class, "Foundation");


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

        deadZone = .05;

        accelDrive = new Accel_Drive();

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


     */
    public void teleDrive(double x, double y, double w) {
        FrontLeftDrive.setPower((y_speed * y) + (x_speed * x)+ (w_speed* w));
        FrontRightDrive.setPower((y_speed * y) - (x_speed * x) - (w_speed * w));
        BackLeftDrive.setPower(((y_speed * y) - (x_speed * x) + (w_speed * w)));
        BackRightDrive.setPower((y_speed * y) + (x_speed * x) - (w_speed * w));
    }
    public void initCamera(CVCamera camera, CamType type){ // Also needs to be moved

        // Initialize Vuforia
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();
        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        switch (type) {
            case INTERNAL:
                parameters.cameraDirection = VuforiaLocalizer.CameraDirection.FRONT;
                // Dims+res of image vary by camera,
                camera.cameraParams = new CVCamera.CameraParams(1280, 720, 1080);
                break;
            case WEBCAM:
                parameters.cameraName = hardwareMap.get(WebcamName.class, "Webcam 1");
                camera.cameraParams = new CVCamera.CameraParams(448, 800, 1080);
                break;
        }
        camera.vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Initialize object detector
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minimumConfidence = 0.8; // May need to experiment with this value
        camera.tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, camera.vuforia);
        //camera.tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_FIRST_ELEMENT, LABEL_SECOND_ELEMENT);
        camera.tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_SECOND_ELEMENT);
        //camera.tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_FIRST_ELEMENT);
        camera.tfod.activate();
    }

    public void updateMotors(){
        FrontLeftDrive.setPower(multi*accelDrive.motorPowers[0]);
        FrontRightDrive.setPower(accelDrive.motorPowers[1]);
        BackLeftDrive.setPower(accelDrive.motorPowers[2]);
        BackRightDrive.setPower(multi*accelDrive.motorPowers[3]);
    }

    public void drive(double x, double y, double w){
        genericDrive(
                auto_x_speed*x,
                auto_y_speed*y*startPoint,
                auto_w_speed*w);
    }

    /*
    public void teleDrive(double x, double y, double w){
        generi(x_speed*x, y_speed*y, w_speed*w);
    }*/

    public void genericDrive(double x, double y, double w){
        accelDrive.drive(x, y, w);
        updateMotors();
    }

    public void updateAndDrive(){
        accelDrive.update();
        updateMotors();
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

    /**
     * Uses gamepad1 to use
     */
    public void c_drive(){
        teleDrive(
                gamepad1.left_stick_x > dead_zone ? gamepad1.left_stick_x : gamepad1.left_stick_x < -dead_zone ? gamepad1.left_stick_x : 0,
                gamepad1.left_stick_y > dead_zone ? -gamepad1.left_stick_y : gamepad1.left_stick_y < -dead_zone ? -gamepad1.left_stick_y : 0,
                gamepad1.right_stick_x > dead_zone ? gamepad1.right_stick_x : gamepad1.right_stick_x < -dead_zone ? gamepad1.right_stick_x : 0
        );
    }

    /*
    public void drive (double[] motorVals){
        double x = motorVals[0];
        double y = motorVals[1];
        double w = motorVals[2];
        drive(x, y, w);
    }
     */

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



    /*
    Why does Waldo wear stripes?

    Because he doesnt want to be spotted
     */
    /*public void encoderDriveWithSpeed(double speed, double desired) {
        //variables that store initial encoder values for the four motors
        double FrontLeftInitial = FrontLeftDrive.getCurrentPosition();
        double FrontRightInitial = FrontRightDrive.getCurrentPosition();
        double BackLeftInitial = BackLeftDrive.getCurrentPosition();
        double BackRightInitial = BackRightDrive.getCurrentPosition();
        //Front Back movement
        while (true) {
            //Front Left Motor movement
            if (desired + FrontLeftInitial > FrontLeftDrive.getCurrentPosition() + 10) {
                FrontLeftDrive.setPower(speed);
            } else if (desired + FrontLeftInitial < FrontLeftDrive.getCurrentPosition() - 10) {
                FrontLeftDrive.setPower(-speed);
            } else {
                FrontLeftDrive.setPower(0);
            }

            //Front Right Motor movement
            if (desired + FrontRightInitial > FrontRightDrive.getCurrentPosition() + 10) {
                FrontRightDrive.setPower(speed);
            } else if (desired + FrontRightInitial < FrontRightDrive.getCurrentPosition() - 10) {
                FrontRightDrive.setPower(-speed);
            } else {
                FrontRightDrive.setPower(0);
            }

            //Back Left Motor movement
            if (desired + BackLeftInitial > BackLeftDrive.getCurrentPosition() + 10) {
                BackLeftDrive.setPower(speed);
            } else if (desired + FrontLeftInitial < BackLeftDrive.getCurrentPosition() - 10) {
                BackLeftDrive.setPower(-speed);
            } else {
                BackLeftDrive.setPower(0);
            }

            //Back Right Motor movement
            if (desired + BackRightInitial > BackRightDrive.getCurrentPosition() + 10) {
                BackRightDrive.setPower(speed);
            } else if (desired + FrontLeftInitial < BackRightDrive.getCurrentPosition() - 10) {
                BackRightDrive.setPower(-speed);
            } else {
                BackRightDrive.setPower(0);
            }

            //Ends loop when all motors reach desired position
            if (FrontLeftDrive.getPower() == 0) {
                if (FrontRightDrive.getPower() == 0) {
                    if (BackLeftDrive.getPower() == 0) {
                        if (BackRightDrive.getPower() == 0) {
                            return;
                        }
                    }
                }
            }
            /*if (desired + FrontLeftInitial < FrontLeftDrive.getCurrentPosition() + 10 &&
                    desired + FrontLeftInitial > FrontLeftDrive.getCurrentPosition() - 10){
                if (desired + FrontRightInitial < FrontRightDrive.getCurrentPosition() + 10 &&
                        desired + FrontRightInitial > FrontRightDrive.getCurrentPosition() - 10) {
                    if (desired + BackLeftInitial < BackLeftDrive.getCurrentPosition() + 10 &&
                            desired + BackLeftInitial > BackLeftDrive.getCurrentPosition() - 10) {
                        if (desired + BackRightInitial < BackRightDrive.getCurrentPosition() + 10 &&
                                desired + BackRightInitial > BackRightDrive.getCurrentPosition() - 10) {
                            return;
                        }
                    }
                }
            }
        }
    }*/

    /*public void encoderStrafeWithSpeed(double speed, double desired) {
        //variables that store initial encoder values for the four motors
        double FrontLeftInitial = FrontLeftDrive.getCurrentPosition();
        double FrontRightInitial = FrontRightDrive.getCurrentPosition();
        double BackLeftInitial = BackLeftDrive.getCurrentPosition();
        double BackRightInitial = BackRightDrive.getCurrentPosition();
        //Constant that converts distance of strafing into distance the wheels need to rotate
        double constant = 1.7;
        //Strafing movement
        while (true) {
            //if robot needs to strafe right
            if (desired > 0) {
                if (constant * desired + FrontLeftInitial > FrontLeftDrive.getCurrentPosition() + 10) {
                    FrontLeftDrive.setPower(-speed);
                } else {
                    FrontLeftDrive.setPower(0);
                }
                if (constant * desired + FrontRightInitial > FrontRightDrive.getCurrentPosition() + 10) {
                    FrontRightDrive.setPower(-speed);
                } else {
                    FrontRightDrive.setPower(0);
                }
                if (constant * desired + BackLeftInitial > BackLeftDrive.getCurrentPosition() + 10) {
                    BackLeftDrive.setPower(speed);
                } else {
                    BackLeftDrive.setPower(0);
                }
                if (constant * desired + BackRightInitial > BackRightDrive.getCurrentPosition() + 10) {
                    BackRightDrive.setPower(speed);
                } else {
                    BackRightDrive.setPower(0);
                }
                //if robot needs to strafe left
            } else {
                if (constant * desired + FrontLeftInitial > FrontLeftDrive.getCurrentPosition() + 10) {
                    FrontLeftDrive.setPower(speed);
                } else {
                    FrontLeftDrive.setPower(0);
                }
                if (constant * desired + FrontRightInitial > FrontRightDrive.getCurrentPosition() + 10) {
                    FrontRightDrive.setPower(speed);
                } else {
                    FrontRightDrive.setPower(0);
                }
                if (constant * desired + BackLeftInitial > BackLeftDrive.getCurrentPosition() + 10) {
                    BackLeftDrive.setPower(-speed);
                } else {
                    BackLeftDrive.setPower(0);
                }
                if (constant * desired + BackRightInitial > BackRightDrive.getCurrentPosition() + 10) {
                    BackRightDrive.setPower(-speed);
                } else {
                    BackRightDrive.setPower(0);
                }
            }
            //Ends loop when all motors reach desired position
            if (FrontLeftDrive.getPower() == 0){
                if (FrontRightDrive.getPower() == 0) {
                    if (BackLeftDrive.getPower() == 0) {
                        if (BackRightDrive.getPower() == 0) {
                            return;
                        }
                    }
                }
            }
        }
    }*/

    /*public void encoderRotationWithSpeed(double speed, double desired) {
        //variables that store initial encoder values for the four motors
        double FrontLeftInitial = FrontLeftDrive.getCurrentPosition();
        double FrontRightInitial = FrontRightDrive.getCurrentPosition();
        double BackLeftInitial = BackLeftDrive.getCurrentPosition();
        double BackRightInitial = BackRightDrive.getCurrentPosition();
        //Constant that converts the degrees of robot rotation into distance the wheels need to rotate
        double constant = 1.7;
        //Rotating movement
        while (true) {
            //if robot needs to rotate clockwise
            if (desired > 0) {
                if (constant * desired + FrontLeftInitial > FrontLeftDrive.getCurrentPosition() + 10) {
                    FrontLeftDrive.setPower(-speed);
                } else {
                    FrontLeftDrive.setPower(0);
                }
                if (constant * desired + FrontRightInitial > FrontRightDrive.getCurrentPosition() + 10) {
                    FrontRightDrive.setPower(-speed);
                } else {
                    FrontRightDrive.setPower(0);
                }
                if (constant * desired + BackLeftInitial > BackLeftDrive.getCurrentPosition() + 10) {
                    BackLeftDrive.setPower(-speed);
                } else {
                    BackLeftDrive.setPower(0);
                }
                if (constant * desired + BackRightInitial > BackRightDrive.getCurrentPosition() + 10) {
                    BackRightDrive.setPower(-speed);
                } else {
                    BackRightDrive.setPower(0);
                }
                //if robot needs to rotate counter clockwise
            } else {
                if (constant * desired + FrontLeftInitial > FrontLeftDrive.getCurrentPosition() + 10) {
                    FrontLeftDrive.setPower(speed);
                } else {
                    FrontLeftDrive.setPower(0);
                }
                if (constant * desired + FrontRightInitial > FrontRightDrive.getCurrentPosition() + 10) {
                    FrontRightDrive.setPower(speed);
                } else {
                    FrontRightDrive.setPower(0);
                }
                if (constant * desired + BackLeftInitial > BackLeftDrive.getCurrentPosition() + 10) {
                    BackLeftDrive.setPower(speed);
                } else {
                    BackLeftDrive.setPower(0);
                }
                if (constant * desired + BackRightInitial > BackRightDrive.getCurrentPosition() + 10) {
                    BackRightDrive.setPower(speed);
                } else {
                    BackRightDrive.setPower(0);
                }
            }
            //Ends loop when all motors reach desired position
            if (FrontLeftDrive.getPower() == 0){
                if (FrontRightDrive.getPower() == 0) {
                    if (BackLeftDrive.getPower() == 0) {
                        if (BackRightDrive.getPower() == 0) {
                            return;
                        }
                    }
                }
            }
        }
    }*/

    /*public void basicEncoderDrive(double straightInches, double strafeInches) {
        //Front Left Motor movement
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
    }*/
}

