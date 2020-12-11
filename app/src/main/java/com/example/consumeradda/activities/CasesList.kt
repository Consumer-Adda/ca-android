package com.example.consumeradda.activities

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.consumeradda.R
import com.example.consumeradda.models.cardModels.ApplicationCardModel
import com.example.consumeradda.models.caseModels.CaseResponse
import com.example.shareapp.adapters.ApplicationListAdapter
import kotlinx.android.synthetic.main.activity_cases_list.*
import kotlinx.android.synthetic.main.case_type_filter.*
import kotlinx.android.synthetic.main.forgot_password_dialog.*
import kotlin.random.Random

class CasesList : AppCompatActivity(),OnCaseClicked {

    var selectedType = ""
    var casePosition = -1
    lateinit var case: ArrayList<String>
    private lateinit var caseListDetail: ArrayList<CaseResponse>
    lateinit var applicationListAdapter:ApplicationListAdapter
    private lateinit var caseList: ArrayList<ApplicationCardModel>
    private lateinit var newList: ArrayList<ApplicationCardModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cases_list)

        case = ArrayList()
        case.add("Consumer Protection Law")
        case.add("Banking Regulation")
        case.add("Insurance Law")
        case.add("Debt Recovery")
        case.add("RERA")
        case.add("Other")

        caseListDetail = ArrayList()

        for(i in 0..29)
        {
            caseListDetail.add(CaseResponse(i, "City $i", "State $i", "Applicant $i", "", "${case[Random.nextInt(0, case.size - 1)]}", "Company $i", "This is a sample description", "", "", "", "", ""))
        }

        loadCases()
        btnBackCaseListSetOnClickListener()
        tvFilterCaseSetOnClickListener()
    }

    private fun loadCases() {
        caseList = ArrayList()
        val n: Int = caseListDetail.size


            for(i in 0..n-1)
            {
                caseList.add(ApplicationCardModel(caseListDetail[i].applicantFirstName.toString(),caseListDetail[i].city.toString()+","+caseListDetail[i].state.toString(),"${caseListDetail[i].caseType}"))
            }


        applicationListAdapter= ApplicationListAdapter(caseList, this)
        rvCaseList.adapter = applicationListAdapter
        rvCaseList.layoutManager = LinearLayoutManager(this)
        rvCaseList.setHasFixedSize(true)

//        caseList.add(ApplicationCardModel("Sample Client 1","City 1, State 1","Consumer Protection Law"))

    }

    private fun tvFilterCaseSetOnClickListener() {
        tvFilterCase.setOnClickListener{
            val mDialog = Dialog(this)
            mDialog.setContentView(R.layout.case_type_filter)
            val window = mDialog.window
            window?.setLayout(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT
            )
            mDialog.setCanceledOnTouchOutside(true)
            mDialog.setCancelable(true)
            mDialog.show()
            mDialog.rbCPLaw.setOnClickListener {
                selectedType = mDialog.rbCPLaw.text.toString()
                toastMaker(selectedType)
                mDialog.dismiss()
            }
            mDialog.rbBLaw.setOnClickListener {
                selectedType = mDialog.rbBLaw.text.toString()
                toastMaker(selectedType)
                mDialog.dismiss()
            }
            mDialog.rbDRLaw.setOnClickListener {
                selectedType = mDialog.rbDRLaw.text.toString()
                toastMaker(selectedType)
                mDialog.dismiss()
            }
            mDialog.rbILaw.setOnClickListener {
                selectedType = mDialog.rbILaw.text.toString()
                toastMaker(selectedType)
                mDialog.dismiss()
            }
            mDialog.rbRERA.setOnClickListener {
                selectedType = mDialog.rbRERA.text.toString()
                toastMaker(selectedType)
                mDialog.dismiss()
            }
            mDialog.rbOther.setOnClickListener {
                selectedType = mDialog.rbOther.text.toString()
                toastMaker(selectedType)
                mDialog.dismiss()
            }
        }
    }

    private fun btnBackCaseListSetOnClickListener() {
        btnBackCaseList.setOnClickListener {
            finish()
        }
    }

    private fun toastMaker(message: String?)
    {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }

    override fun onCaseItemClicked(position: Int) {
        val intent = Intent(this,CaseDetails::class.java)
        intent.putExtra("CaseID",caseListDetail[position].caseID)
        intent.putExtra("ClientName",caseListDetail[position].applicantFirstName)
        intent.putExtra("Location",caseListDetail[position].city+", "+caseListDetail[position].state)
        intent.putExtra("caseAgainst",caseListDetail[position].caseAgainst)
        intent.putExtra("caseDescription",caseListDetail[position].caseDescription)
        intent.putExtra("caseType",caseListDetail[position].caseType)
        startActivity(intent)
    }
}

interface OnCaseClicked
{
    fun onCaseItemClicked(position: Int)
}