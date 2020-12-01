package com.example.shareapp


import android.app.Dialog
import android.app.ProgressDialog
import android.app.ProgressDialog.show
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.forgot_password_dialog.*


class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        signup_page.setOnClickListener{
            val intent= Intent(this,Sign_Up_Activity::class.java)
            startActivity(intent)
        }

        auth = FirebaseAuth.getInstance()

        Login_btn.setOnClickListener {
            if(validData())
            {
                doLogin()
            }
        }

        forgotpwd_btn.setOnClickListener {
//            val builder = AlertDialog.Builder(this)
//            builder.setTitle("Forgot Password")
//            builder.setMessage("Enter your e-mail")
//            val view=layoutInflater.inflate(R.layout.forgot_pwd,null)
//            val email= view.findViewById<EditText>(R.id.et_emailid)
//            builder.setView(view)
//            builder.setPositiveButton("Reset", DialogInterface.OnClickListener{ _, _ ->
//                forgotpwd(email)
//            })
//            builder.setNegativeButton("Close", DialogInterface.OnClickListener{ _, _ -> })
//            builder.show()

            val mDialog = Dialog(this)
            mDialog.setContentView(R.layout.forgot_password_dialog)
            val window = mDialog.window
            window?.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
            mDialog.setCanceledOnTouchOutside(true) // prevent dialog box from getting dismissed on outside touch
            mDialog.setCancelable(true)  //prevent dialog box from getting dismissed on back key pressed
            mDialog.show()
            mDialog.btnFPSubmit.setOnClickListener {
                forgotpwd(mDialog.etUserEmailFP)
            }
            mDialog.btnFPCancel.setOnClickListener{
                mDialog.dismiss()
            }
        }


    }

    private fun validData(): Boolean {
        val email=login_email.text.toString().trim()
        val pwd = login_pwd.text.toString().trim()

        if(!isEmailValid(email) ||email.isEmpty())
        {
            login_email.error="Please enter a valid email"
            login_email.requestFocus()
            return false
        }


        if(pwd.isEmpty() || pwd.length < 6)
        {
            login_pwd.error="Password must contain at-least 6 letters"
            login_pwd.requestFocus()
            return false
        }
        return true
    }

    private fun forgotpwd(email : EditText)
    {
        if(!isEmailValid(email.text.toString()) || email.text.toString().isEmpty())
        {
            return
        }

        val progress=ProgressDialog(this,R.style.AlertDialogTheme)
        progress.setMessage("Sending reset link...")
        progress.setCancelable(false)
        progress.show()
        auth.sendPasswordResetEmail(email.text.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    progress.dismiss()
                    Toast.makeText(this,"Password reset e-mail sent.",Toast.LENGTH_SHORT).show()
                }
            }
    }
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(currentUser : FirebaseUser?)
    {
        if(currentUser!=null)
        {
            if(currentUser.isEmailVerified)
            {
                startActivity(Intent(this,Dashboard::class.java))
                finish()
            }
            else
            {
                Toast.makeText(baseContext, "Verify your E-mail to get started!", Toast.LENGTH_SHORT).show()
            }

        }
        else
        {
            //Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun doLogin()
    {
        val email=login_email.text.toString().trim()
        val pwd = login_pwd.text.toString().trim()

        if(!isEmailValid(email) ||email.isEmpty())
        {
            login_email.error="Please enter a valid email"
            login_email.requestFocus()
            return
        }


        if(pwd.isEmpty())
        {
            login_pwd.error="Password must contain at-least 6 letters"
            login_pwd.requestFocus()
            return
        }

        val progress=ProgressDialog(this,R.style.AlertDialogTheme)
        progress.setMessage("Logining In...")
        progress.setCancelable(false)
        progress.show()


        auth.signInWithEmailAndPassword(email, pwd)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    progress.dismiss()
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    progress.dismiss()
                    Toast.makeText(this, "Invalid login credentials!", Toast.LENGTH_SHORT).show()
                }

            }
    }

    private fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
