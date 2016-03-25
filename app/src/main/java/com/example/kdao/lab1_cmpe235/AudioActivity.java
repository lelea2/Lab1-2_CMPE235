package com.example.kdao.lab1_cmpe235;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaRecorder;

import android.os.Bundle;
import android.os.Environment;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import android.widget.ImageView;
import android.widget.Toast;
import java.io.IOException;

public class AudioActivity extends AppCompatActivity {

    Button playBtn,stopBtn,recordBtn;
    private MediaRecorder myAudioRecorder;
    private String outputFile = null;

    private static String TAG = "AudioActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);

        playBtn = (Button) findViewById(R.id.button_play);
        stopBtn = (Button) findViewById(R.id.button_stop);
        recordBtn = (Button) findViewById(R.id.button_record);

        stopBtn.setEnabled(false);
        playBtn.setEnabled(false);
        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() +
                "/record_audio_sample" +
                ".3gp";

        myAudioRecorder = new MediaRecorder();
        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        myAudioRecorder.setOutputFile(outputFile);
    }

    /**
     * Public function to handle audo record activity
     * @param view
     */
    public void recordAudio(View view) {
        try {
            myAudioRecorder.prepare();
            myAudioRecorder.start();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        recordBtn.setEnabled(false);
        stopBtn.setEnabled(true);
        Toast.makeText(getApplicationContext(), "Recording audio started", Toast.LENGTH_LONG)
                .show();
    }

    /**
     * Public function handle stop audio
     * @param view
     */
    public void stopAudio(View view) {
        myAudioRecorder.stop();
        myAudioRecorder.release();
        myAudioRecorder  = null;
        stopBtn.setEnabled(false);
        playBtn.setEnabled(true);
        Toast.makeText(getApplicationContext(), "Audio recorded successfully",Toast.LENGTH_LONG).show();
    }

    /**
     * Public function handle play audio
     * @param view
     */
    public void playAudio(View view) {
        MediaPlayer m = new MediaPlayer();
        try {
            m.setDataSource(outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            m.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        m.start();
        Toast.makeText(getApplicationContext(), "Playing my audio", Toast.LENGTH_LONG).show();
    }

    /**
     * Public function handle share audio
     * @param view
     */
    public void shareAudio(View view) {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("audio/*");
        share.putExtra(Intent.EXTRA_STREAM, Uri.parse(outputFile));
        try {
            startActivity(Intent.createChooser(share, "Share audio"));
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }
}
