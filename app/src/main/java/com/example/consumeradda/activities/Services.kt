package com.example.consumeradda.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.consumeradda.R
import kotlinx.android.synthetic.main.activity_services.*

class Services : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_services)

//        setSupportActionBar(toolbar)
//        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        btnBackServices.setOnClickListener {
            val intent = Intent(this, Dashboard::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
//            finish()
        }
    }
}