/*package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="Driver Controller")
public class DriverController extends SuperOp {
    // status of the arm and door
    // change the state of the arm and door as it is raising or dropping
    // so the program can execute what it is doing on every loop
    private enum ARMSTATE {INIT, RAISING, DROPPING, RAISED, DROPPED }
    private enum DOORSTATE { INIT, OPEN, CLOSE, OPENED, CLOSED }

    private ARMSTATE arm = ARMSTATE.INIT;
    private DOORSTATE door = DOORSTATE.INIT;

    private double targetPosArm;
    private double targetPosDoor;

    private double currPosArm;
    private double currPosDoor;


    // can be written much more efficiently, just a basic DriverController
    @Override
    public void loop() {
       telemetry.addData("Arm State: ", arm);
       telemetry.addData("Door State: ", door);
       telemetry.addData("Current Arm Position: ", Flipper.getPosition());
       c_drive();

       // inputs - for now all on gamepad1 (driver controlled)
       // add gamepad2 controls as well but give gamepad1 right to override
       if (gamepad1.right_trigger > 0.05) {
           pickupStone();
       } else if (gamepad1.left_trigger > 0.05) {
           dropStone();
       } else {
           RightStoneRamp.setPower(0);
           LeftStoneRamp.setPower(0);
       }

       // if the player wants the arm dropped then drop it, otherwise if the player wants it raised
       // then raise it
       // currently if you press a you have to wait until it fully drops to do another command
       // working on a more efficient way to raise the arm so the driver can change their mind
       // midway through the raise

        /** arm moving code doesnt work right now because of some weird reason
         * it doesn't seem to be a code problem because the code for the door
         * is working perfectly and it's basically identical
         * calling Flipper.setPosition(x) in the runtime loop doesnt move the arm either
         * so it either is a SuperOp problem or a problem with the Servo configuration
         */
//       if (gamepad1.a) {
//           dropArm();
//       } else if (gamepad1.y){
//           raiseArm();
//       }

       /*if (gamepad1.x) {
           openDoor();
       } else {
           closeDoor();
       }
    }

    // Intake
    private void pickupStone() {
        RightStoneRamp.setPower(1);
        LeftStoneRamp.setPower(1);
    }
    private void dropStone() {
        RightStoneRamp.setPower(-1);
        LeftStoneRamp.setPower(-1);
    }


    private void dropArm() {
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


    // NOT WORKING
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
    }
}*/





/*
Two different doctors worked on my knee surgery

It was a joint operation
 */