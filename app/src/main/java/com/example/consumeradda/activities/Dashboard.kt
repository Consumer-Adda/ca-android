package com.example.consumeradda.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.consumeradda.R
import com.example.consumeradda.activities.support.About
import com.example.consumeradda.activities.support.ContactUs
import com.example.consumeradda.activities.support.Services
import com.example.shareapp.adapters.DashboardCardAdapter
import com.example.consumeradda.models.cardModels.DashboardCardModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_complaint_form.*
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.forgot_password_dialog.*
import kotlinx.android.synthetic.main.location_dialog.*

class Dashboard : AppCompatActivity(), OnCardClicked {

    private lateinit var auth: FirebaseAuth
    private var isCardInfoDisplayed : Boolean = false
    private lateinit var prefs: SharedPreferences
    private var cardPositionClicked : Int = -1
    private lateinit var CardModelList: ArrayList<DashboardCardModel>
    private lateinit var cardAdapter: DashboardCardAdapter
    var role = 0

    companion object {
        var DASHBOARDTAG="DASHBOARDTAG"
        var dataForLocationFrag=0 // 1 - state, 2 - district
        var state="State"
        var district="District"
        var stateNum=-1
        var districtNum=-1
//        lateinit var locationDataModel:LocationDataModel
//        lateinit var dashboardAPIResponse:DashboardDefaultResponse
        var isDashboardAPIResponseInitialised=false
        var cardPositionClicked=-1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        prefs=this.getSharedPreferences("com.example.consumeradda", Context.MODE_PRIVATE)

        complaint_btn.setOnClickListener {
            if(complaint_btn.text.toString() == "Submit Complaint")
            {
                startActivity(Intent(this, ComplaintForm::class.java))
            }
            else if(complaint_btn.text.toString() == "Cases List")
            {
                startActivity(Intent(this,CasesListActivity::class.java))
            }
        }

        role = prefs.getInt("Role", 0)

        // Role 0 - Client
        // Role 1 - Lawyer
        complaint_btn.isEnabled = false
        complaint_btn.isClickable = false
        Toast.makeText(this,"$role",Toast.LENGTH_SHORT).show()

        Handler().postDelayed({
            updateDashboard()
        },1500)

        loadcards()
        btnMoreSetOnClickListener()
        tvCardHeaderSetOnClickListener()
    }

