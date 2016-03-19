package com.example.kdao.lab1_cmpe235;

import android.net.Uri;
import android.preference.Preference;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.*;
import org.apache.http.entity.*;
import org.apache.http.client.*;
import org.apache.http.client.methods.*;

import java.io.*;

import com.example.kdao.lab1_cmpe235.util.Utility;
import com.example.kdao.lab1_cmpe235.util.Config;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

public class SigninActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    TextView errMsg;
    EditText emailText;
    EditText pwdText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        getElem();
    }

    /**
     * Private method to get elemnt on page
     */
    private void getElem() {
        errMsg = (TextView) findViewById(R.id.login_error);
        emailText = (EditText) findViewById(R.id.loginEmail);
        pwdText = (EditText) findViewById(R.id.loginPassword);
        // Instantiate Progress Dialog object
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
    }

    /**
     * Method trigger when click on login user
     *
     * @method loginUser
     */
    public void loginUser(View view) {
        String email = emailText.getText().toString();
        String password = pwdText.getText().toString();
        if (!Utility.isEmptyString(email) && !Utility.isEmptyString(password)) {
            // When Email entered is Valid
            if (Utility.isEmailValid(email)) {
                logUserIn(email, password);
            } else { // When Email is invalid
                Toast.makeText(getApplicationContext(), "Please enter valid email", Toast.LENGTH_LONG).show();
            }
        } else { //Empty form handle
            Toast.makeText(getApplicationContext(), "Please fill in the form", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Private function for user login
     *
     * @method logUserIn
     */
    private void logUserIn(String email, String password) {
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                String userEmail = params[0];
                String userPassword = params[1];
                HttpClient httpClient = new DefaultHttpClient();
                System.out.println("String url=" + Config.BASE_URL + "/user/login");
                HttpPost httpPost = new HttpPost(Config.BASE_URL + "/user/login");
                JSONObject json = new JSONObject();
                try {
                    json.put("email", userEmail);
                    json.put("password", userPassword);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    StringEntity se = new StringEntity(json.toString());
                    System.out.println(se);
                    se.setContentEncoding("UTF-8");
                    httpPost.setEntity(se);
                    try {
                        HttpResponse httpResponse = httpClient.execute(httpPost);
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
                //System.out.println(result);
                String userId = "";
                try {
                    JSONObject jObject  = new JSONObject(result);
                    userId = (String) jObject.get("userId");
                } catch(Exception ex) {
                }
                if(!Utility.isEmptyString(userId)){
                    //Toast.makeText(getApplicationContext(), "working", Toast.LENGTH_LONG).show();
                    navigateToMainActivity();
                } else {
                    Toast.makeText(getApplicationContext(), "Invalid email or password. Please " +
                            "try again!", Toast.LENGTH_LONG).show();
                }
            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(email, password);
    }

    /**
     * Public method to navigate to signup page
     * @method navigatetoSignupActivity
     */
    public void navigatetoSignupActivity(View view) {
        Intent loginIntent = new Intent(getApplicationContext(), SignupActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(loginIntent);
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
