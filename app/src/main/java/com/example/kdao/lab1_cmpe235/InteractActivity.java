package com.example.kdao.lab1_cmpe235;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.kdao.lab1_cmpe235.data.Location;
import com.example.kdao.lab1_cmpe235.data.Tree;

import java.util.*;

public class InteractActivity extends AppCompatActivity {

    private List<Tree> myTrees = new ArrayList<Tree>();
    private HashMap<String, Tree> hmTrees= new HashMap<String, Tree>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createHMTrees();
        setContentView(R.layout.activity_interact);
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

}
