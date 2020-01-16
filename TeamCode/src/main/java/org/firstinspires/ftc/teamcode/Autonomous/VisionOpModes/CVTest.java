package org.firstinspires.ftc.teamcode.Autonomous.VisionOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Autonomous.VisionOpModes.CVCamera;
import org.firstinspires.ftc.teamcode.SuperOp;

@Autonomous
public class CVTest extends SuperOp {

    private CVCamera cvCamera;
    enum CameraType {INTERNAL, WEBCAM};

    CVTest(){
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        cvCamera = new CVCamera(tfodMonitorViewId);
    }

    @Override
    public void loop(){
        telemetry.addData("aligned: ", "%b", cvCamera.skystoneAligned());
    }
}
