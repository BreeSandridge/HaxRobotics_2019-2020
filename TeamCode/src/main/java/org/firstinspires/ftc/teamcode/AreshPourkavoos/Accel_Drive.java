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
    private DriveParams currentCommand, adjustment;
    private enum State {STOP, ACCEL, CONST, DECEL};
    private State driveState;
    private ElapsedTime elapsedTime;
    private DcMotor FrontLeftDrive, FrontRightDrive;
    private DcMotor  BackLeftDrive,  BackRightDrive;
    Queue<DriveParams> commands;
    private double buffer = 0.1;
    private double startPoint;
    private HardwareMap hardwareMap;
    //private double x_speed, y_speed, w_speed;

    public Accel_Drive(HardwareMap hardwareMap, double startPoint,
                       double x_speed, double y_speed, double w_speed) {
        this.FrontLeftDrive  = hardwareMap.get(DcMotor.class, "FrontLeftDrive");
        this.FrontRightDrive = hardwareMap.get(DcMotor.class, "FrontRightDrive");
        this.BackLeftDrive  = hardwareMap.get(DcMotor.class, "BackLeftDrive");
        this.BackRightDrive = hardwareMap.get(DcMotor.class, "BackRightDrive");

        this.startPoint = startPoint;
        elapsedTime = new ElapsedTime();
        elapsedTime.reset();
        commands = new LinkedList<>();

        FrontLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        FrontRightDrive.setDirection(DcMotor.Direction.REVERSE);
        BackLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        BackRightDrive.setDirection(DcMotor.Direction.REVERSE);

        this.adjustment = new DriveParams(x_speed, y_speed, w_speed, 1);
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
    public void set(DriveParams state) {
        if (driveState != State.STOP)
            return;
        currentCommand = state;
        driveState = State.ACCEL;
        elapsedTime.reset();
    }

    // Copy of SuperOp.drive()
    public void drive(DriveParams params) {
        drive(params.x, params.y, params.w);
    }

    // This is the bread and butter of the trapezoid drive implementation
    // driveState variable can be one of ACCEL, CONST, DECEL, or STOP
    // If accel/decel, sets the drive appropriately
    // If const, does nothing (motors set once at the end of accel period)
    // If stopped, checks the queue for the next command
    public void update() {
        double portion = elapsedTime.seconds() / currentCommand.t;
        switch (driveState){
            case ACCEL:
                if (portion < buffer)
                    drive(currentCommand.times(portion/buffer));
                else{
                    drive(currentCommand);
                    driveState = State.CONST;
                }
                break;
            case CONST:
                if (portion > 1-buffer)
                    driveState = State.DECEL;
                break;
            case DECEL:
                if (portion < 1)
                    drive(currentCommand.times((1-portion)/buffer));
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

    public void setMode(DcMotor.RunMode mode){
        FrontRightDrive.setMode(mode);
        FrontLeftDrive.setMode(mode);
        BackLeftDrive.setMode(mode);
        BackRightDrive.setMode(mode);
    }
    public void drive(double x, double y, double w) {
        double xAdjusted = (adjustment.x * x) * startPoint;
        double yAdjusted = (adjustment.y * y) * startPoint;
        double wAdjusted = (adjustment.w * w);
        driveAdjusted(xAdjusted, yAdjusted, wAdjusted);
    }
    public void driveAdjusted(double x, double y, double w) {
        FrontLeftDrive.setPower(y - x + w);
        FrontRightDrive.setPower(y + x - w);
        BackLeftDrive.setPower(y + x + w);
        BackRightDrive.setPower(y - x - w);
    }

}
