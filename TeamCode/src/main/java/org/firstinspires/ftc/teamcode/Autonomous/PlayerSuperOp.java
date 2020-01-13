package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.SuperOp;

public abstract class PlayerSuperOp extends SuperOp {
    public ElapsedTime time = new ElapsedTime();
    public double targetTime;
    public ElapsedTime arm = new ElapsedTime();
    public int currPosition;
    public int targetPosition;
    public boolean ran = false;
    public boolean ran1 = true;

    @Override
    public void init() {
        startPoint = 1;
        //declare telemetry for all motors/servos
        //this allows us to see how the motors are behaving in the code
        //and then compare it to how they perform in real life
        telemetry.addData("Arm", arm.seconds());
        telemetry.addData("Power", LatchMotor.getPower());
        telemetry.addData("LatchMotor Position: ", LatchMotor.getCurrentPosition());
        telemetry.addData("Time: ", time.seconds());
        telemetry.addData("Front Right: ", FrontRightDrive.getCurrentPosition());
        telemetry.addData("Back Left: ", BackLeftDrive.getCurrentPosition());
        telemetry.addData("Back Right: ", BackRightDrive.getCurrentPosition());
        telemetry.addData("Front Left: ", FrontLeftDrive.getCurrentPosition());
        telemetry.addData("Latch Position: ", Latch.getPosition());

        currPosition = LatchMotor.getCurrentPosition();
        //switch statements for changing the status of the robot
        //this allows us to use different code for each status
        //there are methods created below the switch statement for easier reading
        //this is the first method run
        //it resets the elapsed time
        //then switches the status to 'TOBLOCK'
    }
        public void flipper() {
            if (ran1) {
                time.reset();
                ran1 = !ran1;
            }
            targetTime = .525;
            FlipperMotor.setPower(.3);
            if (time.seconds() >= targetTime) {
                FlipperMotor.setPower(0);
            }
        }
        public void start1() {
            sleep_secs(10);
            if (ran == false) {
                time.reset();
                ran = !ran;
            }
            targetTime = 0.5;
            drive(0.5, 0, 0);
            if (time.seconds() >= targetTime) {
                drive(0, 0, 0);
                time.reset();
                ran = false;
            }
            //targetTime = .5;
        }

        //method to go to block
        //moves forward for 3 seconds at a motor power of .5
        //if the time is >= 3 seconds, the STATUS changes to 'APPROACH'
        public void toBlock() {
            //move forward for 3 seconds
            leftSpeedMultiplier = 1;
            targetTime = 2.5;
            drive(0, 0.5, 0);

            // vision code
            // if skystone is sighted
            /*
             */

            // set movement values to go towards block
            if (time.seconds() - targetTime > 0) {
                //stop
                drive(0, 0, 0);
                //sleep_secs(0.5);
                //switch STATUS
                time.reset();
            }
        }

        //runs for 1.5 seconds
        //strafes left for 1.5 seconds with .5 motor power
        //if time >= 1.5 seconds, the robot stops
        //and switches the STATUS to 'GETBLOCK'
        public void approach() {
            targetTime = 1.5;
            drive(0.5, 0, 0);

            if (time.seconds() >= targetTime) {
                //robot stops for .5 secs
                drive(0, 0, 0);
                //sleep_secs(0.5);
                //switches STATUS
                //resets clock
            }
        }

        //rotate the arm down
        //check to see if the arm is in position
        //pull the block in and switch STATUS to 'AWAY'
        public void getBlock() {
            if (!ran) {
                arm.reset();
                ran = !ran;
            }
            currPosition = LatchMotor.getCurrentPosition();
            //rotate arm down
            LatchMotor.setPower(0.3);
            sleep_secs(.5);
            if ((currPosition <= targetPosition + 13 && currPosition >= targetPosition - 6) || arm.seconds() > 1) {

                //pull the block in
                LatchMotor.setPower(0);
                //switches STATUS
                //resets clock
                if (!ran1) {
                    time.reset();
                    ran1 = !ran1;
                }
                targetTime = 1.5;
                drive(-0.5, 0, 0);
                if (time.seconds() >= targetTime) {
                    //stop
                    drive(0, 0, 0);
                    //sleep_secs(0.5);
                    //switch STATUS
                    //resets clock
                    time.reset();
                }
            }
        }

        //go to build site and place the block back down
        //go backwards for 3 seconds
        //then release the LatchMotor and consequently, the block
        //check if the arm is up
        //lastly, set STATUS to 'PARK'
        public void toBuild() {

            if (ran) {
                targetPosition = LatchMotor.getCurrentPosition() + 500;
                ran = !ran;
            }
            //sets target position for grabber
            //methods to get the robot back to the build site to place down the block
            targetTime = 1.9;
            drive(0, 0.5, 0);
            if (time.seconds() >= targetTime) {
                drive(0, 0, 0);
                arm.reset();
                //check if the arm is in position
            /*if ((currPosition <= targetPosition + 13 && currPosition >= targetPosition - 6) || arm.seconds() > 1) { drive(0,0,0);
                //leave motor
                LatchMotor.setPower(0);
                time.reset();
                status = STATUS.PARK;
            } */
            }
        }
        public void release() {
            currPosition = LatchMotor.getCurrentPosition();
            //rotate arm up

            LatchMotor.setPower(-0.3);
            if (arm.seconds() >= 1) {
                LatchMotor.setPower(0);
                time.reset();
            }
        }

        //park the robot in the middle of the alliance bridge
        //drive 1.5 seconds forward, then stop
        //switch STATUS to 'STOP'
        public void park() {
            // vision code to park the robot under the bridge
            //t_drive(0, -1, 0, 1);
            //targetTime = .9;
            targetTime = 1.4;
            drive(0, -0.5, 0);
            if (time.seconds() >= targetTime) {
                //stop robot
                drive(0.5, 0, 0);
                sleep_secs(0.4);
                drive(0, 0, 0);
                //switch STATUS
            }
        }

        //stop all motion of the robot
        //set all motor powers to 0
        public void stop1() {
            drive(0, 0, 0);
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
