package com.android.gotonotes.common;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.android.gotonotes.*;

import com.android.gotonotes.utils.PasswordCheck;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

public class SignupActivity extends AppCompatActivity {

    private long backPressedTime;
    private FirebaseAuth mAuth;
    private ConstraintLayout signup_layout;

    TextInputLayout signup_email, signup_pass, confirm_pass;
    Button signup, send_to_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signup = findViewById(R.id.btn_signup);
        send_to_login = findViewById(R.id.btn_calllogin);

        signup_email = findViewById(R.id.sign_email);
        signup_pass = findViewById(R.id.signup_pass);
        confirm_pass = findViewById(R.id.signup_confirm_pass);
        signup_layout = findViewById(R.id.signup_layout);

        mAuth = FirebaseAuth.getInstance();

        send_to_login.setOnClickListener(v -> {
            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        signup.setOnClickListener(v -> {
            CloseKeyboard();
            CreatUser();
        });
    }

    private void CreatUser() {
        String user_mail = signup_email.getEditText().getText().toString().trim();
        String user_pass = signup_pass.getEditText().getText().toString().trim();

        if (validEmail() & validPass() & validConPass()) {
            mAuth.createUserWithEmailAndPassword(user_mail, user_pass).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
//                    sendVerifyMail();
                    SendToLogin();
                } else {
                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Snackbar.make(signup_layout, "Email is already registered.", Snackbar.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    private void SendToLogin() {
        Intent stLog = new Intent(SignupActivity.this, LoginActivity.class);
        startActivity(stLog);
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

//    private void sendVerifyMail() {
//        FirebaseUser firebaseUser = mAuth.getCurrentUser();
//        String uid = firebaseUser.getUid();
//        if (firebaseUser != null) {
//            firebaseUser.sendEmailVerification().addOnCompleteListener(task -> {
//                Snackbar.make(signlayout, "Verification mail have been send to " + firebaseUser.getEmail(), Snackbar.LENGTH_INDEFINITE).setAction("Ok", v -> {
//                    Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
//                    startActivity(intent);
//                    mAuth.signOut();
//                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//                }).show();
//            });
//        } else {
//            Snackbar.make(signlayout, "Failed to send verification mail.", Snackbar.LENGTH_LONG).show();
//        }
//    }

    private boolean validEmail() {
        String val = signup_email.getEditText().getText().toString().trim();
        if (val.isEmpty()) {
            signup_email.setError("Email can't be empty.");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(val).matches()) {
            signup_email.setError("Invalid E-mail address.");
            return false;
        } else {
            signup_email.setError(null);
            signup_email.setErrorEnabled(false);
            return true;
        }

    }

    private boolean validPass() {
        String val = signup_pass.getEditText().getText().toString().trim();

        if (val.isEmpty()) {
            signup_pass.setError("Field can't be empty.");
            return false;
        } else if (!PasswordCheck.PASSWORD_PATTERN.matcher(val).matches()) {
            signup_pass.setError("Password should contain at least of one Capital letter and one Small letter and one Number and one Special Character and should not be greater than 20 character.");
            return false;
        } else if (!(val.length() > 8)) {
            signup_pass.setError("Password must contain minimum of 8 characters.");
            return false;
        } else {
            signup_pass.setError(null);
            signup_pass.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validConPass() {
        String pass = signup_pass.getEditText().getText().toString().trim();
        String cpass = confirm_pass.getEditText().getText().toString().trim();

        if (cpass.isEmpty()) {
            confirm_pass.setError("Field can't be empty.");
            return false;
        } else if (!pass.equals(cpass)) {
            confirm_pass.setError("Password do not match.");
            return false;
        } else {
            confirm_pass.setError(null);
            confirm_pass.setErrorEnabled(false);
            return true;
        }
    }

    private void CloseKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null){
            InputMethodManager methodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

            methodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }

    @Override
    public void onBackPressed() {

        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            finish();
            return;
        } else {
            Toast.makeText(getBaseContext(), "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        }
        backPressedTime = System.currentTimeMillis();

    }
}

