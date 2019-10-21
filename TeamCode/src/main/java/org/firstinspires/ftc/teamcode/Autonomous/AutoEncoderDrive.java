package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.SuperOp;

@Autonomous(name="Pushbot: Auto Drive By Encoder", group="Pushbot")

public class AutoEncoderDrive extends SuperOp {
    private ElapsedTime runtime = new ElapsedTime();

    boolean running = true;



    @Override
    public void init() {
        telemetry.addLine("Initializing");
        super.init();
        telemetry.addLine("Finished stage one, setting encoders");
        //setEncoder(true);
        telemetry.addLine("Initialized");
        telemetry.update();
    }

    public void loop() {
        if (running) {
            running = !running;

            //basicEncoderDrive(0.7, 10, 10);
//            encoderDrive(10, 0, 0);
        }

    }

}
