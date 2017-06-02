package com.tanxinjialan.simpleflashlight;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.ToggleButton;


public class MainActivity extends AppCompatActivity {

    private CameraManager cameraManager;
    private String cameraId;
    private boolean isFlashOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // First check if device is supporting flashlight or not
        boolean hasFlash = getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        // Check for device with or without Flash function
        if (!hasFlash) {
            // If device doesn't support flash
            // Show alert message and close the application
            AlertDialog alert = new AlertDialog.Builder(MainActivity.this).create();
            alert.setTitle("Error");
            alert.setMessage("Sorry, your device doesn't support flash light!");
            alert.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Closing the application
                    finish();
                    System.exit(0);
                }
            });
            alert.show();
            return;
        }
        //

        getCamera();

        ToggleButton flashSwitch = (ToggleButton) findViewById(R.id.flash_switch);

        flashSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    turnOnFlash();
                    isFlashOn = true;
                } else {
                    turnOffFlash();
                    isFlashOn = false;
                }
            }
        });
    }

    // Set up Camera Manager
    private void getCamera() {
        cameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);
        try {
            cameraId = cameraManager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    // Turning On Flash
    private void turnOnFlash() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                cameraManager.setTorchMode(cameraId, true);
            } else Log.w("Stage", "Call requires API level 23");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("Stage", "Flash has been turned on ...");
    }

    // Turning Off Flash
    private void turnOffFlash() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                cameraManager.setTorchMode(cameraId, false);
            } else Log.w("Stage", "Call requires API level 23");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("Stage", "Flash has been turned off ...");
    }


    @Override
    protected void onStart() {
        super.onStart();
        getCamera();
        Log.i("Stage", "Start App");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isFlashOn) turnOnFlash();
        else turnOffFlash();
        Log.i("Stage", "Resume program");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("Stage", "Stop App");
    }

    @Override
    protected void onPause() {
        super.onPause();
        turnOffFlash();
        Log.i("Stage", "Pause Program");
    }

}
