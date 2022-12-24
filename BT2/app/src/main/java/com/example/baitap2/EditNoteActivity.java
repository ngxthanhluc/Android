package com.example.baitap2;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class EditNoteActivity extends AppCompatActivity {
    Toolbar toolbar;
    EditText txtTitle, txtDes;
    String title, des, id;
    long createdTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        Intent i = getIntent();

        id = i.getStringExtra("id");
        title = i.getStringExtra("title");
        des = i.getStringExtra("des");
        createdTime = System.currentTimeMillis();

        txtTitle = findViewById(R.id.titleInput);
        txtTitle.setText(title);
        txtDes = findViewById(R.id.descriptionInput);
        txtDes.setText(des);


        toolbar = findViewById(R.id.editNoteToolbar);
        setSupportActionBar(toolbar);
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
            if (txtTitle.getText().toString().isEmpty() || txtDes.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please enter title and description", Toast.LENGTH_SHORT).show();
            } else {
                //update not
                Realm.init(getApplicationContext());
                Realm realm = Realm.getDefaultInstance();
                //find note
                Note note = realm.where(Note.class).equalTo("id", id).findFirst();
                realm.beginTransaction();
                note.setTitle(txtTitle.getText().toString());
                note.setDescription(txtDes.getText().toString());
                note.setCreatedTime(createdTime);
                realm.commitTransaction();
                Toast.makeText(getApplicationContext(), "Note saved", Toast.LENGTH_LONG).show();
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
