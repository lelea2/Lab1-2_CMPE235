package com.example.kdao.lab1_cmpe235;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Button;
import android.widget.ArrayAdapter;
import java.util.List;
import java.util.ArrayList;
import android.view.View;
import android.widget.TextView;

import com.example.kdao.lab1_cmpe235.data.Comment;

public class CommentListActivity extends AppCompatActivity {

    private ListView commentList;
    private List<Comment> comments = new ArrayList<Comment>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_list);
        populateListView();
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
     * Private function to get all the available comment
     *
     * @method getAllComments
     */
    private void getAllComments() {

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
            username.setText(comment.getComment());
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
