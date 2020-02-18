package org.firstinspires.ftc.teamcode.Autonomous.VisionOpModes;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.teamcode.Autonomous.CameraParams;
import org.firstinspires.ftc.teamcode.SuperOp.CamType;

import org.firstinspires.ftc.teamcode.Autonomous.VisionOpModes.CVTest.CamType;


import java.util.List;

public class CVCamera {

    CameraParams cameraParams;

    VuforiaLocalizer vuforia; // To retrieve images
    TFObjectDetector tfod; // To indentify objects
    public double blockPos;  // These variables are accessed by the opmode

    public float left, top, right, bottom;
    public float ww, hh;
    CamType type; // Internal vs webcam

    public CVCamera(CamType type){
        this.type = type;
    }

    void findSkystone() {

        List<Recognition> recognitions = tfod.getRecognitions();

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

        }

    }

    /**
     * Initialize the TensorFlow Object Detection engine.
     */

    void initTfod() {
    }

    public static class CameraParams {
        private double h; // Image height (unused for now)
        private double w; // Image width
        private double res; // How many pixels in a 45-degree angle
        private double blockWidth = 7.5; // Width of the stones (inches)

        // Given a rectangle, returns horizontal distance of block from center of view
        public double undoPerspective(float x_left, float y_top, float x_right, float y_bottom){
            // Only uses width to calculate distance from camera (depth),
            // although theoretically height might be preferable,
            // since blocks are next to e.o. and can be hard to distinguish
            double rectWidth = x_right-x_left;
            // Similar triangles: an object which is as wide as it is far from the camera
            // will occupy res pixels on the screen
            double blockDist = blockWidth*res/rectWidth;
            // Distance of the rectangle from the center of the screen in pixels
            double rectOffset = (x_left+x_right)/2 - w/2;
            // Similar triangles again: replace "width" with "offset from straight ahead"
            double blockOffset = rectOffset*blockDist/res;
            // pos used to be a double[] containing distance as well
            double pos = blockOffset;
            return pos;
        }

        // Constructor: accepts dimensions and resolution of camera
        public CameraParams(double h, double w, double res){
            this.h=h;
            this.w=w;
            this.res=res;
        }


    }
}