package org.firstinspires.ftc.teamcode.AreshPourkavoos;

// DriveState class

public class DriveState {
    public double x, y, w;

    DriveState(double x, double y, double w){
        this.x = x;
        this.y = y;
        this.w = w;
    }

    DriveState(DriveState d){
        this.x = d.x;
        this.y = d.y;
        this.w = d.w;
    }

    public DriveState times(double f){
        return new DriveState(x*f, y*f, w*f);
    }

    public DriveState times(DriveState d){
        return new DriveState(x*d.x, y*d.y, w*d.w);
    }

}
