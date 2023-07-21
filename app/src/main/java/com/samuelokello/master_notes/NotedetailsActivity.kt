package com.samuelokello.master_notes

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.samuelokello.master_notes.Utility.collectionReferenceForNotes
import com.samuelokello.master_notes.Utility.showToast

class NotedetailsActivity : AppCompatActivity() {
	var titleEditText: EditText? = null
	var contentEditText: EditText? = null
	var saveNoteBtn: ImageButton? = null
	var pageTitleTextView: TextView? = null
	var title: String? = null
	var content: String? = null
	var docId: String? = null
	var isEditMode = false
	var deleteNoteTextViewBtn: TextView? = null
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_note_details)
		titleEditText = findViewById(R.id.notes_title_text)
		contentEditText = findViewById(R.id.notes_content_text)
		saveNoteBtn = findViewById(R.id.save_note_btn)
		pageTitleTextView = findViewById(R.id.page_title)
		deleteNoteTextViewBtn = findViewById(R.id.delete_note_text_view_btn)
		
		//receive data
		title = intent.getStringExtra("title")
		content = intent.getStringExtra("content")
		docId = intent.getStringExtra("docId")
		if (docId != null && !docId!!.isEmpty()) {
			isEditMode = true
		}
		with(titleEditText) { this?.setText(title) }
		with(contentEditText){this?.setText(content)}
		if (isEditMode) {
			with(pageTitleTextView){ this?.setText(R.string.edit_note) }
			with(deleteNoteTextViewBtn){this?.setVisibility(View.VISIBLE)}
		}
		with(saveNoteBtn){this?.setOnClickListener(View.OnClickListener { v: View? -> saveNote() })}
		with(deleteNoteTextViewBtn){this?.setOnClickListener(View.OnClickListener { v: View? -> deleteNoteFromFirebase() })}
	}
	
	fun saveNote() {
		val noteTitle = titleEditText!!.text.toString()
		val noteContent = contentEditText!!.text.toString()
		if (noteTitle == null || noteTitle.isEmpty()) {
			titleEditText!!.error = "Title is required"
			return
		}
		val note = Note()
		note.title = noteTitle
		note.content = noteContent
		note.timestamp = Timestamp.now()
		saveNoteToFirebase(note)
	}
	
	fun saveNoteToFirebase(note: Note?) {
		val documentReference: DocumentReference
		documentReference = if (isEditMode) {
			//update the note
			collectionReferenceForNotes.document(
				docId!!
			)
		} else {
			//create new note
			collectionReferenceForNotes.document()
		}
		documentReference.set(note!!).addOnCompleteListener { task ->
			if (task.isSuccessful) {
				//note is added
				showToast(this@NotedetailsActivity, "Note added successfully")
				finish()
			} else {
				showToast(this@NotedetailsActivity, "Failed while adding note")
			}
		}
	}
	
	fun deleteNoteFromFirebase() {
		val documentReference: DocumentReference
		documentReference = collectionReferenceForNotes.document(
			docId!!
		)
		documentReference.delete().addOnCompleteListener { task ->
			if (task.isSuccessful) {
				//note is deleted
				showToast(this@NotedetailsActivity, "Note deleted successfully")
				finish()
			} else {
				showToast(this@NotedetailsActivity, "Failed while deleting note")
			}
		}
	}
}