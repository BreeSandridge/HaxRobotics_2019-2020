package org.firstinspires.ftc.teamcode.Autonomous;

// CameraParams class
// This class stores features of a camera
// and calculates the position of a (sky)stone
// based on its bounding rectangle

public class CameraParams {
    private double h; // Image height
    private double w; // Image width
    private double res; // How many pixels in a 45-degree angle
    private double blockWidth = 7.5; // Width of the stones (inches)

    // Given a rectangle, returns location in space of bottom edge
    //public double[] undoPerspectiveOnRect(float x_left, float y_top, float x_right, float y_bottom){
    public double undoPerspective(float x_left, float y_top, float x_right, float y_bottom){
        double rectWidth = x_right-x_left;
        double blockDist = blockWidth*res/rectWidth;
        double rectOffset = (x_left+x_right)/2 - w/2;
        double blockOffset = rectOffset*blockDist/res;
        double pos = blockOffset;
        return pos;
    }

    // Constructor: accepts dimensions and resolution of camera
    public CameraParams(double h, double w, double res){
        this.h=h;
        this.w=w;
        this.res=res;
    }

}
