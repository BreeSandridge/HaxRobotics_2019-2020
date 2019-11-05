/*
 * Copyright (c) 2019 OpenFTC Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.firstinspires.ftc.teamcode.EasyOpenCVTest;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;
import org.openftc.easyopencv.OpenCvPipeline;
import org.openftc.easyopencv.OpenCvWebcam;

@TeleOp
public class TwoCamsExample extends LinearOpMode
{
    OpenCvCamera webcam, phoneCam;

    @Override
    public void runOpMode()
    {
        // Find phone screen
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        // Find webcam
        webcam = new OpenCvWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);


        // THIS WAS THE PROBLEM: ADDING THE CAMERAMONITORVIEWID
        //vvvvvvvvvvvvvvvvvvvvv

        //phoneCam = new OpenCvInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);
        phoneCam = new OpenCvInternalCamera(OpenCvInternalCamera.CameraDirection.BACK);

        // OR...  Do Not Activate the Camera Monitor View
        //webcam = new OpenCvWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"));

        // Open the connection to the camera device
        webcam.openCameraDevice();

        // Feed inputs through SamplePipeline
        webcam.setPipeline(new SamplePipeline());

        // Start streaming from webcam, give res+orientation
        webcam.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);

        waitForStart();

        while (opModeIsActive())
        {
            // camera HUD
            telemetry.addData("Frame Count", webcam.getFrameCount());
            telemetry.addData("FPS", String.format("%.2f", webcam.getFps()));
            telemetry.addData("Total frame time ms", webcam.getTotalFrameTimeMs());
            telemetry.addData("Pipeline time ms", webcam.getPipelineTimeMs());
            telemetry.addData("Overhead time ms", webcam.getOverheadTimeMs());
            telemetry.addData("Theoretical max FPS", webcam.getCurrentPipelineMaxFps());
            telemetry.update();

            // Additional functions
            //webcam.stopStreaming();
            //webcam.closeCameraDevice();
            //webcam.pauseViewport();
            //webcam.resumeViewport();
            sleep(100);
        }
    }

    class SamplePipeline extends OpenCvPipeline
    {
        boolean b = true;
        // Don't declare local Mat variables w/o release() bc of memory leakage
        @Override
        public Mat processFrame(Mat input)
        {
            // Draw a rectangle
            if (b)
            Imgproc.rectangle(
                    input,
                    new Point(
                            input.cols()/4,
                            input.rows()/4),
                    new Point(
                            input.cols()*(3f/4f),
                            input.rows()*(3f/4f)),
                    new Scalar(0, 255, 0), 4);

            return input;
        }
        public void onViewportTapped(){
            b = !b;
        }
    }
}
