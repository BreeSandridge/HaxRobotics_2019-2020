package org.firstinspires.ftc.teamcode.Autonomous.OpModes;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Autonomous.BuildSuperOp;

@Autonomous
public class BlueTriangleW extends BuildSuperOp {

    //This uses an enum declared in SuperOp
    //It declares the first STATUS as "START"
    public BUILDSTATUS status = BUILDSTATUS.FLIPPER;
    //create new stopwatch

    @Override
    public void loop() {
        startPoint = 1;
        //declare telemetry for all motors/servos
        //this allows us to see how the motors are behaving in the code
        //and then compare it to how they perform in real life
        telemetry.addData("Power", LatchMotor.getPower());
        telemetry.addData("LatchMotor Position: ", LatchMotor.getCurrentPosition());
        telemetry.addData("Front Right: ", FrontRightDrive.getCurrentPosition());
        telemetry.addData("Back Left: ", BackLeftDrive.getCurrentPosition());
        telemetry.addData("Back Right: ", BackRightDrive.getCurrentPosition());
        telemetry.addData("Front Left: ", FrontLeftDrive.getCurrentPosition());
        telemetry.addData("Status: ", status);

        //switch statements for changing the status of the robot
        //this allows us to use different code for each status
        //there are methods created below the switch statement for easier reading
        switch (status) {
            case FLIPPER:
                toFoundation();
                status = BUILDSTATUS.TOFOUNDATION;
                break;
            case TOFOUNDATION:
                if(accelDrive.isEmpty) {
                    LatchMotor.setPower(0.3);
                    sleep_secs(0.5);
                    LatchMotor.setPower(0);
                    drag();
                    status = BUILDSTATUS.DRAG;
                } else {
                    updateAndDrive();
                }
                break;
            case DRAG:
                if(accelDrive.isEmpty) {
                    LatchMotor.setPower(-0.3);
                    sleep_secs(0.5);
                    LatchMotor.setPower(0);
                    around();
                    status = BUILDSTATUS.AROUND;
                } else {
                    updateAndDrive();
                }
                break;
            case AROUND:
                if(accelDrive.isEmpty) {
                    parkW();
                    status = BUILDSTATUS.PARKW;
                } else {
                    updateAndDrive();
                }
                break;
            case PARKW:
                if(accelDrive.isEmpty) {
                    status = BUILDSTATUS.STOP;
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


