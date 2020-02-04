package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="TeleOpStates")
public class TeleOpStates extends SuperOp {

    private ElapsedTime slideTime = new ElapsedTime();
    // Modifier values for linear slide
    // prolly around .9
    final private double positiveLinearSlideModifier = 1;
    final private double negativeLinearSlideModifier = .7;

    // true == Open && false == Closed
    private boolean grabberState = false;
    private final double grabberOpenPos = 1;
    private final double grabberClosedPos = 0;
    private final double grabberOpenPosRight = -1;
    private final double grabberClosedPosRight = 0;



    @Override
    public void loop() {
        controllerDrive();
        //controllerDriveDebug();

        //controllerExtensionDebug();

        //controllerGrabberDebug();

        telemetry.update();
    }




    // Debugging tool for TeleOp
    private void controllerDriveDebug(){
        c_driveDebug();

        controllerIntakeDebug();

        controllerFoundationDebug();

        controllerLinearSlideDebug();

        controllerExtensionDebug();

        controllerGrabberDebug();

        telemetry.update();
    }

    // Controls all TeleOp Methods
    private void controllerDrive(){
        c_drive();

        controllerIntake();

        controllerFoundation();

        controllerLinearSlide();

        controllerExtension();

        controllerGrabber();

        controllerLatch();
    }





    // Controls the Block Grabber
    private void controllerGrabber() {
        if (gamepad2.b) {
            grabberState = true;
        } else if (gamepad2.a) {
            grabberState = false;
        }
        Gripper.setPosition(grabberState ? grabberOpenPos : grabberClosedPos);
    }

    private void controllerGrabberUnitTest() {
        Gripper.setPosition(grabberOpenPos);
        sleep_secs(3);
        Gripper.setPosition(grabberClosedPos);
        sleep_secs(3);
        Gripper.setPosition(grabberOpenPos);
        sleep_secs(3);
    }

    // Controls Extension servo (Linear progressor)
    private void controllerExtension(){
        if(gamepad2.x) {
            if(slideTime.seconds() < .5) {
                SlideMotor.setPower(.2);
                slideTime.reset();
            }
            ExtensionLeft.setPosition(1);
            ExtensionRight.setPosition(0);
        } else if (gamepad2.y) {
            ExtensionLeft.setPosition(0);
            ExtensionRight.setPosition(1);
            if (slideTime.seconds() < .5) {
                SlideMotor.setPower(-.2);
                slideTime.reset();
            }
        }
    }
    private void controllerExtensionUnitTest(){
        ExtensionLeft.setPosition(.3);
        sleep_secs(.5);
        ExtensionLeft.setPosition(-.3);
        sleep_secs(-.5);
        ExtensionLeft.setPosition(0);
    }

    // Controls linear slide
    private void controllerLinearSlide(){
        SlideMotor.setPower(-linearSlidePowerConverter(gamepad2.left_stick_y));
    }
    private void controllerLinearSlideUnitTest(){
        SlideMotor.setPower(linearSlidePowerConverter(1));
        sleep_secs(1);
        SlideMotor.setPower(linearSlidePowerConverter(0));
        sleep_secs(1);
    }

    // Alters LinearSlide for correct power
    private double linearSlidePowerConverter(double pow) {
        if (pow > deadZone) {
            return pow * positiveLinearSlideModifier;
        } else {
            return pow < -deadZone ? pow * negativeLinearSlideModifier : 0;
        }
    }

    // LatchMotor for foundation
    private void controllerFoundation(){
        if (gamepad2.dpad_up) {
            Foundation.setPosition(1);
        } else if(gamepad2.dpad_down){
            Foundation.setPosition(0);
        }
    }

    private void controllerLatch(){
        if (gamepad2.dpad_left) {
            LatchMotor.setPower(.4);
        } else if(gamepad2.dpad_right){
            LatchMotor.setPower(gamepad2.dpad_right ? .3 : 0);
        }
    }


