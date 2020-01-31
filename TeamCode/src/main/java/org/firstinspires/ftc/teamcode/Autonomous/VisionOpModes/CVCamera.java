package org.firstinspires.ftc.teamcode.Autonomous.VisionOpModes;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.teamcode.Autonomous.CameraParams;
import org.firstinspires.ftc.teamcode.Autonomous.VisionOpModes.CVTest.CamType;

import java.util.List;

public class CVCamera {

    private static final String TFOD_MODEL_ASSET = "Skystone.tflite";
    private static final String LABEL_FIRST_ELEMENT = "Stone";
    private static final String LABEL_SECOND_ELEMENT = "Skystone";
    CameraParams cameraParams;

    VuforiaLocalizer vuforia;
    TFObjectDetector tfod;
    public int tfodMonitorViewId;
    public double blockPos;
    public float left, top, right, bottom;
    public float ww, hh;
    CamType type;

    CVCamera(CamType type){
        this.type = type;
    }

    //boolean skystoneAligned() {
    void findSkystone() {
        // getUpdatedRecognitions() will return null if no new information is available since
        // the last time that call was made.
        List<Recognition> recognitions = tfod.getRecognitions();
        //if (recognitions == null)
        //    return false;
        //telemetry.addData("# Object Detected", updatedRecognitions.size());

        // step through the list of recognitions and display boundary info.
        //int i = 0;
        for (Recognition recognition : recognitions) {
            String label = recognition.getLabel();
            if (!label.equals("Skystone"))
                continue;
            left = recognition.getLeft();
            top = recognition.getTop();
            right = recognition.getRight();
            bottom = recognition.getBottom();
            ww = recognition.getImageWidth();
            hh = recognition.getImageHeight();
            blockPos = cameraParams.undoPerspective(left, top, right, bottom);
            //double armOffset = 10;
            //if (blockPos > armOffset)
            //    return true;
            /*
            telemetry.addData(String.format("label (%d)", i), label);
            telemetry.addData(String.format("  left,top (%d)", i), "%.03f , %.03f",
                    left, top);
            telemetry.addData(String.format("  right,bottom (%d)", i), "%.03f , %.03f",
                    right, bottom);
            telemetry.addData(String.format("  position (%d)", i), "%.03f",
                    (float) blockPos);
             */
        }
        //return false;
    }

    /**
     * Initialize the TensorFlow Object Detection engine.
     */
    void initTfod() {
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minimumConfidence = 0.8;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_FIRST_ELEMENT, LABEL_SECOND_ELEMENT);
        //tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_SECOND_ELEMENT);
    }
}