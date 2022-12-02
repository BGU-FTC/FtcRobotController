package org.firstinspires.ftc.teamcode

import android.content.Context
import android.hardware.SensorManager
import androidx.core.content.ContextCompat.getSystemService
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.Servo
import com.qualcomm.robotcore.util.ElapsedTime

@TeleOp(name="PRESS THIS K THX", group="Tests")
class SuperEpicOpMode : LinearOpMode() {
    override fun runOpMode() {
        val time = ElapsedTime();
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