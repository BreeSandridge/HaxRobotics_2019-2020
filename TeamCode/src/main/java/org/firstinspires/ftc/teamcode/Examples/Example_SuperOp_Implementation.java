package org.firstinspires.ftc.teamcode.Examples;

import org.firstinspires.ftc.teamcode.AreshPourkavoos.Accel_Drive;
import org.firstinspires.ftc.teamcode.SuperOp;

public class Example_SuperOp_Implementation extends SuperOp {


    Accel_Drive accelDrive;
    @Override
    public void init() {
        super.init();
        // initialize variables here. (Hint, they've already been declared)
        accelDrive = new Accel_Drive();
    }

    @Override
    public void loop() {
        // write code to execute here
        accelDrive.update();
    }
}
