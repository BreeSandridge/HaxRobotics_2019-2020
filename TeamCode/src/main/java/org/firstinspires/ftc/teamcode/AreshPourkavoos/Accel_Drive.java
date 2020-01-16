package org.firstinspires.ftc.teamcode.AreshPourkavoos;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

/*
Accel_Drive class
The Accel_Drive class implements the trapezoid drive,
along with the ability to process multiple commands in a queue
The SuperOp class contains an Accel_Drive object to maintain abstraction
 */

public class Accel_Drive{

    // Variable declarations
    private DriveCommand currentCommand;
    private DriveState adjustment;
    private enum State {STOP, ACCEL, CONST, DECEL};
    private State driveState;
    private ElapsedTime elapsedTime;
    private Queue<DriveCommand> commands;

    public Accel_Drive(double startPoint,
                       double x_speed, double y_speed, double w_speed) {

        elapsedTime = new ElapsedTime();
        elapsedTime.reset();
        commands = new LinkedList<>();


        this.adjustment = new DriveState(x_speed*startPoint, y_speed*startPoint, w_speed);
    }

    // Called by SuperOp.t_drive()
    // Pushes a new DriveState object onto the queue
    public void pushCommand(DriveCommand newCommand){
        commands.add(newCommand);
    }

    // Begin executing a new command
    // this.x, y, w, t are the values of the current command
    // but will (probably) later be replaced by a DriveState object
    // Makes sure that robot is stopped,
    // but should only be called under those circumstances anyway
    public void set(DriveCommand newCommand) {
        if (driveState != State.STOP)
            return;
        currentCommand = newCommand;
        driveState = State.ACCEL;
        elapsedTime.reset();
    }

    // This is the bread and butter of the trapezoid drive implementation
    // driveState variable can be one of ACCEL, CONST, DECEL, or STOP
    // If accel/decel, sets the drive appropriately
    // If const, does nothing (motors set once at the end of accel period)
    // If stopped, checks the queue for the next command
    public void update() {
        double buffer = 0.1;
        double portion = elapsedTime.seconds() / currentCommand.t;
        DriveState currentState = currentCommand.state;
        switch (driveState){
            case ACCEL:
                if (portion < buffer)
                    drive(currentState.times(portion/buffer));
                else{
                    drive(currentState);
                    driveState = State.CONST;
                }
                break;
            case CONST:
                if (portion > 1-buffer)
                    driveState = State.DECEL;
                break;
            case DECEL:
                if (portion < 1)
                    drive(currentState.times((1-portion)/buffer));
                else{
                    driveState = State.STOP;
                    drive(0, 0, 0);
                }
                break;
            case STOP:
                DriveCommand nextCommand;
                try {
                    nextCommand = commands.remove();
                }
                catch (NoSuchElementException e){

                    break;
                }
                set(nextCommand);
        }
    }

    public double[] drive(double x, double y, double w) {
        return drive(new DriveState(x, y, w));
    }

    public double[] drive(DriveState state) {
        return driveAdjusted(state.times(adjustment));
    }

    public double[] driveAdjusted(DriveState state) {
        return driveAdjusted(state.x, state.y, state.w);
    }

    public double[] driveAdjusted(double x, double y, double w) {
        double[] vals = {y-x+w, y+x-w, y+x+w, y-x-w};
        return vals;
    }

}
