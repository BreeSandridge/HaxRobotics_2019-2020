package org.firstinspires.ftc.teamcode.Autonomous.OpModes;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Autonomous.BuildSuperOp;
import org.firstinspires.ftc.teamcode.SuperOp;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous
public class RedTriangle extends BuildSuperOp {

    //This uses an enum declared in SuperOp
    //It declares the first STATUS as "START"
    private STATUS status = STATUS.FLIPPER;

    //create new stopwatch
    private ElapsedTime time = new ElapsedTime();
    private double targetTime;
    private ElapsedTime arm = new ElapsedTime();
    private int currPosition;
    private int targetPosition;
    private boolean ran = false;
    private boolean ran1 = true;

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
        telemetry.addData("Latch Position: ", Latch.getPosition());

        currPosition = LatchMotor.getCurrentPosition();
        //switch statements for changing the status of the robot
        //this allows us to use different code for each status
        //there are methods created below the switch statement for easier reading
        switch (status) {
            case FLIPPER:
                flipper();
                break;
            case START:
                start1();
                break;
            case TOBLOCK:
                toBlock();
                break;
            case APPROACH:
                approach();
                break;
            case GETBLOCK:
                getBlock();
                //status = STATUS.AROUND;
                break;
            case AWAY:
                //time.reset();
                //away();
                break;
            case TOBUILD:
                toBuild();
                break;
            case RELEASEBLOCK:
                release();
                break;
            case PARK:
                //time.reset();
                park();
                break;
            case STOP:
                stop1();
                break;
        }
    }
}