package org.firstinspires.ftc.teamcode.Examples;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.SuperOp;

@TeleOp(name="Basic: Linear OpMode", group="Examples")
public class Example_Accel_Drive extends SuperOp {

    boolean last = false;

    @Override
    public void loop() {
        if (gamepad1.a != last) {
            //a_drive(0,1,0,1000);
        }

        accelDrive.update();
    }
}
