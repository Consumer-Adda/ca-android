package com.example.shareapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.viewpager.widget.PagerAdapter
import kotlinx.android.synthetic.main.case_card.view.*

class DashboardCardAdapter (private val context: Context, private val cardlist: ArrayList<DashboardCardModel>): PagerAdapter()
{
    override fun getCount(): Int {
        return cardlist.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view==`object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val view = LayoutInflater.from(context).inflate(R.layout.case_card,container,false)

        val currentModel=cardlist[position]

        val appNumber = currentModel.caseID
        val clientName = currentModel.client
        val lawyerName = currentModel.lawyer
        val caseType=currentModel.caseType

        view.tvCardCaseIdHeader.text = appNumber
        view.tvCardClientName.text = clientName
        view.tvCardLawyerName.text = lawyerName
        view.tvCardCaseType.text = caseType

        view.setOnClickListener{
            Toast.makeText(context,"Case ID $appNumber", Toast.LENGTH_SHORT).show()
        }

        container.addView(view, position)

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}