package com.example.kdao.lab1_cmpe235;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.widget.Button;
import android.content.Intent;
import android.view.View;

import java.util.logging.SocketHandler;

public class MainActivity extends AppCompatActivity {

    static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Handle icon click on main activity
        aboutClick(); //click on about icon
        interactClick(); //click on interact icon
        sharingClick(); //click on sharing icon
        nearByClick(); //click on nearby icon
        cameraClick(); //click on camera icon
        commentClick(); //click on comment icon
    }

    /**
     * Handle direct to barcode activity
     */
    private void aboutClick() {
        Button btnAbout = (Button) findViewById(R.id.button_about);
        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Barcode action");
                Intent barcodeView = new Intent(MainActivity.this, BarCodeActivity.class);
                startActivity(barcodeView);
            }
        });
    }

    /**
     * Handle direct to interact activity page
     */
    private void interactClick() {
        Button btnInteract = (Button) findViewById(R.id.button_interact);
        btnInteract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Interact action");
                Intent interactView = new Intent(MainActivity.this, InteractActivity.class);
                startActivity(interactView);
            }
        });
    }

    /**
     * Handle direct to sharing activity
     */
    private void sharingClick() {
        Button btnSharing = (Button) findViewById(R.id.button_sharing);
        btnSharing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Sharing action");
                Intent sharingView = new Intent(MainActivity.this, SharingActivity.class);
                startActivity(sharingView);
            }
        });
    }

    /**
     * Handle direct to map activity
     */
    private void nearByClick() {
        Button btnNearBy = (Button) findViewById(R.id.button_nearby);
        btnNearBy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Nearby action");
                Intent nearbyView = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(nearbyView);
            }
        });
    }

    /**
     * Handle direct to camera activity
     */
    private void cameraClick() {
        Button btnCamera = (Button) findViewById(R.id.button_camera);
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Camera action");
                Intent cameraView = new Intent(MainActivity.this, CameraActivity.class);
                startActivity(cameraView);
            }
        });
    }

    /**
     * Handle direct to comment activity
     */
    private void commentClick() {

    }
}
