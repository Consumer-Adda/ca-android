package com.example.consumeradda.activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.consumeradda.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_complaint_form.*


class ComplaintForm : AppCompatActivity() {

    private lateinit var mAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complaint_form)

        mAuth=FirebaseAuth.getInstance()
        val curruser= mAuth.currentUser
        val em=curruser?.email
        etClientEmail.text=em?.toEditable()

        btnBack.setOnClickListener {
            val intent = Intent(this, Dashboard::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }

        btnAttachment1.setOnClickListener {
            btnAttachment2.isVisible = true
        }

        btnAttachment2.setOnClickListener {
            btnAttachment3.isVisible = true
        }

        btnAttachment3.setOnClickListener {
            btnAttachment4.isVisible = true
        }

        btnAttachment4.setOnClickListener {
            btnAttachment5.isVisible = true
        }

        btnAttachment5.setOnClickListener {
            Toast.makeText(this,"Bas bhai ho gya! ",Toast.LENGTH_SHORT).show()
        }
    }
    fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)

}

