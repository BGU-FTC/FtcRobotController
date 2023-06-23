// Hey engineers ! This is the fist line of the file so I hope you read it! (I'll add some reminders around the file)
// Don't worry about most of this code, consider is magic that just works (hopefully)
// The code you want starts on line 317. I'll see you there!

package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo
import com.qualcomm.robotcore.hardware.TouchSensor
import com.qualcomm.robotcore.util.ElapsedTime
import java.net.ServerSocket
import kotlin.math.*

// ENGINEER: Anything you want to change should be here, if not contact Jack
object Constants {
	const val TURN_POWER = 1.0 // 0.0 for no turn, 1.0 for max speed turning
	val AUTO_MOVE_FORWARD_FOR = 1.0 // how long the autonomous phase will move forward for
}

object LinearSlideSystem {
    lateinit var motor: DcMotor

    fun init(hardwareMap: HardwareMap) {
        motor = hardwareMap.get("linear_slide") as DcMotor
    }

    fun set(power: Double) {
    	motor.power = power
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

object MecanumMovementSystem {
    lateinit var frontLeft: DcMotor
    lateinit var frontRight: DcMotor
    lateinit var backLeft: DcMotor
    lateinit var backRight: DcMotor

    fun init(hardwareMap: HardwareMap) {
        frontLeft = hardwareMap.get("front_left") as DcMotor
        frontLeft.direction =DcMotorSimple.Direction.REVERSE
        frontRight = hardwareMap.get("front_right") as DcMotor
        backLeft = hardwareMap.get("back_left") as DcMotor
        backLeft.direction = DcMotorSimple.Direction.REVERSE
        backRight = hardwareMap.get("back_right") as DcMotor
    }

    fun set(forwardBackward: Double, leftRight: Double, turn: Double) {
	val magnitude = sqrt(forwardBackward*forwardBackward + leftRight*leftRight)
        val theta = atan2(forwardBackward, leftRight)

	val t = turn * Constants.TURN_POWER;
	
        this.frontLeft.power = magnitude * sin(theta + (PI/4)) + t
        this.backLeft.power = magnitude * sin(theta - (PI/4)) + t
        this.frontRight.power = magnitude * sin(theta - (PI/4)) - t
        this.backRight.power = magnitude * sin(theta + (PI/4)) - t
    }
}

/*
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
        MovementSystem.init(hardwareMap)
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
            LinearSlideSystem.applyLimit()
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
*/ // Lost, Engineers? Go to line 1.
@TeleOp(name="Mecanum Test", group = "Tests")
class MecanumTest : LinearOpMode() {
    override fun runOpMode() {
        MecanumMovementSystem.init(hardwareMap)
        telemetry.addData("Status", "Initialised")
        telemetry.update()

        waitForStart()

        while (opModeIsActive()) {
            val forwardBackward = gamepad1.left_stick_y.toDouble()
            val leftRight = gamepad1.left_stick_x.toDouble()
            val turn = gamepad1.right_stick_x.toDouble()

            MecanumMovementSystem.set(forwardBackward, leftRight, turn)
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
            val right = gamepad1.right_trigger.toDouble() // Lost, Engineers? Go to line 1.

            LinearSlideSystem.set(left - right)
            telemetry.addData("Slider Power", LinearSlideSystem.motor.power)
            telemetry.update()
        }
    }
}

// Hi again! the autonomous code is bellow, again theres more magic, so Ill direct you

@Autonomous(name="Autonomous test", group="Tests")
class AutonomousTestMode: LinearOpMode() {
    override fun runOpMode() {
        MecanumMovementSystem.init(hardwareMap);

        waitForStart()

        val time = ElapsedTime(); // This is the time that its been running for, don't touch this line please, or anything above it :)

	while (opModeIsActive()) {
		if (time.seconds() < Constants.AUTO_MOVE_FORWARD_FOR) {
			MecanumMovementSystem.set(1.0, 0.0, 0.0)
		} else {
			MecanumMovementSystem.set(0.0, 0.0, 0.0)
		}
	}
    }
}
// You've scrolled too far!
