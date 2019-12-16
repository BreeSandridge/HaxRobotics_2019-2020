package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.SuperOp;
import com.qualcomm.robotcore.util.ElapsedTime;
@Autonomous
public class BlueBuild extends SuperOp {

    // status stuff
    private STATUS status = STATUS.START;
    boolean running = true;
    private int targetPosition;
    private int currPosition;
    private ElapsedTime time = new ElapsedTime();
    private double targetTime;
    @Override
    public void loop() {
        telemetry.addData("LatchMotor Position: ", LatchMotor.getCurrentPosition());
        telemetry.addData("Time: ", time.milliseconds());
        telemetry.addData("Front Right: ", FrontRightDrive.getCurrentPosition());
        telemetry.addData("Back Left: ", BackLeftDrive.getCurrentPosition());
        telemetry.addData("Back Right: ", BackRightDrive.getCurrentPosition());
        telemetry.addData("Front Left: ", FrontLeftDrive.getCurrentPosition());
        telemetry.addData("Status: ", status);
        telemetry.addData("Latch Position: ", Latch.getPosition());
        currPosition = LatchMotor.getCurrentPosition();
        // switch cases for changing the status of the robot to do different things
        switch (status) {
            case START:
                start1();
                break;
            case TOBLOCK:
                toBlock();
                break;
            case APPROACH:
                approach();
                break;
            case GETBLOCK:
                getBlock();
                break;
            case AWAY:
                away();
                break;
            case TOBUILD:
                toBuild();
                break;
            case PARK:
                park();
                break;
            case STOP:
                stop1();
                break;
        }
    }
    private void start1(){
        time.reset();
        status = STATUS.TOBLOCK;
    }
    // method to go to block
    private void toBlock() {
        targetTime = 3;
        drive(0, 0.5, 0);
        // vision code
        // if skystone is sighted
        // set movement values to go towards block
        if(time.seconds() >= targetTime) {
            drive(0,0,0);
            sleep_secs(0.5);
            status = STATUS.APPROACH;
            time.reset();
        }
    }

    private void approach() {
        targetTime = 1.5;
        drive(0.5, 0, 0);
        telemetry.addData("Status: ", status);
        if(time.seconds() >= targetTime) {
            drive(0,0,0);
            sleep_secs(0.5);
            status = STATUS.GETBLOCK;
            time.reset();
        }
    }

    // method to pick up the block
     private void getBlock() {
        // rotate the arm down
        currPosition = LatchMotor.getCurrentPosition();
        targetPosition = currPosition - 577;
        LatchMotor.setPower(-0.7);

        // check if the arm is in position
        if (currPosition <= targetPosition + 13 && currPosition >= targetPosition - 6) {
            // pull the block in
            LatchMotor.setPower(0);
            // switch status
            status = STATUS.AWAY;
            time.reset();
        }
    }
    private void away() {
        RightSpeedMultiplier = 1.5;
        targetTime = 1.5;
        drive(-0.5, 0, 0);
        telemetry.addData("Status: ", status);
        if(time.seconds() >= targetTime) {
            drive(0,0,0);
            sleep_secs(0.5);
            status = STATUS.TOBUILD;
            time.reset();
        }

    }


    // go to build site and place the block back down
    private void toBuild() {
        targetPosition = currPosition + 577;
        // methods to get the robot back to the build site to place down the block
        targetTime = 3;
        drive(0,0.5,0);
        telemetry.addData("Status: ", status);
        if(time.seconds() >= targetTime) {
            drive(0, 0, 0);
            // rotate the arm up
            currPosition = LatchMotor.getCurrentPosition();
            LatchMotor.setPower(0.7);

            // check if the arm is in position
            if (currPosition <= targetPosition + 13 && currPosition >= targetPosition - 13) {
                // pull the block in
                LatchMotor.setPower(0);
                Latch.setPosition(0);
                // switch status
                status = STATUS.PARK;
                time.reset();
                RightSpeedMultiplier = 1;
            }
        }
    }

    // park the thing under the bridge
    private void park() {
        // vision stuff to park the robot under the bridge
        //t_drive(0, -1, 0, 1);
        targetTime = 1.5;
        drive(0, -0.5, 0);
        if(time.seconds() >= targetTime){
            drive(0,0,0);
            sleep_secs(0.5);
            status = STATUS.STOP;
        }
    }
    private void stop1(){
        drive(0,0,0);
    }
}





/*
Are you ready, kids?
I cant hear youuuuuu

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
