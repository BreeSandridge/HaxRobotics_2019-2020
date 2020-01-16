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
    public double[] motorPowers = {0.0, 0.0, 0.0};
    private double x, y, w, t;
    private enum State {STOP, ACCEL, CONST, DECEL};
    private State driveState;
    public ElapsedTime elapsedTime;
    Queue<DriveCommand> commands;


    public Accel_Drive() {
        elapsedTime = new ElapsedTime();
        elapsedTime.reset();
        commands = new LinkedList<>();
    }

    // Called by SuperOp.t_drive()
    // Pushes a new DriveCommand object onto the queue
    public void pushCommand(DriveCommand newCommand){
        commands.add(newCommand);
    }

    // Begin executing a new command
    // this.x, y, w, t are the values of the current command
    // but will (probably) later be replaced by a DriveCommand object
    // Makes sure that robot is stopped,
    // but should only be called under those circumstances anyway
    public void set(DriveCommand command) {
        if (driveState != State.STOP) {
            return;
        }

        this.x = command.state.x;
        this.y = command.state.y;
        this.w = command.state.w;
        this.t = command.t;
        driveState = State.ACCEL;
        elapsedTime.reset();
    }


    // This is the bread and butter of the trapezoid drive implementation
    // driveState variable can be one of ACCEL, CONST, DECEL, or STOP
    // If accel/decel, sets the drive appropriately
    // If const, does nothing (motors set once at the end of accel period)
    // If stopped, checks the queue for the next command
    public void update() {
        double portion = elapsedTime.seconds() / t;
        switch (driveState){
            case ACCEL:
                if (portion < 0.1) {
                    motorPowers[0] = x * portion * 10;
                    motorPowers[1] = y * portion * 10;
                    motorPowers[2] = w * portion * 10;
                }
                else{
                    motorPowers[0] = x;
                    motorPowers[1] = y;
                    motorPowers[2] = w;
                    driveState = State.CONST;
                }
                break;
            case CONST:
                if (portion > 0.9)
                    driveState = State.DECEL;
                break;
            case DECEL:
                if (portion < 1) {
                    motorPowers[0] = x * (1 - portion) * 10;
                    motorPowers[1] = y * (1 - portion) * 10;
                    motorPowers[2] = w * (1 - portion) * 10;
                } else{
                    driveState = State.STOP;
                    motorPowers[0] = 0;
                    motorPowers[1] = 1;
                    motorPowers[2] = 2;
                }
                break;
            case STOP:
                DriveCommand nextCommand;
                try{
                    nextCommand = commands.remove();
                }
                catch (NoSuchElementException e){
                    break;
                }
                set(nextCommand);
        }
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
            this.state = state;
            this.t = d.t;
        }
    }
}
