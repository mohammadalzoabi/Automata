package com.example.automata;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

import java.text.ParseException;

public class resetPasswordActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener  {
    EditText emailRequestEditText;


    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
            passwordResetButton(v);
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.resetPasswordButton) {
            Intent homeIntent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(homeIntent);
            finish();
        } else if (v.getId() == R.id.emailRequestTextView || v.getId() == R.id.passwordResetBackgroundLayout) {
            InputMethodManager inputMethodManager2 = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            View focusedView = this.getCurrentFocus();
            if (focusedView != null) {
                inputMethodManager2.hideSoftInputFromWindow(focusedView.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        emailRequestEditText = findViewById(R.id.emailRequestEditText);
    }


    public void passwordResetButton(View view) {
        if (TextUtils.isEmpty(emailRequestEditText.getText())) {
            Toast.makeText(this, "Please Enter an Email.", Toast.LENGTH_SHORT).show();
        } else {
            ParseUser.requestPasswordResetInBackground(emailRequestEditText.getText().toString(), new RequestPasswordResetCallback() {
                @Override
                public void done(com.parse.ParseException e) {
                    if(e==null){
                        Toast.makeText(resetPasswordActivity.this, "An email was sent with the instructions.", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(resetPasswordActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}