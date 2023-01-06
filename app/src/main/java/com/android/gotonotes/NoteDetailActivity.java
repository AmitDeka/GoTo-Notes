package com.android.gotonotes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class NoteDetailActivity extends AppCompatActivity {

    private ConstraintLayout noteDetailActivity;
    EditText editTitle, editContent;
    Intent data;
    FirebaseFirestore fStore;
    FirebaseUser fUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);

//        final String[] darkModeValues = getResources().getStringArray(R.array.dark_mode_values);
//        String pref = PreferenceManager.getDefaultSharedPreferences(this)
//                .getString(getString(R.string.dark_mode), getString(R.string.dark_mode_def_value));
//        if (pref.equals(darkModeValues[0]))
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
//        if (pref.equals(darkModeValues[1]))
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//        if (pref.equals(darkModeValues[2]))
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);


        MaterialToolbar materialToolbar = findViewById(R.id.add_note_toolbar);
        setSupportActionBar(materialToolbar);
        if ( getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        data = getIntent();
        editTitle = findViewById(R.id.detail_note_title);
        editContent = findViewById(R.id.detail_note_content);

        noteDetailActivity = findViewById(R.id.note_detail_activity);

        fStore = FirebaseFirestore.getInstance();
        fUser = FirebaseAuth.getInstance().getCurrentUser();

        String noteTitle = data.getStringExtra("title");
        String noteContent = data.getStringExtra("content");


        editTitle.setText(noteTitle);
        editContent.setText(noteContent);

        TextView showEditDate = findViewById(R.id.note_date);

        DocumentReference docRef = fStore.collection("AllNotes").document(fUser.getUid()).collection("UserNotes").document(data.getStringExtra("noteId"));
        docRef.addSnapshotListener(this, (value, error) -> showEditDate.setText(value.getString("date")));
    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.note_add_fav:
                Toast.makeText(NoteDetailActivity.this, "Fav Click", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.note_add_save:
                EditNote();
                CloseKeyboard();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void EditNote() {
        String nTitle = editTitle.getText().toString();
        String nContent = editContent.getText().toString();

        long date = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy h:mm a", Locale.getDefault());
        String nDateString = sdf.format(date);


        DocumentReference docRef = fStore.collection("AllNotes").document(fUser.getUid()).collection("UserNotes").document(data.getStringExtra("noteId"));
        Map<String, Object> note = new HashMap<>();
        note.put("title", nTitle);
        note.put("content", nContent);
        note.put("date", nDateString);

        docRef.update(note).addOnSuccessListener(unused -> Snackbar.make(noteDetailActivity, "Note has been edited successfully.", Snackbar.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Snackbar.make(noteDetailActivity, "Note can not be saved.", Snackbar.LENGTH_SHORT).show());
    }

    private void CloseKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null){
            InputMethodManager methodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

            methodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }

    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        finish();
        return true;
    }
}