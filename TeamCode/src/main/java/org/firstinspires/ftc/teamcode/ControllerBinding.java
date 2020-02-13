package org.firstinspires.ftc.teamcode;

import java.util.ArrayList;

public class ControllerBinding {

    double[] inputs, outputs;
    ArrayList<BindVector> vectors;
    ArrayList<BindMatrix> matrices;

    ControllerBinding(int numInputs, int numOutputs){
        inputs = new double[numInputs];
        outputs = new double[numOutputs];
    }
    void addBinding(){

    }

    public static class BindVector{
        double[][] bounds;
    }

    public static class BindMatrix{
        double[][] coeffs;
    }
}
