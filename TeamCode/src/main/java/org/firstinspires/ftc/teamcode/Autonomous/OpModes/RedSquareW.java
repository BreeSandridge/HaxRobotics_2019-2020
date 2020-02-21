package org.firstinspires.ftc.teamcode.Autonomous.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Autonomous.PlayerSuperOp;

@Autonomous
public class RedSquareW extends RedSquare {
    @Override
    public void init() {
        super.init();
        parkWall = false;
        parkPos = -1;
    }
}

