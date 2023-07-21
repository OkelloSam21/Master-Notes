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
import com.google.firebase.auth.FirebaseAuth
import com.samuelokello.master_notes.Utility.showToast

class LoginActivity : AppCompatActivity() {
	var etEmail: EditText? = null
	var etPassword: EditText? = null
	var btnSignIn: Button? = null
	var progressBar: ProgressBar? = null
	var tvSignUp: TextView? = null
	var tvForgotPassword: TextView? = null
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_login)
		etEmail = findViewById(R.id.user_name_edit_text)
		etPassword = findViewById(R.id.password_edit_text)
		btnSignIn = findViewById(R.id.sign_in_btn)
		progressBar = findViewById(R.id.progress_bar)
		tvSignUp = findViewById(R.id.sign_up_textview_btn)
		//        tvForgotPasword = findViewById(R.id.forgot_password_textview_btn);
		with(btnSignIn){this?.setOnClickListener(View.OnClickListener { v: View? -> loginUser() })}
		with( tvSignUp){this?.setOnClickListener(View.OnClickListener { v: View? ->
			startActivity(
				Intent(
					this@LoginActivity,
					CreateAccount::class.java
				)
			)
		})}
	}
	
	fun loginUser() {
		val email = etEmail!!.text.toString()
		val password = etPassword!!.text.toString()
		val isValidated = validateData(email, password)
		if (!isValidated) {
			return
		}
		loginAccountInFirebase(email, password)
	}
	
	fun loginAccountInFirebase(email: String?, password: String?) {
		val firebaseAuth = FirebaseAuth.getInstance()
		changeInProgress(true)
		firebaseAuth.signInWithEmailAndPassword(email!!, password!!)
			.addOnCompleteListener { task ->
				changeInProgress(false)
				if (task.isSuccessful) {
					if (firebaseAuth.currentUser!!.isEmailVerified) {
						startActivity(Intent(this@LoginActivity, MainActivity::class.java))
						finish()
					} else {
						showToast(this@LoginActivity, "Email not Verified, please verify")
					}
				} else {
					showToast(
						this@LoginActivity, task.exception!!
							.localizedMessage
					)
				}
			}
	}
	
	fun changeInProgress(inProgress: Boolean) {
		if (inProgress) {
			progressBar!!.visibility = View.VISIBLE
			btnSignIn!!.setOnClickListener(null)
			btnSignIn!!.visibility = View.GONE
		} else {
			progressBar!!.visibility = View.GONE
			//            btnSignIn.setOnClickListener(v -> createAccount());
		}
	}
	
	fun validateData(email: String?, password: String): Boolean {
		
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
		return true
	}
}