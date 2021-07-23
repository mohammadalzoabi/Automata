package com.example.automata;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseUser;

public class LoginOrSignupActivity extends AppCompatActivity {

    public void signUp(View view){
        Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
        startActivity(intent);
    }

    public void login(View view){
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_or_signup);


        if(ParseUser.getCurrentUser() != null){
            Intent intent = new Intent(getApplicationContext(), MainHomeActivity.class);
            startActivity(intent);}
    }
}