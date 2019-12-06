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
                // vision code

                // if skystone is sighted
                // set movement values to go towards block
                status = STATUS.GETBLOCK;
                break;
            case GETBLOCK:
                // servo motor stuff
                Latch.setPosition(0.5);
                status = STATUS.TOBUILD;
                break;
                /*
                    There's a hundred and four days of summer vacation, 'Til school comes along just to end it,
                    So the annual problem for our generation, is finding a good way to spend it. Like maybe
                    Building a rocket, or fighting a mummy, or climbing up the Eiffel tower,
                    Discovering something that doesn't exist, Or giving a monkey a shower. Surfing tidal waves
                    creating nanobots, or locating Frankenstein's brain, Finding a Dodo bird, painting
                    a continent, Or driving our sister insane!
                    As you can see there's a whole lot of stuff to do before school starts this fall
                    So stick with us cause Phineas and Ferb are gonna do it all. Just stick with us cause
                    Phineas and Ferb are gonna do it all. Mom! Phineas and Ferb are making a Title sequence!
                 */
            case TOBUILD:
                // methods
                t_drive(0, -0.75,0, 1);

                // vision stuff, if arrived at build site change case to TOBLOCK
                status = STATUS.TOBLOCK;
                Latch.setPosition(0);
                break;
        }
    }
}
