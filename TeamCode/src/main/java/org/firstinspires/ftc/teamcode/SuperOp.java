package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;


import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
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

    public ElapsedTime timer;


    public DcMotor FrontLeftDrive = null;
    public DcMotor FrontRightDrive = null;
    public DcMotor BackLeftDrive = null;
    public DcMotor BackRightDrive = null;
    public DcMotor LeftStoneRamp = null;
    public DcMotor RightStoneRamp = null;
    public DcMotor LatchMotor = null;
    public DcMotor FlipperMotor = null;

    //public Servo Flipper = null;
    public Servo Trapdoor = null;

    public Servo Latch = null;

    public enum BUILDSTATUS {FLIPPER, TOFOUNDATION, DRAG, AROUND, PARK, STOP}
    public enum PLAYERSTATUS {FLIPPER, TOBLOCK, AWAY, AGAIN, AWAY2, PARK, STOP}

    protected Accel_Drive accelDrive;
    public int startPointBuild = 1;
    public int startPointPlayer = 1;
    public double x_speed;
    public double y_speed;
    public double w_speed;
    public double auto_x_speed;
    public double auto_y_speed;
    public double auto_w_speed;

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
        FlipperMotor = hardwareMap.get (DcMotor.class, "FlipperMotor");

        //Flipper = hardwareMap.get(Servo.class, "Flipper");
        Trapdoor = hardwareMap.get(Servo.class, "Trapdoor");

        // Reverse directions on the right motors
        // so that "forward" and "backward" are the same number for both sides
        FrontLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        FrontRightDrive.setDirection(DcMotor.Direction.REVERSE);
        BackLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        BackRightDrive.setDirection(DcMotor.Direction.REVERSE);



        /**changed values **/
        x_speed = .75;
        y_speed = .40;
        w_speed = .45;

        timer = new ElapsedTime();



        auto_x_speed = .8;
        auto_y_speed = .6;
        auto_w_speed = .6;

        initVuforia();
        initTfod();
        tfod.activate();
        cameraParams = new CameraParams(0, 0, 0, 1280, 720, 1080);

        //Flipper.setPosition(.03);
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

    public void drive(double x, double y, double w) {
        FrontLeftDrive.setPower((auto_y_speed * y) * startPointPlayer - (auto_x_speed * x) * startPointBuild + (auto_w_speed* w));
        FrontRightDrive.setPower((auto_y_speed * y) * startPointPlayer + (auto_x_speed * x) * startPointBuild - (auto_w_speed * w));
        BackLeftDrive.setPower((auto_y_speed * y) * startPointPlayer + (auto_x_speed * x) * startPointBuild + (auto_w_speed * w));
        BackRightDrive.setPower((auto_y_speed * y) * startPointPlayer - (auto_x_speed * x) * startPointBuild - (auto_w_speed * w));
    }

    public void teleDrive(double x, double y, double w) {
        FrontLeftDrive.setPower((y_speed * y) - (x_speed * x)+ (w_speed* w));
        FrontRightDrive.setPower((y_speed * y) + (x_speed * x) - (w_speed * w));
        BackLeftDrive.setPower((y_speed * y) + (x_speed * x) + (w_speed * w));
        BackRightDrive.setPower((y_speed * y) - (x_speed * x) - (w_speed * w));
    }

    /**
     * Uses gamepad1 to use
     */
    public void c_drive(){
        teleDrive(
                -gamepad1.left_stick_x,
                -gamepad1.left_stick_y,
                gamepad1.right_stick_x
        );
    }

    public void drive (double[] motorVals){
        double x = motorVals[0];
        double y = motorVals[1];
        double w = motorVals[2];
        drive(x, y, w);
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

    public boolean skystoneAligned() {
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


}

