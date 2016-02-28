package com.example.kdao.lab1_cmpe235;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Context;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Switch;
import android.widget.CompoundButton;
import android.widget.Button;

import android.util.Log;

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

    static String TAG = "InteractActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interact);
        createHMTrees();
        handleSession();
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
            Log.i(TAG, "SESSION_ID=" + value);
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
        final Location myLocation = mytree.getLocation();
        TextView treeName = (TextView) findViewById(R.id.tree_name);
        TextView treeDesc = (TextView) findViewById(R.id.tree_description);
        TextView treeAddr = (TextView) findViewById(R.id.tree_address);
        treeName.setText(myLocation.getName());
        treeDesc.setText(mytree.getDescription());
        treeAddr.setText(myLocation.getAddress());

        //Set up handler for view location button for specific tree
        Button viewLoc = (Button) findViewById(R.id.tree_view_location);
        viewLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchActivity = new Intent(InteractActivity.this, MapsActivity.class);
                launchActivity.putExtra("longitude", myLocation.getLongitude());
                launchActivity.putExtra("latitude", myLocation.getLatitude());
                launchActivity.putExtra("name", myLocation.getName());
                launchActivity.putExtra("address", myLocation.getAddress());
                startActivity(launchActivity);
            }
        });
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
