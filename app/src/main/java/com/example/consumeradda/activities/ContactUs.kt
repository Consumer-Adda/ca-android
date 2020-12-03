package com.example.consumeradda.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.consumeradda.R
import kotlinx.android.synthetic.main.activity_contact_us.*

class ContactUs : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_us)

        btnBackContactUs.setOnClickListener {
            val intent = Intent(this, Dashboard::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }

        twitter_btn.setOnClickListener {
            val intent= Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/Consumeradda"))
            startActivity(intent)
        }

        fb_btn.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/ConsumerAdda/")))
        }

        gmail_btn.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            val recipients = arrayOf("consumersadda@gmail.com")
            intent.putExtra(Intent.EXTRA_EMAIL, recipients)

            intent.type = "text/html"
            intent.setPackage("com.google.android.gm")
            startActivity(Intent.createChooser(intent, "Send mail"))
        }

        call_btn.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.setData(Uri.parse("tel:8888067063"))
            startActivity(intent)
        }
    }
}
