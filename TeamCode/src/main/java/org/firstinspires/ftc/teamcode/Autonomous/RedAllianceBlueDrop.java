/*package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.SuperOp;

@Autonomous
public class RedAllianceBlueDrop extends SuperOp {
    // status stuff
    private STATUS status = STATUS.TOBLOCK;

    @Override
    public void loop() {
        switch (status) {
            case TOBLOCK:
                toBlock();
                break;
            case APPROACH:
                approach();
                break;
            case GETBLOCK:
                getBlock();
                break;
            case AWAY:
                away();
                break;
            case TOBUILD:
                toBuild();
                break;
            case PARK:
                park();
                break;
            case STOP:
                stop1();
                break;
        }
    }

    private void toBlock() {
        drive(0,-0.7,0);
        sleep_secs(0.5);
        status = STATUS.APPROACH;
    }

    private void approach() {
        drive(0.7,0,0);
        sleep_secs(0.5);
        status = STATUS.GETBLOCK;
    }

    private void getBlock() {
        drive(0, 0, 0);
        sleep_secs(1);
        status = STATUS.AWAY;
    }

    private void away() {
        drive(-0.7,0,0);
        sleep_secs(0.2);
        status = STATUS.TOBUILD;
    }

    private void toBuild() {
        drive(0,0.7,0);
        sleep_secs(2);
        status = STATUS.PARK;
    }

    private void park() {
        drive(0,-0.7,0);
        sleep_secs(1);
        status = STATUS.STOP;
    }
    private void stop1() {
        drive(0,0,0);
    }
} */








/* Theres so much oil in the dish, the United States wanted to invade the f**king plate
                       --Gordon Ramsay
 */