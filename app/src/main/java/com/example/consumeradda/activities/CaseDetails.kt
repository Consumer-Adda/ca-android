package com.example.consumeradda.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import com.example.consumeradda.R
import com.example.consumeradda.models.caseModels.AcceptCaseDefaultReponse
import com.example.consumeradda.models.caseModels.AcceptCaseModel
import com.example.consumeradda.service.RetrofitClient
import kotlinx.android.synthetic.main.activity_case_details.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CaseDetails : AppCompatActivity() {

    var cID: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_case_details)

        val cName = intent.getStringExtra("ClientName")
        val cLocation = intent.getStringExtra("Location")
        val cAgainst = intent.getStringExtra("caseAgainst")
        val cType = intent.getStringExtra("caseType")
        cID = intent.getStringExtra("CaseID").toInt()
        val cDes = intent.getStringExtra("caseDescription")

        tvCaseDetailRecipient.text = cName
        tvCaseDetailCaseAgainst.text = cAgainst
        tvCaseDetailCaseType.text = cType
        tvCaseDetailLocation.text = cLocation
        tvCaseDetailPurpose.text = cDes

        btnBackCaseDetailSetOnClickListener()
        btnAcceptCaseSetOnClickListener()
    }

    private fun btnAcceptCaseSetOnClickListener() {
        btnAcceptCase.setOnClickListener{
            pbAcceptCase.visibility = View.VISIBLE
            val obj = AcceptCaseModel(
                    applicationId = cID.toInt()
            )
            RetrofitClient.instance.caseService.acceptApplication(obj)
                    .enqueue(object : Callback<AcceptCaseDefaultReponse> {
                        override fun onResponse(
                                call: Call<AcceptCaseDefaultReponse>,
                                response: Response<AcceptCaseDefaultReponse>
                        ) {
                            if (response.isSuccessful) {

                                pbAcceptCase.visibility = View.INVISIBLE
                                toastMaker(response.body()?.message)

                                Handler().postDelayed({
                                    toDashboardActivity()
                                },3000)

                            } else {
                                val jObjError = JSONObject(response.errorBody()!!.string())
//                                Log.i(APPLICATIONFRAGTAG, response.toString())
//                                Log.i(APPLICATIONFRAGTAG, jObjError.getString("message"))
                                toastMaker("Failed to Accept - " + jObjError.getString("message"))
                                pbAcceptCase.visibility = View.INVISIBLE
//                                if (ApplicationsListActivity.fragnumber == 0) {
//                                    if (pbAppList != null) {
//                                        pbAppDetail2?.visibility = View.INVISIBLE
//                                    }
//                                }
                            }
                        }

                        override fun onFailure(
                                call: Call<AcceptCaseDefaultReponse>,
                                t: Throwable
                        ) {
//                            Log.i(APPLICATIONFRAGTAG, "error" + t.message)
                            toastMaker("No Internet / Server Down")
                            pbAcceptCase.visibility = View.INVISIBLE
//                            if (ApplicationsListActivity.fragnumber == 0) {
//                                if (pbAppList != null) {
//                                    pbAppDetail2?.visibility = View.INVISIBLE
//                                }
//                            }
                        }
                    })
        }
    }

    private fun toDashboardActivity() {
        val intent = Intent(this, Dashboard::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    private fun toastMaker(message: String?)
    {
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show()
    }

    private fun btnBackCaseDetailSetOnClickListener() {
        btnBackCaseDetail.setOnClickListener{
            val intent = Intent(this,CasesList::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }
    }
}