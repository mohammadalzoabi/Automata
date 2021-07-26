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

public class FollowingList extends AppCompatActivity {

    ListView followingListView;
    ArrayAdapter followingArrayAdapter;
    ArrayList<Object> followingArrayList;
    String usersUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following_list);

        Intent intent = getIntent();
        usersUsername = intent.getStringExtra("username");


        followingListView = findViewById(R.id.followingListView);
        followingArrayList = new ArrayList<>();
        followingArrayAdapter = new ArrayAdapter(this, R.layout.newlistview, followingArrayList);
        followingListView.setAdapter(followingArrayAdapter);


        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("username", usersUsername);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null && objects.size() > 0) {
                    for(ParseUser user : objects){
                        if(user.getUsername().equals(usersUsername)){
                            for(int i = 0; i< user.getList("isFollowing").size(); i++){
                                followingArrayList.add(user.getList("isFollowing").get(i));
                                if(user.getList("isFollowing").get(i).equals(usersUsername)){
                                    followingArrayList.remove(usersUsername);}
                            }
                            followingArrayAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        });

    }
}