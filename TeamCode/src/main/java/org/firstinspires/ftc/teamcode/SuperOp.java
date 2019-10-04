package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.AreshPourkavoos.Accel_Drive;
import org.firstinspires.ftc.teamcode.AreshPourkavoos.Example_Trap_Drive;

// extend OpMode so future classes will extend SuperOp Instead
// implements is for interfaces
public abstract class SuperOp extends OpMode implements SuperOp_Interface {

    DcMotor FrontLeftDrive = null;
    DcMotor FrontRightDrive = null;
    DcMotor BackLeftDrive = null;
    DcMotor BackRightDrive = null;

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
    }

    @Override
    public void drive(double x, double y, double w) {
        FrontLeftDrive.setPower(x+y+w);
        FrontRightDrive.setPower(x+y-w);
        BackLeftDrive.setPower(-x+y+w);
        BackRightDrive.setPower(x+y-w);
    }

    public void sleep_secs(double t){
        try {
            Thread.sleep((long)(t*1000));
        }
        catch(InterruptedException e){
        }
    }

    @Override
    public void t_drive(double x, double y, double w, double t) {
        // write method that drives for t amount of time
        /*
        final double TRANSITION_TIME = 0.1*t;       // accelerating, decelerating time amount
        final int STEPS = 5;    // separating the time to 5 gradations, changing the speed every gradation

        for (int i=0; i<STEPS; i++){

            double ratio = (double)i/STEPS;     // @param ratio: ratio of speed 1 that is changed into in each gradation cycle
            drive(x*ratio, y*ratio, w*ratio);
            sleep_secs(TRANSITION_TIME/STEPS);      // sleeping for one piece of the transition time each gradation cycle
        }

        drive(x, y, w);
        sleep_secs(t-2*TRANSITION_TIME);        // sleeping for the max speed time

        for (int j=STEPS-1; j>-1; j--){         //decelerating gradation cycles

            double e = (double)j/STEPS;
            drive(x*e, y*e, w*e);
            sleep_secs(TRANSITION_TIME/STEPS);
        }
         */
    }


}
