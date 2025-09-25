package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.teamcode.systems.CameraView;
import org.firstinpires.ftc.teamcode.systems.MecanumDriveSystem;

@TeleOp(name="OpModeRunner", group="Launcher")
public class mainOp extends LinearOpMode {

    // Creating names for the classes in /systems
    CameraView c1 = new CameraView();
    MecanumDriveSystem c2 = new MecanumDriveSystem();


    // IDK abt this, so need to do more research
    c1();
    c2();

}