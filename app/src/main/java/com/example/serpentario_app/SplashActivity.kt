package com.example.serpentario_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Window
import com.google.firebase.auth.FirebaseAuth

@Suppress("DEPRECATION")
class SplashActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_splash)

        //Usuario Firebase
        mAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser

        //Verificación de sesión iniciada
        Handler().postDelayed({
            if (user != null){
                goToMainActivity()
            }
            else{
                goToLoginActivity()
            }
        },2000)
    }

    private fun goToMainActivity() {
        var intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun goToLoginActivity() {
        var intent = Intent(this,LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}