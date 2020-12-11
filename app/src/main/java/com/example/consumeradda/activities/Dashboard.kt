package com.example.consumeradda.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.consumeradda.R
import com.example.consumeradda.activities.support.About
import com.example.consumeradda.activities.support.ContactUs
import com.example.consumeradda.activities.support.Services
import com.example.shareapp.adapters.DashboardCardAdapter
import com.example.consumeradda.models.cardModels.DashboardCardModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_dashboard.*

class Dashboard : AppCompatActivity(), OnCardClicked {

    private lateinit var auth: FirebaseAuth
    private var isCardInfoDisplayed : Boolean = false
    private lateinit var prefs: SharedPreferences
    private var cardPositionClicked : Int = -1
    private lateinit var CardModelList: ArrayList<DashboardCardModel>
    private lateinit var cardAdapter: DashboardCardAdapter
    var role = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        prefs=this.getSharedPreferences("com.example.consumeradda", Context.MODE_PRIVATE)

        complaint_btn.setOnClickListener {
            if(complaint_btn.text.toString() == "Submit Complaint")
            {
                startActivity(Intent(this, ComplaintForm::class.java))
            }
            else
            {
                startActivity(Intent(this,CasesList::class.java))
            }
        }

        role = prefs.getInt("Role", 0)

        // Role 0 - Client
        // Role 1 - Lawyer
        Toast.makeText(this,"$role",Toast.LENGTH_SHORT).show()

        Handler().postDelayed({
            updateDashboard()
        },1500)

        loadcards()
        btnMoreSetOnClickListener()

    }

    private fun updateDashboard() {
        if(role!=0)
        {
            tvCardHeader.text = "Case Status"
            tvCardData.text = "Notice Sent"
//            if(CardModelList.size >= 1)
            {
                complaint_btn.visibility = View.INVISIBLE
            }
        }
        else
        {
            tvCardHeader.text = "Location Selected"
            tvCardData.text = "Agra, Uttar Pradesh"
            complaint_btn.text = "Cases List"
        }
    }

    private fun loadcards()
    {
        CardModelList = ArrayList()

        CardModelList.add(DashboardCardModel("CA_RERA_1","Abhi","Vatsal","RERA",2))
        CardModelList.add(DashboardCardModel("CA_OTHER_1","Ankit","Abhishek","Other",1))
        CardModelList.add(DashboardCardModel("CA_RERA_2","Raju","Rasmeet","RERA",3))
        CardModelList.add(DashboardCardModel("CA_BLAW_1","Humraz","N/A","Banking Law",0))

        vpCardView.adapter = DashboardCardAdapter(this,CardModelList,this)

        vpCardView.setPadding(20,10,20,10)
        isCardInfoDisplayed = true
    }

    private fun btnMoreSetOnClickListener() {
        btnMore.setOnClickListener{
            val popupMenu: PopupMenu = PopupMenu(this, btnMore)
            popupMenu.menuInflater.inflate(
                R.menu.toolbar_menu,
                popupMenu.menu
            ) // change the layout file as per the need
            popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.about -> {
                        startActivity(Intent(this, About::class.java))
                    }
                    R.id.service -> {
                        startActivity(Intent(this, Services::class.java))
                    }
                    R.id.sign_out -> doSignOut()
                    R.id.contact -> {
                        startActivity(Intent(this, ContactUs::class.java))
                    };
                }
                true
            })
            popupMenu.show()
        }
    }
    
    private fun doSignOut()
    {
        prefs.edit().putBoolean("isLoggedIn", false).apply()
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }

    override fun onCardClicked(position: Int) {
        if(CardModelList[position].lawyer != "N/A")
        {
            val intent = Intent(this,ChatActivity::class.java)
            intent.putExtra("Client",CardModelList[position].client)
            startActivity(intent)
        }
    }
}

interface OnCardClicked
{
    fun onCardClicked(position: Int)
}