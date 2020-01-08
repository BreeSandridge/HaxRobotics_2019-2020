package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.AreshPourkavoos.Accel_Drive;

// extend OpMode so future classes will extend SuperOp Instead
// implements is for interfaces

/*
SuperOp class
The SuperOp class is the OpMode of the robot,
containing the motors and an AccelDrive through which
the robot can accelerate and decelerate
in a trapezoid drive pattern.
(See AreshPourkavoos/Accel_Drive.java)
 */

public abstract class SuperOp extends OpMode implements SuperOp_Interface {

    public DcMotor FrontLeftDrive = null;
    public DcMotor FrontRightDrive = null;
    public DcMotor BackLeftDrive = null;
    public DcMotor BackRightDrive = null;

    public DcMotor LeftStoneRamp = null;
    public DcMotor RightStoneRamp = null;

    public DcMotor LatchMotor;

    public CRServo Flipper = null;
    public Servo Trapdoor = null;




    public Servo Latch = null;

    public enum STATUS {START, TOBLOCK, APPROACH, GETBLOCK, AWAY, TOBUILD, PARK, STOP}

    protected Accel_Drive accelDrive;

    public double x_speed;
    public double y_speed;
    public double w_speed;




    static final double COUNTS_PER_MOTOR_REV = 1440;            // eg: TETRIX Motor Encoder
    static final double     DRIVE_GEAR_REDUCTION    = 2.0 ;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double     DRIVE_SPEED             = 0.6;
    static final double     TURN_SPEED              = 0.5;



    @Override
    public void init() {
        // Initialize the hardware variables
        FrontLeftDrive  = hardwareMap.get(DcMotor.class, "FrontLeftDrive");
        FrontRightDrive = hardwareMap.get(DcMotor.class, "FrontRightDrive");
        BackLeftDrive  = hardwareMap.get(DcMotor.class, "BackLeftDrive");
        BackRightDrive = hardwareMap.get(DcMotor.class, "BackRightDrive");

        LeftStoneRamp = hardwareMap.get(DcMotor.class, "LeftStoneRamp");
        RightStoneRamp = hardwareMap.get(DcMotor.class, "RightStoneRamp");
        Flipper = hardwareMap.crservo.get("StoneArm");
        Trapdoor = hardwareMap.get(Servo.class, "Trapdoor");

        LatchMotor = hardwareMap.get(DcMotor.class, "LatchMotor");

        Latch = hardwareMap.get(Servo.class, "Latch");

        // Reverse directions on the right motors
        // so that "forward" and "backward" are the same number for both sides
        FrontLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        FrontRightDrive.setDirection(DcMotor.Direction.REVERSE);
        BackLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        BackRightDrive.setDirection(DcMotor.Direction.REVERSE);

        LeftStoneRamp.setDirection(DcMotor.Direction.FORWARD);
        RightStoneRamp.setDirection(DcMotor.Direction.REVERSE);

        // Pass the motors to the AccelDrive so it can access them
        // (may later be bundled into a class or lookup table)
        accelDrive = new Accel_Drive(FrontLeftDrive, FrontRightDrive,
                BackLeftDrive,  BackRightDrive);

        x_speed = .6;
        y_speed = .4;
        w_speed = .4;
    }

    public void setMode(DcMotor.RunMode mode){
        FrontRightDrive.setMode(mode);
        FrontLeftDrive.setMode(mode);
        BackLeftDrive.setMode(mode);
        BackRightDrive.setMode(mode);
    }

    // Mechanum wheel implementation
    // Accepts amount to move left/right (x), move up/down (y), and rotate (w)

    public void drive(double x, double y, double w) {
        FrontLeftDrive.setPower((y_speed * y) - (x_speed * x)+ (w_speed* w));
        FrontRightDrive.setPower((y_speed * y) + (x_speed * x) - (w_speed * w));
        BackLeftDrive.setPower((y_speed * y) + (x_speed * x) + (w_speed * w));
        BackRightDrive.setPower((y_speed * y) - (x_speed * x) - (w_speed * w));
    }

    /**
     * Uses gamepad1 to use
     */
    public void c_drive(){
        drive(
                -gamepad1.left_stick_x,
                -gamepad1.left_stick_y,
                gamepad1.right_stick_x
        );
    }

    // Wait for a given number of seconds (t)
    // Is currently deprecated and will likely remain that way,
    // as this function would halt all data collection, etc.
    // until the wait time is over
    public void sleep_secs(double t){
        try {
            Thread.sleep((long)(t*1000));
        }
        catch(InterruptedException e){
        }
    }