    /*
     * Controls Intake System by given priority to driver intake, then outtake, then operator
     * intake, then outtake
     */
    private void controllerIntake(){
        // if only driver right trigger is pressed down
        if (gamepad1.right_trigger > 0.1 && gamepad1.left_trigger < 0.1) {
            intake(gamepad1.right_trigger);
        }
        // if only driver left trigger is pressed down
        else if (gamepad1.left_trigger > 0.1 && gamepad1.right_trigger < 0.1) {
            intake(-gamepad1.left_trigger);
        }
        // if only operator right trigger is pressed down
        else if (gamepad2.right_trigger > 0.1 && gamepad2.left_trigger < 0.1) {
            intake(gamepad2.right_trigger);
        }
        // if only operator left trigger is pressed down
        else if (gamepad2.left_trigger > 0.1 && gamepad2.right_trigger < 0.1) {
            intake(-gamepad2.left_trigger);
        }
        // else turn off intake
        else{
            intake(0);
        }
    }

    // Intake
    private void intake(float power){
        RightStoneRamp.setPower(-power);
        LeftStoneRamp.setPower(power);
    }






    // Controls the Block Grabber
    private void controllerGrabberDebug() {
        if (gamepad2.a) {
            Gripper.setPosition(1);
            telemetry.addLine("Gripper is up");
        } else if (gamepad2.b) {
            Gripper.setPosition(0);
            telemetry.addLine("Gripper is down");

        }

        telemetry.addData("> Grabber Position: ", Gripper.getPosition());
    }

    // Controls Extension servo (Linear progressor)
    private void controllerExtensionDebug(){

        ExtensionLeft.setPosition(gamepad2.right_stick_y);

        telemetry.addData("> Extension Power: ", ExtensionLeft.getPosition());
    }

    // Controls linear slide
    private void controllerLinearSlideDebug(){
        SlideMotor.setPower(-linearSlidePowerConverter(gamepad2.left_stick_y));
        telemetry.addData("> SlideMotor Power: ", SlideMotor.getPower());
    }

    // LatchMotor for foundation
    private void controllerFoundationDebug(){
        if (gamepad2.dpad_up) {
            LatchMotor.setPower(-.4);
            telemetry.addData("> LatchMotor Power: ", LatchMotor.getPower());
        } else {
            LatchMotor.setPower(gamepad2.dpad_down ? .3 : 0);
            telemetry.addData("> LatchMotor Power: ", LatchMotor.getPower());
        }
    }

    /*
     * Controls Intake System by given priority to driver intake, then outtake, then operator
     * intake, then outtake
     */
    private void controllerIntakeDebug(){
        // if only driver right trigger is pressed down
        if (gamepad1.right_trigger > 0.1 && gamepad1.left_trigger < 0.1) {
            intakeDebug(gamepad1.right_trigger);
        }
        // if only driver left trigger is pressed down
        else if (gamepad1.left_trigger > 0.1 && gamepad1.right_trigger < 0.1) {
            intakeDebug(-gamepad1.left_trigger);
        }
        // if only operator right trigger is pressed down
        else if (gamepad2.right_trigger > 0.1 && gamepad2.left_trigger < 0.1) {
            intakeDebug(gamepad2.right_trigger);
        }
        // if only operator left trigger is pressed down
        else if (gamepad2.left_trigger > 0.1 && gamepad2.right_trigger < 0.1) {
            intakeDebug(-gamepad2.left_trigger);
        }
        // else turn off intake
        else{
            intakeDebug(0);
        }
    }

    // Intake
    private void intakeDebug(float power){
        RightStoneRamp.setPower(-power);
        LeftStoneRamp.setPower(power);
        telemetry.addData("> RightStoneRamp Power: ", RightStoneRamp.getPower());
        telemetry.addData("> LeftStoneRamp Power: ", LeftStoneRamp.getPower());
    }
}
