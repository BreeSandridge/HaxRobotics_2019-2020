package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.SuperOp;

@TeleOp(name="BasicAuto", group="Test")
public class BasicAuto extends SuperOp {

    public void rightTurn () {
        super.t_drive(0, 0,  .6, 0.195);
    }

    public void leftTurn () {
        //super.drive(0,0,-w);
        super.t_drive(0, 0,  -.6, 0.200);
    }

    boolean running = true;
    @Override
    public void loop() {

        if (running) {
            //leftTurn();
            //rightTurn();
            super.basicEncoderDrive(0, 10);
            running = !running;
        }
    }
}