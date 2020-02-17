package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="TeleOpStates")
public class TeleOpStates extends SuperOp {

    // used to move LinearSlide up before extending swing
    // used in controllerExtension
    private ElapsedTime slideTime = new ElapsedTime();

    // Modifier values for linear slide
    // probably around .9
    final private double positiveLinearSlideModifier = 1;
    final private double negativeLinearSlideModifier = .7;

    private boolean grabberState = false;
    // open position is 1, closed is 0
    private final double grabberOpenPos = -1;
    private final double grabberClosedPos = 0;
    private boolean foundationState = false;
    private boolean extensionState = false;


    @Override
    public void loop() {
        // call controller drive
        controllerDrive();
        // update telemetry
        telemetry.addData("Front Right: ", FrontRightDrive.getPower());
        telemetry.addData("Back Left: ", BackLeftDrive.getPower());
        telemetry.addData("Back Right: ", BackRightDrive.getPower());
        telemetry.addData("Front Left: ", FrontLeftDrive.getPower());
        telemetry.addData("Foundation Grabber:", Foundation.getPosition());
        telemetry.addData("Extension Left", ExtensionLeft.getPosition());
        telemetry.addData("Extension Right", ExtensionRight.getPosition());
        telemetry.update();
    }

    // Controls all TeleOp Methods
    private void controllerDrive(){
        // call c_drive (SuperOp, 213)
        c_drive();
        // call controllerIntake
        controllerIntake();
        // call controllerFoundation
        controllerFoundation();
        // call controllerLinearSlide
        controllerLinearSlide();
        // call controllerExtension
        controllerExtension();
        // call controllerGrabber
        controllerGrabber();
        // call controllerLatch
        controllerLatch();

        extensionTest();
    }


    // Controls the Block Grabber
    private void controllerGrabber() {

        if (gamepad2.b){
            grabberState = true;
        } else if (gamepad2.a) {
            grabberState = false;
        }
        Gripper.setPosition(grabberState ? grabberOpenPos : grabberClosedPos);
    }

    // Controls Extension servo (Linear progressor)
    private void controllerExtension(){

        if(gamepad2.x) {
            slideTime.reset();
            while (slideTime.seconds() < 1.5 ) {
                SlideMotor.setPower(0.4);
            }
            Gripper.setPosition(.25);
            extensionState = false;
            slideTime.reset();
        } else if (gamepad2.y) {
            slideTime.reset();
            while (slideTime.seconds() < .5) {
                SlideMotor.setPower(-0.4);
            }
            extensionState = true;
            slideTime.reset();
        }
        ExtensionLeft.setPosition(extensionState ? 0 : 0.9);
        ExtensionRight.setPosition(extensionState ? 0.9: 0);
    }


    // Controls linear slide
    private void controllerLinearSlide(){
        SlideMotor.setPower(linearSlidePowerConverter(gamepad2.left_stick_y));
    }

    // Alters LinearSlide for correct power
    private double linearSlidePowerConverter(double pow) {
        if (pow > deadZone) {
            return pow * positiveLinearSlideModifier;
        } else {
            return pow < -deadZone ? pow * negativeLinearSlideModifier : 0;
        }
    }

    // Servo for foundation
    private void controllerFoundation(){
        if (gamepad2.dpad_up) {
            foundationState = true;
        } else if(gamepad2.dpad_down){
            foundationState = false;
        }
        Foundation.setPosition(foundationState ? 0 : 1);
    }
    private void extensionTest(){
        if (gamepad2.right_bumper) {
            extensionState = true;
        } else if(gamepad2.left_bumper){
            extensionState = false;
        }
        ExtensionLeft.setPosition(extensionState ? 0 : 0.9);
        ExtensionRight.setPosition(extensionState ? 0.9: 0);
    }

    private void controllerLatch(){
        if (gamepad2.dpad_left) {
            LatchMotor.setPower(.3);
        } else if(gamepad2.dpad_right){
            LatchMotor.setPower(gamepad2.dpad_right ? -.3 : 0);
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
        RightStoneRamp.setPower(-power*0.9);
        LeftStoneRamp.setPower(power*0.9);
    }


    // Debugging tool for TeleOp
    // call all debug methods
    /*private void controllerDriveDebug(){
        c_driveDebug();

        controllerIntakeDebug();

        controllerFoundationDebug();

        controllerLinearSlideDebug();

        controllerExtensionDebug();

        controllerGrabberDebug();

        telemetry.update();
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

      //Controls Intake System by given priority to driver intake, then outtake, then operator
      //intake, then outtake

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

    private void controllerGrabberUnitTest() {
        Gripper.setPosition(grabberOpenPos);
        sleep_secs(3);
        Gripper.setPosition(grabberClosedPos);
        sleep_secs(3);
        Gripper.setPosition(grabberOpenPos);
        sleep_secs(3);
    }

    private void controllerLinearSlideUnitTest(){
        SlideMotor.setPower(linearSlidePowerConverter(1));
        sleep_secs(1);
        SlideMotor.setPower(linearSlidePowerConverter(0));
        sleep_secs(1);
    }

    private void controllerExtensionUnitTest(){
        ExtensionLeft.setPosition(.3);
        sleep_secs(.5);
        ExtensionLeft.setPosition(-.3);
        sleep_secs(-.5);
        ExtensionLeft.setPosition(0);
    } */
}
