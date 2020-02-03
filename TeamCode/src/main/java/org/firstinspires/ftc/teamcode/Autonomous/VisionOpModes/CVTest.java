package org.firstinspires.ftc.teamcode.Autonomous.VisionOpModes;

import android.webkit.WebSettings;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.teamcode.Autonomous.CameraParams;

@Autonomous(name="Computer Vision Test")
public class CVTest extends OpMode {

    private CVCamera cvCamera;
    private static final String VUFORIA_KEY =
            "AUAq88//////AAABmU+bO6dpUU4BreRJC5efYI1U4Fc5EvLiP5eGiT94wpCspMiACoccxAAVAgEOcCw87pTuHz671RvMDs3dtUBYrJNGI/x/bm60AsIdy3J7prt5EP8xeJuiKjWX32EoIhEsRnqZPpQOmCh11Q5vboZhsCNkNGMNWUIufrVa2g4SKwkSAjaAdOla8w/LwPKbiQBYvwbikpCb01LQg8iVYzWJHBfWLbQcXbuEBQIG9VSgGzyz4RStzgfG5mCTO4UZQbs7P3b/oJIf2rSzd7Ng1HmpHjldX8uFnLMuvIjgG/mJENP/edAw51wRui/21dV8QNdhV8KwP+KBdgpyVBMj44+OlN4ZrGGRkxYDNzd7yptjiGfe";
    enum CamType{INTERNAL, WEBCAM}

    public void init(){
        CamType type = CamType.WEBCAM;
        cvCamera = new CVCamera(type);
        initCamera(cvCamera, type);
    }

    @Override
    public void loop(){
        cvCamera.findSkystone();
        addStats();
        telemetry.update();
    }

    void addStats(){
        //telemetry.addData("aligned: ", "%b", cvCamera.skystoneAligned());
        telemetry.addData("position: ", "%f", cvCamera.blockPos);
        telemetry.addData("left: ", "%f", cvCamera.left);
        telemetry.addData("top: ", "%f", cvCamera.top);
        telemetry.addData("right: ", "%f", cvCamera.right);
        telemetry.addData("bottom: ", "%f", cvCamera.bottom);
        telemetry.addData("width: ", "%f", cvCamera.ww);
        telemetry.addData("height: ", "%f", cvCamera.hh);
    }

    public void initCamera(CVCamera camera, CamType type){
        camera.tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();
        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        switch (type) {
            case INTERNAL:
                parameters.cameraDirection = VuforiaLocalizer.CameraDirection.FRONT;
                camera.cameraParams = new CameraParams(1280, 720, 1080);
                break;
            case WEBCAM:
                parameters.cameraName = hardwareMap.get(WebcamName.class, "Webcam 1");
                camera.cameraParams = new CameraParams(448, 800, 1080);
                break;
        }
        camera.vuforia = ClassFactory.getInstance().createVuforia(parameters);
        camera.initTfod();
        camera.tfod.activate();
    }

}
