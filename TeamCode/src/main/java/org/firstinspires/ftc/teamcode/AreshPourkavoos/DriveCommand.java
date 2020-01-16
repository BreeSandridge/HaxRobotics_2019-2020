package org.firstinspires.ftc.teamcode.AreshPourkavoos;

public class DriveCommand {
    DriveState state;
    public double t;

    DriveCommand(DriveState state, double t){
        this.state = state;
        this.t = t;
    }
}
