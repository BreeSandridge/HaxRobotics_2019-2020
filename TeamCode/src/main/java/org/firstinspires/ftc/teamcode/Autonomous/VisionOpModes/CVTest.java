package org.firstinspires.ftc.teamcode.Autonomous.VisionOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

@Autonomous(name="Computer Vision Test")
public class CVTest extends OpMode {

    int tfodMonitorViewId;
    private CVInternal cvCamera;

    public void init(){
        tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        cvCamera = new CVInternal(tfodMonitorViewId);

    }

    @Override
    public void loop(){
        telemetry.addData("aligned: ", "%b", cvCamera.skystoneAligned());
        telemetry.addData("position: ", "%f", cvCamera.blockPos);
        telemetry.addData("left: ", "%f", cvCamera.left);
        telemetry.addData("top: ", "%f", cvCamera.top);
        telemetry.addData("right: ", "%f", cvCamera.right);
        telemetry.addData("bottom: ", "%f", cvCamera.bottom);
        telemetry.addData("width: ", "%f", cvCamera.ww);
        telemetry.addData("height: ", "%f", cvCamera.hh);
        telemetry.update();
    }
}
