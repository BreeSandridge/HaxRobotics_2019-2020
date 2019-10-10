package org.firstinspires.ftc.teamcode.TensorFlow;

import java.util.ArrayList;

// camera location to the robot center
//
public class CameraParams {
    private double offset_x = 0;
    private double offset_y = 0;
    private double offset_z = 0;

    public double[] undoPerspective(float x_left, float y_top, float x_right, float y_bottom){
        float h = 200; // Distance of the horizon below the top of the screen in pixels
        float w = 500; // Width of the screen in pixels
        float px = (x_left+x_right)/2, py = (y_top+y_bottom)/2;
        px -= w/2;
        py -= h;
        float y = -h/py, x = y*px; // Perspective transformation
    }

    public CameraParams(double offset_x, double offset_y, double offset_z){
        this.offset_x=offset_x;
        this.offset_y=offset_y;
        this.offset_z=offset_z;
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
    public double DistanceOfItem(double detectedSize){
        double stone_x = 203.2;

    }







}
