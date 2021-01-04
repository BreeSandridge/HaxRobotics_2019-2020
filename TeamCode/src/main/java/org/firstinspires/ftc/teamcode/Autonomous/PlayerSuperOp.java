/* none of this code is tested, use at your own risk
 * all of these methods are distinctly designed for the Player half of the field
 * all player autonomi extend this class  */
package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Autonomous.VisionOpModes.CVCamera;
import org.firstinspires.ftc.teamcode.SuperOp;

@Autonomous
public abstract class PlayerSuperOp extends SuperOp {
    // declares elapsed time and other variables
    public int block = 0;
    public int parkPos = 0; // Will be changed to 1 or -1 by extensions
    public ElapsedTime time = new ElapsedTime();
    public ElapsedTime arm = new ElapsedTime();
    public ElapsedTime repeat = new ElapsedTime();
    public double targetTime;
    public int currPosition;
    public int targetPosition;
    public boolean ran = false;
    public boolean ran1 = true;
    public CVCamera cvCamera;
    public boolean parkWall;
    public boolean noInterference = false;
    @Override
    public void init() {
        super.init();
        //initialize camera for vision autonomous
        CamType type = CamType.INTERNAL;
        cvCamera = new CVCamera(type);
        initCamera(cvCamera, type);

        //int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                //"tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        //cvCamera = new CVCamera(tfodMonitorViewId);;
        //declare telemetry for all motors/servos
        //this allows us to see how the motors are behaving in the code
        //and then compare it to how they perform in real life

        //currPosition = LatchMotor.getCurrentPosition();
    }


    // this is the first method run
    // it resets the elapsed time
    // flips arm/basket down and out of the way

    // strafe towards blocks, deploy latchMotor
    public void toBlock() {
        // strafe towards blocks for targetTime
        accelDrive.pushCommand(0.5, 0, 0, 2.3);
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
        sleep_secs(1);
        // make sure latch motor is is in right position and stop its movement
        LatchMotor.setPower(0.15);
            // forces statment to run once
            // strafe away from block
    }
    public void moveForwards2(){
        accelDrive.pushCommand(0,0.5,0,1);
    }
    public void moveForwards(){
        accelDrive.pushCommand(0,0.5,0,0.5);
    }
    // drive into build zone and release block
    public void away () {
        // drive into build zone
        accelDrive.pushCommand(-0.7,0,0,4);
        if(block == 0){
            accelDrive.pushCommand(0, -0.5, 0, 1.2);
        } else if(block == -1){
            accelDrive.pushCommand(0,-0.5,0,2);
        } else if(block == 1){
            accelDrive.pushCommand(0,-0.5,0,3);
        }
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


    // parkY over midline close to neutral bridge
    // move forward then strafe right


    // parkY over midline against wall
    // move forward then strafe left
    public void park() {
        if(noInterference) {
            accelDrive.pushCommand(0, 0.5, 0, 0.45);
        } else {
            accelDrive.pushCommand(0,0.5,0,0.6);
        }
        accelDrive.pushCommand(parkPos*0.5,0,0,0.5);
    }
    public void parkBlue() {
        if(noInterference) {
            accelDrive.pushCommand(0, -0.5, 0, 0.45);
        } else {
            accelDrive.pushCommand(0,0-.5,0,0.6);
        }
        accelDrive.pushCommand(parkPos*0.5,0,0,0.5);
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
    public void drive1(double x, double y, double w) {
        FrontLeftDrive.setPower((y-x+w) * 0.95);
        FrontRightDrive.setPower((y+x-w) * 0.6);
        BackLeftDrive.setPower((y+x+w) * 0.95);
        BackRightDrive.setPower((y-x-w)*0.6);
    }
}
