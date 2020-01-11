package org.firstinspires.ftc.teamcode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.teamcode.SuperOp;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous
public class BluePlayer extends SuperOp {

    //This uses an enum declared in SuperOp
    //It declares the first STATUS as "START"
    private STATUS status = STATUS.FlIPPER;

    //create new stopwatch
    private ElapsedTime time = new ElapsedTime();
    private double targetTime;
    private ElapsedTime arm = new ElapsedTime();
    private int currPosition;
    private int targetPosition;
    private boolean ran = false;
    private boolean ran1 = true;

    @Override
    public void loop() {
        startPoint = 1;
        //declare telemetry for all motors/servos
        //this allows us to see how the motors are behaving in the code
        //and then compare it to how they perform in real life
        telemetry.addData("Arm", arm.seconds());
        telemetry.addData("Power",LatchMotor.getPower());
        telemetry.addData("LatchMotor Position: ", LatchMotor.getCurrentPosition());
        telemetry.addData("Time: ", time.seconds());
        telemetry.addData("Front Right: ", FrontRightDrive.getCurrentPosition());
        telemetry.addData("Back Left: ", BackLeftDrive.getCurrentPosition());
        telemetry.addData("Back Right: ", BackRightDrive.getCurrentPosition());
        telemetry.addData("Front Left: ", FrontLeftDrive.getCurrentPosition());
        telemetry.addData("Status: ", status);
        telemetry.addData("Latch Position: ", Latch.getPosition());

        currPosition = LatchMotor.getCurrentPosition();
        //switch statements for changing the status of the robot
        //this allows us to use different code for each status
        //there are methods created below the switch statement for easier reading
        switch (status) {
            case FlIPPER:
                flipper();
                break;
            case START:
                start1();
                break;
            /*case TOBLOCK:
                //toBlock();
                break; */
            case APPROACH:
                approach();
                break;
            case GETBLOCK:
                getBlock();
                //status = STATUS.AWAY;
                break;
            case AWAY:
                //time.reset();
                //away();
                break;
            case TOBUILD:
                toBuild();
                break;
            case RELEASEBLOCK:
                release();
                break;
            case PARK:
                //time.reset();
                park();
                break;
            case STOP:
                stop1();
                break;
        }
    }

    //this is the first method run
    //it resets the elapsed time
    //then switches the status to 'TOBLOCK'
    private void flipper(){
        if(ran1){
            time.reset();
            ran1 = !ran1;
        }
        targetTime = 2;
        Flipper.setPower(-1);
        if(time.seconds() >= targetTime){
            Flipper.setPower(0);
            status = STATUS.START;
        }
    }
    private void start1(){
        if(ran == false){
            time.reset();
            ran = !ran;
        }
        targetTime = 0.5;
        drive(0.5,0,0);
        if(time.seconds() >= targetTime){
            drive(0,0,0);
            time.reset();
            status = STATUS.APPROACH;
            ran = false;
        }
        //targetTime = .5;
    }

    //method to go to block
    //moves forward for 3 seconds at a motor power of .5
    //if the time is >= 3 seconds, the STATUS changes to 'APPROACH'
    private void toBlock() {
        //move forward for 3 seconds
        leftSpeedMultiplier = 1;
        targetTime = 2.5;
        drive(0, 0.5, 0);

        // vision code
        // if skystone is sighted
        /*
         */

        // set movement values to go towards block
        if(time.seconds()-targetTime > 0) {
            //stop
            drive(0,0,0);
            //sleep_secs(0.5);
            //switch STATUS
            time.reset();
            status = STATUS.APPROACH;
        }
    }

    //runs for 1.5 seconds
    //strafes left for 1.5 seconds with .5 motor power
    //if time >= 1.5 seconds, the robot stops
    //and switches the STATUS to 'GETBLOCK'
    private void approach() {
        targetTime = 0.9;
        drive(0.5, 0, 0);

        if(time.seconds() >= targetTime) {
            //robot stops for .5 secs
            drive(0,0,0);
            //sleep_secs(0.5);
            //switches STATUS
            //resets clock
            status = STATUS.GETBLOCK;
        }
    }

    //rotate the arm down
    //check to see if the arm is in position
    //pull the block in and switch STATUS to 'AWAY'
    private void getBlock() {
        if(!ran){
            arm.reset();
            ran = !ran;
        }
        currPosition = LatchMotor.getCurrentPosition();
        //rotate arm down
        LatchMotor.setPower(0.3);
        sleep_secs(.5);
        if ((currPosition <= targetPosition + 13 && currPosition >= targetPosition - 6)|| arm.seconds() > 1) {

            //pull the block in
            LatchMotor.setPower(0);
            //switches STATUS
            //resets clock
            if(!ran1){
                time.reset();
                ran1 = !ran1;
            }
            targetTime = 1.1;
            drive(-0.5, 0, 0);
            if(time.seconds() >= targetTime) {
                //stop
                drive(0,0,0);
                //sleep_secs(0.5);
                //switch STATUS
                //resets clock
                time.reset();
                status = STATUS.TOBUILD;
            }
        }
    }

    //go to build site and place the block back down
    //go backwards for 3 seconds
    //then release the LatchMotor and consequently, the block
    //check if the arm is up
    //lastly, set STATUS to 'PARK'
    private void toBuild() {

        if(ran){
            targetPosition = LatchMotor.getCurrentPosition() + 500;
            ran = !ran;
        }
        //sets target position for grabber
        //methods to get the robot back to the build site to place down the block
        targetTime = 1.9;
        drive(0,-0.5,0);
        if(time.seconds() >= targetTime) {
            drive(0, 0, 0);
            arm.reset();
            status = STATUS.RELEASEBLOCK;
            //check if the arm is in position
            /*if ((currPosition <= targetPosition + 13 && currPosition >= targetPosition - 6) || arm.seconds() > 1) { drive(0,0,0);
                //leave motor
                LatchMotor.setPower(0);
                time.reset();
                status = STATUS.PARK;
            } */
        }
    }
    private void release(){
        currPosition = LatchMotor.getCurrentPosition();
        //rotate arm up

        LatchMotor.setPower(-0.3);
        if(arm.seconds() >= 1) {
            LatchMotor.setPower(0);
            time.reset();
            status = STATUS.PARK;
        }
    }

    //park the robot in the middle of the alliance bridge
    //drive 1.5 seconds forward, then stop
    //switch STATUS to 'STOP'
    private void park() {
        // vision code to park the robot under the bridge
        //t_drive(0, -1, 0, 1);
        targetTime = .9;
        drive(0, 0.5, 0);
        if(time.seconds() >= targetTime){
            //stop robot
            drive(0.5,0,0);
            sleep_secs(0.4);
            drive(0,0,0);
            //switch STATUS
            status = STATUS.STOP;
        }
    }

    //stop all motion of the robot
    //set all motor powers to 0
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