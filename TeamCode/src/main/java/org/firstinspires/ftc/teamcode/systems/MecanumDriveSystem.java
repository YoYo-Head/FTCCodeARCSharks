package org.firstinspires.ftc.teamcode.systems;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name="Mecanum Drive System", group="Drive System")
public class MecanumDriveSystem extends OpMode {
    // Creating the variable for the DcMotors
    DcMotor FLeft;
    DcMotor FRight;
    DcMotor BLeft;
    DcMotor BRight;

    @Override
    public void init() {
        // The deviceName needs to be changed to how it in driver station config thingy
        FLeft = hardwareMap.get(DcMotor.class, "Motor0");
        FRight = hardwareMap.get(DcMotor.class, "Motor1");
        BLeft = hardwareMap.get(DcMotor.class, "Motor2");
        BRight = hardwareMap.get(DcMotor.class, "Motor3");

        //Inverting motors as they are opposite
        FLeft.setDirection(DcMotor.Direction.REVERSE);
        BLeft.setDirection(DcMotor.Direction.REVERSE);

    }

    @Override
    public void loop() {
        telemetry.addLine("Driving is now enabled /w Mecanum Drive");
        // The actual driving part. drive is a function, as seen in the other part below this function
        drive(gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);

    }

    public void drive(double thrust, double strafe, double turn) {
        // This is the motor power formula for each driving motor
        double FLeftPower = thrust + strafe + turn;
        double FRightPower = thrust - strafe - turn;
        double BRightPower = thrust + strafe - turn;
        double BLeftPower = thrust - strafe + turn;

        double maxSpeed = 1.0;  // speed divisor

        // This actually then inputs the powers for each motor
        FLeft.setPower(maxSpeed * FLeftPower);
        FRight.setPower(maxSpeed * FRightPower);
        BLeft.setPower(maxSpeed * BLeftPower);
        BRight.setPower(maxSpeed * BRightPower);

    }
}
// :0