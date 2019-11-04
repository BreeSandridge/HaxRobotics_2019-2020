package org.firstinspires.ftc.teamcode;

public class DriveParams {
    public double x, y, w, t, desired_rotations;
    DriveParams(double x, double y, double w, double desired_rotations){
        this.x=x;
        this.y=y;
        this.w=w;
        this.desired_rotations=desired_rotations;
    }
    DriveParams(DriveParams d){
        this.x=d.x;
        this.y=d.y;
        this.w=d.w;
        this.t=d.t;
        this.desired_rotations=d.desired_rotations;
    }
}
