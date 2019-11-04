package org.firstinspires.ftc.teamcode.StaceyZhangCode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

public class Example_Trap_Drive_SZ{
    public double x, y, w, t;
    public boolean isRunning;
    public ElapsedTime elapsedTime = new ElapsedTime();

    public Example_Trap_Drive_SZ(DcMotor FrontLeftDrive, DcMotor FrontRightDrive, DcMotor BackLeftDrive, DcMotor BackRightDrive){

    }



    public void set(double x, double y, double w, double t) {
        this.x=x;
        this.y=y;
        this.w=w;
        this.t=t;
        isRunning = true;
        elapsedTime = new ElapsedTime();
        elapsedTime.reset();



        // initialize variables here. (Hint, they've already been declared)
    }

    public void update() {
        double timeRatio = elapsedTime.seconds()/t;
        if (timeRatio < 0.1){

        }
        else if (timeRatio < 0.9){

        }
        else if (timeRatio <1.0){

        }
    }
}
