package com.android.gotonotes.common;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.gotonotes.DashboardActivity;
import com.android.gotonotes.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private static final int TIME_INTERVAL = 1500;
    private long mBackPressed;
    private ConstraintLayout logLayout;
    private FirebaseAuth mAuth;

    TextInputLayout usrmail, usrpass;
    Button login, forgotpass, callsignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usrmail = findViewById(R.id.login_email);
        usrpass = findViewById(R.id.login_pass);
        login = findViewById(R.id.btn_login);
        forgotpass = findViewById(R.id.btn_forgotpass);
        callsignup = findViewById(R.id.btn_callsignup);
        logLayout = findViewById(R.id.login_layout);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        if (firebaseUser != null) {
            startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }


        login.setOnClickListener(v -> {
            CloseKeyboard();
            LoginUser();
        });

        forgotpass.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        callsignup.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
    }

    private void LoginUser() {
        String umail = usrmail.getEditText().getText().toString().trim();
        String upass = usrpass.getEditText().getText().toString().trim();

        if (validEmail() & validPass()) {
            mAuth.signInWithEmailAndPassword(umail, upass).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
//                    checkMailVerify();
                    SendToDash();
                } else {
                    if (task.getException() instanceof FirebaseAuthInvalidUserException) {
                        Snackbar.make(logLayout, "Couldn't find your Account.", Snackbar.LENGTH_LONG).show();
                    } else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                        Snackbar.make(logLayout, "Your email and password do not match.", Snackbar.LENGTH_LONG).show();
                    }
                }
            });

        }
    }

    private void SendToDash() {
        Intent stdash = new Intent(LoginActivity.this, DashboardActivity.class);
        startActivity(stdash);
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

//    private void checkMailVerify() {
//        FirebaseUser firebaseUser = mAuth.getCurrentUser();
//        if (firebaseUser.isEmailVerified()) {
//            startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
//            finish();
//            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//        } else {
//            Snackbar.make(logLayout, "Verify your email first.", Snackbar.LENGTH_LONG).show();
//            mAuth.signOut();
//        }
//    }

    private boolean validEmail() {
        String email = usrmail.getEditText().getText().toString().trim();
        if (email.isEmpty()) {
            usrmail.setError("Field can't be empty.");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            usrmail.setError("Please enter a valid Email address");
            return false;
        }
        else {
            usrmail.setError(null);
            usrmail.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validPass() {
        String pass = usrpass.getEditText().getText().toString().trim();
        if (pass.isEmpty()) {
            usrpass.setError("Field can't be empty.");
            return false;
        } else {
            usrpass.setError(null);
            usrpass.setErrorEnabled(false);
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
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed();
        } else {
            Toast.makeText(getBaseContext(), "Please click BACK again to exit", Toast.LENGTH_LONG).show();
            mBackPressed = System.currentTimeMillis();
        }
    }


}