package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.SuperOp;
@Autonomous
public class BlueBuild extends SuperOp {

    // status stuff
    private STATUS status = STATUS.TOBLOCK;
    boolean running = true;
    private int targetPosition;
    private int currPosition;

    @Override
    public void loop() {

        // switch cases for changing the status of the robot to do different things
        switch (status) {
            case TOBLOCK:
                toBlock();
                break;
            case APPROACH:
                approach();
                break;
            //case GETBLOCK:
                //getBlock();
                //break;
            case AWAY:
                away();
                break;
            case TOBUILD:
                //toBuild();
                break;
            case PARK:
                park();
                break;
        }
    }

    // method to go to block
    private void toBlock() {
        if (isRunning) {
           t_drive(0, 0.75, 0, 1000);
            // vision code
            // if skystone is sighted
            // set movement values to go towards block
            status = STATUS.APPROACH;
        } else {
            t_drive(0,0,0,0);
        }
    }

    private void approach() {
        t_drive(0.75, 0, 0, 1);
        status = STATUS.GETBLOCK;
    }

    // method to pick up the block
  /*  private void getBlock() {
        // rotate the arm down
        targetPosition = 9000;
        currPosition = LatchMotor.getCurrentPosition();
        LatchMotor.setPower(1);

        // check if the arm is in position
        if (currPosition > targetPosition - 100 || currPosition < targetPosition + 100) {
            // pull the block in
            LatchMotor.setPower(0);
            Latch.setPosition(0.5);
            // switch status
            status = STATUS.AWAY;
        }
    }
*/
    private void away() {
        t_drive(-0.75, 0, 0, 1);
        status = STATUS.TOBUILD;
    }


    // go to build site and place the block back down
    /*private void toBuild() {
        // methods to get the robot back to the build site to place down the block
        t_drive(0, 0.75,0, 1);

        // rotate the arm up
        targetPosition = 7000;
        currPosition = LatchMotor.getCurrentPosition();
        LatchMotor.setPower(-1);

        // check if the arm is in position
        if (currPosition > targetPosition - 100 || currPosition < targetPosition + 100) {
            // pull the block in
            LatchMotor.setPower(0);
            Latch.setPosition(0);
            // switch status
            status = STATUS.PARK;
        }
    }
*/
    // park the thing under the bridge
    private void park() {
        // vision stuff to park the robot under the bridge
        t_drive(0, -1, 0, 1);
    }
}





/*
Are you ready, kids?
I said, are you ready?

Who lives in a pineapple under the sea?
SPONGEBOB SQUAREPANTS!
Absorbent and yellow and porous is he
SPONGEBOB SQUAREPANTS!

If nautical nonsense be something you wish
SPONGEBOB SQUAREPANTS!
Then drop on the deck and flop like a fish
SPONGEBOB SQUAREPANTS!

SPONGEBOB SQUAREPANTS!
SPONGEBOB SQUAREPANTS!
SPONGEBOB SQUAREPANTS!
SPONGEBOB SQUAREPANTS!

 */
