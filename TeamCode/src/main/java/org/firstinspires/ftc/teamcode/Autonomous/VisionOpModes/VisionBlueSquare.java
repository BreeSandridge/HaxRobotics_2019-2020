package org.firstinspires.ftc.teamcode.Autonomous.VisionOpModes;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Autonomous.PlayerSuperOp;

@Autonomous
public class VisionBlueSquare extends PlayerSuperOp {
    private int pos1 = 5;
    private int pos2 = 10;
    private int pos3 = 15;
    private double midpoint12 = (pos1 + pos2) / 2;
    private double midpoint23 = (pos2 + pos3) / 2;
    public boolean ran = false;
    ElapsedTime time = new ElapsedTime();
    PLAYERSTATUS status = PLAYERSTATUS.FLIPPER;
    private CVCamera cvCamera;
    private static final String VUFORIA_KEY =
            "AUAq88//////AAABmU+bO6dpUU4BreRJC5efYI1U4Fc5EvLiP5eGiT94wpCspMiACoccxAAVAgEOcCw87pTuHz671RvMDs3dtUBYrJNGI/x/bm60AsIdy3J7prt5EP8xeJuiKjWX32EoIhEsRnqZPpQOmCh11Q5vboZhsCNkNGMNWUIufrVa2g4SKwkSAjaAdOla8w/LwPKbiQBYvwbikpCb01LQg8iVYzWJHBfWLbQcXbuEBQIG9VSgGzyz4RStzgfG5mCTO4UZQbs7P3b/oJIf2rSzd7Ng1HmpHjldX8uFnLMuvIjgG/mJENP/edAw51wRui/21dV8QNdhV8KwP+KBdgpyVBMj44+OlN4ZrGGRkxYDNzd7yptjiGfe";

    @Override
    public void loop() {
        if (!ran) {
            time.reset();
            ran = !ran;
        }
        //useful variables to help with testing
        telemetry.addData("position: ", "%f", cvCamera.blockPos);
        telemetry.addData("Skystone", block);
        telemetry.addData("Status", status);
        switch (status) {
            case FLIPPER:
                //method to locate skystone
                cvCamera.findSkystone();
                if (time.seconds() > 1) {
                    toBlock();
                    status = PLAYERSTATUS.TOBLOCK;
                }
                break;
            case TOBLOCK:
                //once drive commands have been run and the queue is empty, align with skystone and grab it using the latch
                if (accelDrive.isEmpty) {
                    if (cvCamera.blockPos < midpoint12) {
                        //if skystone is the first stone, align in front of it
                        block = -1;
                        grab();
                        away();
                        status = PLAYERSTATUS.AWAY;
                    }
                    //if skystone is the second stone, grab skystone and move to build zone
                    else if (cvCamera.blockPos < midpoint23) {
                        moveForwards();
                        status = PLAYERSTATUS.AWAY2;
                    }
                    //if skystone is the third stone, align in front of it
                    else {
                        //grab skystone and move to build zone
                        block = 1;
                        moveForwards2();
                        status = PLAYERSTATUS.AWAY2;
                    }
                }
                //cycle through drive commands
                else {
                    updateAndDrive();
                }
                break;
            case AWAY2:
                //if queue is empty, grab skystone and move to build zone
                if (accelDrive.isEmpty) {
                    grab();
                    away();
                    status = PLAYERSTATUS.AWAY;
                } else {
                    updateAndDrive();
                }
                break;
            case AWAY:
                //release the skystone and park
                if (accelDrive.isEmpty) {
                    release();
                    park();
                    status = PLAYERSTATUS.PARK;
                } else {
                    updateAndDrive();
                }
            case PARK:
                //once parked, stop all motion
                if (accelDrive.isEmpty) {
                    stop1();
                    status = PLAYERSTATUS.STOP;
                } else {
                    updateAndDrive();
                }

        }
    }
}
