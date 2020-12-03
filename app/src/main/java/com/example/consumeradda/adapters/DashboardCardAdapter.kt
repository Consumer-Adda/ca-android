package com.example.shareapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.viewpager.widget.PagerAdapter
import com.example.consumeradda.R

import com.example.consumeradda.activities.OnCardClicked
import com.example.shareapp.models.DashboardCardModel
import kotlinx.android.synthetic.main.case_card.view.*

class DashboardCardAdapter (private val context: Context, private val cardlist: ArrayList<DashboardCardModel>, private var onCardClicked: OnCardClicked): PagerAdapter()
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

        if(lawyerName != "N/A")
        {
            view.btnChat.visibility = View.VISIBLE
        }

        when(currentModel.status)
        {
            0 ->
            {
                view.pbComplaintSubmitted.progress = 100
            }
            1->
            {
                view.pbComplaintSubmitted.progress = 100
                view.pbMediation.progress = 100
            }
            2->
            {
                view.pbComplaintSubmitted.progress = 100
                view.pbMediation.progress = 100
                view.pbLegalNotice.progress = 100
            }
            3->
            {
                view.pbComplaintSubmitted.progress = 100
                view.pbMediation.progress = 100
                view.pbLegalNotice.progress = 100
                view.pbCaseFiled.progress = 100
            }

        }


        view.setOnClickListener{

            if(lawyerName != "N/A")
            {
                Toast.makeText(context,"$clientName's Application", Toast.LENGTH_SHORT).show()
                onCardClicked.onCardClicked(position)
            }
            else{
                Toast.makeText(context,"No lawyer has accepted\nyour case yet!",Toast.LENGTH_SHORT).show()
            }

//            Toast.makeText(context,"Case ID $appNumber", Toast.LENGTH_SHORT).show()
        }

        container.addView(view, position)

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}