package org.firstinspires.ftc.teamcode.Autonomous;

import java.util.ArrayList;

// camera location to the robot center
//
public class CameraParams {
    private double h; // Image height
    private double w; // Image width
    private double res; // How many pixels in a 45-degree angle
    //private double y = 3.5; // Height of the camera above the ground (inches)
    private double blockWidth = 7.5; // Width of the stones (inches)

    // Given a rectangle, returns location in space of bottom edge
    //public double[] undoPerspectiveOnRect(float x_left, float y_top, float x_right, float y_bottom){
    public double undoPerspective(float x_left, float y_top, float x_right, float y_bottom){
        double rectWidth = x_right-x_left;
        double blockDist = blockWidth*res/rectWidth;
        double rectOffset = (x_left+x_right)/2 - w/2;
        double blockOffset = rectOffset*blockDist/res;
        //double[] pos = {blockDist, blockOffset};
        double pos = blockOffset;
        //double[] pos = {rectWidth, rectOffset};
        return pos;
    }

    // Constructor: accepts offset, dimensions, and resolution of camera
    //public CameraParams(double offset_x, double offset_y, double offset_z,
    //                    double h, double w, double res){
    public CameraParams(double h, double w, double res){
        //this.offset_x=offset_x;
        //this.offset_y=offset_y;
        //this.offset_z=offset_z;
        this.h=h;
        this.w=w;
        this.res=res;
    }

}
