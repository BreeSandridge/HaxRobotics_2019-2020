package org.firstinspires.ftc.teamcode.Examples;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;




@TeleOp(name="Basic: Mechanum Drive", group="Examples")
@Disabled
public class Example_Linear_Mechanum_OpMode extends LinearOpMode {


    // Declares a runtime timer to keep track of game time
    private ElapsedTime runtime = new ElapsedTime();
    // Declares four motors (Look at naming convention)
    private DcMotor FrontLeftDrive = null;
    private DcMotor FrontRightDrive = null;
    private DcMotor BackLeftDrive = null;
    private DcMotor BackRightDrive = null;
    //speed
    double speed = .8;


    @Override
    public void runOpMode() {
        // Add data to telemetry (console on phone)
        telemetry.addData("Status", "Initializing");
        // push the data and clears telemetry for new data
        telemetry.update();

        // Initialize the hardware variables
        FrontLeftDrive  = hardwareMap.get(DcMotor.class, "FrontLeftDrive");
        FrontRightDrive = hardwareMap.get(DcMotor.class, "FrontRightDrive");
        BackLeftDrive  = hardwareMap.get(DcMotor.class, "BackLeftDrive");
        BackRightDrive = hardwareMap.get(DcMotor.class, "BackRightDrive");


        // Sets direction
        FrontLeftDrive.setDirection(DcMotorSimple.Direction.FORWARD);
        FrontRightDrive.setDirection(DcMotorSimple.Direction.FORWARD);
        BackLeftDrive.setDirection(DcMotorSimple.Direction.FORWARD);
        BackRightDrive.setDirection(DcMotorSimple.Direction.FORWARD);

        // tells phone that initialization is complete
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            // Setup a variable for each drive wheel to save power level for telemetry
            double w = gamepad1.right_stick_x * speed;
            double y = gamepad1.left_stick_y * speed;
            double x = -gamepad1.left_stick_x * speed;


            // Sets motor power (going below or above zero just cuts the speed off
            BackLeftDrive.setPower(y - x - w);
            FrontLeftDrive.setPower(x + y - w);
            BackRightDrive.setPower(x + y + w);
            FrontRightDrive.setPower(y - x + w);



            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            // %.2f is similar to an escape character so you don't have to add it inline and can put it after.
            telemetry.addData("Controller Inputs", "x: (%.2f), y: (%.2f), w: (%.2f)", x, y, w);
            telemetry.update();
        }
    }
}
