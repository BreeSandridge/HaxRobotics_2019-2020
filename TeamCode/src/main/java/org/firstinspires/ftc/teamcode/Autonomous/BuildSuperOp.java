package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.SuperOp;
@Autonomous
public abstract class BuildSuperOp extends SuperOp {
    public ElapsedTime time = new ElapsedTime();
    public ElapsedTime arm = new ElapsedTime();
    public double targetTime;
    public int currPosition;
    public int targetPosition;
    public boolean ran = false;
    public boolean ran1 = true;

    @Override
    public void init() {
        // declare telemetry for all motors/servos
        // this allows us to see how the motors are behaving in the code
        // and then compare it to how they perform in real life
        telemetry.addData("Arm", arm.seconds());
        telemetry.addData("Power", LatchMotor.getPower());
        telemetry.addData("LatchMotor Position: ", LatchMotor.getCurrentPosition());
        telemetry.addData("Time: ", time.seconds());
        telemetry.addData("Front Right: ", FrontRightDrive.getCurrentPosition());
        telemetry.addData("Back Left: ", BackLeftDrive.getCurrentPosition());
        telemetry.addData("Back Right: ", BackRightDrive.getCurrentPosition());
        telemetry.addData("Front Left: ", FrontLeftDrive.getCurrentPosition());

        currPosition = LatchMotor.getCurrentPosition();
        //switch statements for changing the status of the robot
        //this allows us to use different code for each status
        //there are methods created below the switch statement for easier reading
    }
    //this is the first method run
    //it resets the elapsed time
    // flips arm/basket down and out of the way
    public void flipper(){
        if(ran1){
            time.reset();
            ran1 = !ran1;
        }
        // motor goes for .525 seconds and then stops
        targetTime = .525;
        FlipperMotor.setPower(.3);
        if(time.seconds() >= targetTime){
            FlipperMotor.setPower(0);
            time.reset();
        }
    }

    // move towards foundation and deploy latch
    public void toFoundation(){
        // strafe towards foundation
        targetTime = 1.5;
        drive(-.5,0,0);
        // if elapsed time >1.5, deploy latch
        if(time.seconds() >= targetTime){
            drive(0,0,0);
            LatchMotor.setPower(0.5);
            time.reset();
        }
    }

    // attach to foundation and drag to triangle
    public void drag(){
        //deploy latch and move as far as possible into triangle
        drive(0.5,0,0);
        if(time.seconds() >= targetTime){
            drive(0,0,0);
            Latch.setPosition(0);
            time.reset();
        }
    }

    // this allows us to simply move out of the way of a partner
    public void move() {
        // if elapsed time >.5, move closer to build zone
        if(time.seconds() <= 0.5){
            drive(0,-0.5,0);
        }
        // stop all motion for .4 seconds
        drive(0,0,0);
        sleep_secs(0.4);
        // if elapsed time <.5 but  >1.5 strafe right
        if(time.seconds() <= 1.5 && time.seconds() > 0.5){
            drive(-0.5,0,0);

        }
        // stop all motion for .4 seconds
        drive(0,0,0);
        sleep_secs(0.4);
    }

    // allows the robot to move around the foundation and then push
    // it farther into the build zone.
    // Moves toward the midline, then strafes right around it
    // moves farther into the build zone and pushes the foundation
    // farther into the build zone
    public void around(){
        // if elapsed time is >.5, move toward midline
        if(time.seconds() <= 0.5){
            drive(0,-0.5,0);
        }
        // stop all motion for .4 seconds
        drive(0,0,0);
        sleep_secs(0.4);
        // if elapsed time is <.5 but >1.5, strafes toward alliance bridge
        if(time.seconds() <= 1.5 && time.seconds() > 0.5){
            drive(-0.5,0,0);
        }
        // stop all motion for .4 seconds
        drive(0,0,0);
        sleep_secs(0.4);
        // if elapsed time <1.5 but >2, move farther into build zone
        if(time.seconds() <= 2 && time.seconds() > 1.5){
            drive(0,0.5,0);
        }
        // stop all motion for .4 seconds
        drive(0,0,0);
        sleep_secs(0.4);
        // if elapsed time <2 but >2.5, strafe left
        if(time.seconds() <= 2.5 && time.seconds() > 2){
            drive(0.5,0,0);
        }
        // stop motion for .4 seconds
        drive(0,0,0);
        sleep_secs(0.4);
        // if elapsed time <2.5 but >3, turn counter(?)clockwise
        // this allows for more efficient parking and it makes it
        // easier for alliance partners to park too
        if(time.seconds() <= 3 && time.seconds() > 2.5){
            drive(0,0,-0.5);
        }
        drive(0,0,0);
        sleep_secs(0.4);
    }

    // park over midline close to neutral bridge
    // move forward then strafe right
    public void park() {
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

    // park over midline against wall
    // move forward then strafe left
    public void parkW() {
        // move forward for  a set amount of time
        targetTime = 1.4;
        drive(0, -0.5, 0);
        // if the elapsed time is greater than the
        // time the robot moves forward, the robot strafes left
        if(time.seconds() >= targetTime){
            drive(0.5,0,0);
            sleep_secs(0.3);
            drive(0,0,0);
        }
    }

    // stop all motion of the robot
    // set all motor powers to 0
    public void stop1(){
        // stop all motors
        drive(0,0,0);
        telemetry.addData("Emotion", "I hate everything!");
    }
}
