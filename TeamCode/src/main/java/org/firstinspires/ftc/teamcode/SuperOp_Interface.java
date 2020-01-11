package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;

public interface SuperOp_Interface {
    //an interface can be used for declaring methods

    void drive(double x, double y, double w);

    void t_drive(double x, double y, double w, double t);


}
