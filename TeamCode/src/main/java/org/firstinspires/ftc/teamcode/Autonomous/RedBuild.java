package org.firstinspires.ftc.teamcode.Autonomous;

import org.firstinspires.ftc.teamcode.SuperOp;

public class RedBuild extends SuperOp {
    // status stuff
    enum STATUS {TOBLOCK, APPROACH, GETBLOCK, AWAY, TOBUILD, PARK}
    private STATUS status = STATUS.TOBLOCK;
    boolean running = true;
    private int targetPosition;
    private int currPosition;

    @Override
    public void loop() {

        // switch cases for making the robot do different things
        switch (status) {
            case TOBLOCK:
                toBlock();
                break;
            case APPROACH:
                approach();
                break;
            /*case GETBLOCK:
                getBlock();
                break;*/
            case AWAY:
                away();
                break;
            /*case TOBUILD:
                toBuild();
                break;*/
            case PARK:
                park();
                break;
        }
    }

    // method to go to block
    private void toBlock() {
        t_drive(0, 0.75, 0, 1);
        // vision code

        // if skystone is sighted
        // set movement values to go towards block
        status = STATUS.APPROACH;
    }

    // approach the block horizontally
    private void approach() {
        t_drive(0.75, 0, 0, 1);
        status = STATUS.GETBLOCK;
    }

    // method to pick up the block
    /*private void getBlock() {
        // rotate the arm down
        targetPosition = 9000;

        currPosition = LatchMotor.getCurrentPosition();
        LatchMotor.setPower(1);

        // check if the arm is in position
        if (currPosition > targetPosition - 100 || currPosition < targetPosition + 100) {
            // pull the block in
            LatchMotor.setPower(0);
            Latch.setPosition(0.5);
            // switch status
            status = STATUS.AWAY;
        }
    }
*/
    // leave the block horizontally
    private void away() {
        t_drive(-0.75, 0, 0, 1);
        status = STATUS.TOBUILD;
    }

    // go to build site and place the block back down
    /*private void toBuild() {
        // methods to get the robot back to the build site to place down the block
        t_drive(0, -0.75,0, 1);

        // rotate the arm up
        targetPosition = 7000;
        currPosition = LatchMotor.getCurrentPosition();
        LatchMotor.setPower(-1);

        // check if the arm is in position
        if (currPosition > targetPosition - 100 || currPosition < targetPosition + 100) {
            // pull the block in
            LatchMotor.setPower(0);
            Latch.setPosition(0);
            // switch status
            status = STATUS.PARK;
        }
    }
*/
    // park the thing under the bridge
    private void park() {
        // vision stuff to park the robot under the bridge
        t_drive(0, 1, 0, 1);
    }
}





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