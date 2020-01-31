package org.firstinspires.ftc.teamcode.Autonomous.VisionOpModes;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.teamcode.Autonomous.CameraParams;

import java.util.List;

public class CVCamera {

    private static final String TFOD_MODEL_ASSET = "Skystone.tflite";
    private static final String LABEL_FIRST_ELEMENT = "Stone";
    private static final String LABEL_SECOND_ELEMENT = "Skystone";
    CameraParams cameraParams;

    private static final String VUFORIA_KEY =
            "AUAq88//////AAABmU+bO6dpUU4BreRJC5efYI1U4Fc5EvLiP5eGiT94wpCspMiACoccxAAVAgEOcCw87pTuHz671RvMDs3dtUBYrJNGI/x/bm60AsIdy3J7prt5EP8xeJuiKjWX32EoIhEsRnqZPpQOmCh11Q5vboZhsCNkNGMNWUIufrVa2g4SKwkSAjaAdOla8w/LwPKbiQBYvwbikpCb01LQg8iVYzWJHBfWLbQcXbuEBQIG9VSgGzyz4RStzgfG5mCTO4UZQbs7P3b/oJIf2rSzd7Ng1HmpHjldX8uFnLMuvIjgG/mJENP/edAw51wRui/21dV8QNdhV8KwP+KBdgpyVBMj44+OlN4ZrGGRkxYDNzd7yptjiGfe";
    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;
    private int tfodMonitorViewId;

    public CVCamera(int tfodMonitorViewId){
        this.tfodMonitorViewId = tfodMonitorViewId;
        initVuforia();
        initTfod();
        tfod.activate();
        cameraParams = new CameraParams(0, 0, 0, 1280, 720, 1080);
    }
    private void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;

        // Internal:
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.FRONT;

        // Webcam:
        //parameters.cameraName = hardwareMap.get(WebcamName.class, "Webcam 1");


        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the TensorFlow Object Detection engine.
    }

    /**
     * Initialize the TensorFlow Object Detection engine.
     */
    private void initTfod() {
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minimumConfidence = 0.8;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_FIRST_ELEMENT, LABEL_SECOND_ELEMENT);
    }

    public boolean skystoneAligned() {
        // getUpdatedRecognitions() will return null if no new information is available since
        // the last time that call was made.
        List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
        if (updatedRecognitions == null)
            return false;
        //telemetry.addData("# Object Detected", updatedRecognitions.size());

        // step through the list of recognitions and display boundary info.
        //int i = 0;
        for (Recognition recognition : updatedRecognitions) {
            String label = recognition.getLabel();
            if (!label.equals("Skystone"))
                continue;
            float left = recognition.getLeft(), top = recognition.getTop();
            float right = recognition.getRight(), bottom = recognition.getBottom();
            double blockPos = cameraParams.undoPerspective(left, top, right, bottom);
            double armOffset = 10;
            if (blockPos > armOffset)
                return true;
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
        return false;
    }
}
