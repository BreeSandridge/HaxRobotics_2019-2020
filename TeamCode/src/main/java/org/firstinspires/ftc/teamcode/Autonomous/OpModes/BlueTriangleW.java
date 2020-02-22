package org.firstinspires.ftc.teamcode.Autonomous.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous
public class BlueTriangleW extends BlueTriangle {

    @Override
    public void init() {
        super.init();
        parkPos = -1;
    }
}
