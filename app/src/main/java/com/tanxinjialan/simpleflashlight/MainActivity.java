package com.tanxinjialan.simpleflashlight;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.ToggleButton;


public class MainActivity extends AppCompatActivity {

    private android.hardware.Camera camera;
    private boolean isFlashOn;
    private boolean hasFlash;
    android.hardware.Camera.Parameters param;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // First check if device is supporting flashlight or not
        hasFlash = getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        if (!hasFlash) {
            // device doesnt support flash
            // Show alert message and close the application
            AlertDialog alert = new AlertDialog.Builder(MainActivity.this).create();
            alert.setTitle("Error");
            alert.setMessage("Sorry, your device doesn't support flash light!");
            alert.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Closing the application
                    finish();
                }
            });
            alert.show();
            return;
        }
        getCamera();
        ToggleButton flashSwitch = (ToggleButton) findViewById(R.id.flash_switch);
        flashSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)  turnOnFlash();
                else            turnOffFlash();
            }
        });
    }
    private void getCamera() {
        if (camera == null) {
            try {
                camera = android.hardware.Camera.open();
                param = camera.getParameters();
            } catch (RuntimeException e) {
                Log.e(e.getMessage(), "Camera Error. Failed to Open. Error:");
            }
        }
    }

    // Turning On Flash
    private void turnOnFlash() {
        if (!isFlashOn) {
            if (camera == null || param == null)    return;
            param = camera.getParameters();
            param.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            camera.setParameters(param);
            camera.startPreview();
            isFlashOn = true;

            Log.v("AndroidATC", "Flash has been turned on ...");

        }
    }

    // Turning Off Flash
    private void turnOffFlash() {
        if (!isFlashOn) {
            if (camera == null || param == null)    return;
            param = camera.getParameters();
            param.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(param);
            camera.stopPreview();
            isFlashOn = false;

            Log.v("AndroidATC", "Flash has been turned off ...");

        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        getCamera();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        turnOffFlash();
    }

}
