package org.firstinspires.ftc.teamcode.Autonomous.VisionOpModes;

import android.webkit.WebSettings;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Autonomous.PlayerSuperOp;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.teamcode.Autonomous.CameraParams;

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

    public void init() {
        CamType type = CamType.WEBCAM;
        cvCamera = new CVCamera(type);
        initCamera(cvCamera, type);
    }

    @Override
    public void loop() {
        if (!ran) {
            time.reset();
            ran = !ran;
        }
        telemetry.addData("position: ", "%f", cvCamera.blockPos);
        telemetry.addData("left: ", "%f", cvCamera.left);
        telemetry.addData("top: ", "%f", cvCamera.top);
        telemetry.addData("right: ", "%f", cvCamera.right);
        telemetry.addData("bottom: ", "%f", cvCamera.bottom);
        telemetry.addData("width: ", "%f", cvCamera.ww);
        telemetry.addData("height: ", "%f", cvCamera.hh);
        switch (status) {
            case FLIPPER:
                cvCamera.findSkystone();
                if (time.seconds() > 1) {
                    toBlock();
                    status = PLAYERSTATUS.TOBLOCK;
                }
                break;
            case TOBLOCK:
                if (accelDrive.isEmpty) {
                    if (cvCamera.blockPos < midpoint12) {
                        block = -1;
                        moveBackwards();
                        status = PLAYERSTATUS.AWAY2;
                    } else if (cvCamera.blockPos < midpoint23) {
                        grab();
                        away();
                        status = PLAYERSTATUS.AWAY;
                    } else if (cvCamera.blockPos < pos3 + (pos3 - midpoint23)) {
                        block = 1;
                        moveForwards();
                        status = PLAYERSTATUS.AWAY2;
                    } else {
                        telemetry.addLine("Block Position not in range, defaulting to middle block");
                        grab();
                        away();
                        status = PLAYERSTATUS.AWAY;
                    }
                } else {
                    updateAndDrive();
                }
                break;
            case AWAY2:
                if (accelDrive.isEmpty) {
                    grab();
                    away();
                    status = PLAYERSTATUS.AWAY;
                } else {
                    updateAndDrive();
                }
                break;
            case AWAY:
                if (accelDrive.isEmpty) {
                    release();
                    park();
                    status = PLAYERSTATUS.PARK;
                } else {
                    updateAndDrive();
                }
            case PARK:
                if (accelDrive.isEmpty) {
                    stop1();
                    status = PLAYERSTATUS.STOP;
                } else {
                    updateAndDrive();
                }

        }
    }
}
