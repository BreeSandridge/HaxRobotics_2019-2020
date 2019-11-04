package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.AreshPourkavoos.Accel_Drive;

// extend OpMode so future classes will extend SuperOp Instead
// implements is for interfaces
public abstract class SuperOp extends OpMode implements SuperOp_Interface {

    DcMotor FrontLeftDrive = null;
    DcMotor FrontRightDrive = null;
    DcMotor BackLeftDrive = null;
    DcMotor BackRightDrive = null;
    protected Accel_Drive accelDrive;

    @Override
    public void init() {
        // Initialize the hardware variables
        FrontLeftDrive  = hardwareMap.get(DcMotor.class, "FrontLeftDrive");
        FrontRightDrive = hardwareMap.get(DcMotor.class, "FrontRightDrive");
        BackLeftDrive  = hardwareMap.get(DcMotor.class, "BackLeftDrive");
        BackRightDrive = hardwareMap.get(DcMotor.class, "BackRightDrive");

        FrontLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        FrontRightDrive.setDirection(DcMotor.Direction.REVERSE);
        BackLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        BackRightDrive.setDirection(DcMotor.Direction.REVERSE);

        accelDrive = new Accel_Drive(FrontLeftDrive, FrontRightDrive,
                                      BackLeftDrive,  BackRightDrive);
    }

    public double[] update(){
        accelDrive.update();
        double[] motor_values = accelDrive.motorValues();
        return motor_values;
    }


    @Override
    public void drive(double x, double y, double w) {
        FrontLeftDrive.setPower(x+y+w);
        FrontRightDrive.setPower(x+y-w);
        BackLeftDrive.setPower(-x+y+w);
        BackRightDrive.setPower(x+y-w);
    }

    public void drive(double frontleft, double frontright, double backleft, double backright){
        FrontLeftDrive.setPower(frontleft);
        FrontRightDrive.setPower(frontright);
        BackLeftDrive.setPower(backleft);
        BackRightDrive.setPower(backright);
    }

    public void sleep_secs(double t){
        try {
            Thread.sleep((long)(t*1000));
        }
        catch(InterruptedException e){
        }
    }

    @Override
    public void t_drive(double x, double y, double w, double desired_rotations) {
        DriveParams newParams = new DriveParams(x, y, w, desired_rotations);
        accelDrive.pushCommand(newParams);
    }


}
