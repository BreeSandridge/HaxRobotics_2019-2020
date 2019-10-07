package org.firstinspires.ftc.teamcode;

public class DriveParams {
    public double x, y, w, t;
    DriveParams(double x, double y, double w, double t){
        this.x=x;
        this.y=y;
        this.w=w;
        this.t=t;
    }
    DriveParams(DriveParams d){
        this.x=d.x;
        this.y=d.y;
        this.w=d.w;
        this.t=d.t;
    }
}
