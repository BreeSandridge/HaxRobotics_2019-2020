package org.firstinspires.ftc.teamcode.TensorFlow;

import java.util.ArrayList;

// camera location to the robot center
//
public class CameraParams {
    private double offset_x = 0;
    private double offset_y = 0;
    private double offset_z = 0;
    private double h = 1836; // Image height
    private double w = 3264; // Image width
    private double res = 2565; // How many pixels in a 45-degree angle
    private double y = 3.5; // Height of the camera above the ground (inches)

    // Given a location on screen, returns a location in space
    // Assumes value of y (3.5 inches)
    public double[] undoPerspectiveOnPoint(float px, float py){
        // Translate so that the origin is in the center of screen
        px -= w/2;
        py -= h/2;
        double z = y*res/py; // Solve for distance from plane of camera
        double x = z*px/res; // Solve for distance from line of sight
        double[] location  = {x, y, z}; // Return location (note: y is constant)
        // TODO: Use the size of the rectangle to solve for all 3 coords
        return location;
    }

    // Given a rectangle, returns location in space of bottom edge
    public double[] undoPerspectiveOnRect(float x_left, float y_top, float x_right, float y_bottom){
        // Find the middle of the bottom edge of the bounding rectangle
        float px = (x_left+x_right)/2;
        float py = y_bottom;
        return undoPerspectiveOnPoint(px, py);
    }

    // Constructor: accepts offset, dimensions, and resolution of camera
    public CameraParams(double offset_x, double offset_y, double offset_z,
                        double h, double w, double res){
        this.offset_x=offset_x;
        this.offset_y=offset_y;
        this.offset_z=offset_z;
        this.h=h;
        this.w=w;
        this.res=res;
    }

    // input location of item relative to camera
    // @return location relative to robot center
    public double[] getItemLocation(double stone_x, double stone_y, double stone_z){
        double location_x=offset_x+stone_x;
        double location_y=offset_y+stone_y;
        double location_z=offset_z+stone_z;

        double[] stone_location = {location_x, location_y, location_z};
        return stone_location;
    }

    // input detected dimensions (1 dimension?) of the stone
    // @return the distance of the item to the robot center
    //
    public double DistanceOfItem(double detectedSize) {
        double stone_x = 203.2;
        return 0;
    }
}
