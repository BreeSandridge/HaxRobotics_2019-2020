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

    boolean rotate90 = false;

    int fl = 0;
    int fr = 0;
    int bl = 0;
    int br = 0;

    int avg = 0;
    int t_avg = 0;

    @Override
    public void loop() {
        telemetry.addData("> Front Left: ", FrontLeftDrive.getCurrentPosition());
        telemetry.addData("> Front Right: ", FrontRightDrive.getCurrentPosition());
        telemetry.addData("> Back Left: ", BackLeftDrive.getCurrentPosition());
        telemetry.addData("> Back Right: ", BackRightDrive.getCurrentPosition());

        c_drive();

        if (gamepad1.a) {
            resetMotors();
        }

        if (rotate90) {
            fl = FrontLeftDrive.getCurrentPosition();
            fr = FrontRightDrive.getCurrentPosition();
            bl = BackLeftDrive.getCurrentPosition();
            br = BackRightDrive.getCurrentPosition();
            t_avg = (Math.abs(fl + bl) + Math.abs(fr + br)) / 4;

            if (Math.abs(avg - t_avg) < 10) {
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
            drive(0, 0, 0.8);
            avg = (Math.abs(fl + bl) + Math.abs(fr + br)) / 4;
        }
    }

    public void resetMotors(){
        setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
}
