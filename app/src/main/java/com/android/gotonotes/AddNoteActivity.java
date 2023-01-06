package com.android.gotonotes;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.preference.PreferenceManager;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class AddNoteActivity extends AppCompatActivity {

    private TextInputEditText addTitle, addContent;
    private ConstraintLayout addNoteActivity;
    private FirebaseFirestore fStore;
    private FirebaseUser fUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        final String[] darkModeValues = getResources().getStringArray(R.array.dark_mode_values);
        String pref = PreferenceManager.getDefaultSharedPreferences(this)
                .getString(getString(R.string.dark_mode), getString(R.string.dark_mode_def_value));
        if (pref.equals(darkModeValues[0]))
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        if (pref.equals(darkModeValues[1]))
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        if (pref.equals(darkModeValues[2]))
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);


        MaterialToolbar materialToolbar = findViewById(R.id.add_note_toolbar);
        setSupportActionBar(materialToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        fStore = FirebaseFirestore.getInstance();
        fUser = FirebaseAuth.getInstance().getCurrentUser();

        addNoteActivity = findViewById(R.id.add_Note_Activity);
        addTitle = findViewById(R.id.add_note_title);
        addContent = findViewById(R.id.add_note_content);

        long date = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy h:mm a", Locale.getDefault());
        String dateString = sdf.format(date);
        TextView shwDate = findViewById(R.id.add_note_date);
        shwDate.setText(dateString);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.note_add_fav:
                Toast.makeText(AddNoteActivity.this, "Fav Click", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.note_add_save:
                AddNote();
                CloseKeyboard();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void AddNote() {
        String nTitle = addTitle.getText().toString();
        String nContent = addContent.getText().toString();

        long date = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy h:mm a", Locale.getDefault());
        String nDateString = sdf.format(date);

        if (nTitle.isEmpty() || nContent.isEmpty()) {
            Snackbar.make(addNoteActivity, "Note can not be saved with empty fields.", Snackbar.LENGTH_SHORT).show();
        } else {
            DocumentReference documentReference = fStore.collection("AllNotes").document(fUser.getUid()).collection("UserNotes").document();
            Map<String, Object> note = new HashMap<>();
            note.put("title", nTitle);
            note.put("content", nContent);
            note.put("date", nDateString);

            documentReference.set(note).addOnSuccessListener(unused -> Snackbar.make(addNoteActivity, "Note has been added Successfully.", Snackbar.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Snackbar.make(addNoteActivity, "Note can not be saved.", Snackbar.LENGTH_SHORT).show());
        }
    }

    private void CloseKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager methodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

            methodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        finish();
        return true;
    }
}