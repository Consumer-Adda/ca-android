package com.example.consumeradda.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.example.consumeradda.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_chat.*

class ChatActivity : AppCompatActivity() {

    private var btnAttachmentClicked : Boolean = false
    val PDF = 0
    val IMG = 1
    lateinit var mAuth: FirebaseAuth
    lateinit var mStorageRef: StorageReference

    private val rotateOpen: Animation by lazy { AnimationUtils.loadAnimation(
            this,
            R.anim.rotate_open_anim
    ) }
    private val rotateClose: Animation by lazy { AnimationUtils.loadAnimation(
            this,
            R.anim.rotate_close_anim
    ) }
    private val fromBottom: Animation by lazy { AnimationUtils.loadAnimation(
            this,
            R.anim.from_bottom_anim
    ) }
    private val toBottom: Animation by lazy { AnimationUtils.loadAnimation(
            this,
            R.anim.to_bottom_anim
    ) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        mAuth= FirebaseAuth.getInstance()
        mStorageRef = FirebaseStorage.getInstance().reference

        val applicantName = intent.getStringExtra("Client")
        btnChatDetails.text = applicantName

        btnBackChatSetOnClickListener()
        btnChatDetailsSetOnClickListener()
        btnAddAttachmentSetOnClickListener()
    }

    private fun btnAddAttachmentSetOnClickListener() {
        btnAddAttachment.setOnClickListener {
            onAddButtonClicked()
        }

        btnAddPdf.setOnClickListener {
            val intent = Intent()
            intent.type = "application/pdf"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select PDF"), PDF)
        }

        btnAddImage.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select PDF"), IMG)
        }
    }

    private fun onAddButtonClicked() {
        fromBottom.duration=500
        toBottom.duration=300
        rotateOpen.duration=300
        rotateClose.duration=300

        if(!btnAttachmentClicked)
        {
            btnAddPdf.visibility= View.VISIBLE
            btnAddImage.visibility= View.VISIBLE
            btnAddImage.isClickable=true
            btnAddPdf.isClickable=true
            btnAddImage.isFocusable=true
            btnAddPdf.isFocusable=true
            btnAddImage.isEnabled=true
            btnAddPdf.isEnabled=true
            btnAddPdf.startAnimation(fromBottom)
            btnAddImage.startAnimation(fromBottom)
            btnAddAttachment.startAnimation(rotateOpen)
        }
        else
        {
            btnAddPdf.visibility=View.INVISIBLE
            btnAddImage.visibility=View.INVISIBLE
            btnAddImage.isClickable=false
            btnAddPdf.isClickable=false
            btnAddImage.isFocusable=false
            btnAddPdf.isFocusable=false
            btnAddImage.isEnabled=false
            btnAddPdf.isEnabled=false
            btnAddPdf.startAnimation(toBottom)
            btnAddImage.startAnimation(toBottom)
            btnAddAttachment.startAnimation(rotateClose)
        }

        btnAttachmentClicked = !btnAttachmentClicked
    }

    private fun btnChatDetailsSetOnClickListener() {
        btnChatDetails.setOnClickListener{
            startActivity(Intent(this,ChatDetailActivity::class.java))
        }
    }

    private fun btnBackChatSetOnClickListener() {
        btnBackChat.setOnClickListener {
            val intent = Intent(this,Dashboard::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }
    }
}