package com.example.shareapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.shareapp.R
import kotlinx.android.synthetic.main.activity_cases_list.*

class CasesList : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cases_list)

        tvApplicationListBackSetOnClickListener()
    }

    private fun tvApplicationListBackSetOnClickListener() {
        tvApplicationListBack.setOnClickListener {
            val intent= Intent(this, Dashboard::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }
    }
}