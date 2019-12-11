package org.firstinspires.ftc.teamcode.AreshPourkavoos;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.DriveParams;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;


@Autonomous
public class Accel_Drive{
  /*
Accel_Drive class
The Accel_Drive class implements the trapezoid drive,
along with the ability to process multiple commands in a queue
The SuperOp class contains an Accel_Drive object to maintain abstraction
 */
    final double WHEEL_DIAMETER = 10.16; // centimeters, or 4 inches, mecanum wheels
    final double WHEEL_CIRCUMFERENCE = WHEEL_DIAMETER*3.14;
    private double x, y, w, desired_rotations;    // x, y, w are desired values for speed


    private enum State {STOP, ACCEL, CONST, DECEL};
    private State driveState;
    //public ElapsedTime elapsedTime;
    DcMotor FrontLeftDrive, FrontRightDrive;
    DcMotor  BackLeftDrive,  BackRightDrive;
    Queue<DriveParams> commands;
    private double x_current, y_current, w_current; // three speed to be updated and returned

public Accel_Drive(DcMotor FrontLeftDrive, DcMotor FrontRightDrive,
                       DcMotor  BackLeftDrive, DcMotor  BackRightDrive) {
        this.FrontLeftDrive  = FrontLeftDrive;
        this.FrontRightDrive = FrontRightDrive;
        this.BackLeftDrive   = BackLeftDrive;
        this.BackRightDrive  = BackRightDrive;
       // elapsedTime = new ElapsedTime();
       // elapsedTime.reset();
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
        this.desired_rotations = state.desired_rotations;
        driveState = State.ACCEL;
        //elapsedTime.reset();

        FrontLeftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        FrontRightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        BackLeftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        BackRightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        FrontLeftDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        FrontRightDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        BackLeftDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        BackRightDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }


    public String getCurrentPos(){
        String currentPositionStr = "Path0"+"Starting at: " + FrontLeftDrive.getCurrentPosition() + ", "
                + FrontRightDrive.getCurrentPosition() + ", " + BackRightDrive.getCurrentPosition() + ", "
                + BackLeftDrive.getCurrentPosition();
        return currentPositionStr;
    }



    // Copy of SuperOp.drive()
    public void drive(double x, double y, double w) {
        FrontLeftDrive.setPower(x+y+w);
        FrontRightDrive.setPower(x+y-w);
        BackLeftDrive.setPower(-x+y+w);
        BackRightDrive.setPower(x+y-w);
    }



    // converting controller x, y, w values to power values to be set to for each motor
    // return values to autonomous drive to set values to each motor
    public double[] motorValues(){
        double FrontLeftDrive_value = x+y+w;
        double FrontRightDrive_value = x+y-w;
        double BackLeftDrive_value = -x+y+w;
        double BackRightDrive_value = x+y-w;
        double[] motor_values = {FrontLeftDrive_value, FrontRightDrive_value, BackLeftDrive_value, BackRightDrive_value};
        return motor_values;
    }


    // update the values of x, y, and w in relation to current rotations of wheel,
    // (encoder, rather than using time), for acceleration drive
    // This is the bread and butter of the trapezoid drive implementation
    // driveState variable can be one of ACCEL, CONST, DECEL, or STOP
    // If accel/decel, sets the drive appropriately
    // If const, does nothing (motors set once at the end of accel period)
    // If stopped, checks the queue for the next command

    public void update() {
        double portion = (FrontLeftDrive.getCurrentPosition()+FrontRightDrive.getCurrentPosition()+
                BackRightDrive.getCurrentPosition()+BackLeftDrive.getCurrentPosition())/4*desired_rotations;
        switch (driveState){
            case ACCEL:
                if (portion < 0.1){
                    //drive(x * portion * 10, y * portion * 10, w * portion * 10);
                    x_current = x * portion * 10;
                    y_current = y * portion * 10;
                    w_current = w * portion * 10;
                }
                else{
                    //drive(x, y, w);
                    x_current = x;
                    y_current = y;
                    w_current = w;
                    driveState = State.CONST;
                }
                break;
            case CONST:
                if (portion > 0.9)
                    driveState = State.DECEL;
                break;
            case DECEL:
                if (portion < 1) {
                    //drive(x * (1 - portion) * 10, y * (1 - portion) * 10, w * (1 - portion) * 10);
                    x_current = x * (1 - portion) * 10;
                    y_current = y * (1 - portion) * 10;
                    w_current = w * (1 - portion) * 10;
                }
                else{
                    driveState = State.STOP;
                    x_current = 0;
                    y_current = 0;
                    w_current = 0;
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
