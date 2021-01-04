package org.firstinspires.ftc.teamcode.Autonomous.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Autonomous.PlayerSuperOp;

@Autonomous
public class RedSquareW extends PlayerSuperOp {

    //This uses an enum declared in SuperOp
    //It declares the first STATUS as "START"
    private PLAYERSTATUS status = PLAYERSTATUS.FLIPPER;
    //create new stopwatch


    @Override
    public void init() {
        super.init();
        parkWall = false;
        parkPos = -1;
    }
}

