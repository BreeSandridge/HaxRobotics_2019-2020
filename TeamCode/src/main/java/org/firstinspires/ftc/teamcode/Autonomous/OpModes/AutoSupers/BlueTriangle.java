package org.firstinspires.ftc.teamcode.Autonomous.OpModes.AutoSupers;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Autonomous.BuildSuperOp;
import org.firstinspires.ftc.teamcode.SuperOp;

public abstract class BlueTriangle extends BuildSuperOp {

    //This uses an enum declared in SuperOp
    //It declares the first STATUS as "START"
    public BUILDSTATUS status = BUILDSTATUS.FLIPPER;
    //create new stopwatch
    public boolean foundationState = true;

    @Override
    public void init() {
        super.init();
        startPoint = 1;
    }

    @Override
    public void loop() {
        //startPoint = 1;
        //parkPos = -1;
        ElapsedTime time = new ElapsedTime();
        //declare telemetry for all motors/servos
        //this allows us to see how the motors are behaving in the code
        //and then compare it to how they perform in real life
        telemetry.addData("Status: ", status);
        telemetry.addData("Foundation", Foundation.getPosition());


        //switch statements for changing the status of the robot
        //this allows us to use different code for each status
        //there are methods created below the switch statement for easier reading
        foundationState = false;
        switch (status) {
            case FLIPPER:
                toFoundation();
                status = SuperOp.BUILDSTATUS.TOFOUNDATION;
                break;
            case TOFOUNDATION:
                if (accelDrive.isEmpty) {
                    time.reset();
                    status = SuperOp.BUILDSTATUS.LOWER;
                } else {
                    Foundation.setPosition(1);
                    updateAndDrive();
                }
                break;
            case LOWER:
                if (time.seconds() < 1.5) {
                } else {
                    drag();
                    status = SuperOp.BUILDSTATUS.DRAG;
                }
                break;
            case DRAG:
                if (accelDrive.isEmpty) {
                    foundationState = true;
                    around();
                    status = SuperOp.BUILDSTATUS.AROUND;
                } else {
                    updateAndDrive();
                }
                break;
            case AROUND:
                if (accelDrive.isEmpty) {
                    park();
                    status = SuperOp.BUILDSTATUS.PARK;
                } else {
                    updateAndDrive();
                }
                break;
            case PARK:
                if (accelDrive.isEmpty) {
                    status = SuperOp.BUILDSTATUS.STOP;
                } else {
                    updateAndDrive();
                }
                break;
            case STOP:
                stop1();
                break;
        }
    }
}
