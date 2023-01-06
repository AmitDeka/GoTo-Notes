package com.android.gotonotes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.android.gotonotes.common.LoginActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;

public class SettingPrefActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener, Preference.SummaryProvider<ListPreference> {

    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_pref);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings_frame, new SettingsFragment())
                .commit();

        MaterialToolbar materialToolbar = findViewById(R.id.note_set_toolbar);
        setSupportActionBar(materialToolbar);

        mAuth = FirebaseAuth.getInstance();
        Button btnLogout = findViewById(R.id.btn_log_out);

        btnLogout.setOnClickListener(v -> {
            mAuth.signOut();
            Intent logIn = new Intent(SettingPrefActivity.this, LoginActivity.class);
            startActivity(logIn);
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);

    }


    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.settings_preferences, rootKey);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        String darkModeString = getString(R.string.dark_mode);
        if (key != null && sharedPreferences != null)
            if (key.equals(darkModeString)) {
                final String[] darkModeValues = getResources().getStringArray(R.array.dark_mode_values);
                // The apps theme is decided depending upon the saved preferences on app startup
                String pref = PreferenceManager.getDefaultSharedPreferences(this)
                        .getString(getString(R.string.dark_mode), getString(R.string.dark_mode_def_value));
                // Comparing to see which preference is selected and applying those theme settings
                if (pref.equals(darkModeValues[0]))
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                if (pref.equals(darkModeValues[1]))
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                if (pref.equals(darkModeValues[2]))
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
    }

    @Override
    public CharSequence provideSummary(ListPreference preference) {
        String key = preference.getKey();
        if (key != null)
            if (key.equals(getString(R.string.dark_mode)))
                return preference.getEntry();
        return null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}