package com.example.shareapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_dashboard.*

class Dashboard : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        db= FirebaseFirestore.getInstance()


        complaint_btn.setOnClickListener {
            startActivity(Intent(this,ComplaintForm::class.java))
        }


    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId)
        {
            R.id.about-> startActivity(Intent(this,About::class.java))
            R.id.service->startActivity(Intent(this,Services::class.java))
//            R.id.ca_help->startActivity(Intent(this,Steps::class.java))
            R.id.sign_out->niklo()
            R.id.contact->startActivity(Intent(this,ContactUs::class.java));
        }
        return super.onOptionsItemSelected(item)
    }

    private fun niklo()
    {
        FirebaseAuth.getInstance().signOut()
        finish()
        startActivity(Intent(this,MainActivity::class.java))
    }
}
