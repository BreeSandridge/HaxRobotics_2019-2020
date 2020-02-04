package org.firstinspires.ftc.teamcode.Autonomous.VisionOpModes;
import org.firstinspires.ftc.teamcode.Autonomous.VisionOpModes.CVCamera;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Autonomous.PlayerSuperOp;
@Autonomous
public class VisionBlueSquare extends PlayerSuperOp {
    private PLAYERSTATUS status = PLAYERSTATUS.FLIPPER;
    @Override
    public void loop() {
        if(cvCamera.skystoneAligned()){
            switch (status) {
                case FLIPPER:
                    toBlock();
                    status = PLAYERSTATUS.TOBLOCK;
                    break;
                case TOBLOCK:
                    if(accelDrive.isEmpty){
                        grab();
                        away();
                        status = PLAYERSTATUS.AWAY;
                    } else {
                        updateAndDrive();
                    }
                    break;
                case AWAY:
                    if(accelDrive.isEmpty){
                        release();
                        park();
                        status = PLAYERSTATUS.PARK;
                    } else {
                        updateAndDrive();
                    }
                    break;
                case PARK:
                    if(accelDrive.isEmpty){
                        status = PLAYERSTATUS.STOP;
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
}