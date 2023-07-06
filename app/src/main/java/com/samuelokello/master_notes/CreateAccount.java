package com.samuelokello.master_notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class CreateAccount extends AppCompatActivity {


    EditText etUsername,etEmail, etPassword, etConfirmPassword;
    Button btnSignUp;
    ProgressBar progressBar;
    TextView tvSignIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        etUsername = findViewById(R.id.user_name_edit_text);
        etEmail = findViewById(R.id.email_edit_text);
        etPassword = findViewById(R.id.password_edit_text);
        etConfirmPassword = findViewById(R.id.confirm_password_edit_text);
        btnSignUp = findViewById(R.id.sign_up_btn);
        progressBar = findViewById(R.id.progress_bar);
        tvSignIn = findViewById(R.id.sign_In_textview_btn);

        btnSignUp.setOnClickListener(v -> createAccount());
        tvSignIn.setOnClickListener(v -> startActivity(new Intent(CreateAccount.this, LoginActivity.class)));

    }


        void createAccount() {
            String username = etUsername.getText().toString();
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();
            String confirmPassword = etConfirmPassword.getText().toString();

            boolean isValidated = validateData(username, email, password, confirmPassword);

            if (!isValidated) {
                return;
            }

            createAccountInFireBase(username,email,password);
        }
        void createAccountInFireBase(String username, String email, String password) {
            changeInProgress(true);

            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        changeInProgress(false);
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Utility.showToast(CreateAccount.this,"Successfully create account,Check email to verify");
                            firebaseAuth.getCurrentUser().sendEmailVerification();
                            firebaseAuth.signOut();
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Utility.showToast(CreateAccount.this, task.getException().getLocalizedMessage());
                        }
                    });
        }

        void changeInProgress(boolean inProgress) {
            if (inProgress) {
                progressBar.setVisibility(View.VISIBLE);
                btnSignUp.setOnClickListener(null);
                btnSignUp.setVisibility(View.GONE);
            } else {
                progressBar.setVisibility(View.GONE);
                btnSignUp.setOnClickListener(v -> createAccount());
            }
        }

            boolean validateData(String username, String email, String password, String confirmPassword)
            {
                //validate username
                if (username.isEmpty()) {
                    etUsername.setError("Username is required");
                    etUsername.requestFocus();
                    return false;
                }
                //validate email
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    etEmail.setError("Invalid Email");
                    return false;
                }

                //validate password length
                if (password.length() < 8) {
                    etPassword.setError("Password should be at least 8 characters long");
                    etPassword.requestFocus();
                    return false;
                }
                //validate password match
                if (!confirmPassword.equals(confirmPassword)) {
                    etConfirmPassword.setError("Password do not match");
                    etConfirmPassword.requestFocus();
                    return false;
                }

                return true;
            }


        }



