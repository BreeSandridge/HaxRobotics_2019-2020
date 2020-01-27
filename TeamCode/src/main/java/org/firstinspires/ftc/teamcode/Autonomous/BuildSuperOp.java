/* none of this code is tested, use at your own risk
*  all of these methods are distinctly designed for the Build half of the field
*  all build autonomi extend this class  */
package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.SuperOp;

@Autonomous
public abstract class BuildSuperOp extends SuperOp {
    // declares elapsed time and other variables
    public int parkPos;
    public ElapsedTime time = new ElapsedTime();
    public ElapsedTime arm = new ElapsedTime();
    public double targetTime;
    public int currPosition;
    public boolean ran1 = true;

    @Override
    public void init() {
        // declare telemetry for all motors/servos
        // this allows us to see how the motors are behaving in the code
        // and then compare it to how they perform in real life

        //currPosition = LatchMotor.getCurrentPosition();
    }

    // this is the first method run
    // it resets the elapsed time
    // flips arm/basket down and out of the way

    // move towards foundation
    public void toFoundation(){
        // strafe towards foundation
        accelDrive.pushCommand(0.5,0,0,1.5);
    }


    // attach to foundation and drag to triangle
    public void drag(){
        //and move as far as possible into triangle
        accelDrive.pushCommand(0.5,0,0, 1.5);
    }

    // this allows us to simply move out of the way of a partner
    public void move() {
        // if elapsed time >.5, move closer to build zone
        accelDrive.pushCommand(0,-0.5,0,.5);
        accelDrive.pushCommand(-0.5,0,0,1);
    }

    // allows the robot to move around the foundation and then push
    // it farther into the build zone.
    // Moves toward the midline, then strafes right around it
    // moves farther into the build zone and pushes the foundation
    // farther into the build zone
    public void around(){
        accelDrive.pushCommand(0,-0.5,0,0.5);
        accelDrive.pushCommand(-0.5,0,0,1);
        accelDrive.pushCommand(0,0.5,0,0.5);
        accelDrive.pushCommand(0.5,0,0,0.5);
        accelDrive.pushCommand(0,0,-0.5,0.5);
    }

    // parkY over midline close to neutral bridge
    // move forward then strafe right
    public void park() {
        // move forward a set amount of time
        accelDrive.pushCommand(0,-0.5,0,1.4);
        accelDrive.pushCommand(parkPos*0.5,0,0,0.3);
    }

    // stop all motion of the robot
    // set all motor powers to 0
    public void stop1(){
        // stop all motors
        drive(0,0,0);
        telemetry.addData("Emotion", "I hate everything!");
    }
}