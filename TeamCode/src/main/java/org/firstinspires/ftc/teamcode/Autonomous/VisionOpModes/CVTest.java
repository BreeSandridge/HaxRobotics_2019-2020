package org.firstinspires.ftc.teamcode.Autonomous.VisionOpModes;

import android.webkit.WebSettings;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.teamcode.Autonomous.CameraParams;
import org.firstinspires.ftc.teamcode.Autonomous.PlayerSuperOp;
import org.firstinspires.ftc.teamcode.SuperOp;

@Autonomous(name="Computer Vision Test")
public class CVTest extends PlayerSuperOp {


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
}