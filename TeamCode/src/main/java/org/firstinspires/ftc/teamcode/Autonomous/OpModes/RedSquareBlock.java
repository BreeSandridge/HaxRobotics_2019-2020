package org.firstinspires.ftc.teamcode.Autonomous.OpModes;

import org.firstinspires.ftc.teamcode.Autonomous.PlayerSuperOp;

public class RedSquareBlock extends PlayerSuperOp {
    public PLAYERSTATUS status = PLAYERSTATUS.FLIPPER;
    @Override
    public void loop() {
        startPoint = 1;
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
                status = PLAYERSTATUS.AWAY2;
                break;
            case AWAY2:
                away2();
                status = PLAYERSTATUS.STOP;
                break;
            case STOP:
                stop1();
                break;
        }
    }
}
