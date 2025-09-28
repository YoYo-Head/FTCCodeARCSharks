package org.firstinspires.ftc.teamcode.systems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.stream.Collectors;

@TeleOp(name="Camera")
public class CameraView extends LinearOpMode {
    // Creating the camera
    WebcamName camera;

    // Creating camera feed
    VisionPortal vision;

    // Creating April Tag processor
    AprilTagProcessor aprilTag;

    @Override
    public void runOpMode() {
        initScanner();
        // Activates function which defines and starts camera

        waitForStart();

        if (opModeIsActive()) {
            while (opModeIsActive()) {

                if (aprilTag.getDetections() != null && !aprilTag.getDetections().isEmpty()) {
                    telemetry.addData("Tag IDs", aprilTag.getDetections().stream()
                            .map(d -> d.getID())
                            .collect(Collectors.toList()).toString());
                } else {
                    telemetry.addLine("No tags detected");
                }
                telemetry.update();


                // This part of code keep running till the OpMode isn't active

                // Saves CPU resources if not needed (saw this from recommendations)
                if (gamepad1.dpad_down) {
                    vision.stopStreaming();

                } else if (gamepad1.dpad_up) {
                    vision.resumeStreaming();

                }

                // Share the CPU.
                sleep(20);
            }
        }

    }

    public void initScanner() {
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

    }

}
// Camera vision :0