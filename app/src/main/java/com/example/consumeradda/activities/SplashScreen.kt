package com.example.consumeradda.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.util.Log
import com.example.consumeradda.R
import com.example.consumeradda.service.RetrofitClient
import com.google.firebase.auth.FirebaseAuth

class SplashScreen : AppCompatActivity() {

    lateinit var prefs: SharedPreferences
    lateinit var countdownTimer: CountDownTimer
    lateinit var mAuth: FirebaseAuth
    var isSplashScreenDone=false
    var idTokenn=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        prefs=this.getSharedPreferences("com.example.consumeradda", Context.MODE_PRIVATE)

        mAuth= FirebaseAuth.getInstance()

        val isLoggedIn=prefs.getBoolean("isLoggedIn", false)

        countdownTimer = object : CountDownTimer(2000, 1000) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                callIntent(isLoggedIn)
            }
        }
        countdownTimer.start()
    }

    private fun callIntent(loggedIn: Boolean) {
        if (!isSplashScreenDone) {
            if (loggedIn && mAuth.currentUser!=null) {
                getIDToken()
                goToDashboard()
            } else {
                goToAuth()
            }
        }
    }

    private fun goToAuth() {
        isSplashScreenDone=true
        val intent=Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    private fun goToDashboard() {
        isSplashScreenDone=true
        val intent=Intent(this, Dashboard::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    private fun getIDToken() {
        idTokenn = ""
        mAuth.currentUser!!.getIdToken(true).addOnCompleteListener {
            if (it.isSuccessful) {
                idTokenn = it.result!!.token!!
                RetrofitClient.instance.idToken = idTokenn
                Log.i("Testing",idTokenn)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (!isSplashScreenDone) {
            countdownTimer.start()
        }
    }
}