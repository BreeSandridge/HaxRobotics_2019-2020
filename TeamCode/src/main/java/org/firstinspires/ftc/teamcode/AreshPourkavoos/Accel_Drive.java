package org.firstinspires.ftc.teamcode.AreshPourkavoos;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.DriveParams;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

public class Accel_Drive{

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

    public void pushCommand(DriveParams newCommand){
        commands.add(newCommand);
    }

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

    public void drive(double x, double y, double w) {
        FrontLeftDrive.setPower(x+y+w);
        FrontRightDrive.setPower(x+y-w);
        BackLeftDrive.setPower(-x+y+w);
        BackRightDrive.setPower(x+y-w);
    }

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
