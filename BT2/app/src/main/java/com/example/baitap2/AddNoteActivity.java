package com.example.baitap2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;


import io.realm.Realm;

public class AddNoteActivity extends AppCompatActivity {
    Toolbar toolbar;
    EditText txtTitle, txtDes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        toolbar = (Toolbar) findViewById(R.id.addNoteToolbar);
        setSupportActionBar(toolbar);
        txtTitle = (EditText) findViewById(R.id.titleInput);
        txtDes = (EditText) findViewById(R.id.descriptionInput);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.save) {
            Realm.init(getApplicationContext());
            Realm realm = Realm.getDefaultInstance();
            //random id
            String id = String.valueOf(System.currentTimeMillis());
            String title = txtTitle.getText().toString();
            String description = txtDes.getText().toString();
            if (title.isEmpty() || description.isEmpty()) {
                Toast.makeText(this, "Please enter title and description", Toast.LENGTH_SHORT).show();
            } else {
                long createdTime = System.currentTimeMillis();
                realm.beginTransaction();
                Note note = realm.createObject(Note.class);
                note.setId(id);
                note.setTitle(title);
                note.setDescription(description);
                note.setCreatedTime(createdTime);
                realm.commitTransaction();
                Toast.makeText(getApplicationContext(), "Note saved", Toast.LENGTH_LONG).show();
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

}