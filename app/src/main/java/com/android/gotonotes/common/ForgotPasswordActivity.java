package com.android.gotonotes.common;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.gotonotes.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class ForgotPasswordActivity extends AppCompatActivity {

    private static final int TIME_INTERVAL = 1500;
    private long mBackPressed;
    private FirebaseAuth mAuth;
    private LinearLayout forgetLayout;

    ImageView btnback;
    TextInputLayout forgotPass;
    Button passRecoverBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        btnback = findViewById(R.id.backbtn);
        forgotPass = findViewById(R.id.forgotpass_email);
        passRecoverBtn = findViewById(R.id.btn_recover);
        forgetLayout = findViewById(R.id.forget_password_layout);
        mAuth = FirebaseAuth.getInstance();

        btnback.setOnClickListener(v -> {
            Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        });

        passRecoverBtn.setOnClickListener(v -> {
            CloseKeyboard();
            ValidateSend();
        });
    }

    private void ValidateSend() {
        String email = forgotPass.getEditText(). getText().toString().trim();
        if (email.isEmpty()) {
            forgotPass.setError("Field can't be empty.");
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            forgotPass.setError("Invalid E-mail address.");
        } else {
            SendRecoverMail();
        }
    }

    private void SendRecoverMail() {
        String email = forgotPass.getEditText().getText().toString().trim();
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Snackbar.make(forgetLayout, "Email has been sent.", Snackbar.LENGTH_INDEFINITE).setAction("Ok", v -> {
                    Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }).show();
            } else {
                if (task.getException() instanceof FirebaseAuthInvalidUserException) {
                    Snackbar.make(forgetLayout, "Couldn't find your Email.", Snackbar.LENGTH_LONG).show();
                }
            }
        });
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