package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="Driver Controller")
public class DriverController extends SuperOp {


    private final float latch_cd = 500;

    private boolean trapdoorState = false;

    // can be written much more efficiently, just a basic DriverController
    @Override
    public void loop() {
       c_drive();


        /*
         * Controls Intake System by given priority to driver intake, then outtake, then operator
         * intake, then outtake
         */
       // if only driver right trigger is pressed down
       if (gamepad1.right_trigger > 0.05 && gamepad1.left_trigger < 0.05) {
           intake(gamepad1.right_trigger);
       }
       // if only driver left trigger is pressed down
       else if (gamepad1.left_trigger > 0.05 && gamepad1.right_trigger < 0.05) {
           intake(-gamepad1.left_trigger);
       }
       // if only operator right trigger is pressed down
       else if (gamepad2.right_trigger > 0.05 && gamepad2.left_trigger < 0.05) {
           intake(gamepad2.right_trigger);
        }
       // if only operator left trigger is pressed down
       else if (gamepad2.left_trigger > 0.05 && gamepad2.right_trigger < 0.05) {
           intake(-gamepad2.left_trigger);
       }
       // else turn off intake
       else{
           intake(0);
       }


       /*
        * Toggles trapdoor
        */
       if (gamepad2.a && timer.milliseconds() > latch_cd) {
           // sets to 1 if trapdoor state is == to true
           // otherwise set to 0
           Trapdoor.setPosition(trapdoorState ? 1 : 0);
           trapdoorState = !trapdoorState;
           timer.reset();
       }


       Flipper.setPower (gamepad2.left_stick_y);
       telemetry.addData(" > Flipper Power: ", Flipper.getPower());
       telemetry.update();



    }

    // Intake
    private void intake(float power){
        RightStoneRamp.setPower(power);
        LeftStoneRamp.setPower(power);
    }
}







/*
Two different doctors worked on my knee surgery

It was a joint operation
 */