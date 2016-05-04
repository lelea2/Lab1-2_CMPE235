package com.example.kdao.lab1_cmpe235;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.ProgressDialog;
import android.widget.TextView;
import android.widget.EditText;
import android.view.View;
import android.widget.Toast;

import com.example.kdao.lab1_cmpe235.util.Config;
import com.example.kdao.lab1_cmpe235.util.Utility;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.example.kdao.lab1_cmpe235.util.PreferenceData;

public class SignupActivity extends AppCompatActivity {
    ProgressDialog prgDialog;
    TextView errMsg;
    EditText nameInput;
    EditText emailInput;
    EditText phoneInput;
    EditText pwdInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getElem();
    }

    /**
     * Private function to get method
     */
    private void getElem() {
        errMsg = (TextView)findViewById(R.id.register_error);
        nameInput = (EditText)findViewById(R.id.registerName);
        emailInput = (EditText)findViewById(R.id.registerEmail);
        phoneInput = (EditText)findViewById(R.id.registerPhone);
        pwdInput = (EditText)findViewById(R.id.registerPassword);
        //Set up progress dialog
        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("Please wait...");
        prgDialog.setCancelable(false);
    }

    /**
     * Method trigger when click on register user
     *
     * @method registerUser
     */
    public void registerUser(View view) {
        String email = emailInput.getText().toString();
        String password = pwdInput.getText().toString();
        String name = nameInput.getText().toString();
        String phone = phoneInput.getText().toString();
        if (!Utility.isEmptyString(email) && !Utility.isEmptyString(password) && !Utility.isEmptyString(name) && !Utility.isEmptyString(phone)) {
            // When Email entered is Valid
            if (Utility.isEmailValid(email)) {
                signupUser(email, password, name, phone);
            } else { // When Email is invalid
                Toast.makeText(getApplicationContext(), "Please enter valid email", Toast.LENGTH_LONG).show();}

        } else { //Handle empty form
            Toast.makeText(getApplicationContext(), "Please fill in the form", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Private method for signup user
     * @param email
     * @param password
     * @param name
     * @param phone
     */
    private void signupUser(String email, String password, String name, String phone) {
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                String userEmail = params[0];
                String userPassword = params[1];
                String name = params[2];
                String phone = params[3];
                HttpClient httpClient = new DefaultHttpClient();
                HttpPut httpPut = new HttpPut(Config.BASE_URL + "/user/register");
                JSONObject json = new JSONObject();
                try {
                    json.put("email", userEmail);
                    json.put("password", userPassword);
                    json.put("firstName", name);
                    json.put("lastName", name);
                    json.put("phoneNum", phone);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    StringEntity se = new StringEntity(json.toString());
                    System.out.println(se);
                    se.setContentEncoding("UTF-8");
                    httpPut.setEntity(se);
                    try {
                        HttpResponse httpResponse = httpClient.execute(httpPut);
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
                if(!Utility.isEmptyString(userId)) {
                    PreferenceData.setUserLoggedInStatus(getApplication(), true);
                    PreferenceData.setLoggedInUserId(getApplication(), userId);
                    navigateToMainActivity();
                } else {
                    Toast.makeText(getApplicationContext(), "Invalid email or password. Please " +
                            "try again!", Toast.LENGTH_LONG).show();
                }
            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(email, password, name, phone);
    }

    /**
     * Method which navigates from Signup Activity to Signin Activity
     * @method navigatetoSigninActivity
     */
    public void navigatetoSigninActivity(View view){
        Intent loginIntent = new Intent(getApplicationContext(), SigninActivity.class);
        // Clears History of Activity
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
