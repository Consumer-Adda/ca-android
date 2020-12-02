package com.example.shareapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.shareapp.R
import kotlinx.android.synthetic.main.activity_chat_detail.*

class ChatDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_detail)

        btnBackAppDetailSetOnClickListener()
    }

    private fun btnBackAppDetailSetOnClickListener() {
        btnBackAppDetail.setOnClickListener {
            finish()
        }
    }
}