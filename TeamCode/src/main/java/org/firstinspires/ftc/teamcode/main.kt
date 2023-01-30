package org.firstinspires.ftc.teamcode

import android.content.Context
import android.hardware.SensorManager
import androidx.core.content.ContextCompat.getSystemService
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.Servo
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.robotcore.external.ClassFactory
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer

val VUFORIA_KEY = "AekkZQf/////AAABmU/INTYknkiSokr0deyoE7tDO9U4n4OpK1sB67xojuUmkCjdRobDwLQmdRlNk/s8EUdYf1XlTIpkDruJVSbhm6r/LAMLjU4C4ntVOYp7stg+xAG4aoc8SaLEP4Dk+L3oDUGhPtWJWS8dB0z7XRd3ku4jDBvboBDPzR3PMgGWjedD72rr4FGk9fsuQQmbln+pHhx26g2HBttXuSBKy3vaOEuZeqKqIMA28GiPqnUflXn8rnWwWLMdcJZmMCZ7LKvZ6P7c2XtrWDTerpbCvUohB6Zpic+CoF5CjLfm5YroaZ0Rtwq6vzqm8EIJkoqgbrURWN59050Vcb7mS3oXy34PfH67BjtlihQQYv+oSbiBSY22"

@TeleOp(name="PRESS THIS K THX", group="Tests")
class SuperEpicOpMode : LinearOpMode() {
    override fun runOpMode() {
        val time = ElapsedTime();

        val webcam = hardwareMap.get("Webcam 1") as CameraName;
        val cameraMonitorViewID = hardwareMap.appContext.resources.getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.packageName);
        val vuforiaParameters = VuforiaLocalizer.Parameters(cameraMonitorViewID);
        vuforiaParameters.vuforiaLicenseKey = VUFORIA_KEY;
        vuforiaParameters.cameraName = webcam;
        vuforiaParameters.useExtendedTracking = false;
        val vuforia = ClassFactory.getInstance().createVuforia(vuforiaParameters);
        val targets = vuforia.loadTrackablesFromAsset("PowerPlay");

        // TODO: Finish Kotlinification of ConceptVuforiaFieldNavigationWebcam.java

        waitForStart();
        time.startTime()
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        val servo: Servo = hardwareMap.get("test") as Servo;

        var position = 0.0;
        var delta = 0.001;

        while (opModeIsActive()) {
            telemetry.addData("Time", time.seconds());
            telemetry.addData("Servo", servo.position);
            telemetry.addData("Position", position);
            telemetry.addData("Delta", delta);
            telemetry.update();
            servo.position = position;
            position += delta
            if (position > 1) {
                delta *= -1
                position = 1.0
            }

            if (position < -1) {
                delta *= -1
                position = -1.0;
            }
        }
    }
}