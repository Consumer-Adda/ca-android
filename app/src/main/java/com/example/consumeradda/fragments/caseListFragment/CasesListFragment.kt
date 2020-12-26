package com.example.consumeradda.fragments.caseListFragment

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionInflater
import com.example.consumeradda.R
import com.example.consumeradda.activities.CasesListActivity
import com.example.consumeradda.activities.Dashboard
import com.example.consumeradda.models.caseModels.CaseResponse
import com.example.shareapp.adapters.ApplicationListAdapter
import kotlinx.android.synthetic.main.case_type_filter.*
import kotlinx.android.synthetic.main.fragment_cases_list.*
import kotlinx.android.synthetic.main.location_alert_dialog.*

class CasesListFragment : Fragment(), OnApplicationClicked {

    val APPLICATIONTAG="APPLICATIONTAG"
    lateinit var applicationListAdapter: ApplicationListAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = TransitionInflater.from(requireContext())
        enterTransition = inflater.inflateTransition(R.transition.explode)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cases_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        btnClearFilterSetOnClickListener()
        showLocationMessage()
        btnBackCaseListSetOnClickListener()
        tvFilterCaseSetOnClickListener()
        getApplications()
        loadCaseCards()
    }

    private fun btnClearFilterSetOnClickListener() {
        btnClearFilter.setOnClickListener {
            CasesListActivity.caseType = ""
            btnClearFilter.visibility = View.INVISIBLE
            CasesListActivity.applicationList = CasesListActivity.originalList
            loadCaseCards()
        }
    }

    private fun showLocationMessage() {
        val mDialog = Dialog(this.context!!)
        mDialog.setContentView(R.layout.location_alert_dialog)
        val window = mDialog.window
        window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        mDialog.setCanceledOnTouchOutside(false)
        mDialog.setCancelable(false)
        mDialog.show()
        mDialog.btnLocationAlert.setOnClickListener {
            mDialog.dismiss()
        }
    }

    private fun loadCaseCards() {
        applicationListAdapter = ApplicationListAdapter(CasesListActivity.applicationList,this)
        rvCaseList.adapter = applicationListAdapter
        rvCaseList.layoutManager = LinearLayoutManager(this.context)
        rvCaseList.setHasFixedSize(true)
    }

    private fun getApplications() {
        var tempList = mutableListOf<CaseResponse>()
        tempList.add(CaseResponse("CA_RERA_1","Kerala","Trivandrumpuram","Rajeev","Singh","RERA","Mr. Vinod Singh","Illegal kabzaa on my property",1200000,"","","","","",""))
        tempList.add(CaseResponse("CA_BLAW_1","Delhi","New Delhi","Ankur","Sharma","Banking Law","ICICI Bank","Nominee rights not granted",50000,"","","","","",""))
        tempList.add(CaseResponse("CA_CPLaw_1","Uttar Pradesh","Agra","Vishesh","Singh","Consumer Protection Law","IDFC First Bank","Accidental Claim",15000,"","","","","",""))

        CasesListActivity.applicationList = tempList
        CasesListActivity.originalList = tempList
    }

    private fun btnBackCaseListSetOnClickListener() {
        btnBackCaseList.setOnClickListener {
            val intent=Intent(context, Dashboard::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }
    }

    private fun tvFilterCaseSetOnClickListener() {
        tvFilterCase.setOnClickListener {
            val mDialog = Dialog(this.context!!)
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
                CasesListActivity.caseType = "Consumer Protection Law"
                mDialog.dismiss()
                showFilteredCases()
            }
            mDialog.rbOther.setOnClickListener {
                CasesListActivity.caseType = "Other"
                mDialog.dismiss()
                showFilteredCases()
            }
            mDialog.rbRERA.setOnClickListener {
                CasesListActivity.caseType = "RERA"
                mDialog.dismiss()
                showFilteredCases()
            }
            mDialog.rbILaw.setOnClickListener {
                CasesListActivity.caseType = "Insurance Law"
                mDialog.dismiss()
                showFilteredCases()
            }
            mDialog.rbDRLaw.setOnClickListener {
                CasesListActivity.caseType = "Debt Recovery"
                mDialog.dismiss()
                showFilteredCases()
            }
            mDialog.rbBLaw.setOnClickListener {
                CasesListActivity.caseType = "Banking Law"
                mDialog.dismiss()
                showFilteredCases()
            }
        }
    }

    private fun showFilteredCases() {
        var tempList = mutableListOf<CaseResponse>()
        btnClearFilter.visibility = View.VISIBLE
        for(curr in CasesListActivity.originalList)
        {
            if(curr.caseType == CasesListActivity.caseType)
            {
                tempList.add(curr)
            }
        }

        if(tempList.size == 0)
        {
            Toast.makeText(context,"No case of ${CasesListActivity.caseType} found!",Toast.LENGTH_LONG).show()
        }

        CasesListActivity.applicationList = tempList
        loadCaseCards()
    }

    override fun onApplicationItemClicked(position: Int) {
        CasesListActivity.selectedApplicationNumber = position
        activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.fl_wrapper_applications, CaseDetailsFragment())?.commit()
//        Toast.makeText(this.context,"${CasesListActivity.applicationList[position].applicantFirstName}'s Application",Toast.LENGTH_SHORT).show()
    }

}

interface OnApplicationClicked
{
    fun onApplicationItemClicked(position: Int)
}