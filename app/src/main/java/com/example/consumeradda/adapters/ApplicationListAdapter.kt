package com.example.shareapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.consumeradda.R
import com.example.consumeradda.fragments.caseListFragment.OnApplicationClicked

import com.example.consumeradda.models.cardModels.ApplicationCardModel
import com.example.consumeradda.models.caseModels.CaseResponse
import kotlinx.android.synthetic.main.application_list_card.view.*

class ApplicationListAdapter(private var entries: MutableList<CaseResponse>, private var onCaseClicked: OnApplicationClicked) : RecyclerView.Adapter<ApplicationListAdapter.ViewHolder>()
{
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
    {
        val clientName : TextView = itemView.tvAppListApplicant
        val clientLocation : TextView = itemView.tvAppListLocation
        val caseType : TextView = itemView.tvAppListCaseType
    }

    fun updateList(updatedList: MutableList<CaseResponse>)
    {
        entries = updatedList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.application_list_card,parent,false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val client = entries[position].applicantFirstName+" "+entries[position].applicantLastName
        val location = entries[position].district+", "+entries[position].state
        val type = entries[position].caseType

        holder.clientName.text = client
        holder.clientLocation.text = location
        holder.caseType.text = type

        holder.itemView.setOnClickListener {
            onCaseClicked.onApplicationItemClicked(position)
        }

    }

    override fun getItemCount(): Int {
        return entries.size
    }
}