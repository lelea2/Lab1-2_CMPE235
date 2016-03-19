package com.example.kdao.lab1_cmpe235;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

//import cz.msebera.android.httpclient.*;
//import com.loopj.android.http.*;

import com.example.kdao.lab1_cmpe235.util.Utility;
import com.example.kdao.lab1_cmpe235.util.Config;

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
        errMsg = (TextView)findViewById(R.id.login_error);
        emailText = (EditText)findViewById(R.id.loginEmail);
        pwdText = (EditText)findViewById(R.id.loginPassword);
        // Instantiate Progress Dialog object
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
    }

    /**
     * Method trigger when click on login user
     * @method loginUser
     */
    public void loginUser(View view) {
        String email = emailText.getText().toString();
        String password = pwdText.getText().toString();
        // Instantiate Http Request Param Object
        //RequestParams params = new RequestParams();
        // When Email Edit View and Password Edit View have values other than Null
        if(!Utility.isEmptyString(email) && !Utility.isEmptyString(password)) {
            // When Email entered is Valid
            if(Utility.isEmailValid(email)){
               // params.put("username", email);
               // params.put("password", password);
                //invokeWS(params);
            } else { // When Email is invalid
                Toast.makeText(getApplicationContext(), "Please enter valid email", Toast.LENGTH_LONG).show();
            }
        } else { //Empty form handle
            Toast.makeText(getApplicationContext(), "Please fill in the form", Toast.LENGTH_LONG).show();
        }
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
     * Private method handle
     */
    /*public void invokeWS(RequestParams params){
        progressDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(Config.BASE_URL + "/user", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                progressDialog.hide();
                try {
                    /*JSONObject obj = new JSONObject(response);
                    if (obj.getBoolean("status")) {
                        Toast.makeText(getApplicationContext(), "You are successfully logged in!", Toast.LENGTH_LONG).show();
                        // Navigate to Home screen
                        //navigatetoHomeActivity();
                    }
                    // Else display error message
                    else {
                        errMsg.setText(obj.getString("error_msg"));
                        Toast.makeText(getApplicationContext(), obj.getString("error_msg"), Toast.LENGTH_LONG).show();
                    }*/
               /* } catch (Exception e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();

                }
            }
            // When the response returned by REST has Http response code other than '200'
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse,
                                  Throwable e) {
                // Hide Progress Dialog
                progressDialog.hide();
                // When Http response code is '404'
                if (statusCode == 404) {
                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                } else if (statusCode == 500) { // When Http response code is '500'
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                } else { // When Http response code other than 404, 500
                    Toast.makeText(getApplicationContext(), "Unexpected Error occcur.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }*/

}
