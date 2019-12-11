package org.firstinspires.ftc.teamcode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.teamcode.SuperOp;

@Autonomous
public class BluePlayer extends SuperOp {
    @Override
    public void init() {
        super.init();
    }

    // this switch statement uses an enum declared in the SuperOp called STATUS
    // the 4 possible STATUSES are TOBLOCK, GETBLOCK, BUILD, and PARK
    // each case calls a method-the methods are declared below the switch statement
    public void loop() {
        for (RedPlayer.STATUS status : statuses) {
            switch (status) {
                case TOBLOCK: // used find a block and drive to it
                    TOBLOCK();
                    break;
                /*case GETBLOCK: // deploy arm and grab block
                    GETBLOCK();
                    break;*/
                case BUILD: // bring block back to build zone
                    BUILD();
                    break;
                case PARK: // park over middle line of field
                    PARK();
                    break;
            }
        }
    }

    // called above
    public void TOBLOCK() {
        //sense block and drive to it
        // requires computer vision - values arbitrary
        /*if (block == true) { // if the camera sees a block, it strafes right, towards it
            t_drive(-1, .5, 1, 1);
        } else { // otherwise, the robot doesn't move
            t_drive(0,0,0,0);
        }*/
        if (TOBLOCK) {

            fl = FrontLeftDrive.getCurrentPosition();
            fr = FrontRightDrive.getCurrentPosition();
            bl = BackLeftDrive.getCurrentPosition();
            br = BackRightDrive.getCurrentPosition();

            p_avg = (Math.abs(fl + bl)) / 2; //takes the absolute value of the left wheels and averages it
            n_avg = (Math.abs(fr + br)) / 2; //takes the absolute value of the right wheels and averages it

            if ((Math.abs(p_avg - tp_avg) < 10) && (Math.abs(n_avg - tn_avg) < 10)) {
                TOBLOCK = false;
                drive(0, 0, 0);
            }

        } else if (gamepad1.dpad_right) {

            //resetMotors();
            TOBLOCK = true;

            //these are experimental values
            fl = FrontLeftDrive.getCurrentPosition() + 999;
            fr = FrontRightDrive.getCurrentPosition() - 999;
            bl = BackLeftDrive.getCurrentPosition() + 999;
            br = BackRightDrive.getCurrentPosition() - 999;

            tp_avg = (Math.abs(fl + bl)) / 2; //takes the absolute value of the left wheels and averages it
            tn_avg = (Math.abs(fr + br)) / 2; //takes the absolute value of the right wheels and averages it
            drive(0, 0, 0.8);
        }
    }


    // called above
/*public void GETBLOCK() {
        // deploy arm servos
        // values arbitrary
        if (rampUP == true) { // this tells the servo to run to the maximum position, then stop
            position += INCREMENT;
            if (position >= MAX_POS) {
                position = MAX_POS;
                rampUP = !rampUP;
            }
        } else { // this tells the servo to run to the minimum position, then stop
            position -= INCREMENT;
            if (position <= MIN_POS) {
                rampUP = !rampUP;
            }
        } */

    // called above
    public void BUILD() {
      // bring block back to the build zone
        // values arbitrary
        /*if (hasBlock == true) { // if the robot has the block, it will strafe left and backwards, into build zone
            t_drive(1, -1, 0, 1);
        } else { // otherwise, the robot doesn't move
            t_drive(0, 0, 0, 0);
        } */

    }

    // called above
    public void PARK() {
        // park over field mid-line
        // values arbitrary
        t_drive (0, 1, 0, 1); // drive forwards, then stop
    }
}