    // Perform a trapezoid drive with a max translational velocity of (x, y)
    // and max rotation of w lasting t seconds
    // Would be called by implementation, is not yet
    @Override
    public void t_drive(double x, double y, double w, double t) {
        DriveParams newParams = new DriveParams(x, y, w, t);
        accelDrive.pushCommand(newParams);
    }


    /**
     * This method allows forwards and backwards movement for the robot by running the motors
     * until a certain encoder value is reached
     * @param speed (the speed [-1, 1], at which the robot's wheels will turn)
     * @param desired the distance the robot will travel (positive for forwards, negative for backwards)
     */






    /*
    Why does Waldo wear stripes?

    Because he doesnt want to be spotted
     */
    /*public void encoderDriveWithSpeed(double speed, double desired) {
        //variables that store initial encoder values for the four motors
        double FrontLeftInitial = FrontLeftDrive.getCurrentPosition();
        double FrontRightInitial = FrontRightDrive.getCurrentPosition();
        double BackLeftInitial = BackLeftDrive.getCurrentPosition();
        double BackRightInitial = BackRightDrive.getCurrentPosition();
        //Front Back movement
        while (true) {
            //Front Left Motor movement
            if (desired + FrontLeftInitial > FrontLeftDrive.getCurrentPosition() + 10) {
                FrontLeftDrive.setPower(speed);
            } else if (desired + FrontLeftInitial < FrontLeftDrive.getCurrentPosition() - 10) {
                FrontLeftDrive.setPower(-speed);
            } else {
                FrontLeftDrive.setPower(0);
            }

            //Front Right Motor movement
            if (desired + FrontRightInitial > FrontRightDrive.getCurrentPosition() + 10) {
                FrontRightDrive.setPower(speed);
            } else if (desired + FrontRightInitial < FrontRightDrive.getCurrentPosition() - 10) {
                FrontRightDrive.setPower(-speed);
            } else {
                FrontRightDrive.setPower(0);
            }

            //Back Left Motor movement
            if (desired + BackLeftInitial > BackLeftDrive.getCurrentPosition() + 10) {
                BackLeftDrive.setPower(speed);
            } else if (desired + FrontLeftInitial < BackLeftDrive.getCurrentPosition() - 10) {
                BackLeftDrive.setPower(-speed);
            } else {
                BackLeftDrive.setPower(0);
            }

            //Back Right Motor movement
            if (desired + BackRightInitial > BackRightDrive.getCurrentPosition() + 10) {
                BackRightDrive.setPower(speed);
            } else if (desired + FrontLeftInitial < BackRightDrive.getCurrentPosition() - 10) {
                BackRightDrive.setPower(-speed);
            } else {
                BackRightDrive.setPower(0);
            }

            //Ends loop when all motors reach desired position
            if (FrontLeftDrive.getPower() == 0) {
                if (FrontRightDrive.getPower() == 0) {
                    if (BackLeftDrive.getPower() == 0) {
                        if (BackRightDrive.getPower() == 0) {
                            return;
                        }
                    }
                }
            }
            /*if (desired + FrontLeftInitial < FrontLeftDrive.getCurrentPosition() + 10 &&
                    desired + FrontLeftInitial > FrontLeftDrive.getCurrentPosition() - 10){
                if (desired + FrontRightInitial < FrontRightDrive.getCurrentPosition() + 10 &&
                        desired + FrontRightInitial > FrontRightDrive.getCurrentPosition() - 10) {
                    if (desired + BackLeftInitial < BackLeftDrive.getCurrentPosition() + 10 &&
                            desired + BackLeftInitial > BackLeftDrive.getCurrentPosition() - 10) {
                        if (desired + BackRightInitial < BackRightDrive.getCurrentPosition() + 10 &&
                                desired + BackRightInitial > BackRightDrive.getCurrentPosition() - 10) {
                            return;
                        }
                    }
                }
            }
        }
    }*/

