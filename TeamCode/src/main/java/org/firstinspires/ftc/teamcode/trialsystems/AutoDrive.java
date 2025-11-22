package org.firstinspires.ftc.teamcode.trialsystems;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="Auto Drive")
public class AutoDrive extends LinearOpMode {
    // Motor Variables
    static DcMotor FLeft;
    static DcMotor FRight;
    static DcMotor BLeft;
    static DcMotor BRight;

    // Timer
    ElapsedTime runtime = new ElapsedTime();

    // AutoDrive Math Variables
    static final double COUNTS_PER_MOTOR_REV = 1425; // Not final, pls verify by checking the motor label.
    static final double EXTERNAL_OUTPUT_GEAR_RATIO = 1; // No change as gear ratio is maintained
    static final double WHEEL_DIAMETER = 14.5; // In cm. Not final, pls verify by measuring diameter of wheel
    static final double COUNTS_PER_CENTIMETRE = (COUNTS_PER_MOTOR_REV * EXTERNAL_OUTPUT_GEAR_RATIO) / (WHEEL_DIAMETER * 3.1415);

    // Speed variables
    static final double DRIVE_SPEED = 0.7;
    static final double TURN_SPEED = 0.5;
    static final double STRAFE_SPEED = 0.5;


    @Override
    public void runOpMode() {
        autoDriveInit();

    }

    public void autoDriveInit() {
        FLeft = hardwareMap.get(DcMotor.class, "Motor0");
        FRight = hardwareMap.get(DcMotor.class, "Motor1");
        BLeft = hardwareMap.get(DcMotor.class, "Motor2");
        BRight = hardwareMap.get(DcMotor.class, "Motor3");

        //Inverting motors as they are opposite

        BRight.setDirection(DcMotor.Direction.REVERSE);

        FLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        FRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        BLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        BRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // Send telemetry message to indicate successful Encoder reset
        telemetry.addData("Starting at",  "%7d :%7d :%7d :%7d",
                FLeft.getCurrentPosition(),
                FRight.getCurrentPosition(),
 m,                 BRight.getCurrentPosition());
        telemetry.update();

        // Wait for the game to start (driver presses START)
        waitForStart();

        // Commands space
        encoderThrustDrive(-DRIVE_SPEED, 67, 4);

        // Ending telemetry
        telemetry.addData("Path", "Complete");
        telemetry.update();
        sleep(1000);  // pause to display final telemetry message.
    }

    public void encoderThrustDrive(double speed, double movementAmountCM, double timeoutS) {
        // For direction, positive movementAmountM is forward while negative is reverse

        int newTargetFL;
        int newTargetFR;
        int newTargetBL;
        int newTargetBR;

        if (opModeIsActive()) {
            // Determine new target position, and pass to motor controller
            newTargetFL = FLeft.getCurrentPosition() + (int) (movementAmountCM * COUNTS_PER_CENTIMETRE);
            newTargetFR = FRight.getCurrentPosition() + (int) (movementAmountCM * COUNTS_PER_CENTIMETRE);
            newTargetBL = BLeft.getCurrentPosition() + (int) (movementAmountCM * COUNTS_PER_CENTIMETRE);
            newTargetBR = BRight.getCurrentPosition() + (int) (movementAmountCM * COUNTS_PER_CENTIMETRE);
            FLeft.setTargetPosition(newTargetFL);
            FRight.setTargetPosition(newTargetFR);
            BLeft.setTargetPosition(newTargetBL);
            BRight.setTargetPosition(newTargetBR);
            BRight.setDirection(DcMotor.Direction.REVERSE);


            // Turn On RUN_TO_POSITION
            FLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            FRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            BLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            BRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            FLeft.setPower(speed);
            FRight.setPower(speed);
            BLeft.setPower(speed);
            BRight.setPower(speed);

            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (FLeft.isBusy() && FRight.isBusy() && BLeft.isBusy() && BRight.isBusy())) {

                // Display it for the driver.
                telemetry.addData("Running to",  " %7d", newTargetFL);
                telemetry.addData("Currently at",  " at %7d",
                        FLeft.getCurrentPosition());
                telemetry.update();
            }
            // Stop all motion;
            FLeft.setPower(0);
            FRight.setPower(0);
            BLeft.setPower(0);
            BRight.setPower(0);

            // Turn off RUN_TO_POSITION
            FLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            FRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            BLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            BRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            sleep(250);   // optional pause after each move.

        }

    }

    public void encoderTurnDrive() {
        int newTargetFL;
        int newTargetFR;
        int newTargetBL;
        int newTargetBR;

        if (opModeIsActive()) {


        }


    }

}