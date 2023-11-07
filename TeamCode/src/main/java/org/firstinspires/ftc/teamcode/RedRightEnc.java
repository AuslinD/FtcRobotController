package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;

@Autonomous(name = "RedRightEnc", group = "Encoder Auto")
public class RedRightEnc extends LinearOpMode {
    EncoderAutoMethods movement;
    Robot robot;
    OpenCvInternalCamera phoneCam;
    OpenCV.RedCV pipeline;
    @Override
    public void runOpMode() throws InterruptedException {

        movement = new EncoderAutoMethods(this);
            /**
             * NOTE: Many comments have been omitted from this sample for the
             * sake of conciseness. If you're just starting out with EasyOpenCv,
             * you should take a look at {@link InternalCamera1Example} or its
             * webcam counterpart, {@link WebcamExample} first.
             */
            int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
            phoneCam = OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);
            pipeline = new OpenCV.RedCV();
            phoneCam.setPipeline(pipeline);

            // We set the viewport policy to optimized view so the preview doesn't appear 90 deg
            // out when the RC activity is in portrait. We do our actual image processing assuming
            // landscape orientation, though.
            phoneCam.setViewportRenderingPolicy(OpenCvCamera.ViewportRenderingPolicy.OPTIMIZE_VIEW);

            phoneCam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
            {
                @Override
                public void onOpened()
                {
                    phoneCam.startStreaming(320,240, OpenCvCameraRotation.SIDEWAYS_LEFT);
                }

                @Override
                public void onError(int errorCode)
                {
                    telemetry.addLine("No camera");
                }
            });

            while (!isStarted() && !isStopRequested())
            {
                telemetry.addData("Analysis", pipeline.getAnalysis());
                telemetry.update();

                // Don't burn CPU cycles busy-looping in this sample
                sleep(50);
            }
            OpenCV.RedCV.SkystonePosition pos = pipeline.getAnalysis();
        movement.drive(5, 3);
        switch(pipeline.getAnalysis()) {
            case LEFT:
                movement.turn(90, 3);
                //move pixel
                movement.turn(-90, 3);
                break;
            case RIGHT:
                movement.turn(-90, 3);
                //move pixel
                movement.drive(90, 3);

                break;
            case CENTER:
                //move pixel

                break;
            default:
                //move pixel
                //sleep(0);
        }
        movement.drive(-3, 5);
        movement.sideToSide(-4, 3);
            //movement.turn(90, 5);
            //movement.sideToSide(5,5);
    }
}
