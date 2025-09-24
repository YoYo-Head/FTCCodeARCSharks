package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

@TeleOp(name="Camera", group="Camera System")
public class CameraView extends LinearOpMode {
    // Creating the camera
    WebcamName camera;

    // Creating camera feed
    VisionPortal vision;

    // Creating April Tag processor
    AprilTagProcessor aprilTag;

    @Override
    public void runOpMode() {
        // Activates function which defines and starts camera
        initScanner();

        if (opModeIsActive()) {
            telemetry.addLine("Camera stream starting...");
            while (opModeIsActive()) {
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
        // Stops the stream
        vision.close();
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
                .build();

        // Sets active camera (idk if useless so might remove later)
        vision.setActiveCamera(camera);

    }

}
// Camera vision :0