package org.firstinspires.ftc.teamcode.Autonomous.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.Autonomous.PlayerSuperOp;
@Disabled
public class BlueSquareBlock extends PlayerSuperOp {
    public PLAYERSTATUS status = PLAYERSTATUS.FLIPPER;
    @Override
    public void loop() {
        startPoint = -1;
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
                    away2();
                    status = PLAYERSTATUS.AWAY2;
                } else {
                    updateAndDrive();
                }
                break;
            case AWAY2:
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
