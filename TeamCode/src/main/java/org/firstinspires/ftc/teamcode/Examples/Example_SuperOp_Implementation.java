package org.firstinspires.ftc.teamcode.Examples;

import org.firstinspires.ftc.teamcode.SuperOp;

public class Example_SuperOp_Implementation extends SuperOp {

    @Override
    public void init() {
        super.init();
        // initialize variables here. (Hint, they've already been declared)
    }

    @Override
    public void loop() {
        // write code to execute here
        super.accelDrive.update();

    }
}
