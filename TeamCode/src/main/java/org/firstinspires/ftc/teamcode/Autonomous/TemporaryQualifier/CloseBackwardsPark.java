package org.firstinspires.ftc.teamcode.Autonomous.TemporaryQualifier;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.SuperOp;

@Autonomous
public class CloseBackwardsPark extends SuperOp {
    enum AUTOSTATE {INIT, WAIT, LEAVE_WALL, CHANGE_DIRECTION, FORWARD, STOP}

    private AUTOSTATE state = AUTOSTATE.INIT;
    private double startTime;


    private final double delay = 0;
    private final double STRAFE_TIME = 0.5;
    private final double FORWARD_TIME = 1;


    @Override
    public void loop() {
        switch (state){
            case INIT:
                state = AUTOSTATE.WAIT;
            case WAIT:
                sleep_secs(delay);
            case LEAVE_WALL:
                teleDrive(-.5,0,0);
                sleep_secs(STRAFE_TIME);
            case CHANGE_DIRECTION:
                teleDrive(0,0,0);
                sleep_secs(0.5);
            case FORWARD:
                teleDrive(0, .5, 0);
                sleep_secs(FORWARD_TIME);
            case STOP:
                teleDrive(0,0,0);
        }
    }

}
