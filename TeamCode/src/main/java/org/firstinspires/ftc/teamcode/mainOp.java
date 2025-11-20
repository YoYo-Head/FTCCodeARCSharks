package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.stream.Collectors;

@TeleOp(name="MainOp Mode")
public class mainOp extends LinearOpMode {

    // Camera System Variables
    WebcamName camera;
    VisionPortal vision;
    AprilTagProcessor aprilTag;

    // Drive System Variables
    static DcMotor FLeft;
    static DcMotor FRight;
    static DcMotor BLeft;
    static DcMotor BRight;

    // Shooter System Variables
    DcMotor SM;
    DcMotor IM1;
    DcMotor IM2;
    int pwrPercentage = 100; // The percentage that the intake motor will spin at
    double SHOpower = (double) pwrPercentage / 100;
    ElapsedTime timer1 = new ElapsedTime();
    ElapsedTime timer2 = new ElapsedTime();
    ElapsedTime timer3 = new ElapsedTime();

    @Override
    public void runOpMode() {
        cameraInit();
        mecanumDriveSystemInit();
        shooterSystemInit();

        waitForStart();

        if (opModeIsActive()) {
            while (opModeIsActive()) {

                drive(gamepad1.left_stick_y, gamepad1.right_stick_x * -1, gamepad1.left_stick_x * -1);

                if (aprilTag.getDetections() != null && !aprilTag.getDetections().isEmpty()) {
                    telemetry.addData("Tag IDs", aprilTag.getDetections().stream()
                            .map(d -> d.id)
                            .collect(Collectors.toList()).toString());
                } else {
                    telemetry.addLine("No tags detected");
                }
                telemetry.update();

                loader(gamepad1.x);
                shooter(gamepad1.a, gamepad1.y, gamepad1.b, gamepad1.right_bumper);
                SHPower(gamepad1.dpad_left, gamepad1.dpad_right);

                streamSwitch(gamepad1.dpad_up, gamepad1.dpad_down);

                sleep(20);
            }
        }

    }

    public void cameraInit() {
        // Defines camera + finds camera on the Control Hub / Extension Hub
        camera = hardwareMap.get(WebcamName.class, "Webcam1");
        // Defines April Tag
        aprilTag = new AprilTagProcessor.Builder().build();

        // Defines the stream presets and starts stream
        vision = new VisionPortal.Builder()
                .setCamera(camera)
                .addProcessor(aprilTag)
                .enableLiveView(true)
                .build();

        vision.resumeStreaming();
        telemetry.addLine("Camera stream starting...");
        telemetry.update();

        // Telemetry on the console that will verify that the activation process
        telemetry.addData("Camera System", "The activation process is now complete!");
        telemetry.update();

    }

    public void mecanumDriveSystemInit() {
        // The deviceName needs to be changed to how it in driver station config thingy
        FLeft = hardwareMap.get(DcMotor.class, "Motor0");
        FRight = hardwareMap.get(DcMotor.class, "Motor1");
        BLeft = hardwareMap.get(DcMotor.class, "Motor2");
        BRight = hardwareMap.get(DcMotor.class, "Motor3");

        //Inverting motors as they are opposite
        BRight.setDirection(DcMotor.Direction.REVERSE);

        // Telemetry on the console that will verify that the activation process
        telemetry.addData("Mecanum Drive System", "The activation process is now complete!");
        telemetry.update();

    }

    public void shooterSystemInit() {
        // Mapping out the locations of the motors on the Control Hub
        SM = hardwareMap.get(DcMotor.class, "ShootingMotor");
        IM1 = hardwareMap.get(DcMotor.class, "IntakeMotor1");
        IM2 = hardwareMap.get(DcMotor.class, "IntakeMotor2");

        // Intake Motor 1 is always going to be on
        IM1.setPower(SHOpower);

        // Telemetry on the console that will verify that the activation process
        telemetry.addData("Shooter System", "The activation process is now complete!");
        telemetry.update();

    }

    public void streamSwitch(boolean enable, boolean disable) {
        if (enable) {
            vision.resumeStreaming();
        } else if (disable) {
            vision.stopStreaming();
        }

    }

    public void loader(boolean LOAbutton) {
        timer1.reset();
        timer1.startTime();

        if (LOAbutton) {
            IM2.setPower(SHOpower);
            while (timer1.seconds() <= 1) {
                telemetry.addLine("loading...");
                telemetry.update();

                drive(gamepad1.left_stick_y, gamepad1.left_stick_x * -1, gamepad1.right_stick_x * -1);

            }
            telemetry.addData("Loader", "The loader has completed all steps!");
            telemetry.update();

        }
        else {
            IM2.setPower(0);

        }

    }

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

    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public void SHPower(boolean upshift, boolean downshift) {
        // 5% upshift, 5% downshift
        // Will stop at 100%
        String cap = "Shooter System - SHPower";
        String mes = "n/a";
        boolean shift = false;

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

    public static void drive(double thrust, double turn, double strafe) {
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