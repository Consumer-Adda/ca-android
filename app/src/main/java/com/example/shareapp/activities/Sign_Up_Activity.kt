package com.example.shareapp.activities

import android.app.Dialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.shareapp.R
import com.example.shareapp.activities.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.forgot_password_dialog.btnFPSubmit
import kotlinx.android.synthetic.main.lawyer_phone_dialog.*
import kotlinx.android.synthetic.main.sign_up.*

const val systemBlue ="#5896d6"
const val white = "#fff"

class Sign_Up_Activity :AppCompatActivity(){

    val positiveButtonClick = { dialog: DialogInterface, which: Int ->
        Toast.makeText(applicationContext,
            android.R.string.yes, Toast.LENGTH_SHORT).show()
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var db:FirebaseFirestore
    private var role = -1

    private var boolEmail = false
    private var boolName = false
    private var boolPassword = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_up)

        Login_pg.setOnClickListener {
            val intent= Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }


        signup_btn.setOnClickListener {
            if(validData())
            {
                if(role==1)
                {
                    showPhoneDialog()
                }
                else
                {
                    register_user()
                }
            }
        }
        db=FirebaseFirestore.getInstance()

        signup_pwd.setOnClickListener {
            val builder=AlertDialog.Builder(this)

            with(builder)
            {
                setTitle("Alert")
                setMessage("Password should contain atleast 6 letters!")
                setPositiveButton("OK", DialogInterface.OnClickListener(function = positiveButtonClick))

                show()
            }
        }

        auth = FirebaseAuth.getInstance()

        val email=signup_email.text.toString().trim()
        val pwd = signup_pwd.text.toString().trim()
        val naam=cl_name.text.toString().trim()

        rbClient.setOnClickListener {
            role = 0
            rbClient.isChecked = true
            rbLawyer.isChecked = false
        }

        rbLawyer.setOnClickListener {
            role = 1
            rbLawyer.isChecked = true
            rbClient.isChecked = false
        }

    }

    private fun validData(): Boolean {
        val email=signup_email.text.toString().trim()
        val pwd = signup_pwd.text.toString().trim()
        val naam=cl_name.text.toString().trim()

        if(naam.isEmpty())
        {
            cl_name.error="Field should not be empty"
            cl_name.requestFocus()
            return false
        }
        if(!isEmailValid(email) || email.isEmpty())
        {
            signup_email.error="Please enter a valid email"
            signup_email.requestFocus()
            return false
        }


        if(pwd.length < 6 || pwd.isEmpty())
        {
            signup_pwd.error="Password must contain at-least 6 letters"
            signup_pwd.requestFocus()
            return false
        }

        if(role==-1)
        {
            Toast.makeText(this,"Select some role to Sign Up",Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }


    private fun showPhoneDialog() {
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
        mDialog.btnFPSubmit.setOnClickListener {
            if(mDialog.etLaywerPhoneNumber.text.toString().length == 10) {
                mDialog.dismiss()
                register_user()
            }
            else
                Toast.makeText(this,"Enter a valid phone number to register as a new Lawyer",Toast.LENGTH_SHORT).show()
        }
    }

    private fun register_user()
    {
        val email=signup_email.text.toString().trim()
        val pwd = signup_pwd.text.toString().trim()
        val naam=cl_name.text.toString().trim()


        //signup_email?.error="Invalid Email"

        if(naam.isEmpty())
        {
            cl_name.error="Field should not be empty"
            cl_name.requestFocus()
            return
        }
        if(!isEmailValid(email) || email.isEmpty())
        {
            signup_email.error="Please enter a valid email"
            signup_email.requestFocus()
            return
        }


        if(pwd.length < 6 || pwd.isEmpty())
        {
            signup_pwd.error="Password must contain at-least 6 letters"
            signup_pwd.requestFocus()
            return
        }

        if(role==-1)
        {
            Toast.makeText(this,"Select some role to Sign Up",Toast.LENGTH_SHORT).show()
            return
        }

        val progress= ProgressDialog(this,R.style.AlertDialogTheme)
        progress.setMessage("Signing Up...")
        progress.setCancelable(false)
        progress.show()
        auth.createUserWithEmailAndPassword(email, pwd)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful)
                {
                    progress.dismiss()
                    val user = auth.currentUser
                    try {
                        val uid=user?.uid.toString()
                        val info=HashMap<String,Any>()
                        info.put("Name",naam)
                        info.put("Email",email)
                        info.put("Open-Cases",0)
                        //if (casess != null) {
//                            info.put("cases",casess!!)
                        //}
                        db.collection("users").document(email).set(info)
                            .addOnSuccessListener {
                                //Toast.makeText(this,"Data saved",Toast.LENGTH_SHORT).show()
                                user?.sendEmailVerification()
                                    ?.addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            startActivity(Intent(this, MainActivity::class.java))
                                            finish()
                                        }
                                        else
                                        {
                                            Toast.makeText(baseContext, "Sign-Up failed.Try again after sometime",
                                                Toast.LENGTH_SHORT).show()
                                        }
                                    }
                            }
                            .addOnFailureListener {
                                Toast.makeText(this,"Data not saved",Toast.LENGTH_SHORT).show()

                            }

                    }
                    catch (e:Exception)
                    {
                        Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show()
                    }

                } else {
                    progress.dismiss()
                    Toast.makeText(baseContext, "Sign-Up failed.Try again after sometime",
                        Toast.LENGTH_SHORT).show()
                }


            }
    }

    private fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}

private fun EditText.addTextChangedListener(function: () -> Unit) {

}