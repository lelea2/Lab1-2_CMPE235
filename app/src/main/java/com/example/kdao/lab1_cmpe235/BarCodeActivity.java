package com.example.kdao.lab1_cmpe235;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.kdao.lab1_cmpe235.util.Config;
import com.example.kdao.lab1_cmpe235.util.PreferenceData;
import com.example.kdao.lab1_cmpe235.util.Utility;
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

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class BarCodeActivity extends AppCompatActivity implements OnClickListener {

    static String TAG = "BarCodeActivity";

    private ImageView scanBtn;
    private TextView formatTxt;
    private TextView contentTxt;
    private TextView scanTxt;
    private String signinWithBarCode = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_code);
        try {
            Bundle extras = getIntent().getExtras();
            signinWithBarCode = extras.getString("SIGN_IN_WITH_BARCODE");
        } catch(Exception ex) {
            //catching
        }
        scanBtn = (ImageView)findViewById(R.id.scan_button);
        formatTxt = (TextView)findViewById(R.id.scan_format);
        contentTxt = (TextView)findViewById(R.id.scan_content);
        scanTxt = (TextView)findViewById(R.id.scan_text);
        if (signinWithBarCode.equals("1")) {
            scanTxt.setText("Scan barcode to log in");
        }
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
            if (signinWithBarCode.equals("1")) {
                logUserIn(scanContent);
            } else {
                Intent launchActivity = new Intent(BarCodeActivity.this, InteractActivity.class);
                Log.i(TAG, "Barcode content=" + scanContent);
                launchActivity.putExtra("SESSION_ID", scanContent);
                startActivity(launchActivity);
            }
        } else {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    /**
     * Private function to log user in
     */
    private void logUserIn(String userId) {
        scanBtn.setImageResource(R.drawable.progressbar);
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                String id = params[0];
                HttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(Config.BASE_URL + "/user/"+ id);
                try {
                    try {
                        HttpResponse httpResponse = httpClient.execute(httpGet);
                        InputStream inputStream = httpResponse.getEntity().getContent();
                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                        StringBuilder stringBuilder = new StringBuilder();
                        String bufferedStrChunk = null;
                        while((bufferedStrChunk = bufferedReader.readLine()) != null) {
                            stringBuilder.append(bufferedStrChunk);
                        }
                        return stringBuilder.toString();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception uee) {
                    System.out.println("An Exception given because of UrlEncodedFormEntity argument :" + uee);
                    uee.printStackTrace();
                }
                return null;
            }
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                String userId = "";
                try {
                    JSONObject jObject  = new JSONObject(result);
                    userId = (String) jObject.get("userId");
                } catch(Exception ex) {
                }
                if(!Utility.isEmptyString(userId)){
                    //Toast.makeText(getApplicationContext(), "working", Toast.LENGTH_LONG).show();
                    PreferenceData.setUserLoggedInStatus(getApplication(), true);
                    PreferenceData.setLoggedInUserId(getApplication(), userId);
                    navigateToMainActivity();
                } else {
                    scanBtn.setImageResource(R.drawable.scanner);
                    Toast.makeText(getApplicationContext(), "Invalid barcode. Please " +
                            "try again!", Toast.LENGTH_LONG).show();
                }
            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(userId);
    }

    /**
     * Private function to navigate to home activity
     * @method navigateToMainActivity
     */
    private void navigateToMainActivity() {
        Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mainIntent);
    }
}

