package org.firstinspires.ftc.teamcode.Autonomous.OpModes.AutoSupers;

import org.firstinspires.ftc.teamcode.Autonomous.PlayerSuperOp;

public abstract class BlueSquarePark extends PlayerSuperOp {

    public PLAYERSTATUS status = PLAYERSTATUS.FLIPPER;

    @Override
    public void loop() {
        startPoint = -1;
        //declare telemetry for all motors/servos
        //this allows us to see how the motors are behaving in the code
        //and then compare it to how they perform in real life
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
                park();
                status = PLAYERSTATUS.PARK;
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
