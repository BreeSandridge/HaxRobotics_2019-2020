package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

public class Autonomous_Structure {
    public static void main(String[] args) {

        //double leftFrontPower = Range.clip();
        //double rightBackPower = Range.clip();
        //double rightFrontPower = Range.clip();
        //double leftBackPower = Range.clip ()

    }
    public static void rightTurn (double w) {
        DcMotor BackLeftDrive = null;
        DcMotor FrontLeftDrive = null;
        DcMotor BackRightDrive = null;
        DcMotor FrontRightDrive = null;

        BackLeftDrive.setPower(-w);
        FrontLeftDrive.setPower(-w);
        FrontRightDrive.setPower(w);
        BackRightDrive.setPower(w);
    }

    public static void leftTurn (double w) {
        DcMotor BackLeftDrive = null;
        DcMotor FrontLeftDrive = null;
        DcMotor BackRightDrive = null;
        DcMotor FrontRightDrive = null;

        BackLeftDrive.setPower(-w);
        FrontLeftDrive.setPower(-w);
        FrontRightDrive.setPower(w);
        BackRightDrive.setPower(w);

    }

       /* switch (turn) {

            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
        }
    */
}

