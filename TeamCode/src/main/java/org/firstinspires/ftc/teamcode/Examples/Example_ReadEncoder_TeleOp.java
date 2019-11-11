package org.firstinspires.ftc.teamcode.Examples;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.SuperOp;

@TeleOp(name="Example_ReadEncoder ", group="Test")
public class Example_ReadEncoder_TeleOp extends SuperOp {

    @Override
    public void init() {
        super.init();
        setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    //these are used later in the method
    //they make sure that the method doesn't
    //run unless the proper button is pressed
    boolean rotate90 = false;
    boolean strafeRight = false;
    boolean moveForwards = false;

    //these allow the motor's
    //encoder values to be averaged
    //they represent the motor's encoder values
    int fl = 0;
    int fr = 0;
    int bl = 0;
    int br = 0;

    //averages of the motors go into these integers
    int tp_avg = 0;
    int tn_avg = 0;
    int p_avg = 0;
    int n_avg = 0;

    //this declares four modes the robot could be in
    public enum MODE {
        IDLE, TURNING, STRAFING, FORWARDS
    }

    @Override
    public void loop() {
        //display telemetry (encoder values) on driver station
        telemetry.addData("> Front Left: ", FrontLeftDrive.getCurrentPosition());
        telemetry.addData("> Front Right: ", FrontRightDrive.getCurrentPosition());
        telemetry.addData("> Back Left: ", BackLeftDrive.getCurrentPosition());
        telemetry.addData("> Back Right: ", BackRightDrive.getCurrentPosition());

        //method can be found in SuperOp
        c_drive();

        //reset encoders if necessary
        if (gamepad1.a) {
            resetMotors();
        }

        //allows the enum values or modes to be used
        //in the switch statement below
        MODE[] modes = MODE.values();
        for (MODE mode : modes) {
            switch (mode) {
                //if the dpad.left is pressed
                //than the robot calls the method
                //to move forward
                case STRAFING:
                    StrafeRight();
                    break;
                //if dpad.up is pressed
                //than the robot calls the method
                //to move forward
                case FORWARDS:
                    MoveForwards();
                    break;
                //if dpad.right is pressed
                //than the robot calls the method
                //to turn 90 degrees right
                case TURNING:
                    Rotate90();
                    break;
                //if no button is pressed
                //than the robot does nothing
                case IDLE:
                    break;
            }
        }
    }

    //method to reset motors (called above)
    public void resetMotors () {
        setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    //method to rotate 90 degrees right (called above in switch statement)
    //sets desired average for each motor
    //if dpad.right is pressed
    //the rotation variable, w, is
    //increased until the average is met
    //right side is negative
    //but the left side is negative
    //because we want the robot to go right
    //if we wanted it to go left, the values would be reversed
    public void Rotate90 () {
        if (rotate90) {
            fl = FrontLeftDrive.getCurrentPosition();
            fr = FrontRightDrive.getCurrentPosition();
            bl = BackLeftDrive.getCurrentPosition();
            br = BackRightDrive.getCurrentPosition();
            p_avg = (Math.abs(fl + bl)) / 2; //takes the absolute value of the left wheels and averages it
            n_avg = (Math.abs(fr + br)) / 2; //takes the absolute value of the right wheels and averages it


            if ((Math.abs(p_avg - tp_avg) < 10) && (Math.abs(n_avg - tn_avg) < 10)) {
                rotate90 = false;
                drive(0, 0, 0);
            }
        } else if (gamepad1.dpad_right) {
            //resetMotors();
            rotate90 = true;
            fl = FrontLeftDrive.getCurrentPosition() + 999;
            fr = FrontRightDrive.getCurrentPosition() - 999;
            bl = BackLeftDrive.getCurrentPosition() + 999;
            br = BackRightDrive.getCurrentPosition() - 999;
            tp_avg = (Math.abs(fl + bl)) / 2; //takes the absolute value of the left wheels and averages it
            tn_avg = (Math.abs(fr + br)) / 2; //takes the absolute value of the right wheels and averages it
            drive(0, 0, 0.8);
        }
    }

    //method to move robot forwards (called above in switch statement)
    //sets desired average for each motor
    //if dpad.up is pressed
    //the vertical movement variable, y, is
    //increased until the average is met
    //right wheels are positive
    //but the left wheels are negative because
    //it forces all the motors to go forwards
    public void MoveForwards () {
        if (moveForwards) {
            fl = FrontLeftDrive.getCurrentPosition();
            fr = FrontRightDrive.getCurrentPosition();
            bl = BackLeftDrive.getCurrentPosition();
            br = BackRightDrive.getCurrentPosition();
            n_avg = (Math.abs(fl + bl)) / 2; //takes the absolute value of the left wheels and averages it
            p_avg = (Math.abs(fr + br)) / 2; //takes the absolute value of the right wheels and averages it
            if (Math.abs(p_avg - tp_avg) < 10 && Math.abs(n_avg - tn_avg) < 10) {
                moveForwards = false;
                drive(0, 0, 0);
            }
        } else if (gamepad1.dpad_up) {
            //resetMotors();
            moveForwards = true;
            fl = FrontLeftDrive.getCurrentPosition() - 999;
            fr = FrontRightDrive.getCurrentPosition() + 999;
            bl = BackLeftDrive.getCurrentPosition() - 999;
            br = BackRightDrive.getCurrentPosition() + 999;
            tn_avg = (Math.abs(fl + bl)) / 2; //takes the absolute value of the right wheels and averages it
            tp_avg = (Math.abs(fr + br)) / 2; //takes the absolute value of the right wheels and averages it
            drive(0, 0.8, 0);
        }
    }

    //method to strafe right (called above in switch statement)
    //sets desired average for each motor
    //if dpad.left is pressed
    //the horizontal movement variable, x, is
    //increased until the average is met
    //back wheels are positive
    //but the front wheels are negative because
    //it forces the wheels to move inward
    //this is what allows the robot to strafe
    public void StrafeRight () {
        if (strafeRight) {
            fl = FrontLeftDrive.getCurrentPosition();
            fr = FrontRightDrive.getCurrentPosition();
            bl = BackLeftDrive.getCurrentPosition();
            br = BackRightDrive.getCurrentPosition();
            n_avg = ((Math.abs(fl + fr)) / 2);  //takes the absolute value of the left wheels and averages it
            p_avg = ((Math.abs(bl + br)) / 2);  //takes the absolute value of the right wheels and averages it

            if (Math.abs(p_avg - tp_avg) < 10 && Math.abs(n_avg - tn_avg) < 10) {
                strafeRight = false;
                drive(0, 0, 0);
            }

        } else if (gamepad1.dpad_left) {
            //resetMotors();
            strafeRight = true;
            fl = FrontLeftDrive.getCurrentPosition() - 6000;
            fr = FrontRightDrive.getCurrentPosition() - 6000;
            bl = BackLeftDrive.getCurrentPosition() + 6000;
            br = BackRightDrive.getCurrentPosition() + 6000;
            tn_avg = (Math.abs(fl + fr)) / 2; //takes the absolute value of the left wheels and averages it
            tp_avg = (Math.abs(bl + br)) / 2; //takes the absolute value of the right wheels and averages it
            drive(0.8, 0, 0);
        }
    }
}