package com.samuelokello.master_notes

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
	var addButton: ImageButton? = null
	var deleteButton: ImageButton? = null
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)


//        addButton = findViewById(R.id.add_button);
//        deleteButton = findViewById(delete_button);

//        addButton.setOnClickListener((v) -> startActivity(new Intent(MainActivity.this, NotedetailsActivity.class)));
//        deleteButton.setOnClickListener((v) ->);
	}
}