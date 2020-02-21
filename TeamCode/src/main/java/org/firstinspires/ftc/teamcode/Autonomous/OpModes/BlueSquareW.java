package org.firstinspires.ftc.teamcode.Autonomous.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Autonomous.PlayerSuperOp;

@Autonomous
public class BlueSquareW extends BlueSquare {

    @Override
    public void init() {
        super.init();
        parkPos = -1;
    }
}