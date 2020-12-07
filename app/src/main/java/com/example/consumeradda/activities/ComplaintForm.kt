package com.example.consumeradda.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
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
    var casetype :String ?= null
    lateinit var caseAgainst: String
    lateinit var caseOverview: String
    lateinit var moneyInvolved: String
    var phoneNumber = ""

    lateinit var firstName: String
    lateinit var lastName: String
    lateinit var city: String
    lateinit var state: String
    lateinit var attachment1DownloadableUrl : String
    lateinit var attachment2DownloadableUrl : String
    lateinit var attachment3DownloadableUrl : String
    lateinit var attachment4DownloadableUrl : String
    lateinit var attachment5DownloadableUrl : String

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

        val cases = arrayOf("Consumer Protection Law","Banking Regulation","Insurance Law","Debt Recovery","RERA","Other")
        btnCaseType.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,cases)

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

    private fun btnSubmitCaseSetOnClickListener() {
        btnSubmitCase.setOnClickListener{
            if(validData())
            {
                pbSubmitCase.visibility = View.VISIBLE
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
                            toDashboardActivity()
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
        city = etCityDistrict.text.toString()
        state = etState.text.toString()

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

        if(city.isNullOrEmpty() or city.isNullOrBlank())
        {
            etCityDistrict.error="Please enter a valid city name"
            etCityDistrict.requestFocus()
            return false
        }

        if(state.isNullOrEmpty() or state.isNullOrBlank())
        {
            etState.error="Please enter a valid state name"
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

        if(casetype == null || btnNumber == -1)
        {
            Toast.makeText(this,"Select some case type",Toast.LENGTH_SHORT).show()
            btnCaseType.requestFocus()
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

