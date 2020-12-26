package com.example.consumeradda.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.consumeradda.R
import com.example.consumeradda.models.caseModels.SubmitCaseDefaultResponse
import com.example.consumeradda.models.caseModels.SubmitCaseModel
import com.example.consumeradda.service.RetrofitClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_complaint_form.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ComplaintForm : AppCompatActivity() {

    private lateinit var mAuth : FirebaseAuth
    lateinit var mStorageRef:StorageReference
    var attachmentUri1 : Uri ?= null
    var attachmentUri2 : Uri ?= null
    var attachmentUri3 : Uri ?= null
    var attachmentUri4 : Uri ?= null
    var attachmentUri5 : Uri ?= null
    private val ATTACHMENT = 0
    var btnNumber = -1
    var casetype :String = "Select One:"
    lateinit var caseAgainst: String
    lateinit var caseOverview: String
    lateinit var moneyInvolved: String
    var phoneNumber = ""

    lateinit var firstName: String
    lateinit var lastName: String
    var city: String = "Select One:"
    var state: String = "Select One:"
    var attachment1DownloadableUrl : String = ""
    var attachment2DownloadableUrl : String = ""
    var attachment3DownloadableUrl : String = ""
    var attachment4DownloadableUrl : String = ""
    var attachment5DownloadableUrl : String = ""

    var attachment1Uploaded = false
    var attachment2Uploaded = false
    var attachment3Uploaded = false
    var attachment4Uploaded = false
    var attachment5Uploaded = false

    val APPLICATIONSUBMITTAG = "Application-Submit"


//    val caseTypes = resources.getStringArray(R.array.caseType)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complaint_form)

        mAuth=FirebaseAuth.getInstance()
        val curruser= mAuth.currentUser
        val em=curruser?.email
        etClientEmail.text=em?.toEditable()

        mStorageRef = FirebaseStorage.getInstance().reference

        btnBack.setOnClickListener {
            val intent = Intent(this, Dashboard::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }

        val States = arrayOf("Select One:","Andhra Pradesh","Arunachal Pradesh","Assam","Bihar","Chandigarh (UT)","Chhattisgarh","Dadra and Nagar Haveli (UT)","Daman and Diu (UT)","Delhi (NCT)","Goa","Gujarat","Haryana","Himachal Pradesh","Jammu and Kashmir","Jharkhand","Karnataka","Kerala","Lakshadweep (UT)","Madhya Pradesh","Maharashtra","Manipur","Meghalaya","Mizoram","Nagaland","Odisha","Puducherry (UT)","Punjab","Rajasthan","Sikkim","Tamil Nadu","Telangana","Tripura","Uttarakhand","Uttar Pradesh","West Bengal")
        val cases = arrayOf("Select One:","Consumer Protection Law","Banking Regulation","Insurance Law","Debt Recovery","RERA","Other")
        btnCaseType.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,cases)
        etState.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,States)

        etState.onItemSelectedListener = object : AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener{
            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                    state = States[position]
                    etCityDistrict.isClickable = true
                    updateCity(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

        btnCaseType.onItemSelectedListener = object : AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                casetype = cases[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                TODO("Not yet implemented")
            }

        }

        btnAttachment1SetOnClickListener()
        btnAttachment2SetOnClickListener()
        btnAttachment3SetOnClickListener()
        btnAttachment4SetOnClickListener()
        btnAttachment5SetOnClickListener()

        btnSubmitCaseSetOnClickListener()
    }

    private fun updateCity(position: Int) {
        when(position)
        {
            0-> {
                val districts = arrayOf("Select One:")
//                etCityDistrict.visibility = View.INVISIBLE
//                etCityDistrict.isEnabled = false
                etCityDistrict.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, districts)
                etCityDistrict.onItemSelectedListener = object : AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {
                    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        TODO("Not yet implemented")
                    }

                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                            city = "Select One:"
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }

                }
            }
            1-> {
                val districts = arrayOf("Select One:","Anantapur","Chittoor","East Godavari","Guntur","Krishna","Kurnool","Nellore","Prakasam","Srikakulam","Visakhapatnam","Vizianagaram","West Godavari","YSR Kadapa")
                etCityDistrict.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,districts)
                etCityDistrict.onItemSelectedListener = object : AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener{
                    override fun onItemClick(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                        TODO("Not yet implemented")
                    }

                    override fun onItemSelected(parent: AdapterView<*>?, view: View?,position: Int,id: Long) {
                        if(position!=0)
                        {
                            city = districts[position]
                        }
                        else{
                            city = "Select One:"
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }

                }
            }
            2->{
                val districts = arrayOf("Select One:","Tawang","West Kameng","East Kameng","Papum Pare","Kurung Kumey","Kra Daadi","Lower Subansiri","Upper Subansiri","West Siang","East Siang","Siang","Upper Siang","Lower Siang","Lower Dibang Valley","Dibang Valley","Anjaw","Lohit","Namsai","Changlang","Tirap","Longding")
                etCityDistrict.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,districts)
                etCityDistrict.onItemSelectedListener = object : AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener{
                    override fun onItemClick(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                        TODO("Not yet implemented")
                    }

                    override fun onItemSelected(parent: AdapterView<*>?, view: View?,position: Int,id: Long) {
                        if(position!=0)
                        {
                            city = districts[position]
                        }
                        else{
                            city = "Select One:"
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }

                }
            }
            3->{
                val districts = arrayOf("Select One:","Baksa","Barpeta","Biswanath","Bongaigaon","Cachar","Charaideo","Chirang","Darrang","Dhemaji","Dhubri","Dibrugarh","Goalpara","Golaghat","Hailakandi","Hojai","Jorhat","Kamrup Metropolitan","Kamrup","Karbi Anglong","Karimganj","Kokrajhar","Lakhimpur","Majuli","Morigaon","Nagaon","Nalbari","Dima Hasao","Sivasagar","Sonitpur","South Salmara-Mankachar","Tinsukia","Udalguri","West Karbi Anglong")
                etCityDistrict.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,districts)
                etCityDistrict.onItemSelectedListener = object : AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener{
                    override fun onItemClick(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                        TODO("Not yet implemented")
                    }

                    override fun onItemSelected(parent: AdapterView<*>?, view: View?,position: Int,id: Long) {
                        if(position!=0)
                        {
                            city = districts[position]
                        }
                        else{
                            city = "Select One:"
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }

                }
            }
            4->{
                val districts = arrayOf("Select One:","Araria","Arwal","Aurangabad","Banka","Begusarai","Bhagalpur","Bhojpur","Buxar","Darbhanga","East Champaran (Motihari)","Gaya","Gopalganj","Jamui","Jehanabad","Kaimur (Bhabua)","Katihar","Khagaria","Kishanganj","Lakhisarai","Madhepura","Madhubani","Munger (Monghyr)","Muzaffarpur","Nalanda","Nawada","Patna","Purnia (Purnea)","Rohtas","Saharsa","Samastipur","Saran","Sheikhpura","Sheohar","Sitamarhi","Siwan","Supaul","Vaishali","West Champaran")
                etCityDistrict.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,districts)
                etCityDistrict.onItemSelectedListener = object : AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener{
                    override fun onItemClick(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                        TODO("Not yet implemented")
                    }

                    override fun onItemSelected(parent: AdapterView<*>?, view: View?,position: Int,id: Long) {
                        if(position!=0)
                        {
                            city = districts[position]
                        }
                        else{
                            city = "Select One:"
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }

                }
            }
            5->{
                val districts = arrayOf("Select One:","Chandigarh")
                etCityDistrict.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,districts)
                etCityDistrict.onItemSelectedListener = object : AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener{
                    override fun onItemClick(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                        TODO("Not yet implemented")
                    }

                    override fun onItemSelected(parent: AdapterView<*>?, view: View?,position: Int,id: Long) {
                        if(position!=0)
                        {
                            city = districts[position]
                        }
                        else{
                            city = "Select One:"
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }

                }
            }
            6->{
                val districts = arrayOf("Select One:","Balod","Baloda Bazar","Balrampur","Bastar","Bemetara","Bijapur","Bilaspur","Dantewada (South Bastar)","Dhamtari","Durg","Gariyaband","Janjgir-Champa","Jashpur","Kabirdham (Kawardha)","Kanker (North Bastar)","Kondagaon","Korba","Korea (Koriya)","Mahasamund","Mungeli","Narayanpur","Raigarh","Raipur","Rajnandgaon","Sukma","Surajpur","Surguja")
                etCityDistrict.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,districts)
                etCityDistrict.onItemSelectedListener = object : AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener{
                    override fun onItemClick(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                        TODO("Not yet implemented")
                    }

                    override fun onItemSelected(parent: AdapterView<*>?, view: View?,position: Int,id: Long) {
                        if(position!=0)
                        {
                            city = districts[position]
                        }
                        else{
                            city = "Select One:"
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }

                }
            }
            7->{
                val districts = arrayOf("Select One:","Dadra & Nagar Haveli")
                etCityDistrict.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,districts)
                etCityDistrict.onItemSelectedListener = object : AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener{
                    override fun onItemClick(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                        TODO("Not yet implemented")
                    }

                    override fun onItemSelected(parent: AdapterView<*>?, view: View?,position: Int,id: Long) {
                        if(position!=0)
                        {
                            city = districts[position]
                        }
                        else{
                            city = "Select One:"
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }

                }
            }
            8->{
                val districts = arrayOf("Select One:","Daman","Diu")
                etCityDistrict.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,districts)
                etCityDistrict.onItemSelectedListener = object : AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener{
                    override fun onItemClick(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                        TODO("Not yet implemented")
                    }

                    override fun onItemSelected(parent: AdapterView<*>?, view: View?,position: Int,id: Long) {
                        if(position!=0)
                        {
                            city = districts[position]
                        }
                        else{
                            city = "Select One:"
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }

                }
            }
            9->{
                val districts = arrayOf("Select One:","Central Delhi","East Delhi","New Delhi","North Delhi","North East  Delhi","North West  Delhi","Shahdara","South Delhi","South East Delhi","South West  Delhi","West Delhi")
                etCityDistrict.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,districts)
                etCityDistrict.onItemSelectedListener = object : AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener{
                    override fun onItemClick(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                        TODO("Not yet implemented")
                    }

                    override fun onItemSelected(parent: AdapterView<*>?, view: View?,position: Int,id: Long) {
                        if(position!=0)
                        {
                            city = districts[position]
                        }
                        else{
                            city = "Select One:"
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }

                }
            }
            10->{
                val districts = arrayOf("Select One:","North Goa","South Goa")
                etCityDistrict.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,districts)
                etCityDistrict.onItemSelectedListener = object : AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener{
                    override fun onItemClick(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                        TODO("Not yet implemented")
                    }

                    override fun onItemSelected(parent: AdapterView<*>?, view: View?,position: Int,id: Long) {
                        if(position!=0)
                        {
                            city = districts[position]
                        }
                        else{
                            city = "Select One:"
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }

                }
            }
            11->{
                val districts = arrayOf("Select One:","Ahmedabad","Amreli","Anand","Aravalli","Banaskantha (Palanpur)","Bharuch","Bhavnagar","Botad","Chhota Udepur","Dahod","Dangs (Ahwa)","Devbhoomi Dwarka","Gandhinagar","Gir Somnath","Jamnagar","Junagadh","Kachchh","Kheda (Nadiad)","Mahisagar","Mehsana","Morbi","Narmada (Rajpipla)","Navsari","Panchmahal (Godhra)","Patan","Porbandar","Rajkot","Sabarkantha (Himmatnagar)","Surat","Surendranagar","Tapi (Vyara)","Vadodara","Valsad")
                etCityDistrict.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,districts)
                etCityDistrict.onItemSelectedListener = object : AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener{
                    override fun onItemClick(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                        TODO("Not yet implemented")
                    }

                    override fun onItemSelected(parent: AdapterView<*>?, view: View?,position: Int,id: Long) {
                        if(position!=0)
                        {
                            city = districts[position]
                        }
                        else{
                            city = "Select One:"
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }

                }
            }
            12->{
                val districts = arrayOf("Select One:","Ambala","Bhiwani","Charkhi Dadri","Faridabad","Fatehabad","Gurgaon","Hisar","Jhajjar","Jind","Kaithal","Karnal","Kurukshetra","Mahendragarh","Mewat","Palwal","Panchkula","Panipat","Rewari","Rohtak","Sirsa","Sonipat","Yamunanagar")
                etCityDistrict.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,districts)
                etCityDistrict.onItemSelectedListener = object : AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener{
                    override fun onItemClick(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                        TODO("Not yet implemented")
                    }

                    override fun onItemSelected(parent: AdapterView<*>?, view: View?,position: Int,id: Long) {
                        if(position!=0)
                        {
                            city = districts[position]
                        }
                        else{
                            city = "Select One:"
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }

                }
            }
            13->{
                val districts = arrayOf("Select One:","Bilaspur","Chamba","Hamirpur","Kangra","Kinnaur","Kullu","Lahaul & Spiti","Mandi","Shimla","Sirmaur (Sirmour)","Solan","Una")
                etCityDistrict.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,districts)
                etCityDistrict.onItemSelectedListener = object : AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener{
                    override fun onItemClick(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                        TODO("Not yet implemented")
                    }

                    override fun onItemSelected(parent: AdapterView<*>?, view: View?,position: Int,id: Long) {
                        if(position!=0)
                        {
                            city = districts[position]
                        }
                        else{
                            city = "Select One:"
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }

                }
            }
            14->{
                val districts = arrayOf("Select One:","Anantnag","Bandipore","Baramulla","Budgam","Doda","Ganderbal","Jammu","Kargil","Kathua","Kishtwar","Kulgam","Kupwara","Leh","Poonch","Pulwama","Rajouri","Ramban","Reasi","Samba","Shopian","Srinagar","Udhampur")
                etCityDistrict.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,districts)
                etCityDistrict.onItemSelectedListener = object : AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener{
                    override fun onItemClick(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                        TODO("Not yet implemented")
                    }

                    override fun onItemSelected(parent: AdapterView<*>?, view: View?,position: Int,id: Long) {
                        if(position!=0)
                        {
                            city = districts[position]
                        }
                        else{
                            city = "Select One:"
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }

                }
            }
            15->{
                val districts = arrayOf("Select One:","Bokaro","Chatra","Deoghar","Dhanbad","Dumka","East Singhbhum","Garhwa","Giridih","Godda","Gumla","Hazaribag","Jamtara","Khunti","Koderma","Latehar","Lohardaga","Pakur","Palamu","Ramgarh","Ranchi","Sahibganj","Seraikela-Kharsawan","Simdega","West Singhbhum")
                etCityDistrict.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,districts)
                etCityDistrict.onItemSelectedListener = object : AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener{
                    override fun onItemClick(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                        TODO("Not yet implemented")
                    }

                    override fun onItemSelected(parent: AdapterView<*>?, view: View?,position: Int,id: Long) {
                        if(position!=0)
                        {
                            city = districts[position]
                        }
                        else{
                            city = "Select One:"
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }

                }
            }
            16->{
                val districts = arrayOf("Select One:","Bagalkot","Ballari (Bellary)","Belagavi (Belgaum)","Bengaluru (Bangalore) Rural","Bengaluru (Bangalore) Urban","Bidar","Chamarajanagar","Chikballapur","Chikkamagaluru (Chikmagalur)","Chitradurga","Dakshina Kannada","Davangere","Dharwad","Gadag","Hassan","Haveri","Kalaburagi (Gulbarga)","Kodagu","Kolar","Koppal","Mandya","Mysuru (Mysore)","Raichur","Ramanagara","Shivamogga (Shimoga)","Tumakuru (Tumkur)","Udupi","Uttara Kannada (Karwar)","Vijayapura (Bijapur)","Yadgir")
                etCityDistrict.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,districts)
                etCityDistrict.onItemSelectedListener = object : AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener{
                    override fun onItemClick(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                        TODO("Not yet implemented")
                    }

                    override fun onItemSelected(parent: AdapterView<*>?, view: View?,position: Int,id: Long) {
                        if(position!=0)
                        {
                            city = districts[position]
                        }
                        else{
                            city = "Select One:"
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }

                }
            }
            17->{
                val districts = arrayOf("Select One:","Alappuzha","Ernakulam","Idukki","Kannur","Kasaragod","Kollam","Kottayam","Kozhikode","Malappuram","Palakkad","Pathanamthitta","Thiruvananthapuram","Thrissur","Wayanad")
                etCityDistrict.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,districts)
                etCityDistrict.onItemSelectedListener = object : AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener{
                    override fun onItemClick(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                        TODO("Not yet implemented")
                    }

                    override fun onItemSelected(parent: AdapterView<*>?, view: View?,position: Int,id: Long) {
                        if(position!=0)
                        {
                            city = districts[position]
                        }
                        else{
                            city = "Select One:"
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }

                }
            }
            18->{
                val districts = arrayOf("Select One:","Agatti","Amini","Androth","Bithra","Chethlath","Kavaratti","Kadmath","Kalpeni","Kilthan","Minicoy")
                etCityDistrict.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,districts)
                etCityDistrict.onItemSelectedListener = object : AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener{
                    override fun onItemClick(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                        TODO("Not yet implemented")
                    }

                    override fun onItemSelected(parent: AdapterView<*>?, view: View?,position: Int,id: Long) {
                        if(position!=0)
                        {
                            city = districts[position]
                        }
                        else{
                            city = "Select One:"
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }

                }
            }
            19->{
                val districts = arrayOf("Select One:","Agar Malwa","Alirajpur","Anuppur","Ashoknagar","Balaghat","Barwani","Betul","Bhind","Bhopal","Burhanpur","Chhatarpur","Chhindwara","Damoh","Datia","Dewas","Dhar","Dindori","Guna","Gwalior","Harda","Hoshangabad","Indore","Jabalpur","Jhabua","Katni","Khandwa","Khargone","Mandla","Mandsaur","Morena","Narsinghpur","Neemuch","Panna","Raisen","Rajgarh","Ratlam","Rewa","Sagar","Satna","Sehore","Seoni","Shahdol","Shajapur","Sheopur","Shivpuri","Sidhi","Singrauli","Tikamgarh","Ujjain","Umaria","Vidisha")
                etCityDistrict.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,districts)
                etCityDistrict.onItemSelectedListener = object : AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener{
                    override fun onItemClick(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                        TODO("Not yet implemented")
                    }

                    override fun onItemSelected(parent: AdapterView<*>?, view: View?,position: Int,id: Long) {
                        if(position!=0)
                        {
                            city = districts[position]
                        }
                        else{
                            city = "Select One:"
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }

                }
            }
            20->{
                val districts = arrayOf("Select One:","Ahmednagar","Akola","Amravati","Aurangabad","Beed","Bhandara","Buldhana","Chandrapur","Dhule","Gadchiroli","Gondia","Hingoli","Jalgaon","Jalna","Kolhapur","Latur","Mumbai City","Mumbai Suburban","Nagpur","Nanded","Nandurbar","Nashik","Osmanabad","Palghar","Parbhani","Pune","Raigad","Ratnagiri","Sangli","Satara","Sindhudurg","Solapur","Thane","Wardha","Washim","Yavatmal")
                etCityDistrict.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,districts)
                etCityDistrict.onItemSelectedListener = object : AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener{
                    override fun onItemClick(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                        TODO("Not yet implemented")
                    }

                    override fun onItemSelected(parent: AdapterView<*>?, view: View?,position: Int,id: Long) {
                        if(position!=0)
                        {
                            city = districts[position]
                        }
                        else{
                            city = "Select One:"
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }

                }
            }
            21->{
                val districts = arrayOf("Select One:","Bishnupur","Chandel","Churachandpur","Imphal East","Imphal West","Jiribam","Kakching","Kamjong","Kangpokpi","Noney","Pherzawl","Senapati","Tamenglong","Tengnoupal","Thoubal","Ukhrul")
                etCityDistrict.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,districts)
                etCityDistrict.onItemSelectedListener = object : AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener{
                    override fun onItemClick(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                        TODO("Not yet implemented")
                    }

                    override fun onItemSelected(parent: AdapterView<*>?, view: View?,position: Int,id: Long) {
                        if(position!=0)
                        {
                            city = districts[position]
                        }
                        else{
                            city = "Select One:"
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }

                }
            }
            22->{
                val districts = arrayOf("Select One:","East Garo Hills","East Jaintia Hills","East Khasi Hills","North Garo Hills","Ri Bhoi","South Garo Hills","South West Garo Hills ","South West Khasi Hills","West Garo Hills","West Jaintia Hills","West Khasi Hills")
                etCityDistrict.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,districts)
                etCityDistrict.onItemSelectedListener = object : AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener{
                    override fun onItemClick(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                        TODO("Not yet implemented")
                    }

                    override fun onItemSelected(parent: AdapterView<*>?, view: View?,position: Int,id: Long) {
                        if(position!=0)
                        {
                            city = districts[position]
                        }
                        else{
                            city = "Select One:"
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }

                }
            }
            23->{
                val districts = arrayOf("Select One:","Aizawl","Champhai","Kolasib","Lawngtlai","Lunglei","Mamit","Saiha","Serchhip")
                etCityDistrict.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,districts)
                etCityDistrict.onItemSelectedListener = object : AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener{
                    override fun onItemClick(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                        TODO("Not yet implemented")
                    }

                    override fun onItemSelected(parent: AdapterView<*>?, view: View?,position: Int,id: Long) {
                        if(position!=0)
                        {
                            city = districts[position]
                        }
                        else{
                            city = "Select One:"
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }

                }
            }
            24->{
                val districts = arrayOf("Select One:","Dimapur","Kiphire","Kohima","Longleng","Mokokchung","Mon","Peren","Phek","Tuensang","Wokha","Zunheboto")
                etCityDistrict.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,districts)
                etCityDistrict.onItemSelectedListener = object : AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener{
                    override fun onItemClick(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                        TODO("Not yet implemented")
                    }

                    override fun onItemSelected(parent: AdapterView<*>?, view: View?,position: Int,id: Long) {
                        if(position!=0)
                        {
                            city = districts[position]
                        }
                        else{
                            city = "Select One:"
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }

                }
            }
            25->{
                val districts = arrayOf("Select One:","Angul","Balangir","Balasore","Bargarh","Bhadrak","Boudh","Cuttack","Deogarh","Dhenkanal","Gajapati","Ganjam","Jagatsinghapur","Jajpur","Jharsuguda","Kalahandi","Kandhamal","Kendrapara","Kendujhar (Keonjhar)","Khordha","Koraput","Malkangiri","Mayurbhanj","Nabarangpur","Nayagarh","Nuapada","Puri","Rayagada","Sambalpur","Sonepur","Sundargarh")
                etCityDistrict.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,districts)
                etCityDistrict.onItemSelectedListener = object : AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener{
                    override fun onItemClick(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                        TODO("Not yet implemented")
                    }

                    override fun onItemSelected(parent: AdapterView<*>?, view: View?,position: Int,id: Long) {
                        if(position!=0)
                        {
                            city = districts[position]
                        }
                        else{
                            city = "Select One:"
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }

                }
            }
            26->{
                val districts = arrayOf("Select One:","Karaikal","Mahe","Pondicherry","Yanam")
                etCityDistrict.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,districts)
                etCityDistrict.onItemSelectedListener = object : AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener{
                    override fun onItemClick(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                        TODO("Not yet implemented")
                    }

                    override fun onItemSelected(parent: AdapterView<*>?, view: View?,position: Int,id: Long) {
                        if(position!=0)
                        {
                            city = districts[position]
                        }
                        else{
                            city = "Select One:"
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }

                }
            }
            27->{
                val districts = arrayOf("Select One:","Amritsar","Barnala","Bathinda","Faridkot","Fatehgarh Sahib","Fazilka","Ferozepur","Gurdaspur","Hoshiarpur","Jalandhar","Kapurthala","Ludhiana","Mansa","Moga","Muktsar","Nawanshahr (Shahid Bhagat Singh Nagar)","Pathankot","Patiala","Rupnagar","Sahibzada Ajit Singh Nagar (Mohali)","Sangrur","Tarn Taran")
                etCityDistrict.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,districts)
                etCityDistrict.onItemSelectedListener = object : AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener{
                    override fun onItemClick(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                        TODO("Not yet implemented")
                    }

                    override fun onItemSelected(parent: AdapterView<*>?, view: View?,position: Int,id: Long) {
                        if(position!=0)
                        {
                            city = districts[position]
                        }
                        else{
                            city = "Select One:"
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }

                }
            }
            28->{
                val districts = arrayOf("Select One:","Ajmer","Alwar","Banswara","Baran","Barmer","Bharatpur","Bhilwara","Bikaner","Bundi","Chittorgarh","Churu","Dausa","Dholpur","Dungarpur","Hanumangarh","Jaipur","Jaisalmer","Jalore","Jhalawar","Jhunjhunu","Jodhpur","Karauli","Kota","Nagaur","Pali","Pratapgarh","Rajsamand","Sawai Madhopur","Sikar","Sirohi","Sri Ganganagar","Tonk","Udaipur")
                etCityDistrict.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,districts)
                etCityDistrict.onItemSelectedListener = object : AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener{
                    override fun onItemClick(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                        TODO("Not yet implemented")
                    }

                    override fun onItemSelected(parent: AdapterView<*>?, view: View?,position: Int,id: Long) {
                        if(position!=0)
                        {
                            city = districts[position]
                        }
                        else{
                            city = "Select One:"
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }

                }
            }
            29->{
                val districts = arrayOf("Select One:","East Sikkim","North Sikkim","South Sikkim","West Sikkim")
                etCityDistrict.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,districts)
                etCityDistrict.onItemSelectedListener = object : AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener{
                    override fun onItemClick(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                        TODO("Not yet implemented")
                    }

                    override fun onItemSelected(parent: AdapterView<*>?, view: View?,position: Int,id: Long) {
                        if(position!=0)
                        {
                            city = districts[position]
                        }
                        else{
                            city = "Select One:"
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }

                }
            }
            30->{
                val districts = arrayOf("Select One:","Ariyalur","Chennai","Coimbatore","Cuddalore","Dharmapuri","Dindigul","Erode","Kanchipuram","Kanyakumari","Karur","Krishnagiri","Madurai","Nagapattinam","Namakkal","Nilgiris","Perambalur","Pudukkottai","Ramanathapuram","Salem","Sivaganga","Thanjavur","Theni","Thoothukudi (Tuticorin)","Tiruchirappalli","Tirunelveli","Tiruppur","Tiruvallur","Tiruvannamalai","Tiruvarur","Vellore","Viluppuram","Virudhunagar")
                etCityDistrict.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,districts)
                etCityDistrict.onItemSelectedListener = object : AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener{
                    override fun onItemClick(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                        TODO("Not yet implemented")
                    }

                    override fun onItemSelected(parent: AdapterView<*>?, view: View?,position: Int,id: Long) {
                        if(position!=0)
                        {
                            city = districts[position]
                        }
                        else{
                            city = "Select One:"
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }

                }
            }
            31->{
                val districts = arrayOf("Select One:","Adilabad","Bhadradri Kothagudem","Hyderabad","Jagtial","Jangaon","Jayashankar Bhoopalpally","Jogulamba Gadwal","Kamareddy","Karimnagar","Khammam","Komaram Bheem Asifabad","Mahabubabad","Mahabubnagar","Mancherial","Medak","Medchal","Nagarkurnool","Nalgonda","Nirmal","Nizamabad","Peddapalli","Rajanna Sircilla","Rangareddy","Sangareddy","Siddipet","Suryapet","Vikarabad","Wanaparthy","Warangal (Rural)","Warangal (Urban)","Yadadri Bhuvanagiri")
                etCityDistrict.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,districts)
                etCityDistrict.onItemSelectedListener = object : AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener{
                    override fun onItemClick(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                        TODO("Not yet implemented")
                    }

                    override fun onItemSelected(parent: AdapterView<*>?, view: View?,position: Int,id: Long) {
                        if(position!=0)
                        {
                            city = districts[position]
                        }
                        else{
                            city = "Select One:"
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }
                }
            }
            32->{
                val districts = arrayOf("Select One:","Dhalai","Gomati","Khowai","North Tripura","Sepahijala","South Tripura","Unakoti","West Tripura")
                etCityDistrict.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,districts)
                etCityDistrict.onItemSelectedListener = object : AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener{
                    override fun onItemClick(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                        TODO("Not yet implemented")
                    }

                    override fun onItemSelected(parent: AdapterView<*>?, view: View?,position: Int,id: Long) {
                        if(position!=0)
                        {
                            city = districts[position]
                        }
                        else{
                            city = "Select One:"
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }
                }
            }
            33->{
                val districts = arrayOf("Select One:","Almora","Bageshwar","Chamoli","Champawat","Dehradun","Haridwar","Nainital","Pauri Garhwal","Pithoragarh","Rudraprayag","Tehri Garhwal","Udham Singh Nagar","Uttarkashi")
                etCityDistrict.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,districts)
                etCityDistrict.onItemSelectedListener = object : AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener{
                    override fun onItemClick(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                        TODO("Not yet implemented")
                    }

                    override fun onItemSelected(parent: AdapterView<*>?, view: View?,position: Int,id: Long) {
                        if(position!=0)
                        {
                            city = districts[position]
                        }
                        else{
                            city = "Select One:"
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }
                }
            }
            34->{
                val districts = arrayOf("Select One:","Agra","Aligarh","Allahabad","Ambedkar Nagar","Amethi (Chatrapati Sahuji Mahraj Nagar)","Amroha (J.P. Nagar)","Auraiya","Azamgarh","Baghpat","Bahraich","Ballia","Balrampur","Banda","Barabanki","Bareilly","Basti","Bhadohi","Bijnor","Budaun","Bulandshahr","Chandauli","Chitrakoot","Deoria","Etah","Etawah","Faizabad","Farrukhabad","Fatehpur","Firozabad","Gautam Buddha Nagar","Ghaziabad","Ghazipur","Gonda","Gorakhpur","Hamirpur","Hapur (Panchsheel Nagar)","Hardoi","Hathras","Jalaun","Jaunpur","Jhansi","Kannauj","Kanpur Dehat","Kanpur Nagar","Kanshiram Nagar (Kasganj)","Kaushambi","Kushinagar (Padrauna)","Lakhimpur - Kheri","Lalitpur","Lucknow","Maharajganj","Mahoba","Mainpuri","Mathura","Mau","Meerut","Mirzapur","Moradabad","Muzaffarnagar","Pilibhit","Pratapgarh","RaeBareli","Rampur","Saharanpur","Sambhal (Bhim Nagar)","Sant Kabir Nagar","Shahjahanpur","Shamali (Prabuddh Nagar)","Shravasti","Siddharth Nagar","Sitapur","Sonbhadra","Sultanpur","Unnao","Varanasi")
                etCityDistrict.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,districts)
                etCityDistrict.onItemSelectedListener = object : AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener{
                    override fun onItemClick(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                        TODO("Not yet implemented")
                    }

                    override fun onItemSelected(parent: AdapterView<*>?, view: View?,position: Int,id: Long) {
                        if(position!=0)
                        {
                            city = districts[position]
                        }
                        else{
                            city = "Select One:"
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }
                }
            }
            35->{
                val districts = arrayOf("Select One:","Alipurduar","Bankura","Birbhum","Burdwan (Bardhaman)","Cooch Behar","Dakshin Dinajpur (South Dinajpur)","Darjeeling","Hooghly","Howrah","Jalpaiguri","Kalimpong","Kolkata","Malda","Murshidabad","Nadia","North 24 Parganas","Paschim Medinipur (West Medinipur)","Purba Medinipur (East Medinipur)","Purulia","South 24 Parganas","Uttar Dinajpur (North Dinajpur)")
                etCityDistrict.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,districts)
                etCityDistrict.onItemSelectedListener = object : AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener{
                    override fun onItemClick(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                        TODO("Not yet implemented")
                    }

                    override fun onItemSelected(parent: AdapterView<*>?, view: View?,position: Int,id: Long) {
                        if(position!=0)
                        {
                            city = districts[position]
                        }
                        else{
                            city = "Select One:"
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }
                }
            }
        }
    }

    private fun btnSubmitCaseSetOnClickListener() {
        btnSubmitCase.setOnClickListener{
            if(validData())
            {
                pbSubmitCase.visibility = View.VISIBLE
                btnSubmitCase.isEnabled = false
                btnSubmitCase.isClickable = false
                btnSubmitCase.text = ""
                uploadDoc1()
            }
        }
    }

    private fun uploadDoc1() {
        val mDocsRef = mStorageRef.child(mAuth.currentUser!!.uid).child("docs/attachmentUri1.pdf")
        mDocsRef.putFile(attachmentUri1!!).continueWithTask{ task->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            mDocsRef.downloadUrl
        }.addOnCompleteListener {
            if (it.isSuccessful){
                Log.i("test",it.result.toString())
                attachment1DownloadableUrl=it.result!!.toString()
                attachment1Uploaded=true
                if(btnNumber >= 2)
                {
                    Log.i("Download-URL-1",attachment1DownloadableUrl)
                    uploadDoc2()
                }
                else
                {
                    submitCase()
                }
            }
            else{
                Log.i("test","upload failed")
            }
        }
    }

    private fun uploadDoc2() {
        val mDocsRef = mStorageRef.child(mAuth.currentUser!!.uid).child("docs/attachmentUri2.pdf")
        mDocsRef.putFile(attachmentUri2!!).continueWithTask{ task->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            mDocsRef.downloadUrl
        }.addOnCompleteListener {
            if (it.isSuccessful){
                Log.i("test",it.result.toString())
                attachment2DownloadableUrl=it.result!!.toString()
                attachment2Uploaded=true
                if(btnNumber>=3)
                {
                    uploadDoc3()
                }
                else{
                    submitCase()
                }
            }
            else{
                Log.i("test","upload failed")
            }
        }
    }

    private fun uploadDoc3() {
        val mDocsRef = mStorageRef.child(mAuth.currentUser!!.uid).child("docs/attachmentUri3.pdf")
        mDocsRef.putFile(attachmentUri3!!).continueWithTask{ task->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            mDocsRef.downloadUrl
        }.addOnCompleteListener {
            if (it.isSuccessful){
                Log.i("test",it.result.toString())
                attachment3DownloadableUrl=it.result!!.toString()
                attachment3Uploaded=true
                if(btnNumber>=4)
                {
                    uploadDoc4()
                }
                else{
                    submitCase()
                }
            }
            else{
                Log.i("test","upload failed")
            }
        }
    }

    private fun uploadDoc4() {
        val mDocsRef = mStorageRef.child(mAuth.currentUser!!.uid).child("docs/attachmentUri4.pdf")
        mDocsRef.putFile(attachmentUri4!!).continueWithTask{ task->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            mDocsRef.downloadUrl
        }.addOnCompleteListener {
            if (it.isSuccessful){
                Log.i("test",it.result.toString())
                attachment4DownloadableUrl=it.result!!.toString()
                attachment4Uploaded=true
                if(btnNumber>=5)
                {
                    uploadDoc5()
                }
                else{
                    submitCase()
                }
            }
            else{
                Log.i("test","upload failed")
            }
        }
    }

    private fun uploadDoc5() {
        val mDocsRef = mStorageRef.child(mAuth.currentUser!!.uid).child("docs/attachmentUri5.pdf")
        mDocsRef.putFile(attachmentUri5!!).continueWithTask{ task->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            mDocsRef.downloadUrl
        }.addOnCompleteListener {
            if (it.isSuccessful){
                Log.i("test",it.result.toString())
                attachment5DownloadableUrl=it.result!!.toString()
                attachment5Uploaded=true

                submitCase()
            }
            else{
                Log.i("test","upload failed")
            }
        }
    }

    private fun submitCase() {
        val obj = SubmitCaseModel(
                applicantFirstName = firstName,
                applicantLastName = lastName,
                contactNumber = phoneNumber,
                district = city,
                state = state,
                caseAgainst = caseAgainst,
                caseType = casetype,
                caseOverview = caseOverview,
                moneyInvolved = moneyInvolved.toInt(),
                doc1 = attachment1DownloadableUrl,
                doc2 = attachment2DownloadableUrl,
                doc3 = attachment3DownloadableUrl,
                doc4 = attachment4DownloadableUrl,
                doc5 = attachment5DownloadableUrl
        )

        RetrofitClient.instance.caseService.submitApplication(obj)
                .enqueue(object : Callback<SubmitCaseDefaultResponse> {
                    override fun onResponse(
                            call: Call<SubmitCaseDefaultResponse>,
                            response: Response<SubmitCaseDefaultResponse>
                    ) {
                        if (response.isSuccessful) {
                            Log.i(APPLICATIONSUBMITTAG, "success")
                            toastMaker(response.body()?.message)
                            Handler().postDelayed({
                                toDashboardActivity()
                            },3000)
                        } else {
                            val jObjError = JSONObject(response.errorBody()!!.string())
                            Log.i(APPLICATIONSUBMITTAG, response.toString())
                            Log.i(APPLICATIONSUBMITTAG, jObjError.getString("message"))
                            toastMaker(jObjError.getString("message"))
                            toDashboardActivity()
                        }
                    }

                    override fun onFailure(call: Call<SubmitCaseDefaultResponse>, t: Throwable) {
                        Log.i(APPLICATIONSUBMITTAG, "error" + t.message)
                        toastMaker("Failed to Submit Application - " + t.message)
                        pbSubmitCase.visibility=View.INVISIBLE
                        btnSubmitCase.isEnabled = true
                        btnSubmitCase.isClickable = true
                        btnSubmitCase.text = "Submit"
                    }
                })
    }

    private fun toDashboardActivity() {
        val intent = Intent(this, Dashboard::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    private fun toastMaker(message: String?) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }

    private fun validData(): Boolean {

        firstName = etFirstName.text.toString()
        lastName = etLastName.text.toString()
//        city = etCityDistrict.text.toString()
//        state = etState.text.toString()

        caseAgainst = etCaseAgainst.text.toString()
        caseOverview = etCaseDescription.text.toString()
        moneyInvolved = etMoneyInvolved.text.toString()

        phoneNumber = etClientPhoneNumber.text.toString()

        if(firstName.isNullOrEmpty() or firstName.isNullOrBlank())
        {
            etFirstName.error="Please enter a valid first name"
            etFirstName.requestFocus()
            return false
        }

        if(lastName.isNullOrEmpty() or lastName.isNullOrBlank())
        {
            etLastName.error="Please enter a valid last name"
            etLastName.requestFocus()
            return false
        }

        if(city.isNullOrEmpty() or city.isBlank() or city.equals("Select One:"))
        {
//            etCityDistrict.error="Please enter a valid city name"
            Toast.makeText(this,"Kindly select district of your residence!",Toast.LENGTH_LONG).show()
            etCityDistrict.requestFocus()
            return false
        }

        if(state == "Select One:")
        {
            Toast.makeText(this,"Kindly select state of your residence!",Toast.LENGTH_LONG).show()
            etState.requestFocus()
            return false
        }

        if(caseAgainst.isNullOrEmpty() or caseAgainst.isNullOrBlank())
        {
            etCaseAgainst.error="This field is mandatory"
            etCaseAgainst.requestFocus()
            return false
        }

        if(caseOverview.isNullOrEmpty() or caseOverview.isNullOrBlank())
        {
            etCaseDescription.error="This field is mandatory"
            etCaseDescription.requestFocus()
            return false
        }

        if(moneyInvolved.isNullOrEmpty() or moneyInvolved.isNullOrBlank())
        {
            etMoneyInvolved.error="This field is mandatory"
            etMoneyInvolved.requestFocus()
            return false
        }

        if(casetype == "Select One:")
        {
            Toast.makeText(this,"Select some case type",Toast.LENGTH_SHORT).show()
            btnCaseType.requestFocus()
            return false
        }

        if(btnNumber == -1)
        {
            Toast.makeText(this,"Upload at-least one attachment",Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun btnAttachment1SetOnClickListener() {
        btnAttachment1.setOnClickListener {
            val intent = getFileChooserIntentForImageAndPdf()
            btnNumber = 1
            startActivityForResult(Intent.createChooser(intent, "Select Attachment"), ATTACHMENT)
        }
    }

    private fun btnAttachment2SetOnClickListener() {
        btnAttachment2.setOnClickListener{
            val intent = getFileChooserIntentForImageAndPdf()
            btnNumber = 2
            startActivityForResult(Intent.createChooser(intent, "Select Attachment"), ATTACHMENT)
        }
    }

    private fun btnAttachment3SetOnClickListener() {
        btnAttachment3.setOnClickListener {
            val intent = getFileChooserIntentForImageAndPdf()
            btnNumber = 3
            startActivityForResult(Intent.createChooser(intent, "Select Attachment"), ATTACHMENT)
        }
    }

    private fun btnAttachment4SetOnClickListener() {
        btnAttachment4.setOnClickListener {
            val intent = getFileChooserIntentForImageAndPdf()
            btnNumber = 4
            startActivityForResult(Intent.createChooser(intent, "Select Attachment"), ATTACHMENT)
        }
    }

    private fun btnAttachment5SetOnClickListener() {
        btnAttachment5.setOnClickListener {
            val intent = getFileChooserIntentForImageAndPdf()
            btnNumber = 5
            startActivityForResult(Intent.createChooser(intent, "Select Attachment"), ATTACHMENT)
        }
    }

    private fun getFileChooserIntentForImageAndPdf(): Intent {
        val mimeTypes = arrayOf("image/*", "application/pdf")
        val intent = Intent(Intent.ACTION_GET_CONTENT)
                .setType("image/*|application/pdf")
                .putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        return intent
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        if(resultCode == Activity.RESULT_OK)
        {
            if(requestCode == ATTACHMENT)
            {
                if(data != null)
                {
                    when(btnNumber)
                    {
                        1-> {
                            attachmentUri1 = data.data!!
                            btnAttachment1.setImageResource(R.drawable.ic_baseline_check_24)
                            btnAttachment1.isClickable = false
                            btnAttachment1.isActivated = false
                            btnAttachment2.isVisible = true
                        }
                        2-> {
                            attachmentUri2 = data.data!!
                            btnAttachment2.setImageResource(R.drawable.ic_baseline_check_24)
                            btnAttachment2.isClickable = false
                            btnAttachment2.isActivated = false
                            btnAttachment3.isVisible = true
                        }
                        3->{
                            attachmentUri3 = data.data!!
                            btnAttachment3.setImageResource(R.drawable.ic_baseline_check_24)
                            btnAttachment3.isClickable = false
                            btnAttachment3.isActivated = false
                            btnAttachment4.isVisible = true

                        }
                        4->{
                            attachmentUri4 = data.data!!
                            btnAttachment4.setImageResource(R.drawable.ic_baseline_check_24)
                            btnAttachment4.isClickable = false
                            btnAttachment4.isActivated = false
                            btnAttachment5.isVisible = true
                        }
                        5->{
                            attachmentUri5 = data.data!!
                            btnAttachment5.setImageResource(R.drawable.ic_baseline_check_24)
                            btnAttachment5.isClickable = false
                            btnAttachment5.isActivated = false
                        }
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)

}

