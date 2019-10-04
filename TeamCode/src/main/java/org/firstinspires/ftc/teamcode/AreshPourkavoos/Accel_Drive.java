package org.firstinspires.ftc.teamcode.AreshPourkavoos;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

public class Accel_Drive{

    private double x, y, w, t;
    private boolean running;
    private enum state {STOP, ACCEL, CONST, DECEL};
    public ElapsedTime elapsedTime;
    DcMotor FrontLeftDrive, FrontRightDrive;
    DcMotor  BackLeftDrive,  BackRightDrive;

    public Accel_Drive(DcMotor FrontLeftDrive, DcMotor FrontRightDrive,
                       DcMotor  BackLeftDrive, DcMotor  BackRightDrive) {
        this.FrontLeftDrive  = FrontLeftDrive;
        this.FrontRightDrive = FrontRightDrive;
        this.BackLeftDrive   = BackLeftDrive;
        this.BackRightDrive  = BackRightDrive;
        elapsedTime = new ElapsedTime();
        elapsedTime.reset();
        running = false;
    }

    public void set(double x, double y, double w, double t){
        this.x = x;
        this.y = y;
        this.w = w;
        this.t = t;
        state = ;
        elapsedTime.reset();
    }

    public void drive(double x, double y, double w) {
        FrontLeftDrive.setPower(x+y+w);
        FrontRightDrive.setPower(x+y-w);
        BackLeftDrive.setPower(-x+y+w);
        BackRightDrive.setPower(x+y-w);
    }

    public void update() {
        if (running) {
            double s = elapsedTime.seconds() / t;
            if (s < 0.1)
                drive(x * s * 10, y * s * 10, w * s * 10);
            else if (s < 0.9)
                drive(x, y, w);
            else if (s < 1)
                drive(x * (1 - s) * 10, y * (1 - s) * 10, w * (1 - s) * 10);
            else
                running = false;
        }
    }
}
