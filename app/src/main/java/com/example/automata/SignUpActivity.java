package com.example.automata;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.parse.ParseUser;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener{

    EditText emailSignUpEditText;
    EditText usernameSignUpEditText;
    EditText passwordSignUpEditText;
    TextView loginTextView;
    TextView showPassword;


    @Override
    public void onBackPressed() {
        if(ParseUser.getCurrentUser() == null){
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
            signUp(v);
        }
        return false;
    }


    private void showAlert(String title, String message, boolean error) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.cancel();
                    if (!error) {
                        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
        AlertDialog ok = builder.create();
        ok.show();
    }



    public void signUp (View view) {
        if (usernameSignUpEditText.getText().toString().matches("") || passwordSignUpEditText.getText().toString().matches("") || emailSignUpEditText.getText().toString().matches("")) {
            Toast.makeText(this, "Please enter the required info", Toast.LENGTH_SHORT).show();
        }
        if (!usernameSignUpEditText.getText().toString().matches("") && !passwordSignUpEditText.getText().toString().matches("") && !Patterns.EMAIL_ADDRESS.matcher(emailSignUpEditText.getText().toString()).matches()) {
            Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
        }
        if (!usernameSignUpEditText.getText().toString().matches("") &&
                !passwordSignUpEditText.getText().toString().matches("") &&
                Patterns.EMAIL_ADDRESS.matcher(emailSignUpEditText.getText().toString()).matches() && passwordSignUpEditText.getText().toString().length() < 8) {
            Toast.makeText(this, "The password must be at least 8 characters long", Toast.LENGTH_SHORT).show();
        }
        if (!usernameSignUpEditText.getText().toString().matches("") &&
                !passwordSignUpEditText.getText().toString().matches("") &&
                Patterns.EMAIL_ADDRESS.matcher(emailSignUpEditText.getText().toString()).matches() &&
                passwordSignUpEditText.getText().toString().length() >= 8) {
            ParseUser user = new ParseUser();
            user.setUsername(usernameSignUpEditText.getText().toString());
            user.setPassword(passwordSignUpEditText.getText().toString());
            user.setEmail(emailSignUpEditText.getText().toString());
            user.signUpInBackground(e -> {
                if (e == null) {
                    ParseUser.logOut();
                    showAlert("Account Created Successfully!", "\n" +"Please verify your email before Login", false);
                } else {
                    ParseUser.logOut();
                    showAlert("Account Creation Failed", "\n" + e.getMessage(), true);
                }
            });
        }
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.loginTextView) {
            Intent homeIntent = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(homeIntent);
            finish();
        }
        if (v.getId() == R.id.logoImageView2 || v.getId() == R.id.signUpBackgroundLayout) {
            InputMethodManager inputMethodManager2 = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            View focusedView = this.getCurrentFocus();
            if (focusedView != null) {
                inputMethodManager2.hideSoftInputFromWindow(focusedView.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        if (v.getId() == R.id.showPassword) {
            if (showPassword.getText().toString().equals("SHOW")) {
                showPassword.setText("HIDE");
                passwordSignUpEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                passwordSignUpEditText.setSelection(passwordSignUpEditText.length());
            }
            if (showPassword.getText().toString().equals("HIDE")) {
                showPassword.setText("SHOW");
                passwordSignUpEditText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                passwordSignUpEditText.setSelection(passwordSignUpEditText.length());
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        loginTextView = findViewById(R.id.loginTextView);
        loginTextView.setOnClickListener(this);

        emailSignUpEditText = findViewById(R.id.emailSignUpEditText);
        usernameSignUpEditText = findViewById(R.id.usernameSignUpEditText);
        passwordSignUpEditText = findViewById(R.id.passwordSignUpEditText);
        passwordSignUpEditText.setOnKeyListener(this);

        ImageView imageViewLogo = findViewById(R.id.logoImageView2);
        imageViewLogo.setOnClickListener(this);

        ConstraintLayout backgroundLayout = findViewById(R.id.signUpBackgroundLayout);
        backgroundLayout.setOnClickListener(this);

        showPassword = findViewById(R.id.showPassword);
        showPassword.setOnClickListener(this);


    }




}