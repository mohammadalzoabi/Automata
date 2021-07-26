package com.example.automata;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class FollowerList extends AppCompatActivity {

    ListView followersListView;
    ArrayAdapter followerArrayAdapter;
    ArrayList<String> followerArrayList;
    String usersUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follower_list);

        Intent intent = getIntent();
        usersUsername = intent.getStringExtra("username");


        followersListView = findViewById(R.id.followersListView);
        followerArrayList = new ArrayList<>();
        followerArrayAdapter = new ArrayAdapter(this, R.layout.newlistview, followerArrayList);
        followersListView.setAdapter(followerArrayAdapter);



        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereNotEqualTo("username", usersUsername);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null && objects.size() > 0) {
                    for (ParseUser user : objects) {
                        if(user.getList("isFollowing").contains(usersUsername)){
                            followerArrayList.add(user.getUsername());
                            followerArrayAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        });
    }
}