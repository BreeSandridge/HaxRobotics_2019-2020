package org.firstinspires.ftc.teamcode.Examples;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.SuperOp;

@TeleOp(name="Example Accel Drive", group="Examples")
public class Example_Accel_Drive_Implementation extends SuperOp {

    enum State{FORWARD, TURN, BACK, WAIT};
    State currState = State.BACK;
    @Override
    public void loop() {
        switch (currState){
            case FORWARD:
                if (accelDrive.isEmpty)
                    toTurn();
                else
                    forward();
                break;
            case TURN:
                if (accelDrive.isEmpty)
                    toBack();
                else
                    turn();
                break;
            case BACK:
                if (accelDrive.isEmpty)
                    toWait();
                else
                    back();
                break;
            case WAIT:
                if (accelDrive.isEmpty)
                    toForward();
                else
                    waitState();
                break;
        }
    }

    void toForward(){
        accelDrive.pushCommand(1, 0, 0, 1);
        currState = State.FORWARD;
    }

    void forward(){
        telemetry.addLine("Going forward...");
        updateAndDrive();
    }

    void toTurn(){
        accelDrive.pushCommand(0, 0, 1, 1);
        accelDrive.pushCommand(0, 0, -1, 1);
        currState = State.TURN;
    }

    void turn(){
        telemetry.addLine("Turning...");
        updateAndDrive();
    }

    void toBack(){
        accelDrive.pushCommand(-1, 0, 0, 1);
        currState = State.BACK;
    }

    void back(){
        telemetry.addLine("Going back...");
        updateAndDrive();
    }

    void toWait(){
        currState = State.WAIT;
    }

    void waitState(){
        telemetry.addLine("Waiting...");
    }
}
