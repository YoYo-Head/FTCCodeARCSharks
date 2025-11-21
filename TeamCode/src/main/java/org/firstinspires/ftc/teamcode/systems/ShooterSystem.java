package org.firstinspires.ftc.teamcode.systems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.DcMotor;

import java.math.BigDecimal;
import java.math.RoundingMode;

@TeleOp(name="Shooting & Intake System")
public class ShooterSystem extends LinearOpMode {
    DcMotor SM;
    CRServo IS1;
    //CRServo IS2;

    ElapsedTime timer1 = new ElapsedTime();
    ElapsedTime timer2 = new ElapsedTime();
    ElapsedTime timer3 = new ElapsedTime();

    // Shifter Specific Variables
    boolean shift = false;

    // The intake speed power percentage
    int pwrPercentage = 100; // For shooting power
    double SHOpower = (double) pwrPercentage / 100;

    public void ShooterInit() {
        // Mapping out the locations of the motors on the Control Hub
        SM = hardwareMap.get(DcMotor.class, "ShootingMotor");
        IS1 = hardwareMap.get(CRServo.class, "IntakeServo1");
        //IS2 = hardwareMap.get(CRServo.class, "IntakeServo2");

        IS1.setPower(0);
        //IS2.setPower(0);

    }

    @Override
    public void runOpMode() {
        ShooterInit();

        waitForStart();

        if (opModeIsActive()) {
            while (opModeIsActive()) {
                // If using this OpMode, this is where the inputs will be taken
                //looper();
                loader(gamepad1.a);
                shooter(gamepad1.x);
                //SHPower(gamepad1.dpad_left, gamepad1.dpad_right);


            }

        }

    }

    /*
    public void looper() {


    }
     */

    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    // Defining the function of the loader
    public void loader(boolean LOAbuttonPos) {
        String cap = "Shooter System - Loader";
        timer1.reset();
        timer1.startTime();
        if (LOAbuttonPos) {
            IS1.setPower(1);

            while (timer1.seconds() <= 0.5)
                telemetry.addData(cap, "loading... " + timer1.seconds());

            IS1.setPower(-1);

            telemetry.addData(cap, "The loader has completed all steps to load!");
            telemetry.update();

        }

    }

    // Defining the function of the shooter
    public void shooter(boolean SHObutton) {
        timer2.reset();
        timer2.startTime();

        if (SHObutton) {
            SM.setPower(SHOpower);
            while (timer2.seconds() <= 1) {
                telemetry.addLine("Shooting...");
                telemetry.update();
                //looper();

            }
            telemetry.addData("Shooter System - Shooter", "The shooter has completed all steps!");
            telemetry.update();

        } else {
            SM.setPower(0);

        }

    }

    /*
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
            looper();

        }

        if (!mes.equals("n/a")) {
            telemetry.addData(cap, mes);

        }
        telemetry.update();

    }
*/
}

