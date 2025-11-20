package org.firstinspires.ftc.teamcode.systems;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.DcMotor;

import java.math.BigDecimal;
import java.math.RoundingMode;

@TeleOp(name="Shooting & Intake System")
public class ShooterSystem extends OpMode {
    DcMotor SM;
    CRServo IS1;
    CRServo IS2;


    ElapsedTime timer1 = new ElapsedTime();
    ElapsedTime timer2 = new ElapsedTime();
    ElapsedTime timer3 = new ElapsedTime();

    // Shifter Specific Variables
    boolean shift = false;

    // The intake speed power percentage
    int pwrPercentage = 100; // For shooting power
    double SHOpower = (double) pwrPercentage / 100;

    @Override
    public void init() {
        // Mapping out the locations of the motors on the Control Hub
        SM = hardwareMap.get(DcMotor.class, "ShootingMotor");
        IS1 = hardwareMap.get(CRServo.class, "IntakeServo1");
        IS2 = hardwareMap.get(CRServo.class, "IntakeServo2");

        IS1.setDirection(CRServo.Direction.REVERSE);
        IS2.setDirection(CRServo.Direction.REVERSE);

    }

    @Override
    public void loop() {
        // If using this OpMode, this is where the inputs will be taken
        loader(gamepad1.a);
        shooter(gamepad1.x, gamepad1.y, gamepad1.b, gamepad1.right_bumper);
        SHPower(gamepad1.dpad_left, gamepad1.dpad_right);

    }

    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    // Defining the function of the loader
    public void loader(boolean LOAbutton) {
        if (LOAbutton) {
            IS1.setPower(1);
            IS2.setPower(1);
            telemetry.addData("Loader", "The loader has completed all steps!");
            telemetry.update();

        } /* else {
            IS1.setPower(0);
            IS2.setPower(0);

        } */

    }

    // Defining the function of the shooter
    public void shooter(boolean SHObutton, boolean SHObutton70, boolean SHObutton80, boolean SHObutton90) {
        timer2.reset();
        timer2.startTime();

        if (SHObutton) {
            SM.setPower(SHOpower);
        } else if (SHObutton70) {
            SM.setPower(0.7);
        } else if (SHObutton80) {
            SM.setPower(0.8);
        } else if (SHObutton90) {
            SM.setPower(0.9);
        } else {
            SM.setPower(0);
        }

        if (SHObutton || SHObutton70 || SHObutton80 || SHObutton90) {
            while (timer2.seconds() <= 1) {
                telemetry.addLine("Shooting...");
                telemetry.update();
            }
            telemetry.addData("Shooter System - Shooter", "The shooter has completed all steps!");
            telemetry.update();

        }

    }

    public void SHPower(boolean upshift, boolean downshift) {
        // 5% upshift, 5% downshift
        // Will stop at 100%
        String cap = "Shooter System - SHPower";
        String mes = "n/a";
        shift = false;

        timer3.reset();
        timer3.startTime();

        if (upshift && SHOpower < 1) {
            SHOpower = round(SHOpower + 0.05, 2);
            mes = "Upshifted to " + (SHOpower * 100) + "%.";
            shift = true;

        } else if (upshift && SHOpower == 1) {
            mes = "Warning! Power has already been set to max!";

        } else if (downshift && SHOpower > 0.7) {
            SHOpower = round(SHOpower - 0.05, 2);
            mes = "Downshifted to " + (SHOpower * 100) + "%.";
            shift = true;

        } else if (downshift && SHOpower == 0.7) {
            mes = "Warning! Power has already been set to minimum!";

        }

        while (timer3.seconds() <= 0.5 && shift) {
            telemetry.addData(cap, "Shifting... " + timer3.seconds());
            telemetry.update();

        }

        if (!mes.equals("n/a")) {
            telemetry.addData(cap, mes);

        }
        telemetry.update();

    }

}