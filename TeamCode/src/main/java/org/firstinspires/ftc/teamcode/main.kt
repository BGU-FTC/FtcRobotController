package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo
import com.qualcomm.robotcore.hardware.TouchSensor
import com.qualcomm.robotcore.util.ElapsedTime
import java.net.ServerSocket

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
    lateinit var left: DcMotor
    lateinit var right: DcMotor

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

object LinearSlideSystem {
    lateinit var motor: DcMotor
    private lateinit var limit: TouchSensor
    fun init(hardwareMap: HardwareMap) {
        motor = hardwareMap.get("linear_slide") as DcMotor
        limit = hardwareMap.get("limit") as TouchSensor
    }
    fun applylimit (){
        if (limit.isPressed)
        {
            motor.power = -1.0
        }

    }

    fun set(power: Double) {
        if (!limit.isPressed){
            motor.power = power
        }

    }
}

object GrabberSystem {
    lateinit var servo: Servo

    fun init(hardwareMap: HardwareMap) {
        servo = hardwareMap.get("grabber") as Servo
    }

    fun open() {
        servo.position = 0.0
    }

    fun close() {
        servo.position = 1.0
    }

    fun toggle() {
        servo.position = 1.0 - servo.position
    }
}

@TeleOp(name="Motor assignment and direction test", group="Tests")
class MotorTestMode: LinearOpMode() {
    override fun runOpMode() {
        MovementSystem.init(hardwareMap)

        telemetry.addData("Status", "Initialised")
        telemetry.update()
        waitForStart()

        while (opModeIsActive()) {
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

@TeleOp(name="Motor control test", group="Tests")
class MotorControlTestMode: LinearOpMode() {
    override fun runOpMode() {
        MovementSystem.init(hardwareMap)

        telemetry.addData("Status", "Initialised")
        telemetry.update()
        waitForStart()

        while (opModeIsActive()) {
            val drive = gamepad1.left_stick_y
            val turn = gamepad1.right_stick_x

            val leftPower = (drive - turn)*3/4;
            val rightPower = (drive + turn)*3/4

            MovementSystem.setPower(leftPower.toDouble(), rightPower.toDouble())

            telemetry.addData("Left Power:", leftPower)
            telemetry.addData("Right Power:", rightPower)
            telemetry.update();
        }
    }
}

@TeleOp(name="Linear slide test", group="Tests")
class LinearSlideControlTestMode: LinearOpMode() {
    override fun runOpMode() {
        LinearSlideSystem.init(hardwareMap)

        telemetry.addData("Status", "Initialised")
        telemetry.update()
        waitForStart()

        while (opModeIsActive()) {
            val left = gamepad1.left_trigger.toDouble()
            val right = gamepad1.right_trigger.toDouble()

            LinearSlideSystem.set(left - right)
            LinearSlideSystem.applylimit()
            telemetry.addData("Slider Power", LinearSlideSystem.motor.power)
            telemetry.update()
        }
    }
}

@TeleOp(name="Grabber servo test", group="Tests")
class GrabberServerTest: LinearOpMode() {
    override fun runOpMode() {
        GrabberSystem.init(hardwareMap)

        GrabberSystem.open()

        telemetry.addData("Status", "Initialised")
        telemetry.update()
        waitForStart()
        telemetry.addData("Status", "Running")
        telemetry.update()
        val lastToggle = ElapsedTime()

        while (opModeIsActive()) {
            if (gamepad1.x && lastToggle.seconds() > 0.5) {
                GrabberSystem.toggle()
                telemetry.addData("Servo Pos", GrabberSystem.servo.position)
                lastToggle.reset()
            }
            telemetry.update()
        }
    }
}

@TeleOp(name="Admit that the cyber students did something", group="Tests")//Admit that the cyber students did something
class FullImplementation: LinearOpMode() {
    override fun runOpMode() {
        GrabberSystem.init(hardwareMap)
        MovementSystem.init((hardwareMap))
        GrabberSystem.open()
        LinearSlideSystem.init(hardwareMap)
        telemetry.addData("Status", "Initialised")
        telemetry.update()
        waitForStart()
        telemetry.addData("Status", "Running")
        val lastToggle = ElapsedTime()
        while (opModeIsActive()) {
            val drive = gamepad1.left_stick_y
            val turn = gamepad1.right_stick_x

            val leftPower = (drive - turn)/2;
            val rightPower = (drive + turn)/2

            val left = gamepad1.left_trigger.toDouble()
            val right = gamepad1.right_trigger.toDouble()

            LinearSlideSystem.set(left - right)
            LinearSlideSystem.applylimit()
            MovementSystem.setPower(leftPower.toDouble(), rightPower.toDouble())

            if (gamepad1.x && lastToggle.seconds() > 0.5) {
                GrabberSystem.toggle()
                lastToggle.reset()
            }
            telemetry.addData("Slider Power", LinearSlideSystem.motor.power)
            telemetry.addData("Left Power:", leftPower)
            telemetry.addData("Right Power:", rightPower)
            telemetry.addData("Servo Pos", GrabberSystem.servo.position)
            telemetry.update()
        }
    }
}