    private fun tvCardHeaderSetOnClickListener() {
        tvCardHeader.setOnClickListener {
            if(tvCardHeader.text.equals("Location Selected") || tvCardHeader.text.equals("Select Location"))
            {
                val mDialog = BottomSheetDialog(this)
                mDialog.setContentView(R.layout.location_dialog)
                val window = mDialog.window
                window?.setLayout(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT
                )
                mDialog.setCanceledOnTouchOutside(true)
                mDialog.setCancelable(true)
                mDialog.show()
                val StatesList = arrayOf("Select One:","Andhra Pradesh","Arunachal Pradesh","Assam","Bihar","Chandigarh (UT)","Chhattisgarh","Dadra and Nagar Haveli (UT)","Daman and Diu (UT)","Delhi (NCT)","Goa","Gujarat","Haryana","Himachal Pradesh","Jammu and Kashmir","Jharkhand","Karnataka","Kerala","Lakshadweep (UT)","Madhya Pradesh","Maharashtra","Manipur","Meghalaya","Mizoram","Nagaland","Odisha","Puducherry (UT)","Punjab","Rajasthan","Sikkim","Tamil Nadu","Telangana","Tripura","Uttarakhand","Uttar Pradesh","West Bengal")
                mDialog.spState.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,StatesList)

                mDialog.spState.onItemSelectedListener = object : AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener{
                    override fun onItemClick(parent: AdapterView<*>?,view: View?,position: Int,id: Long)
                    {
                        TODO("Not yet implemented")
                    }

                    override fun onItemSelected(parent: AdapterView<*>?, view: View?,position: Int, id: Long)
                    {
                        state = StatesList[position]
                        when(position)
                        {
                            0-> {
                                val districts = arrayOf("Select One:")
                                mDialog.spDistrict.adapter = ArrayAdapter<String>(this@Dashboard,android.R.layout.simple_list_item_1,districts)
                                mDialog.spDistrict.onItemSelectedListener = object : AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {
                                    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                                        TODO("Not yet implemented")
                                    }

                                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                                        district = "Select One:"
                                    }

                                    override fun onNothingSelected(parent: AdapterView<*>?) {
                                        TODO("Not yet implemented")
                                    }

                                }
                            }
                            1-> {
                                val districts = arrayOf("Select One:","Anantapur","Chittoor","East Godavari","Guntur","Krishna","Kurnool","Nellore","Prakasam","Srikakulam","Visakhapatnam","Vizianagaram","West Godavari","YSR Kadapa")
                                mDialog.spDistrict.adapter = ArrayAdapter<String>(this@Dashboard,android.R.layout.simple_list_item_1,districts)
                                mDialog.spDistrict.onItemSelectedListener = object : AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener{
                                    override fun onItemClick(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                                        TODO("Not yet implemented")
                                    }

                                    override fun onItemSelected(parent: AdapterView<*>?, view: View?,position: Int,id: Long) {
                                        if(position!=0)
                                        {
                                            district = districts[position]
                                        }
                                        else{
                                            district = "Select One:"
                                        }
                                    }

                                    override fun onNothingSelected(parent: AdapterView<*>?) {
                                        TODO("Not yet implemented")
                                    }

                                }
                            }
                            2->{
                                val districts = arrayOf("Select One:","Tawang","West Kameng","East Kameng","Papum Pare","Kurung Kumey","Kra Daadi","Lower Subansiri","Upper Subansiri","West Siang","East Siang","Siang","Upper Siang","Lower Siang","Lower Dibang Valley","Dibang Valley","Anjaw","Lohit","Namsai","Changlang","Tirap","Longding")
                                mDialog.spDistrict.adapter = ArrayAdapter<String>(this@Dashboard,android.R.layout.simple_list_item_1,districts)
                                mDialog.spDistrict.onItemSelectedListener = object : AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener{
                                    override fun onItemClick(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                                        TODO("Not yet implemented")
                                    }

                                    override fun onItemSelected(parent: AdapterView<*>?, view: View?,position: Int,id: Long) {
                                        if(position!=0)
                                        {
                                            district = districts[position]
                                        }
                                        else{
                                            district = "Select One:"
                                        }
                                    }

                                    override fun onNothingSelected(parent: AdapterView<*>?) {
                                        TODO("Not yet implemented")
                                    }

                                }
                            }
                            3->{
                                val districts = arrayOf("Select One:","Baksa","Barpeta","Biswanath","Bongaigaon","Cachar","Charaideo","Chirang","Darrang","Dhemaji","Dhubri","Dibrugarh","Goalpara","Golaghat","Hailakandi","Hojai","Jorhat","Kamrup Metropolitan","Kamrup","Karbi Anglong","Karimganj","Kokrajhar","Lakhimpur","Majuli","Morigaon","Nagaon","Nalbari","Dima Hasao","Sivasagar","Sonitpur","South Salmara-Mankachar","Tinsukia","Udalguri","West Karbi Anglong")
                                mDialog.spDistrict.adapter = ArrayAdapter<String>(this@Dashboard,android.R.layout.simple_list_item_1,districts)
                                mDialog.spDistrict.onItemSelectedListener = object : AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener{
                                    override fun onItemClick(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                                        TODO("Not yet implemented")
                                    }

                                    override fun onItemSelected(parent: AdapterView<*>?, view: View?,position: Int,id: Long) {
                                        if(position!=0)
                                        {
                                            district = districts[position]
                                        }
                                        else{
                                            district = "Select One:"
                                        }
                                    }

                                    override fun onNothingSelected(parent: AdapterView<*>?) {
                                        TODO("Not yet implemented")
                                    }

                                }
                            }
                            4->{
                                val districts = arrayOf("Select One:","Araria","Arwal","Aurangabad","Banka","Begusarai","Bhagalpur","Bhojpur","Buxar","Darbhanga","East Champaran (Motihari)","Gaya","Gopalganj","Jamui","Jehanabad","Kaimur (Bhabua)","Katihar","Khagaria","Kishanganj","Lakhisarai","Madhepura","Madhubani","Munger (Monghyr)","Muzaffarpur","Nalanda","Nawada","Patna","Purnia (Purnea)","Rohtas","Saharsa","Samastipur","Saran","Sheikhpura","Sheohar","Sitamarhi","Siwan","Supaul","Vaishali","West Champaran")
                                mDialog.spDistrict.adapter = ArrayAdapter<String>(this@Dashboard,android.R.layout.simple_list_item_1,districts)
                                mDialog.spDistrict.onItemSelectedListener = object : AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener{
                                    override fun onItemClick(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                                        TODO("Not yet implemented")
                                    }

                                    override fun onItemSelected(parent: AdapterView<*>?, view: View?,position: Int,id: Long) {
                                        if(position!=0)
                                        {
                                            district = districts[position]
                                        }
                                        else{
                                            district = "Select One:"
                                        }
                                    }

                                    override fun onNothingSelected(parent: AdapterView<*>?) {
                                        TODO("Not yet implemented")
                                    }

                                }
                            }
                            5->{
                                val districts = arrayOf("Select One:","Chandigarh")
                                mDialog.spDistrict.adapter = ArrayAdapter<String>(this@Dashboard,android.R.layout.simple_list_item_1,districts)
                                mDialog.spDistrict.onItemSelectedListener = object : AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener{
                                    override fun onItemClick(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                                        TODO("Not yet implemented")
                                    }

                                    override fun onItemSelected(parent: AdapterView<*>?, view: View?,position: Int,id: Long) {
                                        if(position!=0)
                                        {
                                            district = districts[position]
                                        }
                                        else{
                                            district = "Select One:"
                                        }
                                    }

                                    override fun onNothingSelected(parent: AdapterView<*>?) {
                                        TODO("Not yet implemented")
                                    }

                                }
                            }
                            6->{
                                val districts = arrayOf("Select One:","Balod","Baloda Bazar","Balrampur","Bastar","Bemetara","Bijapur","Bilaspur","Dantewada (South Bastar)","Dhamtari","Durg","Gariyaband","Janjgir-Champa","Jashpur","Kabirdham (Kawardha)","Kanker (North Bastar)","Kondagaon","Korba","Korea (Koriya)","Mahasamund","Mungeli","Narayanpur","Raigarh","Raipur","Rajnandgaon","Sukma","Surajpur","Surguja")
                                mDialog.spDistrict.adapter = ArrayAdapter<String>(this@Dashboard,android.R.layout.simple_list_item_1,districts)
                                mDialog.spDistrict.onItemSelectedListener = object : AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener{
                                    override fun onItemClick(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                                        TODO("Not yet implemented")
                                    }

                                    override fun onItemSelected(parent: AdapterView<*>?, view: View?,position: Int,id: Long) {
                                        if(position!=0)
                                        {
                                            district = districts[position]
                                        }
                                        else{
                                            district = "Select One:"
                                        }
                                    }

                                    override fun onNothingSelected(parent: AdapterView<*>?) {
                                        TODO("Not yet implemented")
                                    }

                                }
                            }
                            7->{
                                val districts = arrayOf("Select One:","Dadra & Nagar Haveli")
                                mDialog.spDistrict.adapter = ArrayAdapter<String>(this@Dashboard,android.R.layout.simple_list_item_1,districts)
                                mDialog.spDistrict.onItemSelectedListener = object : AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener{
                                    override fun onItemClick(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                                        TODO("Not yet implemented")
                                    }

                                    override fun onItemSelected(parent: AdapterView<*>?, view: View?,position: Int,id: Long) {
                                        if(position!=0)
                                        {
                                            district = districts[position]
                                        }
                                        else{
                                            district = "Select One:"
                                        }
                                    }

                                    override fun onNothingSelected(parent: AdapterView<*>?) {
                                        TODO("Not yet implemented")
                                    }

                                }
                            }
                            8->{
                                val districts = arrayOf("Select One:","Daman","Diu")
                                mDialog.spDistrict.adapter = ArrayAdapter<String>(this@Dashboard,android.R.layout.simple_list_item_1,districts)
                                mDialog.spDistrict.onItemSelectedListener = object : AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener{
                                    override fun onItemClick(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                                        TODO("Not yet implemented")
                                    }

                                    override fun onItemSelected(parent: AdapterView<*>?, view: View?,position: Int,id: Long) {
                                        if(position!=0)
                                        {
                                            district = districts[position]
                                        }
                                        else{
                                            district = "Select One:"
                                        }
                                    }

                                    override fun onNothingSelected(parent: AdapterView<*>?) {
                                        TODO("Not yet implemented")
                                    }

                                }
                            }
                            9->{
                                val districts = arrayOf("Select One:","Central Delhi","East Delhi","New Delhi","North Delhi","North East  Delhi","North West  Delhi","Shahdara","South Delhi","South East Delhi","South West  Delhi","West Delhi")
                                mDialog.spDistrict.adapter = ArrayAdapter<String>(this@Dashboard,android.R.layout.simple_list_item_1,districts)
                                mDialog.spDistrict.onItemSelectedListener = object : AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener{
                                    override fun onItemClick(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                                        TODO("Not yet implemented")
                                    }

                                    override fun onItemSelected(parent: AdapterView<*>?, view: View?,position: Int,id: Long) {
                                        if(position!=0)
                                        {
                                            district = districts[position]
                                        }
                                        else{
                                            district = "Select One:"
                                        }
                                    }

                                    override fun onNothingSelected(parent: AdapterView<*>?) {
                                        TODO("Not yet implemented")
                                    }

                                }
                            }
                            10->{
                                val districts = arrayOf("Select One:","North Goa","South Goa")
                                mDialog.spDistrict.adapter = ArrayAdapter<String>(this@Dashboard,android.R.layout.simple_list_item_1,districts)
                                mDialog.spDistrict.onItemSelectedListener = object : AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener{
                                    override fun onItemClick(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                                        TODO("Not yet implemented")
                                    }

                                    override fun onItemSelected(parent: AdapterView<*>?, view: View?,position: Int,id: Long) {
                                        if(position!=0)
                                        {
                                            district = districts[position]
                                        }
                                        else{
                                            district = "Select One:"
                                        }
                                    }

                                    override fun onNothingSelected(parent: AdapterView<*>?) {
                                        TODO("Not yet implemented")
                                    }

                                }
                            }
                            11->{
                                val districts = arrayOf("Select One:","Ahmedabad","Amreli","Anand","Aravalli","Banaskantha (Palanpur)","Bharuch","Bhavnagar","Botad","Chhota Udepur","Dahod","Dangs (Ahwa)","Devbhoomi Dwarka","Gandhinagar","Gir Somnath","Jamnagar","Junagadh","Kachchh","Kheda (Nadiad)","Mahisagar","Mehsana","Morbi","Narmada (Rajpipla)","Navsari","Panchmahal (Godhra)","Patan","Porbandar","Rajkot","Sabarkantha (Himmatnagar)","Surat","Surendranagar","Tapi (Vyara)","Vadodara","Valsad")
                                mDialog.spDistrict.adapter = ArrayAdapter<String>(this@Dashboard,android.R.layout.simple_list_item_1,districts)
                                mDialog.spDistrict.onItemSelectedListener = object : AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener{
                                    override fun onItemClick(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                                        TODO("Not yet implemented")
                                    }

                                    override fun onItemSelected(parent: AdapterView<*>?, view: View?,position: Int,id: Long) {
                                        if(position!=0)
                                        {
                                            district = districts[position]
                                        }
                                        else{
                                            district = "Select One:"
                                        }
                                    }

                                    override fun onNothingSelected(parent: AdapterView<*>?) {
                                        TODO("Not yet implemented")
                                    }

                                }
                            }
                            12->{
                                val districts = arrayOf("Select One:","Ambala","Bhiwani","Charkhi Dadri","Faridabad","Fatehabad","Gurgaon","Hisar","Jhajjar","Jind","Kaithal","Karnal","Kurukshetra","Mahendragarh","Mewat","Palwal","Panchkula","Panipat","Rewari","Rohtak","Sirsa","Sonipat","Yamunanagar")
                                mDialog.spDistrict.adapter = ArrayAdapter<String>(this@Dashboard,android.R.layout.simple_list_item_1,districts)
                                mDialog.spDistrict.onItemSelectedListener = object : AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener{
                                    override fun onItemClick(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                                        TODO("Not yet implemented")
                                    }

                                    override fun onItemSelected(parent: AdapterView<*>?, view: View?,position: Int,id: Long) {
                                        if(position!=0)
                                        {
                                            district = districts[position]
                                        }
                                        else{
                                            district = "Select One:"
                                        }
                                    }

                                    override fun onNothingSelected(parent: AdapterView<*>?) {
                                        TODO("Not yet implemented")
                                    }

                                }
                            }
                            13->{
                                val districts = arrayOf("Select One:","Bilaspur","Chamba","Hamirpur","Kangra","Kinnaur","Kullu","Lahaul & Spiti","Mandi","Shimla","Sirmaur (Sirmour)","Solan","Una")
                                mDialog.spDistrict.adapter = ArrayAdapter<String>(this@Dashboard,android.R.layout.simple_list_item_1,districts)
                                mDialog.spDistrict.onItemSelectedListener = object : AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener{
                                    override fun onItemClick(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                                        TODO("Not yet implemented")
                                    }

                                    override fun onItemSelected(parent: AdapterView<*>?, view: View?,position: Int,id: Long) {
                                        if(position!=0)
                                        {
                                            district = districts[position]
                                        }
                                        else{
                                            district = "Select One:"
                                        }
                                    }

                                    override fun onNothingSelected(parent: AdapterView<*>?) {
                                        TODO("Not yet implemented")
                                    }

                                }
                            }
                            14->{
                                val districts = arrayOf("Select One:","Anantnag","Bandipore","Baramulla","Budgam","Doda","Ganderbal","Jammu","Kargil","Kathua","Kishtwar","Kulgam","Kupwara","Leh","Poonch","Pulwama","Rajouri","Ramban","Reasi","Samba","Shopian","Srinagar","Udhampur")
                                mDialog.spDistrict.adapter = ArrayAdapter<String>(this@Dashboard,android.R.layout.simple_list_item_1,districts)
                                mDialog.spDistrict.onItemSelectedListener = object : AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener{
                                    override fun onItemClick(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                                        TODO("Not yet implemented")
                                    }

                                    override fun onItemSelected(parent: AdapterView<*>?, view: View?,position: Int,id: Long) {
                                        if(position!=0)
                                        {
                                            district = districts[position]
                                        }
                                        else{
                                            district = "Select One:"
                                        }
                                    }

                                    override fun onNothingSelected(parent: AdapterView<*>?) {
                                        TODO("Not yet implemented")
                                    }

                                }
                            }
                            15->{
                                val districts = arrayOf("Select One:","Bokaro","Chatra","Deoghar","Dhanbad","Dumka","East Singhbhum","Garhwa","Giridih","Godda","Gumla","Hazaribag","Jamtara","Khunti","Koderma","Latehar","Lohardaga","Pakur","Palamu","Ramgarh","Ranchi","Sahibganj","Seraikela-Kharsawan","Simdega","West Singhbhum")
                                mDialog.spDistrict.adapter = ArrayAdapter<String>(this@Dashboard,android.R.layout.simple_list_item_1,districts)
                                mDialog.spDistrict.onItemSelectedListener = object : AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener{
                                    override fun onItemClick(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                                        TODO("Not yet implemented")
                                    }

                                    override fun onItemSelected(parent: AdapterView<*>?, view: View?,position: Int,id: Long) {
                                        if(position!=0)
                                        {
                                            district = districts[position]
                                        }
                                        else{
                                            district = "Select One:"
                                        }
                                    }

                                    override fun onNothingSelected(parent: AdapterView<*>?) {
                                        TODO("Not yet implemented")
                                    }

                                }
                            }
                            16->{
                                val districts = arrayOf("Select One:","Bagalkot","Ballari (Bellary)","Belagavi (Belgaum)","Bengaluru (Bangalore) Rural","Bengaluru (Bangalore) Urban","Bidar","Chamarajanagar","Chikballapur","Chikkamagaluru (Chikmagalur)","Chitradurga","Dakshina Kannada","Davangere","Dharwad","Gadag","Hassan","Haveri","Kalaburagi (Gulbarga)","Kodagu","Kolar","Koppal","Mandya","Mysuru (Mysore)","Raichur","Ramanagara","Shivamogga (Shimoga)","Tumakuru (Tumkur)","Udupi","Uttara Kannada (Karwar)","Vijayapura (Bijapur)","Yadgir")
                                mDialog.spDistrict.adapter = ArrayAdapter<String>(this@Dashboard,android.R.layout.simple_list_item_1,districts)
                                mDialog.spDistrict.onItemSelectedListener = object : AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener{
                                    override fun onItemClick(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                                        TODO("Not yet implemented")
                                    }

                                    override fun onItemSelected(parent: AdapterView<*>?, view: View?,position: Int,id: Long) {
                                        if(position!=0)
                                        {
                                            district = districts[position]
                                        }
                                        else{
                                            district = "Select One:"
                                        }
                                    }

                                    override fun onNothingSelected(parent: AdapterView<*>?) {
                                        TODO("Not yet implemented")
                                    }

                                }
                            }
                            17->{
                                val districts = arrayOf("Select One:","Alappuzha","Ernakulam","Idukki","Kannur","Kasaragod","Kollam","Kottayam","Kozhikode","Malappuram","Palakkad","Pathanamthitta","Thiruvananthapuram","Thrissur","Wayanad")
                                mDialog.spDistrict.adapter = ArrayAdapter<String>(this@Dashboard,android.R.layout.simple_list_item_1,districts)
                                mDialog.spDistrict.onItemSelectedListener = object : AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener{
                                    override fun onItemClick(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                                        TODO("Not yet implemented")
                                    }

                                    override fun onItemSelected(parent: AdapterView<*>?, view: View?,position: Int,id: Long) {
                                        if(position!=0)
                                        {
                                            district = districts[position]
                                        }
                                        else{
                                            district = "Select One:"
                                        }
                                    }

                                    override fun onNothingSelected(parent: AdapterView<*>?) {
                                        TODO("Not yet implemented")
                                    }

                                }
                            }
                            18->{
                                val districts = arrayOf("Select One:","Agatti","Amini","Androth","Bithra","Chethlath","Kavaratti","Kadmath","Kalpeni","Kilthan","Minicoy")
                                mDialog.spDistrict.adapter = ArrayAdapter<String>(this@Dashboard,android.R.layout.simple_list_item_1,districts)
                                mDialog.spDistrict.onItemSelectedListener = object : AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener{
                                    override fun onItemClick(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                                        TODO("Not yet implemented")
                                    }

                                    override fun onItemSelected(parent: AdapterView<*>?, view: View?,position: Int,id: Long) {
                                        if(position!=0)
                                        {
                                            district = districts[position]
                                        }
                                        else{
                                            district = "Select One:"
                                        }
                                    }

                                    override fun onNothingSelected(parent: AdapterView<*>?) {
                                        TODO("Not yet implemented")
                                    }

                                }
                            }
                            19->{
                                val districts = arrayOf("Select One:","Agar Malwa","Alirajpur","Anuppur","Ashoknagar","Balaghat","Barwani","Betul","Bhind","Bhopal","Burhanpur","Chhatarpur","Chhindwara","Damoh","Datia","Dewas","Dhar","Dindori","Guna","Gwalior","Harda","Hoshangabad","Indore","Jabalpur","Jhabua","Katni","Khandwa","Khargone","Mandla","Mandsaur","Morena","Narsinghpur","Neemuch","Panna","Raisen","Rajgarh","Ratlam","Rewa","Sagar","Satna","Sehore","Seoni","Shahdol","Shajapur","Sheopur","Shivpuri","Sidhi","Singrauli","Tikamgarh","Ujjain","Umaria","Vidisha")
                                mDialog.spDistrict.adapter = ArrayAdapter<String>(this@Dashboard,android.R.layout.simple_list_item_1,districts)
                                mDialog.spDistrict.onItemSelectedListener = object : AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener{
                                    override fun onItemClick(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                                        TODO("Not yet implemented")
                                    }

                                    override fun onItemSelected(parent: AdapterView<*>?, view: View?,position: Int,id: Long) {
                                        if(position!=0)
                                        {
                                            district = districts[position]
                                        }
                                        else{
                                            district = "Select One:"
                                        }
                                    }

                                    override fun onNothingSelected(parent: AdapterView<*>?) {
                                        TODO("Not yet implemented")
                                    }

                                }
                            }
                            20->{
                                val districts = arrayOf("Select One:","Ahmednagar","Akola","Amravati","Aurangabad","Beed","Bhandara","Buldhana","Chandrapur","Dhule","Gadchiroli","Gondia","Hingoli","Jalgaon","Jalna","Kolhapur","Latur","Mumbai district","Mumbai Suburban","Nagpur","Nanded","Nandurbar","Nashik","Osmanabad","Palghar","Parbhani","Pune","Raigad","Ratnagiri","Sangli","Satara","Sindhudurg","Solapur","Thane","Wardha","Washim","Yavatmal")
                                mDialog.spDistrict.adapter = ArrayAdapter<String>(this@Dashboard,android.R.layout.simple_list_item_1,districts)
                                mDialog.spDistrict.onItemSelectedListener = object : AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener{
                                    override fun onItemClick(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                                        TODO("Not yet implemented")
                                    }

                                    override fun onItemSelected(parent: AdapterView<*>?, view: View?,position: Int,id: Long) {
                                        if(position!=0)
                                        {
                                            district = districts[position]
                                        }
                                        else{
                                            district = "Select One:"
                                        }
                                    }

                                    override fun onNothingSelected(parent: AdapterView<*>?) {
                                        TODO("Not yet implemented")
                                    }

                                }
                            }
                            21->{
                                val districts = arrayOf("Select One:","Bishnupur","Chandel","Churachandpur","Imphal East","Imphal West","Jiribam","Kakching","Kamjong","Kangpokpi","Noney","Pherzawl","Senapati","Tamenglong","Tengnoupal","Thoubal","Ukhrul")
                                mDialog.spDistrict.adapter = ArrayAdapter<String>(this@Dashboard,android.R.layout.simple_list_item_1,districts)
                                mDialog.spDistrict.onItemSelectedListener = object : AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener{
                                    override fun onItemClick(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                                        TODO("Not yet implemented")
                                    }

                                    override fun onItemSelected(parent: AdapterView<*>?, view: View?,position: Int,id: Long) {
                                        if(position!=0)
                                        {
                                            district = districts[position]
                                        }
                                        else{
                                            district = "Select One:"
                                        }
                                    }

                                    override fun onNothingSelected(parent: AdapterView<*>?) {
                                        TODO("Not yet implemented")
                                    }

                                }
                            }
                            22->{
                                val districts = arrayOf("Select One:","East Garo Hills","East Jaintia Hills","East Khasi Hills","North Garo Hills","Ri Bhoi","South Garo Hills","South West Garo Hills ","South West Khasi Hills","West Garo Hills","West Jaintia Hills","West Khasi Hills")
                                mDialog.spDistrict.adapter = ArrayAdapter<String>(this@Dashboard,android.R.layout.simple_list_item_1,districts)
                                mDialog.spDistrict.onItemSelectedListener = object : AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener{
                                    override fun onItemClick(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                                        TODO("Not yet implemented")
                                    }

                                    override fun onItemSelected(parent: AdapterView<*>?, view: View?,position: Int,id: Long) {
                                        if(position!=0)
                                        {
                                            district = districts[position]
                                        }
                                        else{
                                            district = "Select One:"
                                        }
                                    }

                                    override fun onNothingSelected(parent: AdapterView<*>?) {
                                        TODO("Not yet implemented")
                                    }

                                }
                            }
                            23->{
                                val districts = arrayOf("Select One:","Aizawl","Champhai","Kolasib","Lawngtlai","Lunglei","Mamit","Saiha","Serchhip")
                                mDialog.spDistrict.adapter = ArrayAdapter<String>(this@Dashboard,android.R.layout.simple_list_item_1,districts)
                                mDialog.spDistrict.onItemSelectedListener = object : AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener{
                                    override fun onItemClick(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                                        TODO("Not yet implemented")
                                    }

                                    override fun onItemSelected(parent: AdapterView<*>?, view: View?,position: Int,id: Long) {
                                        if(position!=0)
                                        {
                                            district = districts[position]
                                        }
                                        else{
                                            district = "Select One:"
                                        }
                                    }

                                    override fun onNothingSelected(parent: AdapterView<*>?) {
                                        TODO("Not yet implemented")
                                    }

                                }
                            }
                            24->{
                                val districts = arrayOf("Select One:","Dimapur","Kiphire","Kohima","Longleng","Mokokchung","Mon","Peren","Phek","Tuensang","Wokha","Zunheboto")
                                mDialog.spDistrict.adapter = ArrayAdapter<String>(this@Dashboard,android.R.layout.simple_list_item_1,districts)
                                mDialog.spDistrict.onItemSelectedListener = object : AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener{
                                    override fun onItemClick(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                                        TODO("Not yet implemented")
                                    }

                                    override fun onItemSelected(parent: AdapterView<*>?, view: View?,position: Int,id: Long) {
                                        if(position!=0)
                                        {
                                            district = districts[position]
                                        }
                                        else{
                                            district = "Select One:"
                                        }
                                    }

                                    override fun onNothingSelected(parent: AdapterView<*>?) {
                                        TODO("Not yet implemented")
                                    }

                                }
                            }
                            25->{
                                val districts = arrayOf("Select One:","Angul","Balangir","Balasore","Bargarh","Bhadrak","Boudh","Cuttack","Deogarh","Dhenkanal","Gajapati","Ganjam","Jagatsinghapur","Jajpur","Jharsuguda","Kalahandi","Kandhamal","Kendrapara","Kendujhar (Keonjhar)","Khordha","Koraput","Malkangiri","Mayurbhanj","Nabarangpur","Nayagarh","Nuapada","Puri","Rayagada","Sambalpur","Sonepur","Sundargarh")
                                mDialog.spDistrict.adapter = ArrayAdapter<String>(this@Dashboard,android.R.layout.simple_list_item_1,districts)
                                mDialog.spDistrict.onItemSelectedListener = object : AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener{
                                    override fun onItemClick(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                                        TODO("Not yet implemented")
                                    }

                                    override fun onItemSelected(parent: AdapterView<*>?, view: View?,position: Int,id: Long) {
                                        if(position!=0)
                                        {
                                            district = districts[position]
                                        }
                                        else{
                                            district = "Select One:"
                                        }
                                    }

                                    override fun onNothingSelected(parent: AdapterView<*>?) {
                                        TODO("Not yet implemented")
                                    }

                                }
                            }
                            26->{
                                val districts = arrayOf("Select One:","Karaikal","Mahe","Pondicherry","Yanam")
                                mDialog.spDistrict.adapter = ArrayAdapter<String>(this@Dashboard,android.R.layout.simple_list_item_1,districts)
                                mDialog.spDistrict.onItemSelectedListener = object : AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener{
                                    override fun onItemClick(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                                        TODO("Not yet implemented")
                                    }

                                    override fun onItemSelected(parent: AdapterView<*>?, view: View?,position: Int,id: Long) {
                                        if(position!=0)
                                        {
                                            district = districts[position]
                                        }
                                        else{
                                            district = "Select One:"
                                        }
                                    }

                                    override fun onNothingSelected(parent: AdapterView<*>?) {
                                        TODO("Not yet implemented")
                                    }

                                }
                            }
                            27->{
                                val districts = arrayOf("Select One:","Amritsar","Barnala","Bathinda","Faridkot","Fatehgarh Sahib","Fazilka","Ferozepur","Gurdaspur","Hoshiarpur","Jalandhar","Kapurthala","Ludhiana","Mansa","Moga","Muktsar","Nawanshahr (Shahid Bhagat Singh Nagar)","Pathankot","Patiala","Rupnagar","Sahibzada Ajit Singh Nagar (Mohali)","Sangrur","Tarn Taran")
                                mDialog.spDistrict.adapter = ArrayAdapter<String>(this@Dashboard,android.R.layout.simple_list_item_1,districts)
                                mDialog.spDistrict.onItemSelectedListener = object : AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener{
                                    override fun onItemClick(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                                        TODO("Not yet implemented")
                                    }

                                    override fun onItemSelected(parent: AdapterView<*>?, view: View?,position: Int,id: Long) {
                                        if(position!=0)
                                        {
                                            district = districts[position]
                                        }
                                        else{
                                            district = "Select One:"
                                        }
                                    }

                                    override fun onNothingSelected(parent: AdapterView<*>?) {
                                        TODO("Not yet implemented")
                                    }

                                }
                            }
                            28->{
                                val districts = arrayOf("Select One:","Ajmer","Alwar","Banswara","Baran","Barmer","Bharatpur","Bhilwara","Bikaner","Bundi","Chittorgarh","Churu","Dausa","Dholpur","Dungarpur","Hanumangarh","Jaipur","Jaisalmer","Jalore","Jhalawar","Jhunjhunu","Jodhpur","Karauli","Kota","Nagaur","Pali","Pratapgarh","Rajsamand","Sawai Madhopur","Sikar","Sirohi","Sri Ganganagar","Tonk","Udaipur")
                                mDialog.spDistrict.adapter = ArrayAdapter<String>(this@Dashboard,android.R.layout.simple_list_item_1,districts)
                                mDialog.spDistrict.onItemSelectedListener = object : AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener{
                                    override fun onItemClick(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                                        TODO("Not yet implemented")
                                    }

                                    override fun onItemSelected(parent: AdapterView<*>?, view: View?,position: Int,id: Long) {
                                        if(position!=0)
                                        {
                                            district = districts[position]
                                        }
                                        else{
                                            district = "Select One:"
                                        }
                                    }

                                    override fun onNothingSelected(parent: AdapterView<*>?) {
                                        TODO("Not yet implemented")
                                    }

                                }
                            }
                            29->{
                                val districts = arrayOf("Select One:","East Sikkim","North Sikkim","South Sikkim","West Sikkim")
                                mDialog.spDistrict.adapter = ArrayAdapter<String>(this@Dashboard,android.R.layout.simple_list_item_1,districts)
                                mDialog.spDistrict.onItemSelectedListener = object : AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener{
                                    override fun onItemClick(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                                        TODO("Not yet implemented")
                                    }

                                    override fun onItemSelected(parent: AdapterView<*>?, view: View?,position: Int,id: Long) {
                                        if(position!=0)
                                        {
                                            district = districts[position]
                                        }
                                        else{
                                            district = "Select One:"
                                        }
                                    }

                                    override fun onNothingSelected(parent: AdapterView<*>?) {
                                        TODO("Not yet implemented")
                                    }

                                }
                            }
                            30->{
                                val districts = arrayOf("Select One:","Ariyalur","Chennai","Coimbatore","Cuddalore","Dharmapuri","Dindigul","Erode","Kanchipuram","Kanyakumari","Karur","Krishnagiri","Madurai","Nagapattinam","Namakkal","Nilgiris","Perambalur","Pudukkottai","Ramanathapuram","Salem","Sivaganga","Thanjavur","Theni","Thoothukudi (Tuticorin)","Tiruchirappalli","Tirunelveli","Tiruppur","Tiruvallur","Tiruvannamalai","Tiruvarur","Vellore","Viluppuram","Virudhunagar")
                                mDialog.spDistrict.adapter = ArrayAdapter<String>(this@Dashboard,android.R.layout.simple_list_item_1,districts)
                                mDialog.spDistrict.onItemSelectedListener = object : AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener{
                                    override fun onItemClick(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                                        TODO("Not yet implemented")
                                    }

                                    override fun onItemSelected(parent: AdapterView<*>?, view: View?,position: Int,id: Long) {
                                        if(position!=0)
                                        {
                                            district = districts[position]
                                        }
                                        else{
                                            district = "Select One:"
                                        }
                                    }

                                    override fun onNothingSelected(parent: AdapterView<*>?) {
                                        TODO("Not yet implemented")
                                    }

                                }
                            }
                            31->{
                                val districts = arrayOf("Select One:","Adilabad","Bhadradri Kothagudem","Hyderabad","Jagtial","Jangaon","Jayashankar Bhoopalpally","Jogulamba Gadwal","Kamareddy","Karimnagar","Khammam","Komaram Bheem Asifabad","Mahabubabad","Mahabubnagar","Mancherial","Medak","Medchal","Nagarkurnool","Nalgonda","Nirmal","Nizamabad","Peddapalli","Rajanna Sircilla","Rangareddy","Sangareddy","Siddipet","Suryapet","Vikarabad","Wanaparthy","Warangal (Rural)","Warangal (Urban)","Yadadri Bhuvanagiri")
                                mDialog.spDistrict.adapter = ArrayAdapter<String>(this@Dashboard,android.R.layout.simple_list_item_1,districts)
                                mDialog.spDistrict.onItemSelectedListener = object : AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener{
                                    override fun onItemClick(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                                        TODO("Not yet implemented")
                                    }

                                    override fun onItemSelected(parent: AdapterView<*>?, view: View?,position: Int,id: Long) {
                                        if(position!=0)
                                        {
                                            district = districts[position]
                                        }
                                        else{
                                            district = "Select One:"
                                        }
                                    }

                                    override fun onNothingSelected(parent: AdapterView<*>?) {
                                        TODO("Not yet implemented")
                                    }
                                }
                            }
                            32->{
                                val districts = arrayOf("Select One:","Dhalai","Gomati","Khowai","North Tripura","Sepahijala","South Tripura","Unakoti","West Tripura")
                                mDialog.spDistrict.adapter = ArrayAdapter<String>(this@Dashboard,android.R.layout.simple_list_item_1,districts)
                                mDialog.spDistrict.onItemSelectedListener = object : AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener{
                                    override fun onItemClick(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                                        TODO("Not yet implemented")
                                    }

                                    override fun onItemSelected(parent: AdapterView<*>?, view: View?,position: Int,id: Long) {
                                        if(position!=0)
                                        {
                                            district = districts[position]
                                        }
                                        else{
                                            district = "Select One:"
                                        }
                                    }

                                    override fun onNothingSelected(parent: AdapterView<*>?) {
                                        TODO("Not yet implemented")
                                    }
                                }
                            }
                            33->{
                                val districts = arrayOf("Select One:","Almora","Bageshwar","Chamoli","Champawat","Dehradun","Haridwar","Nainital","Pauri Garhwal","Pithoragarh","Rudraprayag","Tehri Garhwal","Udham Singh Nagar","Uttarkashi")
                                mDialog.spDistrict.adapter = ArrayAdapter<String>(this@Dashboard,android.R.layout.simple_list_item_1,districts)
                                mDialog.spDistrict.onItemSelectedListener = object : AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener{
                                    override fun onItemClick(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                                        TODO("Not yet implemented")
                                    }

                                    override fun onItemSelected(parent: AdapterView<*>?, view: View?,position: Int,id: Long) {
                                        if(position!=0)
                                        {
                                            district = districts[position]
                                        }
                                        else{
                                            district = "Select One:"
                                        }
                                    }

                                    override fun onNothingSelected(parent: AdapterView<*>?) {
                                        TODO("Not yet implemented")
                                    }
                                }
                            }
                            34->{
                                val districts = arrayOf("Select One:","Agra","Aligarh","Allahabad","Ambedkar Nagar","Amethi (Chatrapati Sahuji Mahraj Nagar)","Amroha (J.P. Nagar)","Auraiya","Azamgarh","Baghpat","Bahraich","Ballia","Balrampur","Banda","Barabanki","Bareilly","Basti","Bhadohi","Bijnor","Budaun","Bulandshahr","Chandauli","Chitrakoot","Deoria","Etah","Etawah","Faizabad","Farrukhabad","Fatehpur","Firozabad","Gautam Buddha Nagar","Ghaziabad","Ghazipur","Gonda","Gorakhpur","Hamirpur","Hapur (Panchsheel Nagar)","Hardoi","Hathras","Jalaun","Jaunpur","Jhansi","Kannauj","Kanpur Dehat","Kanpur Nagar","Kanshiram Nagar (Kasganj)","Kaushambi","Kushinagar (Padrauna)","Lakhimpur - Kheri","Lalitpur","Lucknow","Maharajganj","Mahoba","Mainpuri","Mathura","Mau","Meerut","Mirzapur","Moradabad","Muzaffarnagar","Pilibhit","Pratapgarh","RaeBareli","Rampur","Saharanpur","Sambhal (Bhim Nagar)","Sant Kabir Nagar","Shahjahanpur","Shamali (Prabuddh Nagar)","Shravasti","Siddharth Nagar","Sitapur","Sonbhadra","Sultanpur","Unnao","Varanasi")
                                mDialog.spDistrict.adapter = ArrayAdapter<String>(this@Dashboard,android.R.layout.simple_list_item_1,districts)
                                mDialog.spDistrict.onItemSelectedListener = object : AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener{
                                    override fun onItemClick(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                                        TODO("Not yet implemented")
                                    }

                                    override fun onItemSelected(parent: AdapterView<*>?, view: View?,position: Int,id: Long) {
                                        if(position!=0)
                                        {
                                            district = districts[position]
                                        }
                                        else{
                                            district = "Select One:"
                                        }
                                    }

                                    override fun onNothingSelected(parent: AdapterView<*>?) {
                                        TODO("Not yet implemented")
                                    }
                                }
                            }
                            35->{
                                val districts = arrayOf("Select One:","Alipurduar","Bankura","Birbhum","Burdwan (Bardhaman)","Cooch Behar","Dakshin Dinajpur (South Dinajpur)","Darjeeling","Hooghly","Howrah","Jalpaiguri","Kalimpong","Kolkata","Malda","Murshidabad","Nadia","North 24 Parganas","Paschim Medinipur (West Medinipur)","Purba Medinipur (East Medinipur)","Purulia","South 24 Parganas","Uttar Dinajpur (North Dinajpur)")
                                mDialog.spDistrict.adapter = ArrayAdapter<String>(this@Dashboard,android.R.layout.simple_list_item_1,districts)
                                mDialog.spDistrict.onItemSelectedListener = object : AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener{
                                    override fun onItemClick(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                                        TODO("Not yet implemented")
                                    }

                                    override fun onItemSelected(parent: AdapterView<*>?, view: View?,position: Int,id: Long) {
                                        if(position!=0)
                                        {
                                            district = districts[position]
                                        }
                                        else{
                                            district = "Select One:"
                                        }
                                    }

                                    override fun onNothingSelected(parent: AdapterView<*>?) {
                                        TODO("Not yet implemented")
                                    }
                                }
                            }
                        }

                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }

                }

                mDialog.btnUpdateLocation.setOnClickListener {
                    tvCardData.text = "${district}, ${state}"
                    mDialog.dismiss()
                }
            }
        }
    }

    private fun updateDashboard() {

        complaint_btn.isEnabled = true
        complaint_btn.isClickable = true

        if(role!=0)
        {
            tvCardHeader.text = "Case Status"
            tvCardData.text = "Notice Sent"
//            if(CardModelList.size >= 1)
            {
                complaint_btn.visibility = View.INVISIBLE
            }
//            else
            {
                complaint_btn.text = "Submit Complaint"
            }
        }
        else
        {
            tvCardHeader.text = "Location Selected"
            state = "Uttar Pradesh"
            district = "Agra"
            tvCardData.text = "${district}, ${state}"
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
            popupMenu.menuInflater.inflate( R.menu.toolbar_menu, popupMenu.menu )
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
                    }
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