package com.android.gotonotes;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.color.DynamicColors;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        MaterialToolbar materialToolbar = findViewById(R.id.note_toolbar);
        setSupportActionBar(materialToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Context context = getApplicationContext();
        PackageManager packageManager = context.getPackageManager();
        String packageName = context.getPackageName();
        String myVersionName = "Not Available";
        try {
            myVersionName = packageManager.getPackageInfo(packageName, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String resVer = "Version " + myVersionName;
        TextView versionName = findViewById(R.id.about_version);
        versionName.setText(resVer);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        finish();
        return true;
    }
}
