package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.Scanner;

@TeleOp(name="Driver Controller")
public class DriverController extends SuperOp {


    private final float latch_cd = 1000;

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


    /*private void dropArm() {
        switch (arm) {
            case INIT:
                // set target position
                currPosArm = Flipper.getPosition();
                targetPosArm = 1;
                arm = ARMSTATE.DROPPING;
                break;
            case RAISED:
                currPosArm = Flipper.getPosition();
                targetPosArm = currPosArm += 0.5;
                arm = ARMSTATE.DROPPING;
                break;
            case DROPPING:
                if (currPosArm >= targetPosArm) {
                    arm = ARMSTATE.DROPPED;
                }
                // want it to go up in increments so the program can take other inputs
                currPosArm = Flipper.getPosition();
                Flipper.setPosition(currPosArm + 0.1);
                break;
            // dont want to do anything if the arm is raising or dropping
            case DROPPED:
                break;
            case RAISING:
                break;
        }
    }

    // find way to combine this with dropArm()
    private void raiseArm() {
        switch (arm) {
            case INIT:
                // set target position
                currPosArm = Flipper.getPosition();
                targetPosArm = 0;
                arm = ARMSTATE.RAISING;
                break;
            case DROPPED:
                currPosArm = Flipper.getPosition();
                targetPosArm = currPosArm - 0.5;
                arm = ARMSTATE.RAISING;
                break;
            case RAISING:
                // want it to go up in increments so the program can take other inputs
                if (currPosArm <= targetPosArm) {
                    arm = ARMSTATE.RAISED;
                }
                currPosArm = Flipper.getPosition();
                Flipper.setPosition(currPosArm - 0.1);

                break;
            // dont want to do anything if the arm is raising or dropping
            case RAISED:
                break;
            case DROPPING:
                break;
        }
    }
*/

/*    // NOT WORKING
    private void openDoor() {
        switch (door) {
            case INIT:
            case CLOSED:
                // set target position
                targetPosDoor = 0.95;
                currPosDoor = Trapdoor.getPosition();
                door = DOORSTATE.OPEN;
                break;
            case OPEN:
                if (currPosDoor >= targetPosDoor) {
                    door = DOORSTATE.OPENED;
                }
                // want it to go up in increments so the program can take other inputs
                Trapdoor.setPosition(currPosDoor + 0.1);
                currPosDoor += 0.1;
                break;
            // dont want to do anything if the arm is raising or dropping
            case OPENED:
                break;
            case CLOSE:
                break;
        }
    }
    private void closeDoor() {
        switch (door) {
            case INIT:
                // set target position
                targetPosDoor = 0;
                currPosDoor = Trapdoor.getPosition();
                door = DOORSTATE.CLOSE;
                break;
            case OPENED:
                targetPosDoor = 0;
                currPosDoor = Trapdoor.getPosition();
                door = DOORSTATE.CLOSE;
                break;
            case CLOSE:
                if (currPosDoor <= targetPosDoor) {
                    door = DOORSTATE.CLOSED;
                }
                // want it to go up in increments so the program can take other inputs
                Trapdoor.setPosition(currPosDoor - 0.1);
                currPosDoor -= 0.1;
                break;
            // dont want to do anything if the arm is raising or dropping
            case CLOSED:
                break;
            case OPEN:
                break;
        }
    }*/
}







/*
Two different doctors worked on my knee surgery

It was a joint operation
 */