    /*public void encoderStrafeWithSpeed(double speed, double desired) {
        //variables that store initial encoder values for the four motors
        double FrontLeftInitial = FrontLeftDrive.getCurrentPosition();
        double FrontRightInitial = FrontRightDrive.getCurrentPosition();
        double BackLeftInitial = BackLeftDrive.getCurrentPosition();
        double BackRightInitial = BackRightDrive.getCurrentPosition();
        //Constant that converts distance of strafing into distance the wheels need to rotate
        double constant = 1.7;
        //Strafing movement
        while (true) {
            //if robot needs to strafe right
            if (desired > 0) {
                if (constant * desired + FrontLeftInitial > FrontLeftDrive.getCurrentPosition() + 10) {
                    FrontLeftDrive.setPower(-speed);
                } else {
                    FrontLeftDrive.setPower(0);
                }
                if (constant * desired + FrontRightInitial > FrontRightDrive.getCurrentPosition() + 10) {
                    FrontRightDrive.setPower(-speed);
                } else {
                    FrontRightDrive.setPower(0);
                }
                if (constant * desired + BackLeftInitial > BackLeftDrive.getCurrentPosition() + 10) {
                    BackLeftDrive.setPower(speed);
                } else {
                    BackLeftDrive.setPower(0);
                }
                if (constant * desired + BackRightInitial > BackRightDrive.getCurrentPosition() + 10) {
                    BackRightDrive.setPower(speed);
                } else {
                    BackRightDrive.setPower(0);
                }
                //if robot needs to strafe left
            } else {
                if (constant * desired + FrontLeftInitial > FrontLeftDrive.getCurrentPosition() + 10) {
                    FrontLeftDrive.setPower(speed);
                } else {
                    FrontLeftDrive.setPower(0);
                }
                if (constant * desired + FrontRightInitial > FrontRightDrive.getCurrentPosition() + 10) {
                    FrontRightDrive.setPower(speed);
                } else {
                    FrontRightDrive.setPower(0);
                }
                if (constant * desired + BackLeftInitial > BackLeftDrive.getCurrentPosition() + 10) {
                    BackLeftDrive.setPower(-speed);
                } else {
                    BackLeftDrive.setPower(0);
                }
                if (constant * desired + BackRightInitial > BackRightDrive.getCurrentPosition() + 10) {
                    BackRightDrive.setPower(-speed);
                } else {
                    BackRightDrive.setPower(0);
                }
            }
            //Ends loop when all motors reach desired position
            if (FrontLeftDrive.getPower() == 0){
                if (FrontRightDrive.getPower() == 0) {
                    if (BackLeftDrive.getPower() == 0) {
                        if (BackRightDrive.getPower() == 0) {
                            return;
                        }
                    }
                }
            }
        }
    }*/

    /*public void encoderRotationWithSpeed(double speed, double desired) {
        //variables that store initial encoder values for the four motors
        double FrontLeftInitial = FrontLeftDrive.getCurrentPosition();
        double FrontRightInitial = FrontRightDrive.getCurrentPosition();
        double BackLeftInitial = BackLeftDrive.getCurrentPosition();
        double BackRightInitial = BackRightDrive.getCurrentPosition();
        //Constant that converts the degrees of robot rotation into distance the wheels need to rotate
        double constant = 1.7;
        //Rotating movement
        while (true) {
            //if robot needs to rotate clockwise
            if (desired > 0) {
                if (constant * desired + FrontLeftInitial > FrontLeftDrive.getCurrentPosition() + 10) {
                    FrontLeftDrive.setPower(-speed);
                } else {
                    FrontLeftDrive.setPower(0);
                }
                if (constant * desired + FrontRightInitial > FrontRightDrive.getCurrentPosition() + 10) {
                    FrontRightDrive.setPower(-speed);
                } else {
                    FrontRightDrive.setPower(0);
                }
                if (constant * desired + BackLeftInitial > BackLeftDrive.getCurrentPosition() + 10) {
                    BackLeftDrive.setPower(-speed);
                } else {
                    BackLeftDrive.setPower(0);
                }
                if (constant * desired + BackRightInitial > BackRightDrive.getCurrentPosition() + 10) {
                    BackRightDrive.setPower(-speed);
                } else {
                    BackRightDrive.setPower(0);
                }
                //if robot needs to rotate counter clockwise
            } else {
                if (constant * desired + FrontLeftInitial > FrontLeftDrive.getCurrentPosition() + 10) {
                    FrontLeftDrive.setPower(speed);
                } else {
                    FrontLeftDrive.setPower(0);
                }
                if (constant * desired + FrontRightInitial > FrontRightDrive.getCurrentPosition() + 10) {
                    FrontRightDrive.setPower(speed);
                } else {
                    FrontRightDrive.setPower(0);
                }
                if (constant * desired + BackLeftInitial > BackLeftDrive.getCurrentPosition() + 10) {
                    BackLeftDrive.setPower(speed);
                } else {
                    BackLeftDrive.setPower(0);
                }
                if (constant * desired + BackRightInitial > BackRightDrive.getCurrentPosition() + 10) {
                    BackRightDrive.setPower(speed);
                } else {
                    BackRightDrive.setPower(0);
                }
            }
            //Ends loop when all motors reach desired position
            if (FrontLeftDrive.getPower() == 0){
                if (FrontRightDrive.getPower() == 0) {
                    if (BackLeftDrive.getPower() == 0) {
                        if (BackRightDrive.getPower() == 0) {
                            return;
                        }
                    }
                }
            }
        }
    }*/

