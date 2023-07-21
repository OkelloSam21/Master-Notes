package com.samuelokello.master_notes;

import androidx.annotation.NonNull;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText etEmail, etPassword;
    Button btnSignIn;
    ProgressBar progressBar;
    TextView tvSignUp, tvForgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.user_name_edit_text);
        etPassword = findViewById(R.id.password_edit_text);
        btnSignIn = findViewById(R.id.sign_in_btn);

        progressBar = findViewById(R.id.progress_bar);
        tvSignUp = findViewById(R.id.sign_up_textview_btn);
//        tvForgotPasword = findViewById(R.id.forgot_password_textview_btn);

        btnSignIn.setOnClickListener((v) -> loginUser());
        tvSignUp.setOnClickListener((v) -> startActivity
                (new Intent(LoginActivity.this, CreateAccount.class)));
    }

    void loginUser(){
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        boolean isValidated = validateData(email, password);

        if (!isValidated) {
            return;
        }

        loginAccountInFirebase(email,password);
    }

    void loginAccountInFirebase(String email, String password){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        changeInProgress(true);
        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        changeInProgress(false);
                        if (task.isSuccessful()){
                            if (firebaseAuth.getCurrentUser().isEmailVerified()){
                                startActivity(new Intent(LoginActivity.this,MainActivity.class));
                                finish();
                            }else {
                                Utility.showToast(LoginActivity.this,"Email not Verified, please verify");
                            }
                        }else {
                            Utility.showToast(LoginActivity.this, task.getException().getLocalizedMessage());
                        }
                    }
                });
    }
    void changeInProgress(boolean inProgress) {
        if (inProgress) {
            progressBar.setVisibility(View.VISIBLE);
            btnSignIn.setOnClickListener(null);
            btnSignIn.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
//            btnSignIn.setOnClickListener(v -> createAccount());
        }
    }

    boolean validateData(String email, String password)
    {

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


        return true;
    }


}