package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.SuperOp;
@Autonomous
public abstract class PlayerSuperOp extends SuperOp {

    public ElapsedTime time = new ElapsedTime();
    public double targetTime;
    public ElapsedTime arm = new ElapsedTime();
    public ElapsedTime repeat = new ElapsedTime();
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
        //switch statements for changing the status of the robot
        //this allows us to use different code for each status
        //there are methods created below the switch statement for easier reading
    }


    //this is the first method run
    //it resets the elapsed time
    //then switches the status to 'TOBLOCK'
    //this is the first method run
    //it resets the elapsed time
    //then switches the status to 'TOBLOCK'
    public void flipper() {
        if (ran1) {
            time.reset();
            ran1 = !ran1;
        }
        targetTime = .525;
        FlipperMotor.setPower(.3);

        if (time.seconds() >= targetTime) {

            if (time.seconds() >= targetTime) {
                FlipperMotor.setPower(0);
                time.reset();
            }
        }
    }
    public void toBlock() {
        targetTime = 1;
        drive(-.5, 0, 0);
        time.reset();

        if (!ran) {
            arm.reset();
            ran = !ran;
        }
        currPosition = LatchMotor.getCurrentPosition();
        LatchMotor.setPower(0.3);
        sleep_secs(.5);
        if ((currPosition <= targetPosition + 13 && currPosition >= targetPosition - 6) || arm.seconds() > 1) {
            LatchMotor.setPower(0);
            if (!ran1) {
                time.reset();
                ran1 = !ran1;
            }
            targetTime = 1.1;
            drive(-0.5, 0, 0);
            if (time.seconds() >= targetTime) {
                drive(0, 0, 0);
                time.reset();
            }
        }

        targetTime = 1;
        drive(.5, 0, 0);
        time.reset();
    }

        public void away () {
        targetTime = 1;
        drive(0, -.5, 0);
        time.reset();
        LatchMotor.setPower(-.3);
    }

        public void again () {
        targetTime = 1;
        drive(0,.5,0);

        toBlock();
        away();
    }

    public void toFoundation(){
        targetTime = 1.5;
        drive(0,-0.5,0);
        if(time.seconds() >= targetTime){
            drive(0,0,0);
            Latch.setPosition(1);
            time.reset();
        }
    }
    public void drag(){
        drive(0,0.5,0);
        if(time.seconds() >= targetTime){
            drive(0,0,0);
            Latch.setPosition(0);
            time.reset();
        }
    }
    public void around(){
        if(time.seconds() <= 0.5){
            drive(-0.5,0,0);
        }
        drive(0,0,0);
        sleep_secs(0.4);
        if(time.seconds() <= 1.5 && time.seconds() > 0.5){
            drive(0,-0.5,0);
        }
        drive(0,0,0);
        sleep_secs(0.4);
        if(time.seconds() <= 2 && time.seconds() > 1.5){
            drive(0.5,0,0);
        }
        drive(0,0,0);
        sleep_secs(0.4);
        if(time.seconds() <= 2.5 && time.seconds() > 2){
            drive(0,0.5,0);
        }
        drive(0,0,0);
        sleep_secs(0.4);
        if(time.seconds() <= 3 && time.seconds() > 2.5){
            drive(0,0,-0.5);
        }
        drive(0,0,0);
        sleep_secs(0.4);
    }
    //park the robot in the middle of the alliance bridge
    //drive 1.5 seconds forward, then stop
    //switch STATUS to 'STOP'
    public void park() {
        // vision code to park the robot under the bridge
        //t_drive(0, -1, 0, 1);
        //targetTime = .9;
        targetTime = 1.4;
        drive(0, 0.5, 0);
        if(time.seconds() >= targetTime){
            //stop robot
            drive(0.5,0,0);
            sleep_secs(0.3);
            drive(0,0,0);
            //switch STATUS
        }
    }

    //stop all motion of the robot
    //set all motor powers to 0
    public void stop1(){
        drive(0,0,0);
        telemetry.addData("Emotion", "I hate everything!");
    }
}
