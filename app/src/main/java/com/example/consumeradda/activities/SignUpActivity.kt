@file:Suppress("DEPRECATION")

package com.example.consumeradda.activities

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.widget.Toast
import com.example.consumeradda.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.lawyer_phone_dialog.*

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private var role = -1
    private var lawyerPhoneNumber: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        auth = FirebaseAuth.getInstance()

        rbClientSetOnClickListener()
        rbLawyerSetOnClickListener()
        btnSignupSetOnClickListener()
        Login_pgSetOnClickListener()
    }

    private fun Login_pgSetOnClickListener() {
        Login_pg.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }
    }

    private fun btnSignupSetOnClickListener() {
        btnSignup.setOnClickListener {
            if(validData())
            {
                if(role == 1)
                {
                    getPhoneNumber()
                }
                else
                {
                    doSignUp()
                }
            }
        }
    }

    private fun validData(): Boolean {

        val userName = etSignUpName.text.toString()
        val userEmail = etSignUpEmail.text.toString()
        val userPassword = etSignUpPassword.text.toString()

        if(userName.isNullOrBlank() || userName.isNullOrEmpty())
        {
            etSignUpName.error="Field should not be empty"
            etSignUpName.requestFocus()
            return false
        }

        if(!isEmailValid(userEmail) || userEmail.isNullOrEmpty() || userEmail.isNullOrBlank())
        {
            etSignUpEmail.error="Please enter a valid email"
            etSignUpEmail.requestFocus()
            return false
        }

        if(userPassword.length < 6 || userPassword.isNullOrBlank() || userPassword.isNullOrEmpty())
        {
            etSignUpPassword.error="Password must contain at-least 6 letters"
            etSignUpPassword.requestFocus()
            return false
        }

        if( role==-1 )
        {
            Toast.makeText(this,"Select some role to Sign Up",Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun doSignUp() {
        val userName = etSignUpName.text.toString().trim()
        val userEmail = etSignUpEmail.text.toString().trim()
        val userPassword = etSignUpPassword.text.toString().trim()

        val progress= ProgressDialog(this, R.style.AlertDialogTheme)
        progress.setMessage("Signing Up...")
        progress.setCancelable(false)
        progress.show()
        auth.createUserWithEmailAndPassword(userEmail, userPassword)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful)
                {
                    progress.dismiss()
                    val user = auth.currentUser
                    user?.sendEmailVerification()
                        ?.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                startActivity(Intent(this, LoginActivity::class.java))
                                finish()
                            }
                            else
                            {
                                Toast.makeText(baseContext, "Sign-Up failed.Try again after sometime",
                                    Toast.LENGTH_SHORT).show()
                            }
                        }
                }
                else
                {
                    progress.dismiss()
                    Toast.makeText(baseContext, "Sign-Up failed.Try again after sometime",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun getPhoneNumber() {
        val mDialog = Dialog(this)
        mDialog.setContentView(R.layout.lawyer_phone_dialog)
        val window = mDialog.window
        window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        mDialog.setCanceledOnTouchOutside(false)
        mDialog.setCancelable(false)
        mDialog.show()
        mDialog.btnPhoneSubmit.setOnClickListener {
            if(mDialog.etLawyerPhoneNumber.text.toString().length == 10) {
                lawyerPhoneNumber = mDialog.etLawyerPhoneNumber.text.toString()
                mDialog.dismiss()
                doSignUp()
            }
            else
                Toast.makeText(this,"Enter a valid phone number to register as a new Lawyer",Toast.LENGTH_SHORT).show()
        }
    }

    private fun rbLawyerSetOnClickListener() {
        rbLawyer.setOnClickListener {
            role = 1
            rbLawyer.isChecked = true
            rbClient.isChecked = false
        }
    }

    private fun rbClientSetOnClickListener() {
        rbClient.setOnClickListener {
            role = 0
            rbClient.isChecked = true
            rbLawyer.isChecked = false
        }
    }

    private fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}