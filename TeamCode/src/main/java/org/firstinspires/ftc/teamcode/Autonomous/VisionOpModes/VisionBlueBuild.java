/*package org.firstinspires.ftc.teamcode.Autonomous.VisionOpModes;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.SuperOp;

import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous
public class VisionBlueBuild extends SuperOp {

    //This uses an enum declared in SuperOp
    //It declares the first STATUS as "START"
    private STATUS status = STATUS.FlIPPER;

    //create new stopwatch
    private ElapsedTime time = new ElapsedTime();
    private double targetTime;
    private ElapsedTime arm = new ElapsedTime();
    private int currPosition;
    private int targetPosition;
    private boolean ran = false;
    private boolean ran1 = true;
    private CVCamera cvCamera = new CVCamera(tfodMonitorViewId);

    @Override
    public void loop() {
        startPoint = 1;
        //declare telemetry for all motors/servos
        //this allows us to see how the motors are behaving in the code
        //and then compare it to how they perform in real life
        telemetry.addData("Arm", arm.seconds());
        telemetry.addData("Power",LatchMotor.getPower());
        telemetry.addData("LatchMotor Position: ", LatchMotor.getCurrentPosition());
        telemetry.addData("Time: ", time.seconds());
        telemetry.addData("Front Right: ", FrontRightDrive.getCurrentPosition());
        telemetry.addData("Back Left: ", BackLeftDrive.getCurrentPosition());
        telemetry.addData("Back Right: ", BackRightDrive.getCurrentPosition());
        telemetry.addData("Front Left: ", FrontLeftDrive.getCurrentPosition());
        telemetry.addData("Status: ", status);
        telemetry.addData("Latch Position: ", Latch.getPosition());

        currPosition = LatchMotor.getCurrentPosition();
        //switch statements for changing the status of the robot
        //this allows us to use different code for each status
        //there are methods created below the switch statement for easier reading
        switch (status) {
            case FlIPPER:
                if(ran1){
                    time.reset();
                    ran1 = !ran1;
                }
                targetTime = 2;
                //Flipper.setPosition(0);
                if(time.seconds() >= targetTime){
//                    Flipper.setPower(0);
                    status = STATUS.START;
                }
                break;
            case START:
                if(ran == false){
                    time.reset();
                    ran = !ran;
                }
                targetTime = 0.5;
                drive(0.5,0,0);
                if(time.seconds() >= targetTime){
                    drive(0,0,0);
                    time.reset();
                    status = STATUS.TOBLOCK;
                    ran = false;
                }
                break;
            case TOBLOCK:
                //move forward for 3 seconds
                leftSpeedMultiplier = 1;
                targetTime = 4;
                drive(0, 0.5, 0);

                // vision code
                // if skystone is sighted
                /*


                // set movement values to go towards block
                if(time.seconds()-targetTime > 0 || cvCamera.findSkystone()) {
                    //stop
                    drive(0,0,0);
                    //sleep_secs(0.5);
                    //switch STATUS
                    time.reset();
                    status = STATUS.APPROACH;
                }
                break;
            case APPROACH:
                //approach();
                break;
            case GETBLOCK:
//                getBlock();
                //status = STATUS.AWAY;
                break;
            case AWAY:
                //time.reset();
                //away();
                break;
            case TOBUILD:
//                toBuild();
                break;
            case RELEASEBLOCK:
//                release();
                break;
            case PARK:
                //time.reset();
//                park();
                break;
            case STOP:
//                stop1();
                break;
        }
    }

} */
