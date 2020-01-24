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
        telemetry.addData("LatchMotor Position: ", LatchMotor.getCurrentPosition());
        telemetry.addData("Time: ", time.seconds());
        telemetry.addData("Front Right: ", FrontRightDrive.getCurrentPosition());
        telemetry.addData("Back Left: ", BackLeftDrive.getCurrentPosition());
        telemetry.addData("Back Right: ", BackRightDrive.getCurrentPosition());
        telemetry.addData("Front Left: ", FrontLeftDrive.getCurrentPosition());

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
        accelDrive.pushCommand(0.5, 0, 0, 1);

        // forces method to only run once
    }
    public void grab(){
        if (!ran) {
            time.reset();
            ran = !ran;
        }
        // deploy latch motor to pick up block
        targetTime = 0.5;
        LatchMotor.setPower(0.3);
        sleep_secs(0.3);
        // make sure latch motor is is in right position and stop its movement
        if (time.seconds() >= targetTime) {
            LatchMotor.setPower(0);
            // forces statment to run once
            // strafe away from blocks
            accelDrive.pushCommand(-0.5,0,0,1.1);
        }
    }

    // drive into build zone and release block
    public void away () {
        // drive into build zone
        accelDrive.pushCommand(0, -0.5, 0, 1);
    }
    public void release(){
        // release block
        if (!ran) {
            time.reset();
            ran = !ran;
        }
        targetTime = 0.5;
        // deploy latch motor to pick up block
        LatchMotor.setPower(-0.3);
        sleep_secs(0.3);
        // make sure latch motor is is in right position and stop its movement
        if (time.seconds() >= targetTime) { // elapsed time...
            LatchMotor.setPower(0);
        }
    }

    // this method only runs if its been less then 15 seconds
    // it drive forward then repeats the commands done in toBlock() and away()
    public void again () {
        accelDrive.pushCommand(0,0.5,0,1);
        // drive forward
    }


    // park over midline close to neutral bridge
    // move forward then strafe right
    public void park() {
        // move forward a set amount of time
        accelDrive.pushCommand(0,0.5,0,1.4);
        accelDrive.pushCommand(0.5,0,0,0.3);
    }


    // park over midline against wall
    // move forward then strafe left
    public void parkW() {
        accelDrive.pushCommand(0,-0.5,0,1.4);
        accelDrive.pushCommand(0.5,0,0,0.3);
    }

    // move backwards
    //needs to be changed dont know to what yet
    public void away2() {
        accelDrive.pushCommand(0,0,0,1);
    }


    // stop all motion of the robot
    // set all motor powers to 0
    public void stop1(){
        // stop
        drive(0,0,0);
        telemetry.addData("Emotion: ", "I hate everything!");
    }
}
