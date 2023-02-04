package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.util.ElapsedTime
import org.checkerframework.checker.nullness.qual.Nullable
import org.firstinspires.ftc.robotcontroller.external.samples.ConceptVuforiaFieldNavigationWebcam
import org.firstinspires.ftc.robotcore.external.ClassFactory
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix
import org.firstinspires.ftc.robotcore.external.matrices.VectorF
import org.firstinspires.ftc.robotcore.external.navigation.*

val VUFORIA_KEY = "AekkZQf/////AAABmU/INTYknkiSokr0deyoE7tDO9U4n4OpK1sB67xojuUmkCjdRobDwLQmdRlNk/s8EUdYf1XlTIpkDruJVSbhm6r/LAMLjU4C4ntVOYp7stg+xAG4aoc8SaLEP4Dk+L3oDUGhPtWJWS8dB0z7XRd3ku4jDBvboBDPzR3PMgGWjedD72rr4FGk9fsuQQmbln+pHhx26g2HBttXuSBKy3vaOEuZeqKqIMA28GiPqnUflXn8rnWwWLMdcJZmMCZ7LKvZ6P7c2XtrWDTerpbCvUohB6Zpic+CoF5CjLfm5YroaZ0Rtwq6vzqm8EIJkoqgbrURWN59050Vcb7mS3oXy34PfH67BjtlihQQYv+oSbiBSY22"

