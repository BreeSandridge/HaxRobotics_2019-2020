package org.firstinspires.ftc.teamcode.Autonomous.VisionOpModes;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;


import org.firstinspires.ftc.teamcode.Autonomous.PlayerSuperOp;
import org.firstinspires.ftc.teamcode.SuperOp;

@Autonomous(name="Computer Vision Test")
public class CVTest extends PlayerSuperOp {

    // Which field elements to load, need to include visual targets later

    public void init(){
        super.init();
    }


    @Override
    public void loop(){
        cvCamera.findSkystone(); // Stores values like position as variables within cvCamera
        addStats();
        telemetry.update();
    }

    void addStats(){
        telemetry.addData("position: ", "%f", cvCamera.blockPos);
        telemetry.addData("left: ", "%f", cvCamera.left);
        telemetry.addData("top: ", "%f", cvCamera.top);
        telemetry.addData("right: ", "%f", cvCamera.right);
        telemetry.addData("bottom: ", "%f", cvCamera.bottom);
        telemetry.addData("width: ", "%f", cvCamera.ww);
        telemetry.addData("height: ", "%f", cvCamera.hh);
    }



    /*public void initCamera(CVCamera camera, CamType type){ // Also needs to be moved

        // Initialize Vuforia
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();
        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        switch (type) {
            case INTERNAL:
                parameters.cameraDirection = VuforiaLocalizer.CameraDirection.FRONT;
                // Dims+res of image vary by camera,
                camera.cameraParams = new CVCamera.CameraParams(1280, 720, 1080);
                break;
            case WEBCAM:
                parameters.cameraName = hardwareMap.get(WebcamName.class, "Webcam 1");
                camera.cameraParams = new CVCamera.CameraParams(448, 800, 1080);
                break;
        }
        camera.vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Initialize object detector
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minimumConfidence = 0.8; // May need to experiment with this value
        camera.tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, camera.vuforia);
        camera.tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_FIRST_ELEMENT, LABEL_SECOND_ELEMENT);
        camera.tfod.activate();
    }*/

}
