package com.samuelokello.master_notes

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
class SplashActivity : AppCompatActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_splash)
		Handler().postDelayed({ //                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//                if(currentUser == null){
//                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
//                }else{
//                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
//                }
			startActivity(Intent(this@SplashActivity, MainActivity::class.java))
			finish()
		}, 2000)
	}
}