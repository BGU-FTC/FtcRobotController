package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp

@TeleOp(name="PRESS THIS K THX", group="Tests")
class SuperEpicOpMode : LinearOpMode() {
    override fun runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();
    }
}