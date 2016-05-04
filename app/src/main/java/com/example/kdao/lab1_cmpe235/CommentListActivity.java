package com.example.kdao.lab1_cmpe235;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Button;
import android.widget.ArrayAdapter;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kdao.lab1_cmpe235.data.Comment;
import com.example.kdao.lab1_cmpe235.util.Config;
import com.example.kdao.lab1_cmpe235.util.PreferenceData;
import com.example.kdao.lab1_cmpe235.util.Utility;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.parser.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
public class CommentListActivity extends AppCompatActivity {

    private ListView commentList;
    private List<Comment> comments = new ArrayList<Comment>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_list);
        getAllComments();
    }

    /**
     * Private function to set up list view
     * @method populateListView
     */
    private void populateListView() {
        ArrayAdapter<Comment> adapter = new MyListAdapter();
        commentList = (ListView) findViewById(R.id.commentList);
        commentList.setAdapter(adapter);
    }

    /**
     * Private function to populate comments
     * @method populateComments
     */
    private void populateComments(JSONArray arrayObj) {
        for (int i = 0; i < arrayObj.size(); i++) {
            try {
                JSONObject object = (JSONObject) arrayObj.get(i);
                comments.add(new Comment(object.get("comment").toString(), object.get("firstName")
                        .toString(), Integer.parseInt(object.get("rating").toString())));
            } catch(Exception e) {
                System.out.println(e);
            }
        }
    }

    /**
     * Private function to get all the available comment
     *
     * @method getAllComments
     */
    private void getAllComments() {
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                HttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(Config.BASE_URL + "/comments");
                try {
                    try {
                        HttpResponse httpResponse = httpClient.execute(httpGet);
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
                try {
                    JSONArray arrayObj = null;
                    JSONParser jsonParser = new JSONParser();
                    arrayObj= (JSONArray) jsonParser.parse(result);
                    populateComments(arrayObj);
                    populateListView();
                } catch(Exception ex) {
                    System.out.println(ex);
                    Toast.makeText(getApplicationContext(), "Technical difficulty, please try " +
                            "again", Toast.LENGTH_LONG).show();
                }
            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute();
    }

    private class MyListAdapter extends ArrayAdapter<Comment> {
        public MyListAdapter() {
            super(CommentListActivity.this, R.layout.item, comments);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Make sure we have a view to work with (may have been given null)
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.item, parent, false);
            }

            // Find the car to work with.
            Comment comment = comments.get(position);
            TextView username = (TextView) itemView.findViewById(R.id.item_userName);
            username.setText(comment.getUsername());
            TextView commentTxt = (TextView) itemView.findViewById(R.id.item_comment);
            commentTxt.setText("Comment: " + comment.getComment());
            TextView ratingTxt = (TextView) itemView.findViewById(R.id.item_rating);
            ratingTxt.setText("Rating: " + (comment.getRating() + ""));
            return itemView;
        }
    }

    /**
     * Public function navigate to add comment activity
     * @method addComment
     */
    public void addComment(View view) {
        Intent newIntent = new Intent(getApplicationContext(), CommentActivity.class);
        newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(newIntent);
    }
}
