package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.teamcode.SuperOp;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp
public class TeleOp extends SuperOp {
    @Override
    public void loop() {
        c_drive();

        // intake system
        if (gamepad1.right_trigger > 0.1 && gamepad1.left_trigger < 0.1) {
            intake(gamepad1.right_trigger);
        }
        // if only driver left trigger is pressed down
        else if (gamepad1.left_trigger > 0.1 && gamepad1.right_trigger < 0.1) {
            intake(-gamepad1.left_trigger);
        }
        // if only operator right trigger is pressed down
        else if (gamepad2.right_trigger > 0.1 && gamepad2.left_trigger < 0.1) {
            intake(gamepad2.right_trigger);
        }
        // if only operator left trigger is pressed down
        else if (gamepad2.left_trigger > 0.1 && gamepad2.right_trigger < 0.1) {
            intake(-gamepad2.left_trigger);
        }
        // else turn off intake
        else {
            intake(0);
        }

        // latch motor
        if (gamepad2.right_stick_y > 0.1 || gamepad2.right_stick_y < -0.1) {
            LatchMotor.setPower(-gamepad2.right_stick_y * .25);
        } else {
            LatchMotor.setPower(0);
        }
    }

    private void intake(float power){
        RightStoneRamp.setPower(-power);
        LeftStoneRamp.setPower(power);
    }
}
