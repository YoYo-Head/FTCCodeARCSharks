package org.firstinspires.ftc.teamcode.systems;

import static java.lang.Thread.sleep;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
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


    // The intake speed power percentage
    int pwrPercentage = 10;
    double power = (double) pwrPercentage / 100;

    @Override
    public void init() {
        // Mapping out the locations of the motors on the Control Hub
        SM = hardwareMap.get(DcMotor.class, "ShootingMotor");
        IM1 = hardwareMap.get(DcMotor.class, "IntakeMotor1");
        IM2 = hardwareMap.get(DcMotor.class, "IntakeMotor2");

        // Intake Motor 1 is always going to be on
        IM1.setPower(power);

    }

    @Override
    public void loop() {
        // If using this OpMode, this is where the inputs will be taken
        loader(gamepad1.a);
        shooter(gamepad1.x);

    }

    // Defining the function of the loader
    public void loader(boolean LOAbutton) {
        timer1.reset();

        if (LOAbutton) {
            IM2.setPower(power);

        }
        else {
            IM2.setPower(0);

        }

        timer1.startTime();

        while (timer1.seconds() <= 1) {
            // Just waiting...


        }
        telemetry.addData("Loader", "The loader has completed all steps!");


    }

    // Defining the function of the shooter
    public void shooter(boolean SHObutton) {
        timer2.reset();

        if (SHObutton) {
            SM.setPower(1);
        }
        else {
            SM.setPower(0);
        }

        timer2.startTime();

        while (timer2.seconds() <= 1) {
            // Also just waiting...

        }
        telemetry.addData("Shooter", "The shooter has completed all steps!");


    }


}
