package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
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
    double power = (double) pwrPercentage / 100;
    ElapsedTime timer1 = new ElapsedTime();
    ElapsedTime timer2 = new ElapsedTime();

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
                shooter(gamepad1.a);

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
        IM1.setPower(power);

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
            IM2.setPower(power);
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

    public void shooter(boolean SHObutton) {
        timer2.reset();
        timer2.startTime();

        if (SHObutton) {
            SM.setPower(1);
            while (timer2.seconds() <= 1) {
                telemetry.addLine("loading...");
                telemetry.update();

                drive(gamepad1.left_stick_y, gamepad1.left_stick_x * -1, gamepad1.right_stick_x * -1);

            }
            telemetry.addData("Shooter", "The shooter has completed all steps!");
            telemetry.update();
        }
        else {
            SM.setPower(0);
        }

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