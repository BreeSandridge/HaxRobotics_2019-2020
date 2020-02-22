package org.firstinspires.ftc.teamcode.Autonomous.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Autonomous.BuildSuperOp;

@Autonomous
public class BlueTriangleY extends BlueTriangle {

    @Override
    public void init() {
        super.init();
        parkPos = 1;
    }
}
