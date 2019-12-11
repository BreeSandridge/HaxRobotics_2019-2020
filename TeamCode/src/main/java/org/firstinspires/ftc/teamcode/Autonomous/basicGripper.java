package org.firstinspires.ftc.teamcode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class basicGripper extends OpMode {
    Servo basicGripper = null;
    @Override
    public void init() {
        basicGripper = hardwareMap.get(Servo.class, "basicGripper");
    }
    public void loop() {
        if (gamepad1.b == true) {
            basicGripper.setPosition(-.3);
            telemetry.addData("Servo Position", basicGripper.getPosition());
        } else {
            basicGripper.setPosition (.6);
            telemetry.addData("Servo Position", basicGripper.getPosition());
        }

        telemetry.update();
    }
}

