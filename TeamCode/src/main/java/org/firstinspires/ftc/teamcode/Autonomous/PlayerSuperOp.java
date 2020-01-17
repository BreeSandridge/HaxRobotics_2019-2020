/* none of this code is tested, use at your own risk
 * all of these methods are distinctly designed for the Player half of the field
 * all player autonomi extend this class  */
package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.SuperOp;

@Autonomous
public abstract class PlayerSuperOp extends SuperOp {
    // declares elapsed time and other variables
    public ElapsedTime time = new ElapsedTime();
    public ElapsedTime arm = new ElapsedTime();
    public ElapsedTime repeat = new ElapsedTime();
    public double targetTime;
    public int currPosition;
    public int targetPosition;
    public boolean ran = false;
    public boolean ran1 = true;

    @Override
    public void init() {
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
    }


    // this is the first method run
    // it resets the elapsed time
    // flips arm/basket down and out of the way
    public void flipper() {
        // forces code to only run once
        if (ran1) {
            time.reset();
            ran1 = !ran1;
        }
        // motor goes for .525 seconds and then stops
        targetTime = .525;
        FlipperMotor.setPower(.3);
        if (time.seconds() >= targetTime) {
            FlipperMotor.setPower(0);
            time.reset();
        }
    }

    // strafe towards blocks, deploy latchMotor
    public void toBlock() {
        // strafe towards blocks for targetTime
        targetTime = 1;
        drive(-.5, 0, 0);
        time.reset();
        // forces method to only run once
        if (!ran) {
            arm.reset();
            ran = !ran;
        }
        // deploy latch motor to pick up block
        currPosition = LatchMotor.getCurrentPosition();
        LatchMotor.setPower(0.3);
        sleep_secs(.5);
        // make sure latch motor is is in right position and stop its movement
        if ((currPosition <= targetPosition + 13 && currPosition >= targetPosition - 6) || arm.seconds() > 1) {
            LatchMotor.setPower(0);
            // forces statment to run once
            if (!ran1) {
                time.reset();
                ran1 = !ran1;
            }
            // strafe away from blocks
            targetTime = 1.1;
            drive(-0.5, 0, 0);
            // if elapsed time > targetTime, stop all motion
            if (time.seconds() >= targetTime) {
                drive(0, 0, 0);
                time.reset();
            }
        }
    }

    // drive into build zone and release block
    public void away () {
        // drive into build zone
        targetTime = 1;
        drive(0, -.5, 0);
        time.reset();
        // release block
        LatchMotor.setPower(-.3);
    }

    // this method only runs if its been less then 15 seconds
    // it drive forward then calls toBlock() and away()
    public void again () {
        // drive forward
        targetTime = 1;
        drive(0,.5,0);
        // call methods
        toBlock();
        away();
    }


    // park over midline close to neutral bridge
    // move forward then strafe right
    public void park() {
        // move forward a set amount of time
        targetTime = 1.4;
        drive(0, 0.5, 0);
        // if the elapsed time is greater than the
        // time the robot moves forward, then the robot strafes left
        if(time.seconds() >= targetTime){
            drive(0.5,0,0);
            sleep_secs(0.3);
            drive(0,0,0);
        }
    }


    // park over midline against wall
    // move forward then strafe left
    public void parkW() {
        // move forward a set amount of time
        targetTime = 1.4;
        drive(0, -0.5, 0);
        // if the elapsed time is greater than the
        // time the robot moves forward, then the robot strafes right
        if(time.seconds() >= targetTime){
            drive(0.5,0,0);
            sleep_secs(0.3);
            drive(0,0,0);
        }
    }

    // move backwards
    public void away2() {
        targetTime = 1;
        drive(0, -0.5, 0);
    }


    // stop all motion of the robot
    // set all motor powers to 0
    public void stop1(){
        // stop
        drive(0,0,0);
        telemetry.addData("Emotion", "I hate everything!");
    }
}
