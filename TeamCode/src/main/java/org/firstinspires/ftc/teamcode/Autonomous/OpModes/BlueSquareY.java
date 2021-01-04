package org.firstinspires.ftc.teamcode.Autonomous.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Autonomous.PlayerSuperOp;

@Autonomous
public class BlueSquareY extends BlueSquare {


    //This uses an enum declared in SuperOp
    //It declares the first STATUS as "START"
    private PLAYERSTATUS status = PLAYERSTATUS.FLIPPER;
    //create new stopwatch

      @Override
    public void init() {
        super.init();
        parkPos = 1;
    }
  
    @Override
    public void loop() {
        noInterference = true;
        parkWall = false;
        startPoint = -1;
        parkPos = 1;
        //declare telemetry for all motors/servos
        //this allows us to see how the motors are behaving in the code
        //and then compare it to how they perform in real life
        telemetry.addData("Arm", arm.seconds());
        telemetry.addData("Time: ", time.seconds());
        telemetry.addData("Status: ", status);
        //switch statements for changing the status of the robot
        //this allows us to use different code for each status
        //there are methods created below the switch statement for easier reading
        switch (status) {
            case FLIPPER:
                toBlock();
                status = PLAYERSTATUS.GRAB;
                break;
            case GRAB:
                if(accelDrive.isEmpty) {
                    grab();
                    status = PLAYERSTATUS.TOBLOCK;
                } else {
                    updateAndDrive();
                }
                break;
            case TOBLOCK:
                if(time.seconds() < (parkWall? 4.8 : 3.6)){
                    teleDrive(0.5,0,0);
                } else {
                    teleDrive(0,0,0);
                    time.reset();
                    status = PLAYERSTATUS.TOBLOCK2;
                }
                break;
            case TOBLOCK2:
                if(time.seconds() < (2.3)){
                    drive1(0,0.5,0);
                } else {
                    drive1(0,0,0);
                    status = PLAYERSTATUS.AWAY;
                }
                break;
            case AWAY:
                if(accelDrive.isEmpty){
                    release();
                    status = PLAYERSTATUS.DECISION;
                } else {
                    updateAndDrive();
                }
                break;
            case DECISION:
                if (repeat.seconds() < 15) {
                    parkBlue();
                    status = PLAYERSTATUS.PARK;
                } else {
                    parkBlue();
                    status = PLAYERSTATUS.PARK;
                }
            case AGAIN:
                if(accelDrive.isEmpty){
                    status = PLAYERSTATUS.FLIPPER;
                } else {
                    updateAndDrive();
                }
                break;
            case PARK:
                if(accelDrive.isEmpty){
                    status = PLAYERSTATUS.STOP;
                } else {
                    updateAndDrive();
                }
                break;
            case STOP:
                stop1();
                break;
        }
    }
}

