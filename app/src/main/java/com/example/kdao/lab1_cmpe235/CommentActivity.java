package com.example.kdao.lab1_cmpe235;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.kdao.lab1_cmpe235.util.Config;
import com.example.kdao.lab1_cmpe235.util.PreferenceData;
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

public class CommentActivity extends AppCompatActivity {
    // Insert your Application Title
    private final static String TITLE = "Rate Us";

    // Insert your Application Package Name
    private final static String PACKAGE_NAME = "com.kdao.lab1_cmpe235";

    // Day until the Rate Us Dialog Prompt(Default 2 Days)
    private final static int DAYS_UNTIL_PROMPT = 0;

    // App launches until Rate Us Dialog Prompt(Default 5 Launches)
    private final static int LAUNCHES_UNTIL_PROMPT = 2;

    static String TAG = "CommentActivity";
    private EditText commentText;
    private RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        //Comment to app stores
        //app_launched(this);
        commentText = (EditText) findViewById(R.id.commentText);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        handleHomeIconClick();
    }

    /**
     * Handle click on homeicon
     */
    private void handleHomeIconClick() {
        ImageView homeicon = (ImageView) findViewById(R.id.home_icon);
        homeicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Go back home action");
                Intent mainView = new Intent(CommentActivity.this, MainActivity.class);
                startActivity(mainView);
            }
        });
    }

    /**
     * Public function handling add comment
     * @method addComment
     */
    public void addComment(View view) {
        String rateValue = String.valueOf(ratingBar.getRating());
        String comment = commentText.getText().toString();
        if (Utility.isEmptyString(comment)) {
            Toast.makeText(CommentActivity.this, "Please fill out your comment", Toast.LENGTH_LONG).show();
        } else {
            addCommentToDB(rateValue, comment);
        }
    }

    /**
     * Private function update new comment to DB
     * @method addCommentToDB
     */
    private void addCommentToDB(String rateValue, String comment) {
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                String rateValue = params[0];
                String comment = params[1];
                HttpClient httpClient = new DefaultHttpClient();
                HttpPut httpPut = new HttpPut(Config.BASE_URL + "/comment/add");
                JSONObject json = new JSONObject();
                try {
                    json.put("comment", comment);
                    json.put("rating", rateValue);
                    json.put("userId", PreferenceData.getLoggedInUserId(getApplicationContext()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    StringEntity se = new StringEntity(json.toString());
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
                        System.out.println("An Exception given because of UrlEncodedFormEntity " +
                                "argument :" + e);
                        e.printStackTrace();
                    }
                } catch (Exception uee) {
                    System.out.println("An Exception given because of UrlEncodedFormEntity argument :" + uee);
                    uee.printStackTrace();
                }
                return "error";
            }
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                if (result == "error") { //error case
                    Toast.makeText(getApplicationContext(), "Technical difficulty, please try " +
                            "again!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Comment added successfully", Toast
                            .LENGTH_LONG).show();
                    navigateToCommentListActivity();
                }
            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(rateValue, comment);
    }

    /**
     * Function to navigate to comment list
     * @method navigateToCommentListActivity
     */
    private void navigateToCommentListActivity() {
        Intent commentListIntent = new Intent(getApplicationContext(), CommentListActivity.class);
        commentListIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(commentListIntent);
    }

    /**
     * Handle dialog context popup for applaunch
     * @param mContext
     */
    private static void app_launched(Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences("rateus", 0);
        if (prefs.getBoolean("dontshowagain", false)) {
            return;
        }

        SharedPreferences.Editor editor = prefs.edit();

        // Increment launch counter
        long launch_count = prefs.getLong("launch_count", 0) + 1;
        editor.putLong("launch_count", launch_count);

        // Get date of first launch
        Long date_firstLaunch = prefs.getLong("date_firstlaunch", 0);
        if (date_firstLaunch == 0) {
            date_firstLaunch = System.currentTimeMillis();
            editor.putLong("date_firstlaunch", date_firstLaunch);
        }

        // Wait at least n days before opening
        if (launch_count >= LAUNCHES_UNTIL_PROMPT) {
            if (System.currentTimeMillis() >= date_firstLaunch
                    + (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)) {
                showRateDialog(mContext, editor);
            }
        }

        editor.commit();
    }

    private static void showRateDialog(final Context mContext,
                                      final SharedPreferences.Editor editor) {
        final Dialog dialog = new Dialog(mContext);
        // Set Dialog Title
        dialog.setTitle("Rate " + TITLE);

        LinearLayout ll = new LinearLayout(mContext);
        ll.setOrientation(LinearLayout.VERTICAL);

        TextView tv = new TextView(mContext);
        tv.setText("If you like " + TITLE
                + ", please give us some stars and comment");
        tv.setWidth(240);
        tv.setPadding(4, 0, 4, 10);
        ll.addView(tv);

        // First Button
        Button b1 = new Button(mContext);
        b1.setText("Rate " + TITLE);
        b1.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri
                        .parse("market://details?id=" + PACKAGE_NAME)));
                dialog.dismiss();
            }
        });
        ll.addView(b1);

        // Second Button
        Button b2 = new Button(mContext);
        b2.setText("Remind me later");
        b2.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ll.addView(b2);

        // Third Button
        Button b3 = new Button(mContext);
        b3.setText("Stop Bugging me");
        b3.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (editor != null) {
                    editor.putBoolean("dontshowagain", true);
                    editor.commit();
                }
                dialog.dismiss();
            }
        });
        ll.addView(b3);

        dialog.setContentView(ll);

        // Show Dialog
        dialog.show();
    }
}
