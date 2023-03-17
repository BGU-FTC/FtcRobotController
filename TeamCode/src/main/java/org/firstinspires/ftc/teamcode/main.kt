package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap

//val VUFORIA_KEY = "AekkZQf/////AAABmU/INTYknkiSokr0deyoE7tDO9U4n4OpK1sB67xojuUmkCjdRobDwLQmdRlNk/s8EUdYf1XlTIpkDruJVSbhm6r/LAMLjU4C4ntVOYp7stg+xAG4aoc8SaLEP4Dk+L3oDUGhPtWJWS8dB0z7XRd3ku4jDBvboBDPzR3PMgGWjedD72rr4FGk9fsuQQmbln+pHhx26g2HBttXuSBKy3vaOEuZeqKqIMA28GiPqnUflXn8rnWwWLMdcJZmMCZ7LKvZ6P7c2XtrWDTerpbCvUohB6Zpic+CoF5CjLfm5YroaZ0Rtwq6vzqm8EIJkoqgbrURWN59050Vcb7mS3oXy34PfH67BjtlihQQYv+oSbiBSY22"
//
//@TeleOp(name="PRESS THIS K THX", group="Tests")
//class SuperEpicOpMode : LinearOpMode() {
//    override fun runOpMode() {
//        val time = ElapsedTime();
//
//        val webcam = hardwareMap.get("Webcam 1") as CameraName;
//        val cameraMonitorViewID = hardwareMap.appContext.resources.getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.packageName);
//        val vuforiaParameters = VuforiaLocalizer.Parameters(cameraMonitorViewID);
//        vuforiaParameters.vuforiaLicenseKey = VUFORIA_KEY;
//        vuforiaParameters.cameraName = webcam;
//        vuforiaParameters.useExtendedTracking = false;
//        val vuforia = ClassFactory.getInstance().createVuforia(vuforiaParameters);
//        val targets = vuforia.loadTrackablesFromAsset("PowerPlay");
//
//        // TODO: Finish Kotlinification of ConceptVuforiaFieldNavigationWebcam.java
//
//        waitForStart();
//        time.startTime()
//        telemetry.addData("Status", "Initialized");
//        telemetry.update();
//
//        val servo: Servo = hardwareMap.get("test") as Servo;
//
//        var position = 0.0;
//        var delta = 0.001;
//
//        while (opModeIsActive()) {
//            telemetry.addData("Time", time.seconds());
//            telemetry.addData("Servo", servo.position);
//            telemetry.addData("Position", position);
//            telemetry.addData("Delta", delta);
//            telemetry.update();
//            servo.position = position;
//            position += delta
//            if (position > 1) {
//                delta *= -1
//                position = 1.0
//            }
//
//            if (position < -1) {
//                delta *= -1
//                position = -1.0;
//            }
//        }
//    }
//}

object MovementSystem {
    private lateinit var left: DcMotor
    private lateinit var right: DcMotor

    fun init(hardwareMap: HardwareMap) {
        left = hardwareMap.get("left_drive") as DcMotor;
        right = hardwareMap.get("right_drive") as DcMotor

        right.direction = DcMotorSimple.Direction.REVERSE
    }

    fun setPower(leftPower: Double, rightPower: Double) {
        left.power = leftPower.coerceIn(-1.0, 1.0)
        right.power = rightPower.coerceIn(-1.0, 1.0)
    }
}

@TeleOp(name="Motor assignment and direction test", group="Tests")
class MotorTestMode: LinearOpMode() {
    override fun runOpMode() {
        MovementSystem.init(hardwareMap)

        telemetry.addData("Status", "Initialised")
        telemetry.update()
        waitForStart()

        while (true) {
            telemetry.addData("Forward", "")

            telemetry.update()
            MovementSystem.setPower(1.0, 1.0)
            sleep(3000)

            telemetry.addData("Backward", "")
            telemetry.update()
            MovementSystem.setPower(-1.0, -1.0)
            sleep(3000)

            telemetry.addData("Twist Left", "")
            telemetry.update()
            MovementSystem.setPower(-1.0, 1.0)
            sleep(3000)

            telemetry.addData("Twist Right", "")
            telemetry.update()
            MovementSystem.setPower(1.0, -1.0)
            sleep(3000)
        }
    }
}

fun lerp(min: Double, max: Double, t: Double): Double {
    return min + ((max - min) * t)
}

fun thetaToPower(theta: Double): Double {
    when (theta) {
        in 0.0..90.0 -> {
            return 1.0
        }
        in 90.0..135.0 -> {
            val t = (theta - 90.0) / 45.0;
            return lerp(1.0, 0.0, t)
        }
        in 135.0..180.0 -> {
            val t = (theta - 135.0) / 45.0
            return lerp(0.0, -1.0, t)
        }
        in 180.0..225.0 -> {
            val t = (theta - 180.0) / 45.0
            return lerp(-1.0, 0.0, t)
        }
        in 225.0..270.0 -> {
            val t = (theta - 225.0) / 45.0
            return lerp(0.0, -1.0, t)
        }
        in 270.0..315.0 -> {
            val t = (theta - 270.0) / 45.0
            return lerp(-1.0, 0.0, t)
        }
        else -> {
            val t = (theta - 315.0) / 45.0
            return lerp(0.0, 1.0, t)
        }
    }
}

@TeleOp(name="Motor control test", group="Tests")
class MotorControlTestMode: LinearOpMode() {
    override fun runOpMode() {
        MovementSystem.init(hardwareMap);

        telemetry.addData("Status", "Initialised")
        telemetry.update()
        waitForStart()

        while (true) {
            val drive = gamepad1.left_stick_y;
            val turn = gamepad1.right_stick_x

            val leftPower = drive - turn;
            val rightPower = drive + turn

            MovementSystem.setPower(leftPower.toDouble(), rightPower.toDouble())

            telemetry.addData("Left Power:", leftPower)
            telemetry.addData("Right Power:", rightPower)
            telemetry.update();
        }
    }
}