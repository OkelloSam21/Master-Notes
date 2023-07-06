package com.samuelokello.master_notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    ImageButton addButton, deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        addButton = findViewById(R.id.add_button);
//        deleteButton = findViewById(delete_button);

        addButton.setOnClickListener((v) -> startActivity(new Intent(MainActivity.this, NotedetailsActivity.class)));
//        deleteButton.setOnClickListener((v) ->);
    }
}