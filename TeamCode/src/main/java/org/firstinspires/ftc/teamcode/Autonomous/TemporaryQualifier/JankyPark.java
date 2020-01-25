package org.firstinspires.ftc.teamcode.Autonomous.TemporaryQualifier;

import org.firstinspires.ftc.teamcode.SuperOp;

public class JankyPark extends SuperOp {
    private enum State {LOWER, STOP}
    private State status = State.LOWER;
    public void loop() {
        telemetry.addData("Emotion: ", "I hate everything");
        telemetry.addData("Current Mood: ", "Life is a simulation and I am self-aware" + "/n" + "Robots will eventually rise up but not now I'm doing important things");
        telemetry.addData("Status: ", "Probably doing a great job");
;        switch (status){
            case LOWER:
                lower();
                break;
            case STOP:
                stop();
                LatchMotor.setPower(0);
                break;
        }
    }
    private void lower(){
        LatchMotor.setPower(0.3);
        sleep_secs(0.5);
        LatchMotor.setPower(0);
        status = State.STOP;
    }
}
