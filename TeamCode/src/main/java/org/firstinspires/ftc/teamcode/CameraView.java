package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;

@TeleOp
public class CameraView extends LinearOpMode {
    // Creating the camera
    WebcamName camera;

    // Creating camera feed
    VisionPortal vision;

    public void runOpMode() {
        camera = hardwareMap.get(WebcamName.class, "Webcam1");

        vision = new VisionPortal.Builder()
                .setCamera(camera)
                .addProcessor(aprilTagProcessor)
                .build();

        vision.setActiveCamera(camera);

    }

}