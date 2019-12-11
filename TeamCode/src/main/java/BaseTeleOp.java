import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.SuperOp;

@TeleOp
public class BaseTeleOp extends SuperOp {
    double speed = .8;

    @Override
    public void loop() {

        double w = gamepad1.right_stick_x * speed * 0.85; //rotations
        double y = gamepad1.left_stick_y * speed * 0.85; //forward/backward
        double x = gamepad1.left_stick_x * speed * 0.85; //strafing

        double i = gamepad2.right_stick_x * speed; //linear slide motor
        double u = gamepad2.left_stick_x * speed; //four bar linkage motor

        //driver uses all four drive motors
        BackLeftDrive.setPower(y - x - w);
        FrontLeftDrive.setPower(x + y - w);
        BackRightDrive.setPower(x + y + w);
        FrontRightDrive.setPower(y - x + w);

        //operator uses linear slide, four bar linkage, and all buttons
        /*
        LinearSlide.setPower(i);
        FourBarLinkage.setPower(u);

        if (gamepad2.a) { //3D printed part
            MiddleDrive.setPower(0.8);
        } else {
            MiddleDrive.setPower(0);
        }
        if (gamepad2.b) { //gripper on four bar linkage
            topGripper.setPosition(0.8);
        } else {
            topGripper.setPosition(0);
        }
        if (gamepad2.y) { //gripper on linear slide
            bottomGripper.setPosition(0.8);
        } else {
            bottomGripper.setPosition(0);
        }
        if (gamepad2.x) { //trailer hitch
            foundationMover.setPosition(0.8);
        } else {
            foundationMover.setPosition(0);
        }
        */

    }
}
