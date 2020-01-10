package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.teamcode.AreshPourkavoos.Accel_Drive;
import org.firstinspires.ftc.teamcode.Autonomous.CameraParams;

import java.util.List;

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
    public DcMotor FrontLeftDrive = null;
    public DcMotor FrontRightDrive = null;
    public DcMotor BackLeftDrive = null;
    public DcMotor BackRightDrive = null;
    public DcMotor LeftStoneRamp = null;
    public DcMotor RightStoneRamp = null;
    public DcMotor LatchMotor = null;

    public CRServo Flipper = null;
    public Servo Trapdoor = null;
    public Servo Latch = null;

    public enum STATUS {FlIPPER, START, TOBLOCK, APPROACH, GETBLOCK, AWAY, TOBUILD, RELEASEBLOCK, PARK, STOP}

    protected Accel_Drive accelDrive;
    public int startPoint = 1;
    public double x_speed;
    public double y_speed;
    public double w_speed;
    public double leftSpeedMultiplier = 1;
    public double rightSpeedMultiplier = 1;

    static final double COUNTS_PER_MOTOR_REV = 1440;            // eg: TETRIX Motor Encoder
    static final double     DRIVE_GEAR_REDUCTION    = 2.0 ;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double     DRIVE_SPEED             = 0.6;
    static final double     TURN_SPEED              = 0.5;

    public boolean isRunning = true;

    private static final String TFOD_MODEL_ASSET = "Skystone.tflite";
    private static final String LABEL_FIRST_ELEMENT = "Stone";
    private static final String LABEL_SECOND_ELEMENT = "Skystone";
    CameraParams cameraParams;

    private static final String VUFORIA_KEY =
            "AUAq88//////AAABmU+bO6dpUU4BreRJC5efYI1U4Fc5EvLiP5eGiT94wpCspMiACoccxAAVAgEOcCw87pTuHz671RvMDs3dtUBYrJNGI/x/bm60AsIdy3J7prt5EP8xeJuiKjWX32EoIhEsRnqZPpQOmCh11Q5vboZhsCNkNGMNWUIufrVa2g4SKwkSAjaAdOla8w/LwPKbiQBYvwbikpCb01LQg8iVYzWJHBfWLbQcXbuEBQIG9VSgGzyz4RStzgfG5mCTO4UZQbs7P3b/oJIf2rSzd7Ng1HmpHjldX8uFnLMuvIjgG/mJENP/edAw51wRui/21dV8QNdhV8KwP+KBdgpyVBMj44+OlN4ZrGGRkxYDNzd7yptjiGfe";
    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;


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

        Flipper = hardwareMap.crservo.get("Flipper");
        Trapdoor = hardwareMap.get(Servo.class, "Trapdoor");
        Latch = hardwareMap.get(Servo.class, "Latch");

        // Reverse directions on the right motors
        // so that "forward" and "backward" are the same number for both sides
        FrontLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        FrontRightDrive.setDirection(DcMotor.Direction.REVERSE);
        BackLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        BackRightDrive.setDirection(DcMotor.Direction.REVERSE);

        x_speed = .8;
        y_speed = .6;
        w_speed = .6;

        initVuforia();
        initTfod();
        tfod.activate();
        cameraParams = new CameraParams(0, 0, 0, 1280, 720, 1080);

    }

    public void setMode(DcMotor.RunMode mode){
        FrontRightDrive.setMode(mode);
        FrontLeftDrive.setMode(mode);
        BackLeftDrive.setMode(mode);
        BackRightDrive.setMode(mode);
    }

    // Mechanum wheel implementation
    // Accepts amount to move left/right (x), move up/down (y), and rotate (w)

    public void drive(double x, double y, double w) {
        FrontLeftDrive.setPower((y_speed * y) * startPoint - (x_speed * x) * startPoint + (w_speed* w));
        FrontRightDrive.setPower((y_speed * y) * startPoint + (x_speed * x) * startPoint - (w_speed * w));
        BackLeftDrive.setPower((y_speed * y) * startPoint + (x_speed * x) * startPoint + (w_speed * w));
        BackRightDrive.setPower((y_speed * y) * startPoint - (x_speed * x) * startPoint - (w_speed * w));
    }

    public void c_drive(){
        drive(
                gamepad1.left_stick_x,
                gamepad1.left_stick_y,
                    gamepad1.right_stick_x
        );
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


    private void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        //parameters.cameraDirection = CameraDirection.BACK;
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.FRONT;
        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the TensorFlow Object Detection engine.
    }

    /**
     * Initialize the TensorFlow Object Detection engine.
     */
    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minimumConfidence = 0.8;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_FIRST_ELEMENT, LABEL_SECOND_ELEMENT);
    }

    boolean skystoneAligned() {
        // getUpdatedRecognitions() will return null if no new information is available since
        // the last time that call was made.
        List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
        if (updatedRecognitions == null)
            return false;
        //telemetry.addData("# Object Detected", updatedRecognitions.size());

        // step through the list of recognitions and display boundary info.
        //int i = 0;
        for (Recognition recognition : updatedRecognitions) {
            String label = recognition.getLabel();
            if (!label.equals("Skystone"))
                continue;
            float left = recognition.getLeft(), top = recognition.getTop();
            float right = recognition.getRight(), bottom = recognition.getBottom();
            double blockPos = cameraParams.undoPerspective(left, top, right, bottom);
            double armOffset = 10;
            if (blockPos > armOffset)
                return true;
            /*
            telemetry.addData(String.format("label (%d)", i), label);
            telemetry.addData(String.format("  left,top (%d)", i), "%.03f , %.03f",
                    left, top);
            telemetry.addData(String.format("  right,bottom (%d)", i), "%.03f , %.03f",
                    right, bottom);
            telemetry.addData(String.format("  position (%d)", i), "%.03f",
                    (float) blockPos);
             */
        }
        return false;
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