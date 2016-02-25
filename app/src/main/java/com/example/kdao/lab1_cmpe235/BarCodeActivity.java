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

    private Button scanBtn;
    private TextView formatTxt;
    private TextView contentTxt;
    private List<Tree> myTrees = new ArrayList<Tree>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_code);
        scanBtn = (Button)findViewById(R.id.scan_button);
        formatTxt = (TextView)findViewById(R.id.scan_format);
        contentTxt = (TextView)findViewById(R.id.scan_content);
        scanBtn.setOnClickListener(this);
        handleHomeIconClick(); //handle homeicon click
        createTreeListView();
        populateListView();
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
            formatTxt.setText("FORMAT: " + scanFormat);
            contentTxt.setText("CONTENT: " + scanContent);
        } else {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    /**
     * Function create tree list view
     */
    private void createTreeListView() {
        myTrees.add(new Tree("Landmark art deco-style theater presenting Broadway musicals & " +
                "ballet & dance performances.", "12345",
                R.drawable.barcode_icon, "", new Location(37.32, -121.89, "255 S Almaden Blvd, " +
                "San " +
                "Jose, CA 95113", "San Jose Center for the Performing Arts")));
        myTrees.add(new Tree("Home of Shark", "54321",
                R.drawable.barcode_icon, "", new Location(37.33, -121.90, "525 West Santa Clara " +
                "Street, San Jose", "SAP center")));
    }

    /**
     * Function to populate list view
     */
    private void populateListView() {
        ArrayAdapter<Tree> adapter = new MyListAdapter();
        ListView list = (ListView) findViewById(R.id.treesListView);
        list.setAdapter(adapter);
    }

    //Private class create for tree list function
    private class MyListAdapter extends ArrayAdapter<Tree> {
        public MyListAdapter() {
            super(BarCodeActivity.this, R.layout.item_view, myTrees);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Make sure we have a view to work with (may have been given null)
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.item_view, parent, false);
            }

            // Find the car to work with.
            Tree currentTree = myTrees.get(position);

            // Fill the view
            ImageView imageView = (ImageView)itemView.findViewById(R.id.item_icon);
            imageView.setImageResource(currentTree.getIcon());

            return itemView;
        }
    }
}

