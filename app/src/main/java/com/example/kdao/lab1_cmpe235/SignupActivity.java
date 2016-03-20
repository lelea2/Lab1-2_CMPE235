package com.example.kdao.lab1_cmpe235;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.ProgressDialog;
import android.widget.TextView;
import android.widget.EditText;
import android.view.View;

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
