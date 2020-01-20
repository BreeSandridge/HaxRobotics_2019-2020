package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.util.ElapsedTime;

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
    private enum State {STOP, ACCEL, CONST, DECEL};
    private State driveState;
    private ElapsedTime elapsedTime;
    private Queue<DriveCommand> commands;
    public double[] motorPowers = {0, 0, 0, 0};
    public boolean isEmpty = true;

    public Accel_Drive() {

        elapsedTime = new ElapsedTime();
        elapsedTime.reset();
        commands = new LinkedList<>();

    }

    // Called by SuperOp.t_drive()
    // Pushes a new DriveState object onto the queue
    public void pushCommand(double x, double y, double w, double t){
        pushCommand(new DriveCommand(x, y, w, t));
    }

    public void pushCommand(DriveCommand newCommand){
        commands.add(newCommand);
        isEmpty = false;
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
                    set(nextCommand);
                }
                catch (NoSuchElementException e){
                    isEmpty = true;
                }
        }
    }

    public void drive(DriveState state) {
        drive(state.x, state.y, state.w);
    }

    public void drive(double x, double y, double w) {
        motorPowers[0] = y-x+w;
        motorPowers[1] = y+x-w;
        motorPowers[2] = y+x+w;
        motorPowers[3] = y-x-w;
    }

    public static class DriveState {
        public double x, y, w;
        DriveState(double x, double y, double w){
            this.x = x;
            this.y = y;
            this.w = w;
        }
        public DriveState times(double f){
            return new DriveState(x*f, y*f, w*f);
        }
        public DriveState times(DriveState f){
            return new DriveState(x*f.x, y*f.y, w*f.w);
        }
    }

    public static class DriveCommand {
        public DriveState state;
        public double t;
        DriveCommand(double x, double y, double w, double t){
            this.state = new DriveState(x, y, w);
            this.t = t;
        }
        DriveCommand(DriveCommand d){
            this.state = d.state;
            this.t = d.t;
        }
    }
}
