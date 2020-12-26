package com.example.consumeradda.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.consumeradda.R
import com.example.consumeradda.fragments.caseListFragment.CasesListFragment
import com.example.consumeradda.models.LocationDataModel
import com.example.consumeradda.models.caseModels.CaseResponse
import com.example.shareapp.adapters.ApplicationListAdapter

class CasesListActivity : AppCompatActivity() {

    val APPLICATIONTAG="APPLICATIONTAG"
    lateinit var applicationListAdapter: ApplicationListAdapter

    companion object {
        var applicationList= mutableListOf<CaseResponse>()
        var originalList = mutableListOf<CaseResponse>()
        var selectedApplicationNumber=-1
        var fragnumber=0
        var dataForLocationFrag=0 // 1 - state, 2 - district
        var state=""
        var district=""
        var filterState=""
        var filterDistrict=""
        var stateNum=-1
        var districtNum=-1
        var caseType=""
        lateinit var locationDataModel: LocationDataModel
        var isLocationFiltered=false
        var isCaseTypeFiltered = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cases_list)

        supportFragmentManager.beginTransaction().replace(R.id.fl_wrapper_applications, CasesListFragment()).commit()
    }
}