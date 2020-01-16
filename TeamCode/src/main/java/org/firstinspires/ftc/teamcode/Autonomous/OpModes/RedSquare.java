package org.firstinspires.ftc.teamcode.Autonomous.OpModes;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Autonomous.PlayerSuperOp;
import org.firstinspires.ftc.teamcode.SuperOp;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous
public class RedSquare extends PlayerSuperOp {

    //This uses an enum declared in SuperOp
    //It declares the first STATUS as "START"
    private PLAYERSTATUS status = PLAYERSTATUS.FLIPPER;

    //create new stopwatch

    @Override
    public void loop() {
        startPointPlayer = -1;
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
        telemetry.addData("Latch Position: ", Latch.getPosition());

        currPosition = LatchMotor.getCurrentPosition();
        //switch statements for changing the status of the robot
        //this allows us to use different code for each status
        //there are methods created below the switch statement for easier reading
        switch (status) {
            case FLIPPER:
                flipper();
                status = PLAYERSTATUS.TOBLOCK;
                break;
            case TOBLOCK:
                toBlock();
                status = PLAYERSTATUS.AWAY;
                break;
            case AWAY:
                away();
                status = PLAYERSTATUS.AGAIN;
                break;
            case AGAIN:
                if (repeat.seconds() < 15) {
                    again();
                    status = PLAYERSTATUS.PARK;
                } else {
                    status = PLAYERSTATUS.PARK;
                }
            case PARK:
                park();
                status = PLAYERSTATUS.STOP;
                break;
            case STOP:
                stop1();
                break;
        }
    }
}

