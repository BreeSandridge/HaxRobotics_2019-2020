package org.firstinspires.ftc.teamcode.Autonomous;

import org.firstinspires.ftc.teamcode.SuperOp;

public class RedBuild extends SuperOp {
    // status booleans
    enum STATUS {TOBLOCK, GETBLOCK, TOBUILD}
    STATUS status = STATUS.TOBLOCK;
    boolean running = true;

    @Override
    public void loop() {
        // if it doesnt have the stone, drive it forward
        switch (status) {
            case TOBLOCK:
                t_drive(0, 0.75, 0, 1);
                /* vision code
                   if skystone is sighted
                   status = STATUS.GETBLOCK
                 */
                break;
            case GETBLOCK:
                // servo motor stuff
                break;
            case TOBUILD:
                t_drive(0, -0.75,0, 1);
                // vision stuff, if arrived at build site change case to TOBLOCK
                break;
        }
    }
}
