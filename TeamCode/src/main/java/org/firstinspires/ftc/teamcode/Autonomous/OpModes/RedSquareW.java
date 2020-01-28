package org.firstinspires.ftc.teamcode.Autonomous.OpModes;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Autonomous.PlayerSuperOp;

@Autonomous
public class RedSquareW extends PlayerSuperOp {

    //This uses an enum declared in SuperOp
    //It declares the first STATUS as "START"
    private PLAYERSTATUS status = PLAYERSTATUS.FLIPPER;

    //create new stopwatch

    @Override
    public void loop() {
        startPoint = 1;
        parkPos = -1;
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
        telemetry.addData("Status: ", status);

        currPosition = LatchMotor.getCurrentPosition();
        //switch statements for changing the status of the robot
        //this allows us to use different code for each status
        //there are methods created below the switch statement for easier reading
        switch (status) {
            case FLIPPER:
                toBlock();
                status = PLAYERSTATUS.TOBLOCK;
                break;
            case TOBLOCK:
                if(accelDrive.isEmpty){
                    grab();
                    away();
                    status = PLAYERSTATUS.AWAY;
                } else {
                    updateAndDrive();
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
                    again();
                    status = PLAYERSTATUS.AGAIN;
                } else {
                    park();
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

