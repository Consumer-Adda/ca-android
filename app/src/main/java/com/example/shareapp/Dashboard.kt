package com.example.shareapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_dashboard.*

class Dashboard : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    private lateinit var CardModelList: ArrayList<DashboardCardModel>
    private lateinit var cardAdapter: DashboardCardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        db= FirebaseFirestore.getInstance()


        complaint_btn.setOnClickListener {
            startActivity(Intent(this,ComplaintForm::class.java))
        }

        loadcards()

        btnMoreSetOnClickListener()

    }

    private fun loadcards()
    {
        CardModelList = ArrayList()

        CardModelList.add(DashboardCardModel("CA_RERA_1","Abhi","Vatsal","RERA"))
        CardModelList.add(DashboardCardModel("CA_OTHER_1","Ankit","Abhishek","Other"))
        CardModelList.add(DashboardCardModel("CA_RERA_2","Raju","Rasmeet","RERA"))
        CardModelList.add(DashboardCardModel("CA_BLAW_1","Humraz","John Doe","Banking Law"))

        vpCardView.adapter = DashboardCardAdapter(this,CardModelList)

        vpCardView.setPadding(20,10,20,10)
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
                    R.id.sign_out -> niklo()
                    R.id.contact -> {
                        startActivity(Intent(this, ContactUs::class.java))
                    };
                }
                true
            })
            popupMenu.show()
        }
    }




    private fun niklo()
    {
        FirebaseAuth.getInstance().signOut()
        finish()
        startActivity(Intent(this,MainActivity::class.java))
    }
}
