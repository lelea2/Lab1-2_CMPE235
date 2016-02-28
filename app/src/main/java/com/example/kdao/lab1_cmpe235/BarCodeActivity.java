package com.example.kdao.lab1_cmpe235;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.kdao.lab1_cmpe235.data.Tree;
import com.example.kdao.lab1_cmpe235.data.Location;
import java.util.*;

public class BarCodeActivity extends AppCompatActivity implements OnClickListener {

    static String TAG = "BarCodeActivity";

    private ImageView scanBtn;
    private TextView formatTxt;
    private TextView contentTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_code);
        scanBtn = (ImageView)findViewById(R.id.scan_button);
        formatTxt = (TextView)findViewById(R.id.scan_format);
        contentTxt = (TextView)findViewById(R.id.scan_content);
        scanBtn.setOnClickListener(this);
        handleHomeIconClick(); //handle homeicon click
    }

    /**
     * Handle function on scan button click
     * @param v
     */
    public void onClick(View v) {
        if(v.getId()== R.id.scan_button) {
            IntentIntegrator scanIntegrator = new IntentIntegrator(this);
            scanIntegrator.initiateScan();
        }
    }

    /**
     * Function handle home icon click
     * navigate back to main activity
     */
    private void handleHomeIconClick() {
        ImageView homeicon = (ImageView) findViewById(R.id.home_icon);
        homeicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Comment action");
                Intent mainView = new Intent(BarCodeActivity.this, MainActivity.class);
                startActivity(mainView);
            }
        });
    }

    /**
     * Function handle barcode scanning result
     * @param requestCode
     * @param resultCode
     * @param intent
     */
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            String scanContent = scanningResult.getContents();
            String scanFormat = scanningResult.getFormatName();
            //formatTxt.setText("FORMAT: " + scanFormat);
            //contentTxt.setText("CONTENT: " + scanContent);
            //Direct user to interact page with value of barcode
            Intent launchActivity = new Intent(BarCodeActivity.this, InteractActivity.class);
            Log.i(TAG, "Barcode content=" + scanContent);
            launchActivity.putExtra("SESSION_ID", scanContent);
            startActivity(launchActivity);
        } else {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}

