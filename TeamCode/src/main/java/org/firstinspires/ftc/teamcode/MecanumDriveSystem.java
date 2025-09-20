package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp
public class MecanumDriveSystem  extends OpMode {
    // Creating the variable for the DcMotors
    DcMotor FLeft;
    DcMotor FRight;
    DcMotor BLeft;
    DcMotor BRight;

    @Override
    public void init() {
        FLeft = hardwareMap.get(DcMotor.class, "motor1");
        FRight = hardwareMap.get(DcMotor.class, "motor2");
        BLeft = hardwareMap.get(DcMotor.class, "motor3");
        BRight = hardwareMap.get(DcMotor.class, "motor4");

        //Inverting motors as they are opposite
        FLeft.setDirection(DcMotor.Direction.REVERSE);
        BLeft.setDirection(DcMotor.Direction.REVERSE);
    }

    @Override
    public void loop() {
        telemetry.addLine("Driving is now enabled /w Mecanum Drive");
        drive(gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);

    }

    public void drive(double thrust, double strafe, double turn) {

        // This is the motor power formula for each driving motor
        double FLeftPower = thrust + strafe + turn;
        double FRightPower = thrust - strafe - turn;
        double BRightPower = thrust + strafe - turn;
        double BLeftPower = thrust - strafe + turn;

        double maxSpeed = 1.0;  // speed divisor

        FLeft.setPower(maxSpeed * FLeftPower);
        FRight.setPower(maxSpeed * FRightPower);
        BLeft.setPower(maxSpeed * BLeftPower);
        BRight.setPower(maxSpeed * BRightPower);


    }
}
