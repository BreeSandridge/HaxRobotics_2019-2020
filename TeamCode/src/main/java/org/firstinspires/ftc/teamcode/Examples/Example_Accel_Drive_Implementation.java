package org.firstinspires.ftc.teamcode.Examples;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.SuperOp;

@TeleOp(name="Example Accel Drive", group="Examples")
public class Example_Accel_Drive_Implementation extends SuperOp {

    enum State{FORWARD, TURN, BACK};
    State currState = State.BACK;
    @Override
    public void loop() {
        switch (currState){
            case FORWARD:
                if (accelDrive.isEmpty) {
                    accelDrive.pushCommand(0, 0, 1, 1);
                    accelDrive.pushCommand(0, 0, -1, 1);
                    currState = State.TURN;
                }
                break;
            case TURN:
                if (accelDrive.isEmpty) {
                    accelDrive.pushCommand(-1, 0, 0, 1);
                    currState = State.BACK;
                }
                break;
            case BACK:
                if (accelDrive.isEmpty)
                    accelDrive.pushCommand(1, 0, 0, 1);
                    currState = State.FORWARD;
                break;
        }
        updateAndDrive();
    }
}
