package com.example.kdao.lab1_cmpe235;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Context;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Switch;
import android.widget.CompoundButton;

import com.example.kdao.lab1_cmpe235.data.Location;
import com.example.kdao.lab1_cmpe235.data.Tree;

import java.util.*;

public class InteractActivity extends AppCompatActivity {

    private HashMap<String, Tree> hmTrees= new HashMap<String, Tree>();
    final Context context = this;
    Switch switchButton;
    TextView textView;
    String switchOn = "ON";
    String switchOff = "OFF";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createHMTrees();
        handleSession();
        setContentView(R.layout.activity_interact);
        handleSwitchButton();
    }

    /**
     * Helper function to handle switch button
     */
    private void handleSwitchButton() {
        // For first switch button
        switchButton = (Switch) findViewById(R.id.switchButton);
        textView = (TextView) findViewById(R.id.textView);

        switchButton.setChecked(true);
        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    textView.setText(switchOn);
                } else {
                    textView.setText(switchOff);
                }
            }
        });

    }

    /*
     * Function create tree list view
     */
    private void createHMTrees() {
        hmTrees.put("45304c60-9eac-48bf-9d0b-c02dda6c6cb3", new Tree("Landmark art deco-style " +
                "theater presenting Broadway musicals" +
                " & " +
                "ballet & dance performances.",
                R.drawable.barcode_icon, "", new Location(37.32, -121.89, "255 S Almaden Blvd, " +
                "San " +
                "Jose, CA 95113", "San Jose Center for the Performing Arts")));
        hmTrees.put("8f14886c-d267-44b8-8518-8cf363634929", new Tree("Home of Shark",
                R.drawable.barcode_icon, "", new Location(37.33, -121.90, "525 West Santa Clara " +
                "Street, San Jose", "SAP center")));
    }

    //Function handle session passed by other activity
    private void handleSession() {
        Bundle extras = getIntent().getExtras();
        String value = "";
        Tree currentTree = null;
        if (extras != null) {
            value = extras.getString("SESSION_ID");
            currentTree = hmTrees.get(value);
        }
        if (currentTree != null) {
            createCurrentTreeView(currentTree);
        } else {
            showAlertDialog();
        }
    }

    /**
     * Create view for current tree
     */
    private void createCurrentTreeView(Tree mytree) {

    }

    /**
     * Helper function to show alert box
     */
    private void showAlertDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set title
        alertDialogBuilder.setTitle("My Tree View");

        // set dialog message
        alertDialogBuilder
                .setMessage("No information for tree exist. Please try again")
                .setCancelable(false)
                .setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Navigate back to barcode page
                        Intent activity = new Intent(InteractActivity.this, BarCodeActivity.class);
                        startActivity(activity);
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

}
