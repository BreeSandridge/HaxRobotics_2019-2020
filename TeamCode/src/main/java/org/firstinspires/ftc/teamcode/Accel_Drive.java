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
    private DriveCommand currentCommand = new DriveCommand(0, 0, 0, 0); // Command being executed
    private enum State {STOP, ACCEL, CONST, DECEL}; // 4 stages of movement
    private State driveState = State.STOP; // Current stage of movement
    private ElapsedTime elapsedTime; // Time since current command began execution
    private Queue<DriveCommand> commands; // Stores all commands to be run
    public double[] motorPowers = {0, 0, 0, 0}; // Accessed by opmode to drive motors
    public boolean isEmpty = true;  // Whether all commands have been executed


    public Accel_Drive() {
        elapsedTime = new ElapsedTime();
        elapsedTime.reset();
        commands = new LinkedList<>();

    }

    // Called by SuperOp.t_drive()
    // Pushes a new DriveCommand object onto the queue
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
        double buffer = 0.1; // For longer acceleration period, increase (up to 0.5)
        // portion varies from 0 to 1
        double portion = elapsedTime.seconds() / currentCommand.t;
        // currentState is not the current state of the motors
        // but the state of the current command,
        // i.e. the max speed it will reach
        DriveState currentState = currentCommand.state;
        switch (driveState){ // Could be done with portion as well
            case ACCEL:
                if (portion < buffer)
                    drive(currentState.times(portion/buffer));
                else{
                    // Only need to modify motor state once
                    // since the next state is CONST (max speed)
                    drive(currentState);
                    driveState = State.CONST;
                }
                break;
            case CONST: // Does nothing until the state is ready to be switched
                if (portion > 1-buffer)
                    driveState = State.DECEL;
                break;
            case DECEL: // Analogous to ACCEL
                if (portion < 1)
                    drive(currentState.times((1-portion)/buffer));
                else{
                    // Again, only need to modify motor state once
                    driveState = State.STOP;
                    drive(0, 0, 0);
                }
                break;
            case STOP:
                DriveCommand nextCommand;
                // Prepare to execute the next command if there is one
                try {
                    nextCommand = commands.remove();
                    set(nextCommand);
                }
                catch (NoSuchElementException e){
                    isEmpty = true;
                }
        }
    }

    // Unpack values from DriveState for readability
    public void drive(DriveState state) { drive(state.x, state.y, state.w); }

    // Since Accel_Drive can't access the motors directly,
    // it assigns values to the public motorPowers array,
    // which SuperOp accesses with the updateMotors method
    public void drive(double x, double y, double w) {
        motorPowers[0] = y-x+w;
        motorPowers[1] = y+x-w;
        motorPowers[2] = y+x+w;
        motorPowers[3] = y-x-w;
    }

    // Not used outside of Accel_Drive: stores state of motors
    public static class DriveState {

        double x, y, w; // Strafing, forward movement, rotation

        DriveState(double x, double y, double w){
            this.x = x;
            this.y = y;
            this.w = w;
        }

        // Used for accel and decel stages
        DriveState times(double f){
            return new DriveState(x*f, y*f, w*f);
        }
    }

    // Stores a single command (motor state + duration)
    static class DriveCommand {
        DriveState state;
        double t;

        DriveCommand(double x, double y, double w, double t){
            this.state = new DriveState(x, y, w);
            this.t = t;
        }

        DriveCommand(DriveCommand d){ // Copy constructor
            this.state = d.state;
            this.t = d.t;
        }
    }
}
