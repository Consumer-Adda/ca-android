package com.example.consumeradda.fragments.caseListFragment

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.transition.TransitionInflater
import com.example.consumeradda.R
import com.example.consumeradda.activities.CasesListActivity
import kotlinx.android.synthetic.main.fragment_case_details.*
import kotlinx.android.synthetic.main.fragment_cases_list.*


class CaseDetailsFragment : Fragment() {

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
        return inflater.inflate(R.layout.fragment_case_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnBackCaseDetailSetOnClickListener()
        btnAcceptCaseSetOnClickListener()
        displayData()
    }

    private fun btnBackCaseDetailSetOnClickListener() {
        btnBackCaseDetail.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.fl_wrapper_applications, CasesListFragment())?.commit()
        }
    }

    private fun displayData() {
        tvCaseDetailRecipient.text = CasesListActivity.applicationList[CasesListActivity.selectedApplicationNumber].applicantFirstName+" "+CasesListActivity.applicationList[CasesListActivity.selectedApplicationNumber].applicantLastName
        tvCaseDetailLocation.text = CasesListActivity.applicationList[CasesListActivity.selectedApplicationNumber].district+", "+CasesListActivity.applicationList[CasesListActivity.selectedApplicationNumber].state
        tvCaseDetailCaseAgainst.text = CasesListActivity.applicationList[CasesListActivity.selectedApplicationNumber].caseAgainst
        tvCaseDetailCaseType.text = CasesListActivity.applicationList[CasesListActivity.selectedApplicationNumber].caseType
        tvCaseDetailMoneyInvolved.text = CasesListActivity.applicationList[CasesListActivity.selectedApplicationNumber].moneyInvolved.toString()
        tvCaseDetailPurpose.text = CasesListActivity.applicationList[CasesListActivity.selectedApplicationNumber].caseDescription
    }

    private fun btnAcceptCaseSetOnClickListener() {
        btnAcceptCase.setOnClickListener {
            btnAcceptCase.text = ""
            pbAcceptCase.visibility = View.VISIBLE
            btnAcceptCase.isEnabled = false
            btnAcceptCase.isClickable = false
            Handler().postDelayed({
                btnAcceptCase.text = "Accept Case"
                btnAcceptCase.isClickable = true
                btnAcceptCase.isEnabled = true
                Toast.makeText(context,"Work in progress!", Toast.LENGTH_LONG).show()
                pbAcceptCase.visibility = View.INVISIBLE
            },3500)
//            Toast.makeText(this.context,"Ruko zara sabar karo",Toast.LENGTH_SHORT).show()
        }
    }

}