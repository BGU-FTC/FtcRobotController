package org.firstinspires.ftc.teamcode

import android.content.Context
import android.hardware.SensorManager
import androidx.core.content.ContextCompat.getSystemService
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.Servo
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.robotcore.external.Telemetry


@TeleOp(name="Controller Tester", group="Tests")
class ControllerOpMode : LinearOpMode() {
    override fun runOpMode() {
        val time = ElapsedTime();
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        var rawNumber = 0
        var actionNumber = 0
        var buttonNumber = 0

        val caller = Controller()
        caller.actions.add(Action {rawNumber++})
        caller.actions.add(Action { if(time.seconds().toInt()%10==0)actionNumber++ })
        fun addOne(){
            buttonNumber++
        }
        caller.actions.add(ButtonAction({addOne()},{time.seconds().toInt()%10==0}))
        waitForStart()
        time.startTime()

        while (opModeIsActive()) {
            telemetry.addData("Time", time.seconds())
            telemetry.addData("raw",rawNumber)
            telemetry.addData("action",actionNumber)
            telemetry.addData("button",buttonNumber)
            telemetry.update();
        }
    }
}
open class Action(val function: ()->Any) {
    open fun update() {
        this.function()
    }
}

class ButtonAction(function: ()->Any,val trigger: () -> Boolean) : Action(function) {
    var called = false
    override fun update() {
        if (trigger()) {
            if (!called) {
                function()
                called = true
            }
        } else
            called = false
    }
}
class Controller{
    public val actions = mutableListOf<Action>()
    fun step(){
        for (action in actions){
            action.update()
        }
    }
}


