package com.example.kdao.lab1_cmpe235;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Context;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Switch;
import android.widget.CompoundButton;
import android.widget.Button;
import android.widget.Toast;
import android.view.Menu;
import android.view.MenuItem;

import android.util.Log;

import com.example.kdao.lab1_cmpe235.data.Location;
import com.example.kdao.lab1_cmpe235.data.Tree;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.*;

public class InteractActivity extends YouTubeBaseActivity implements
        YouTubePlayer.OnInitializedListener {

    private HashMap<String, Tree> hmTrees= new HashMap<String, Tree>();
    final Context context = this;
    Switch switchButton;
    TextView textView;
    String switchOn = "ON";
    String switchOff = "OFF";

    static String TAG = "InteractActivity";

    //Pre-define for youtube video handler
    private static final int RECOVERY_REQUEST = 1;
    public static final String YOUTUBE_API_KEY = "AIzaSyCplV4mTS_Pu-I7ccgGI3RAgmjNa7HDkAI";
    private YouTubePlayerView youTubeView;
    private MyPlayerStateChangeListener playerStateChangeListener;
    private MyPlaybackEventListener playbackEventListener;
    private YouTubePlayer player;
    private Tree currentTree;


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
                R.drawable.barcode_icon, "HZS3cWlr4AI", new Location(37.32, -121.89, "255 S Almaden Blvd, " +
                "San " +
                "Jose, CA 95113", "San Jose Center for the Performing Arts")));
        hmTrees.put("8f14886c-d267-44b8-8518-8cf363634929", new Tree("Home of Shark",
                R.drawable.barcode_icon, "0fAgFUiBmQE", new Location(37.33, -121.90, "525 West Santa Clara " +
                "Street, San Jose", "SAP center")));
    }

    //Function handle session passed by other activity
    private void handleSession() {
        Bundle extras = getIntent().getExtras();
        String value = "";
        currentTree = null;
        if (extras != null) {
            value = extras.getString("SESSION_ID");
            Log.i(TAG, "SESSION_ID=" + value);
            currentTree = hmTrees.get(value);
        }
        if (currentTree != null) {
            createCurrentTreeView(currentTree);
            //Initialize youtube video view
            youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
            youTubeView.initialize(YOUTUBE_API_KEY, this);
            playerStateChangeListener = new MyPlayerStateChangeListener();
            playbackEventListener = new MyPlaybackEventListener();
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

    /**
     * Initialize Youtube video
     * @param provider
     * @param player
     * @param wasRestored
     */
    @Override
    public void onInitializationSuccess(Provider provider, YouTubePlayer player, boolean wasRestored) {
        this.player = player;
        player.setPlayerStateChangeListener(playerStateChangeListener);
        player.setPlaybackEventListener(playbackEventListener);
        if (!wasRestored) {
            Log.i(TAG, "Initial youtube video load");
            player.cueVideo(currentTree.getVideoId()); // Plays https://www.youtube.com/watch?v=fhWaJi1Hsfo
        }
    }

    /**
     * Handle youtube video failure
     * @param provider
     * @param errorReason
     */
    @Override
    public void onInitializationFailure(Provider provider, YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_REQUEST).show();
        } else {
            String error = String.format(getString(R.string.player_error), errorReason.toString());
            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Handle youtube video loading
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(YOUTUBE_API_KEY, this);
        }
    }

    protected Provider getYouTubePlayerProvider() {
        return youTubeView;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_interact, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private final class MyPlaybackEventListener implements YouTubePlayer.PlaybackEventListener {

        @Override
        public void onPlaying() {
            // Called when playback starts, either due to user action or call to play().
            showMessage("Playing");
        }

        @Override
        public void onPaused() {
            // Called when playback is paused, either due to user action or call to pause().
            showMessage("Paused");
        }

        @Override
        public void onStopped() {
            // Called when playback stops for a reason other than being paused.
            showMessage("Stopped");
        }

        @Override
        public void onBuffering(boolean b) {
            // Called when buffering starts or ends.
        }

        @Override
        public void onSeekTo(int i) {
            // Called when a jump in playback position occurs, either
            // due to user scrubbing or call to seekRelativeMillis() or seekToMillis()
        }
    }

    //The following is to handle youtube video action
    private final class MyPlayerStateChangeListener implements YouTubePlayer.PlayerStateChangeListener {

        @Override
        public void onLoading() {
            // Called when the player is loading a video
            // At this point, it's not ready to accept commands affecting playback such as play() or pause()
        }

        @Override
        public void onLoaded(String s) {
            // Called when a video is done loading.
            // Playback methods such as play(), pause() or seekToMillis(int) may be called after this callback.
        }

        @Override
        public void onAdStarted() {
            // Called when playback of an advertisement starts.
        }

        @Override
        public void onVideoStarted() {
            // Called when playback of the video starts.
        }

        @Override
        public void onVideoEnded() {
            // Called when the video reaches its end.
        }

        @Override
        public void onError(YouTubePlayer.ErrorReason errorReason) {
            // Called when an error occurs.
            Log.e(TAG, errorReason.toString());
            showMessage("Error");
        }
    }

}
