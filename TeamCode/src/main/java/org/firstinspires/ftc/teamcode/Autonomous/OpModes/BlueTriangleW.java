package org.firstinspires.ftc.teamcode.Autonomous.OpModes;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.teamcode.Autonomous.BuildSuperOp;

@Autonomous
public class BlueTriangleW extends BuildSuperOp {

    //This uses an enum declared in SuperOp
    //It declares the first STATUS as "START"
    public BUILDSTATUS status = BUILDSTATUS.FLIPPER;
    //create new stopwatch
    public boolean foundationState = true;
    @Override
    public void loop() {
        startPoint = 1;
        parkPos = -1;
        //declare telemetry for all motors/servos
        //this allows us to see how the motors are behaving in the code
        //and then compare it to how they perform in real life
        telemetry.addData("Status: ", status);
        telemetry.addData("Foundation",Foundation.getPosition());


        //switch statements for changing the status of the robot
        //this allows us to use different code for each status
        //there are methods created below the switch statement for easier reading
        Foundation.setPosition(0);
        switch (status) {
            case FLIPPER:
                toFoundation();
                status = BUILDSTATUS.TOFOUNDATION;
                break;
            case TOFOUNDATION:
                if(accelDrive.isEmpty) {
                    time.reset();
                    while(time.seconds() < 1.5){
                        Foundation.setPosition(1);
                    }
                    drag();
                    status = BUILDSTATUS.DRAG;
                } else {
                    updateAndDrive();
                }
                break;
            case DRAG:
                if(accelDrive.isEmpty) {
                    Foundation.setPosition(1);
                    around();
                    status = BUILDSTATUS.AROUND;
                } else {
                    updateAndDrive();
                }
                break;
            case AROUND:
                if(accelDrive.isEmpty) {
                    park();
                    status = BUILDSTATUS.PARK;
                } else {
                    updateAndDrive();
                }
                break;
            case PARK:
                if(accelDrive.isEmpty) {
                    status = BUILDSTATUS.STOP;
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