@TeleOp(name="PRESS THIS K THX", group="Tests")
class SuperEpicOpMode : LinearOpMode() {
    override fun runOpMode() {
        val time = ElapsedTime();
        val mmPerInch = 25.4f
        val mmTargetHeight =
            6 * mmPerInch // the height of the center of the target image above the floor
        val halfField = 72 * mmPerInch
        val halfTile = 12 * mmPerInch
        val oneAndHalfTile = 36 * mmPerInch
        var targetVisible = false
        var lastLocation: OpenGLMatrix? = null

        val webcam = hardwareMap.get("Webcam 1") as CameraName;
        val cameraMonitorViewID = hardwareMap.appContext.resources.getIdentifier(
            "cameraMonitorViewId",
            "id",
            hardwareMap.appContext.packageName
        );
        val vuforiaParameters = VuforiaLocalizer.Parameters(cameraMonitorViewID);
        vuforiaParameters.vuforiaLicenseKey = VUFORIA_KEY;
        vuforiaParameters.cameraName = webcam;
        vuforiaParameters.useExtendedTracking = false;
        val vuforia = ClassFactory.getInstance().createVuforia(vuforiaParameters);
        val targets = vuforia.loadTrackablesFromAsset("PowerPlay");
        // For convenience, gather together all the trackable objects in one easily-iterable collection */

        // For convenience, gather together all the trackable objects in one easily-iterable collection */
        val allTrackables: MutableList<VuforiaTrackable> = ArrayList()
        allTrackables.addAll(targets)
        fun identifyTarget(
            targetIndex: Int,
            targetName: String?,
            dx: Float,
            dy: Float,
            dz: Float,
            rx: Float,
            ry: Float,
            rz: Float
        ) {
            val aTarget: VuforiaTrackable = targets.get(targetIndex)
            aTarget.name = targetName
            aTarget.location = OpenGLMatrix.translation(dx, dy, dz)
                .multiplied(
                    Orientation.getRotationMatrix(
                        AxesReference.EXTRINSIC,
                        AxesOrder.XYZ,
                        AngleUnit.DEGREES,
                        rx,
                        ry,
                        rz
                    )
                )
        }
        identifyTarget(
            0,
            "Red Audience Wall",
            -halfField,
            -oneAndHalfTile,
            mmTargetHeight,
            90f,
            0f,
            90f
        )
        identifyTarget(
            1,
            "Red Rear Wall",
            halfField,
            -oneAndHalfTile,
            mmTargetHeight,
            90f,
            0f,
            -90f
        )
        identifyTarget(
            2,
            "Blue Audience Wall",
            -halfField,
            oneAndHalfTile,
            mmTargetHeight,
            90f,
            0f,
            90f
        )
        identifyTarget(
            3,
            "Blue Rear Wall",
            halfField,
            oneAndHalfTile,
            mmTargetHeight,
            90f,
            0f,
            -90f
        )

        /*
         * Create a transformation matrix describing where the camera is on the robot.
         *
         * Info:  The coordinate frame for the robot looks the same as the field.
         * The robot's "forward" direction is facing out along X axis, with the LEFT side facing out along the Y axis.
         * Z is UP on the robot.  This equates to a bearing angle of Zero degrees.
         *
         * For a WebCam, the default starting orientation of the camera is looking UP (pointing in the Z direction),
         * with the wide (horizontal) axis of the camera aligned with the X axis, and
         * the Narrow (vertical) axis of the camera aligned with the Y axis
         *
         * But, this example assumes that the camera is actually facing forward out the front of the robot.
         * So, the "default" camera position requires two rotations to get it oriented correctly.
         * 1) First it must be rotated +90 degrees around the X axis to get it horizontal (its now facing out the right side of the robot)
         * 2) Next it must be be rotated +90 degrees (counter-clockwise) around the Z axis to face forward.
         *
         * Finally the camera can be translated to its actual mounting position on the robot.
         *      In this example, it is centered on the robot (left-to-right and front-to-back), and 6 inches above ground level.
         */
        val CAMERA_FORWARD_DISPLACEMENT =
            0.0f * mmPerInch // eg: Enter the forward distance from the center of the robot to the camera lens

        val CAMERA_VERTICAL_DISPLACEMENT =
            6.0f * mmPerInch // eg: Camera is 6 Inches above ground

        val CAMERA_LEFT_DISPLACEMENT =
            0.0f * mmPerInch // eg: Enter the left distance from the center of the robot to the camera lens

        val cameraLocationOnRobot = OpenGLMatrix
            .translation(
                CAMERA_FORWARD_DISPLACEMENT,
                CAMERA_LEFT_DISPLACEMENT,
                CAMERA_VERTICAL_DISPLACEMENT
            )
            .multiplied(
                Orientation.getRotationMatrix(
                    AxesReference.EXTRINSIC,
                    AxesOrder.XZY,
                    AngleUnit.DEGREES,
                    90f,
                    90f,
                    0f
                )
            )
        /*  Let all the trackable listeners know where the camera is.  */

        /*  Let all the trackable listeners know where the camera is.  */for (trackable in allTrackables) {
            (trackable.listener as VuforiaTrackableDefaultListener).setCameraLocationOnRobot(
                webcam,
                cameraLocationOnRobot
            )
        }

        /*
         * WARNING:
         * In this sample, we do not wait for PLAY to be pressed.  Target Tracking is started immediately when INIT is pressed.
         * This sequence is used to enable the new remote DS Camera Preview feature to be used with this sample.
         * CONSEQUENTLY do not put any driving commands in this loop.
         * To restore the normal opmode structure, just un-comment the following line:
         */

        // waitForStart();

        /* Note: To use the remote camera preview:
         * AFTER you hit Init on the Driver Station, use the "options menu" to select "Camera Stream"
         * Tap the preview window to receive a fresh image.
         * It is not permitted to transition to RUN while the camera preview window is active.
         * Either press STOP to exit the OpMode, or use the "options menu" again, and select "Camera Stream" to close the preview window.
         */targets.activate()
        while (!isStopRequested) {

            // check all the trackable targets to see which one (if any) is visible.
            targetVisible = false
            for (trackable in allTrackables) {
                if ((trackable.listener as VuforiaTrackableDefaultListener).isVisible) {
                    telemetry.addData("Visible Target", trackable.name)
                    targetVisible = true

                    // getUpdatedRobotLocation() will return null if no new information is available since
                    // the last time that call was made, or if the trackable is not currently visible.
                    val robotLocationTransform =
                        (trackable.listener as VuforiaTrackableDefaultListener).updatedRobotLocation
                    if (robotLocationTransform != null) {
                        lastLocation = robotLocationTransform
                    }
                    break
                }
            }

            // Provide feedback as to where the robot is located (if we know).
            if (targetVisible) {
                // express position (translation) of robot in inches.
                val translation: VectorF = lastLocation!!.getTranslation()
                telemetry.addData(
                    "Pos (inches)",
                    "{X, Y, Z} = %.1f, %.1f, %.1f",
                    translation[0] / mmPerInch,
                    translation[1] / mmPerInch,
                    translation[2] / mmPerInch
                )

                // express the rotation of the robot in degrees.
                val rotation = Orientation.getOrientation(
                    lastLocation,
                    AxesReference.EXTRINSIC,
                    AxesOrder.XYZ,
                    AngleUnit.DEGREES
                )
                telemetry.addData(
                    "Rot (deg)",
                    "{Roll, Pitch, Heading} = %.0f, %.0f, %.0f",
                    rotation.firstAngle,
                    rotation.secondAngle,
                    rotation.thirdAngle
                )
            } else {
                telemetry.addData("Visible Target", "none")
            }
            telemetry.update()
        }

        // Disable Tracking when we are done;

        // Disable Tracking when we are done;
        targets.deactivate()
    }
}