package com.example.consumeradda.activities

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.example.consumeradda.R
import com.example.consumeradda.models.DashboardDefaultResponse
import com.example.consumeradda.service.RetrofitClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.forgot_password_dialog.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var idToken : String
    val LOGINFRAGTAG = "Login-Tag"
    val ROLETAG = "Role"
    lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        prefs=this.getSharedPreferences(
                "com.example.consumeradda",
                Context.MODE_PRIVATE
        )

//        Toast.makeText(this,"At Login Screen",Toast.LENGTH_SHORT).show()

        signup_pageSetOnClickListener()
        btnLoginSetOnClickListener()
        btnForgotPasswordSetOnClickListener()
    }

    public override fun onStart() {
        super.onStart()

        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun btnForgotPasswordSetOnClickListener() {
        btnForgotPassword.setOnClickListener {
            val mDialog = Dialog(this)
            mDialog.setContentView(R.layout.forgot_password_dialog)
            val window = mDialog.window
            window?.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
            mDialog.setCanceledOnTouchOutside(true)
            mDialog.setCancelable(true)
            mDialog.show()
            mDialog.btnFPSubmit.setOnClickListener {
                forgotpwd(mDialog.etUserEmailFP.text.toString())
            }
            mDialog.btnFPCancel.setOnClickListener{
                mDialog.dismiss()
            }
        }
    }

    private fun forgotpwd(email: String)
    {
        if(!isEmailValid(email) || email.isEmpty())
        {
            return
        }

        val progress=ProgressDialog(this, R.style.AlertDialogTheme)
        progress.setMessage("Sending reset link...")
        progress.setCancelable(false)
        progress.show()
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    progress.dismiss()
                    Toast.makeText(this,"Password reset e-mail sent.",Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun btnLoginSetOnClickListener()
    {
        btnLogin.setOnClickListener {
            if(validData())
            {
                doLogin()
            }
        }
    }

    private fun doLogin() {
        val email=etLoginEmail.text.toString().trim()
        val pwd = etLoginPassword.text.toString().trim()

//        val progress= ProgressDialog(this, R.style.AlertDialogTheme)
//        progress.setMessage("Logining In...")
//        progress.setCancelable(false)
//        progress.show()

        startLogin()



        auth.signInWithEmailAndPassword(email, pwd)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
//                    progress.dismiss()
                    updateUI(user)
//                    progress.dismiss()
                } else {
//                    progress.dismiss()
                    toastMaker("Failed To Login - " + task.exception?.message)
                }

            }
    }

    private fun startLogin() {
        pbLogin.visibility = View.VISIBLE
        etLoginEmail.isActivated = false
        etLoginEmail.isClickable = false
        etLoginPassword.isClickable = false
        etLoginPassword.isActivated = false
        btnLogin.isEnabled = false
        btnLogin.text = ""
        btnForgotPassword.isEnabled = false
    }

    private fun toastMaker(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if(currentUser!=null) {
            if (currentUser.isEmailVerified) {
                getIDToken()
//                startActivity(Intent(this, Dashboard::class.java))
                finish()
            } else {
                Toast.makeText(this, "Verify your e-mail to get started", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getIDToken() {
        idToken = ""
        auth.currentUser!!.getIdToken(true).addOnCompleteListener {
            if (it.isSuccessful) {
                Log.i(LOGINFRAGTAG, idToken)
                idToken = it.result!!.token!!
                if (idToken!="") {
                    Log.i("Token-Tag",idToken)
                    callingAfterGettingIdToken()
                }
                else{
                    toastMaker("SERVER ERROR")
                    actionWhenLoginFailed()
                }
            }
            else{
                toastMaker("SERVER ERROR")
                actionWhenLoginFailed()
            }
        }
    }

    private fun actionWhenLoginFailed() {
        toastMaker("Server Error\nPlease try again")
        pbLogin.visibility = View.INVISIBLE
        etLoginEmail.isActivated = true
        etLoginEmail.isClickable = true
        etLoginPassword.isClickable = true
        etLoginPassword.isActivated = true
        btnLogin.isEnabled = true
        btnLogin.text = "Login"
        btnForgotPassword.isEnabled = true
    }

    private fun callingAfterGettingIdToken() {
        RetrofitClient.instance.idToken=idToken
        RetrofitClient.instance.dashboardService.getUserRole()
            .enqueue(object : Callback<DashboardDefaultResponse> {
                override fun onResponse(
                    call: Call<DashboardDefaultResponse>,
                    response: Response<DashboardDefaultResponse>
                ) {
                    if (response.isSuccessful) {
                        Log.i(ROLETAG, "Role " + response.body()!!.role.toString())
                        when (response.body()?.role) {
                            0 -> {
                                prefs.edit().putInt("Role", 0).apply()
                                Log.i(LOGINFRAGTAG, "Client")
                            }
                            1 -> {
                                prefs.edit().putInt("Role", 1).apply()
                                Log.i(LOGINFRAGTAG, "Lawyer")
                            }
                        }

                        goingToDashboard()
                    } else {
                        val jObjError = JSONObject(response.errorBody()!!.string())
                        Log.i(LOGINFRAGTAG, response.toString())
                        Log.i(LOGINFRAGTAG, jObjError.getString("message"))
                        toastMaker("Login failed" + jObjError.getString("message"))
                        actionWhenLoginFailed()
                    }
                }

                override fun onFailure(call: Call<DashboardDefaultResponse>, t: Throwable) {
                    Log.i(LOGINFRAGTAG, "error" + t.message)
                    toastMaker("No Internet / Server Down")
                    actionWhenLoginFailed()
                }
            })
    }

    private fun goingToDashboard() {
        toastMaker("Logging In")

        Log.i(LOGINFRAGTAG, prefs.getInt("Role", 0).toString())

        prefs.edit().putBoolean("isLoggedIn", true).apply()

        val intent = Intent(this, Dashboard::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    private fun validData(): Boolean {
        val email=etLoginEmail.text.toString().trim()
        val pwd = etLoginPassword.text.toString().trim()

        if(!isEmailValid(email) ||email.isEmpty())
        {
            etLoginEmail.error="Please enter a valid email"
            etLoginEmail.requestFocus()
            return false
        }


        if(pwd.isEmpty() || pwd.length < 6)
        {
            etLoginPassword.error="Password must contain at-least 6 letters"
            etLoginPassword.requestFocus()
            return false
        }
        return true
    }

    private fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun signup_pageSetOnClickListener()
    {
        signup_page.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }
}