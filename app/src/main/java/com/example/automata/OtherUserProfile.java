package com.example.automata;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class OtherUserProfile extends AppCompatActivity {

    TextView profileUsername2;
    TextView fullBioTextView;
    ImageView imageView3;
    String bio;
    TextView followersNumTextView;
    TextView followingNumTextView;
    String usersUsername;
    int followersNum;
    int followingNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user_profile);

        profileUsername2 = findViewById(R.id.profileUsername2);
        imageView3 = findViewById(R.id.imageView3);
        fullBioTextView = findViewById(R.id.bioEditText3);

        Intent intent = getIntent();
        usersUsername = intent.getStringExtra("username");
        profileUsername2.setText(usersUsername);




        ParseQuery<ParseUser> bioQuery = ParseUser.getQuery();
        bioQuery.whereEqualTo("username", usersUsername);
        bioQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null){
                    for(ParseUser user : objects){
                        bio = (String) user.get("bio");
                    }
                    if(bio != null){
                    fullBioTextView.setText(bio);}
                } else {
                    e.printStackTrace();
                }
            }
        });


        bioQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null){
                    ParseFile image = null;
                    for(ParseUser user : objects){
                        image = (ParseFile) user.get("profileImage");
                    }
                    if(image != null){
                        image.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {
                                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                imageView3.setImageBitmap(bitmap);
                            }
                        });
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });




        followingNumTextView = findViewById(R.id.followingNumberTextView2);
        ParseQuery<ParseUser> query1 = ParseUser.getQuery();
        query1.whereEqualTo("username", usersUsername);
        query1.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null && objects.size() > 0) {
                    for (ParseUser user : objects) {
                        if (user.getUsername().equals(usersUsername)) {
                            followingNum = user.getList("isFollowing").size();
                        }
                    }
                }
            }
        });

        followersNumTextView = findViewById(R.id.followersNumberTextView2);
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereNotEqualTo("username", usersUsername);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if(e == null && objects.size() > 0){
                    for(ParseUser user : objects) {
                        if (user.getList("isFollowing").contains(usersUsername)) {
                            followersNum++;
                        }
                        followersNumTextView.setText(String.valueOf(followersNum));
                        if(followingNum >0){
                            followingNumTextView.setText(String.valueOf(followingNum-1));}
                        else {
                            followingNumTextView.setText(String.valueOf(followingNum));
                        }
                    }
                }
            }
        });



        followersNumTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FollowerList.class);
                intent.putExtra("username", profileUsername2.getText().toString());
                startActivity(intent);
            }
        });

        followingNumTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FollowingList.class);
                intent.putExtra("username", profileUsername2.getText().toString());
                startActivity(intent);
            }
        });




    }
}