    /*public void basicEncoderDrive(double straightInches, double strafeInches) {
        //Front Left Motor movement
        int newFrontLeftTarget, newFrontRightTarget, newBackLeftTarget, newBackRightTarget;

        // Determine new target position, and pass to motor controller
        newFrontLeftTarget = FrontLeftDrive.getCurrentPosition() - (int) (straightInches * COUNTS_PER_INCH);
        newFrontRightTarget = FrontRightDrive.getCurrentPosition() + (int) (straightInches * COUNTS_PER_INCH);
        newBackLeftTarget = BackLeftDrive.getCurrentPosition() - (int) (straightInches * COUNTS_PER_INCH);
        newBackRightTarget = BackRightDrive.getCurrentPosition() + (int) (straightInches * COUNTS_PER_INCH);

        FrontLeftDrive.setTargetPosition(newFrontLeftTarget);
        BackLeftDrive.setTargetPosition(newBackLeftTarget);
        FrontRightDrive.setTargetPosition(newFrontRightTarget);
        BackRightDrive.setTargetPosition(newBackRightTarget);

        BackRightDrive.setPower(DRIVE_SPEED);
        BackLeftDrive.setPower(DRIVE_SPEED);
        FrontRightDrive.setPower(DRIVE_SPEED);
        FrontLeftDrive.setPower(DRIVE_SPEED);

        FrontLeftDrive.setMode (DcMotor.RunMode.RUN_TO_POSITION);
        FrontRightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        BackLeftDrive.setMode  (DcMotor.RunMode.RUN_TO_POSITION);
        BackRightDrive.setMode (DcMotor.RunMode.RUN_TO_POSITION);

        // Determine new target position, and pass to motor controller
        double strafeConstant = 1.3;

        if(strafeInches > 0) {
            newFrontLeftTarget = FrontLeftDrive.getCurrentPosition() - (int) (strafeConstant * strafeInches * COUNTS_PER_INCH);
            newFrontRightTarget = FrontRightDrive.getCurrentPosition() - (int) (strafeConstant * strafeInches * COUNTS_PER_INCH);
            newBackLeftTarget = BackLeftDrive.getCurrentPosition() + (int) (strafeConstant * strafeInches * COUNTS_PER_INCH);
            newBackRightTarget = BackRightDrive.getCurrentPosition() + (int) (strafeConstant * strafeInches * COUNTS_PER_INCH);
        } else {
            newFrontLeftTarget = FrontLeftDrive.getCurrentPosition() + (int) (strafeConstant * strafeInches * COUNTS_PER_INCH);
            newFrontRightTarget = FrontRightDrive.getCurrentPosition() + (int) (strafeConstant * strafeInches * COUNTS_PER_INCH);
            newBackLeftTarget = BackLeftDrive.getCurrentPosition() - (int) (strafeConstant * strafeInches * COUNTS_PER_INCH);
            newBackRightTarget = BackRightDrive.getCurrentPosition() - (int) (strafeConstant * strafeInches * COUNTS_PER_INCH);
        }

        FrontLeftDrive.setTargetPosition(newFrontLeftTarget);
        BackLeftDrive.setTargetPosition(newBackLeftTarget);
        FrontRightDrive.setTargetPosition(newFrontRightTarget);
        BackRightDrive.setTargetPosition(newBackRightTarget);

        BackRightDrive.setPower(DRIVE_SPEED);
        BackLeftDrive.setPower(DRIVE_SPEED);
        FrontRightDrive.setPower(DRIVE_SPEED);
        FrontLeftDrive.setPower(DRIVE_SPEED);

        FrontLeftDrive.setMode (DcMotor.RunMode.RUN_TO_POSITION);
        FrontRightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        BackLeftDrive.setMode  (DcMotor.RunMode.RUN_TO_POSITION);
        BackRightDrive.setMode (DcMotor.RunMode.RUN_TO_POSITION);
    }*/
}