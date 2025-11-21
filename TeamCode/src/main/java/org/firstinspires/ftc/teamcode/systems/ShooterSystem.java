package org.firstinspires.ftc.teamcode.systems;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name="Shooting & Intake System")
public class ShooterSystem extends OpMode {
    DcMotor SM;
    DcMotor IM1;
    DcMotor IM2;

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
        IM1 = hardwareMap.get(DcMotor.class, "IntakeMotor1");
        IM2 = hardwareMap.get(DcMotor.class, "IntakeMotor2");

        // Intake Motor 1 is always going to be on
        IM1.setPower(1);

    }

    @Override
    public void loop() {
        // If using this OpMode, this is where the inputs will be taken
        loader(gamepad1.a);
        shooter(gamepad1.x, gamepad1.y, gamepad1.b, gamepad1.right_bumper);
        SHPower(gamepad1.dpad_left, gamepad1.dpad_right);

    }

    // Defining the function of the loader
    public void loader(boolean LOAbutton) {
        timer1.reset();
        timer1.startTime();

        if (LOAbutton) {
            IM2.setPower(1);
            while (timer1.seconds() <= 1) {
                telemetry.addLine("loading...");
                telemetry.update();

            }
            telemetry.addData("Loader", "The loader has completed all steps!");
            telemetry.update();

        }
        else {
            IM2.setPower(0);

        }

    }

    // Defining the function of the shooter
    public void shooter(boolean SHObutton, boolean SHObutton30, boolean SHObutton50, boolean SHObutton70) {
        timer2.reset();
        timer2.startTime();

        if (SHObutton) {
            SM.setPower(SHOpower);
        } else if (SHObutton30) {
            SM.setPower(0.3);
        } else if (SHObutton50) {
            SM.setPower(0.5);
        } else if (SHObutton70) {
            SM.setPower(0.7);
        } else {
            SM.setPower(0);
        }

        if (SHObutton || SHObutton30 || SHObutton50 || SHObutton70) {
            while (timer2.seconds() <= 1) {
                telemetry.addLine("loading...");
                telemetry.update();
            }
            telemetry.addData("Shooter", "The shooter has completed all steps!");
            telemetry.update();

        }
    }

    public void SHPower(boolean upshift, boolean downshift) {
        // 5% upshift, 5% downshift
        // Will stop at 100%
        String cap = "Shooter System - SHPower";
        String mes = "n/a";

        if (upshift && SHOpower < 1) {
            SHOpower = SHOpower + 0.05;
            mes = "Upshifted to " + (SHOpower * 100) + "%.";
            shift = true;

        } else if (upshift && SHOpower == 1) {
            mes = "Warning! Power has already been set to max!";

        } else if (downshift && SHOpower > 0.7) {
            SHOpower = SHOpower - 0.05;
            mes = "Downshifted to " + (SHOpower * 100) + "%.";
            shift = true;

        } else if (downshift && SHOpower == 0.7) {
            mes = "Warning! Power has already been set to minimum!";

        }
        timer3.startTime();
        timer3.reset();

        while (timer3.seconds() <= 0.5 && shift) {
            telemetry.update();

        }

        if (!mes.equals("n/a")) {
            telemetry.addData(cap, mes);

        }

        telemetry.update();

    }

}