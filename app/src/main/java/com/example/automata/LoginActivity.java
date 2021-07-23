package com.example.automata;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener{


    EditText usernameLoginEditText;
    EditText passwordLoginEditText;
    TextView signUpTextView;

    @Override
    public void onBackPressed() {
        if(ParseUser.getCurrentUser() == null){
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
        }
    }



    public void redirectUser(){
        if(ParseUser.getCurrentUser() != null){
            Intent intent = new Intent(getApplicationContext(), MainHomeActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
            login(v);
        }
        return false;
    }

    public void login (View view){
        if(usernameLoginEditText.getText().toString().matches("") || passwordLoginEditText.getText().toString().matches("")){
            Toast.makeText(this, "Please enter the required info", Toast.LENGTH_SHORT).show();
        } else {
            ParseUser.logInInBackground(usernameLoginEditText.getText().toString(), passwordLoginEditText.getText().toString(), new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if(user != null){
                        Log.i("Login", "done");
                        redirectUser();
                    } else {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.signUpTextView) {
            Intent homeIntent = new Intent(getApplicationContext(), SignUpActivity.class);
            startActivity(homeIntent);
            finish();
        } else if (v.getId() == R.id.logoImageView3 || v.getId() == R.id.loginBackgroundLayout) {
            InputMethodManager inputMethodManager2 = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            View focusedView = this.getCurrentFocus();

            if (focusedView != null) {
                inputMethodManager2.hideSoftInputFromWindow(focusedView.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        } else if(v.getId() == R.id.forgotPasswordTextView){
            Intent homeIntent = new Intent(getApplicationContext(), resetPasswordActivity.class);
            startActivity(homeIntent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        signUpTextView = findViewById(R.id.signUpTextView);
        signUpTextView.setOnClickListener(this);

        usernameLoginEditText = findViewById(R.id.usernameLoginEditText);
        passwordLoginEditText = findViewById(R.id.passwordLoginEditText);
        passwordLoginEditText.setOnKeyListener(this);

        ImageView imageViewLogo = findViewById(R.id.logoImageView3);
        imageViewLogo.setOnClickListener(this);

        ConstraintLayout backgroundLayout = findViewById(R.id.loginBackgroundLayout);
        backgroundLayout.setOnClickListener(this);




    }

}