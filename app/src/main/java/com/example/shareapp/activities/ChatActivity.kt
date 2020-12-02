package com.example.shareapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.shareapp.R
import kotlinx.android.synthetic.main.activity_chat.*

class ChatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

//        val applicantName = Dashboard.cardPositionClicked
//        val applicantName = intent.getStringExtra("ApplicantName")
//        btnChatDetails.text = applicantName

        btnChatDetailsSetOnClickListener()
        btnBackChatSetOnClickListener()
    }

    private fun btnChatDetailsSetOnClickListener() {
        Toast.makeText(this,"Work in Progress",Toast.LENGTH_SHORT).show()
    }

    private fun btnBackChatSetOnClickListener() {
        startActivity(Intent(this,Dashboard::class.java))
        finish()
    }
}