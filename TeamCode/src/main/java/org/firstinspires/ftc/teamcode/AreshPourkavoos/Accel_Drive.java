package org.firstinspires.ftc.teamcode.AreshPourkavoos;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.DriveParams;

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
    private double x, y, w, t;
    private enum State {STOP, ACCEL, CONST, DECEL};
    private State driveState;
    public ElapsedTime elapsedTime;
    DcMotor FrontLeftDrive, FrontRightDrive;
    DcMotor  BackLeftDrive,  BackRightDrive;
    Queue<DriveParams> commands;

    public Accel_Drive(DcMotor FrontLeftDrive, DcMotor FrontRightDrive,
                       DcMotor  BackLeftDrive, DcMotor  BackRightDrive) {
        this.FrontLeftDrive  = FrontLeftDrive;
        this.FrontRightDrive = FrontRightDrive;
        this.BackLeftDrive   = BackLeftDrive;
        this.BackRightDrive  = BackRightDrive;
        elapsedTime = new ElapsedTime();
        elapsedTime.reset();
        commands = new LinkedList<>();
    }

    // Called by SuperOp.t_drive()
    // Pushes a new DriveParams object onto the queue
    public void pushCommand(DriveParams newCommand){
        commands.add(newCommand);
    }

    // Begin executing a new command
    // this.x, y, w, t are the values of the current command
    // but will (probably) later be replaced by a DriveParams object
    // Makes sure that robot is stopped,
    // but should only be called under those circumstances anyway
    public void set(DriveParams state){
        if (driveState != State.STOP)
            return;
        this.x = state.x;
        this.y = state.y;
        this.w = state.w;
        this.t = state.t;
        driveState = State.ACCEL;
        elapsedTime.reset();
    }

    // Copy of SuperOp.drive()
    public void drive(double x, double y, double w) {
        FrontLeftDrive.setPower(x+y+w);
        FrontRightDrive.setPower(x+y-w);
        BackLeftDrive.setPower(-x+y+w);
        BackRightDrive.setPower(x+y-w);
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
                if (portion < 0.1)
                    drive(x * portion * 10, y * portion * 10, w * portion * 10);
                else{
                    drive(x, y, w);
                    driveState = State.CONST;
                }
                break;
            case CONST:
                if (portion > 0.9)
                    driveState = State.DECEL;
                break;
            case DECEL:
                if (portion < 1)
                    drive(x * (1 - portion) * 10, y * (1 - portion) * 10, w * (1 - portion) * 10);
                else{
                    driveState = State.STOP;
                    drive(0, 0, 0);
                }
                break;
            case STOP:
                DriveParams nextCommand;
                try{
                    nextCommand = commands.remove();
                }
                catch (NoSuchElementException e){
                    break;
                }
                set(nextCommand);
        }
    }
}
