package com.samuelokello.master_notes

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.samuelokello.master_notes.Utility.showToast

class CreateAccount : AppCompatActivity() {
	private var etUsername: EditText? = null
	private var etEmail: EditText? = null
	private var etPassword: EditText? = null
	private var etConfirmPassword: EditText? = null
	private var btnSignUp: Button? = null
	private var progressBar: ProgressBar? = null
	private var tvSignIn: TextView? = null
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_create_account)
		etUsername = findViewById(R.id.user_name_edit_text)
		etEmail = findViewById(R.id.email_edit_text)
		etPassword = findViewById(R.id.password_edit_text)
		etConfirmPassword = findViewById(R.id.confirm_password_edit_text)
		btnSignUp = findViewById(R.id.sign_up_btn)
		progressBar = findViewById(R.id.progress_bar)
		tvSignIn = findViewById(R.id.sign_In_textview_btn)
		with(btnSignUp){this?.setOnClickListener(View.OnClickListener { v: View? -> createAccount() })}
		with(tvSignIn){this?.setOnClickListener(View.OnClickListener { v: View? ->
			startActivity(
				Intent(
					this@CreateAccount,
					LoginActivity::class.java
				)
			)
		})}
	}
	
	fun createAccount() {
		val username = etUsername!!.text.toString()
		val email = etEmail!!.text.toString()
		val password = etPassword!!.text.toString()
		val confirmPassword = etConfirmPassword!!.text.toString()
		val isValidated = validateData(username, email, password, confirmPassword)
		if (!isValidated) {
			return
		}
		createAccountInFireBase(username, email, password)
	}
	
	fun createAccountInFireBase(username: String?, email: String?, password: String?) {
		changeInProgress(true)
		val firebaseAuth = FirebaseAuth.getInstance()
		firebaseAuth.createUserWithEmailAndPassword(email!!, password!!)
			.addOnCompleteListener { task: Task<AuthResult?> ->
				changeInProgress(false)
				if (task.isSuccessful) {
					// Sign in success, update UI with the signed-in user's information
					showToast(
						this@CreateAccount,
						"Successfully create account,Check email to verify"
					)
					firebaseAuth.currentUser!!.sendEmailVerification()
					firebaseAuth.signOut()
					finish()
				} else {
					// If sign in fails, display a message to the user.
					showToast(this@CreateAccount, task.exception!!.localizedMessage)
				}
			}
	}
	
	fun changeInProgress(inProgress: Boolean) {
		if (inProgress) {
			progressBar!!.visibility = View.VISIBLE
			btnSignUp!!.setOnClickListener(null)
			btnSignUp!!.visibility = View.GONE
		} else {
			progressBar!!.visibility = View.GONE
			btnSignUp!!.setOnClickListener { createAccount() }
		}
	}
	
	fun validateData(
		username: String,
		email: String?,
		password: String,
		confirmPassword: String
	): Boolean {
		//validate username
		if (username.isEmpty()) {
			etUsername!!.error = "Username is required"
			etUsername!!.requestFocus()
			return false
		}
		//validate email
		if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
			etEmail!!.error = "Invalid Email"
			return false
		}
		
		//validate password length
		if (password.length < 8) {
			etPassword!!.error = "Password should be at least 8 characters long"
			etPassword!!.requestFocus()
			return false
		}
		//validate password match
		if (confirmPassword != password) {
			etConfirmPassword!!.error = "Password do not match"
			etConfirmPassword!!.requestFocus()
			return false
		}
		return true
	}
}