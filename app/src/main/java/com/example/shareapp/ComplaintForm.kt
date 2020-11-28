package com.example.shareapp

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ServerTimestamp
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_complaint_form.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.properties.Delegates


class ComplaintForm : AppCompatActivity() {


    private var imgs:Int=0
    private var cid:String=""
    private lateinit var casetypes:Spinner
    private lateinit var temp:String

    private val PICK_IMAGE_REQUEST=1234

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private var storage:FirebaseStorage?=null
    private var storageRef: StorageReference? = null

    private var imageurl1:String?=null
    private var imageurl2:String?=null
    var imageurl3:String?=null
    var imageurl4:String?=null
    var imageurl5:String?=null

    var selectedPhotoUri1: Uri?=null
    var selectedPhotoUri2: Uri?=null
    var selectedPhotoUri3: Uri?=null
    var selectedPhotoUri4: Uri?=null
    var selectedPhotoUri5: Uri?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complaint_form)

        casetypes=findViewById(R.id.case_type) as Spinner

        val catype= arrayOf("Consumer Protection Law","Banking Regulation","Insurance Law","Debt Recovery","Real Estate / RERA","Other")
        casetypes.adapter=ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,catype)
        casetypes.onItemSelectedListener=object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                textView19.error="Select Case Type"
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long ) {
                if(position==0)
                    temp="consumer_law"
                else if(position==1)
                    temp="banking_law"
                else if(position==2)
                    temp="insurance_law"
                else if(position==3)
                    temp="debt_recovery"
                else if(position==4)
                    temp="rera"
                else
                    temp="other"
            }
        }

//        setSupportActionBar(toolbar)
//        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        storage= FirebaseStorage.getInstance()
        storageRef=storage!!.reference

        auth=FirebaseAuth.getInstance()
        val curruser= auth.currentUser
        val em=curruser?.email
        case_email.text=em?.toEditable()

        just_start();

        db= FirebaseFirestore.getInstance()
        db.collection("users").document(em.toString()).get()
            .addOnSuccessListener {
             document ->
                if(document!=null)
                {
                    var nt=document.getString("Name")
                    case_name.text=nt?.toEditable()
                }
            }

        final_btn.setOnClickListener {
            register_case()
        }

        img_btn1.setOnClickListener {
            chooseImage()
        }

        img_btn2.setOnClickListener {
            chooseImage()
        }

        img_btn3.setOnClickListener {
            chooseImage()
        }

        img_btn4.setOnClickListener {
            chooseImage()
        }

        img_btn5.setOnClickListener {
            chooseImage()
        }

    }
    fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)



    private fun register_case()
    {
        val against=case_against.text.toString().trim()
        val description=case_desc.text.toString().trim()
        val curruser= auth.currentUser
        val em=curruser?.email
        var money=case_money.text.toString().trim().toInt()
        var lawyer=""
        val status="Complaint Submitted"


        if(against.isEmpty())
        {
            case_against.error="Enter opposition name!"
            case_against.requestFocus()
            return
        }

        if(description.length>100)
        {
            case_desc.error="Word limit exceeded!"
            case_desc.requestFocus()
            return
        }

        if(description.isEmpty())
        {
            case_desc.error="Must not be empty!"
            case_desc.requestFocus()
            return
        }

        if(this.imgs==0)
        {
            textView22.error="Select atleast one attachment!"
            textView22.requestFocus()
            return
        }
        if(case_money.text.toString().trim().isEmpty())
        {
            case_money.error="Enter 0 if no money involved"
            case_money.requestFocus()
            return
        }


        var caval:Long?=0
        //var cid:String=""
        val progress= ProgressDialog(this,R.style.AlertDialogTheme)
        progress.setMessage("Submitting your Complaint")
        progress.setCancelable(false)
        progress.show()
        db.collection("case_type").document("types").update(temp,FieldValue.increment(1))
        db.collection("case_type").document("types").get()
            .addOnSuccessListener {
                    document ->
                if(document!=null)
                {
                    caval=document.getLong(temp)
//                        Toast.makeText(this,caval.toString(),Toast.LENGTH_SHORT).show()
                    cid="ca_"+temp+"_"+caval
                }
                progress.dismiss()
//                Toast.makeText(this,cid,Toast.LENGTH_SHORT).show()

                perform_upload()
                val info=HashMap<String,Any>()
                info.put("against",against)
                info.put("case_id",cid)
                info.put("description",description)
                info.put("type",temp)
                info.put("money_involved",money)
                if (em != null) {
                    info.put("user_id",em)
                }
                if(case_phno.text.toString().trim().isNotEmpty())
                {
                    info.put("user_phno",case_phno.text.toString().trim())
                }
                info.put("lawyer_id",lawyer)
                info.put("last_status",status)
                val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
                val subd = sdf.format(Date())
                info.put("submission_date",subd)

                FirebaseFirestore.getInstance().collection("case").document(cid).set(info)
                    .addOnSuccessListener {
                        updateuser(cid)

                        db.collection("users").document((auth.currentUser?.email).toString()).
                        collection("user_cases").document(cid).set(info)

                        addpics(cid)
                        progress.dismiss()
                        Toast.makeText(this,"Your Cases' ID : "+cid,Toast.LENGTH_SHORT).show()
                        //finish()
                        Handler().postDelayed({
                            finish()
                            startActivity(Intent(this,Dashboard::class.java))
                        }, 500)

//
                    }
                    .addOnFailureListener {
                        Toast.makeText(this,"Error occured ",Toast.LENGTH_SHORT).show()
                    }


            }.addOnFailureListener {
                Toast.makeText(this,"Unable to process request. Try later.",Toast.LENGTH_SHORT).show()
            }
//        var newcnum=caval.toString()
//        Toast.makeText(this,newcnum,Toast.LENGTH_SHORT).show()
//        cid+=newcnum
//        Toast.makeText(this,"->>>"+cid,Toast.LENGTH_SHORT).show()
    }

    private fun chooseImage() {
        val intent=Intent()
        intent.type="image/*"
        intent.action=Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent,"Select Image"),PICK_IMAGE_REQUEST)
    }

    private fun updateuser(cid: String?)
    {
        var oc:Long?=0
        val tempppp="Open-Cases"
        db.collection("users").document((auth.currentUser?.email).toString()).update(tempppp,FieldValue.increment(1))
//        db.collection("users").document((auth.currentUser?.email).toString()).get()
//            .addOnSuccessListener {
//                    document ->
//                if(document!=null) {
//                    oc = document?.getLong(tempppp)
//                }

//                val ncid="case_id_$oc"

//                val caid=cid

//                db.collection("users").document((auth.currentUser?.email).toString())
//                    .update("Cases",FieldValue.arrayUnion(caid))
//                val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
//                val subd = sdf.format(Date())
//                db.collection("users").document((auth.currentUser?.email).toString())
//                    .update("cases.$ncid",caid)
//                db.collection("users").document((auth.currentUser?.email).toString())
//                    .update("cases.subm_dat_$oc",subd)

//            }
    }

    private fun addpics(cid: String)
    {
        val tt="attachments"
        //db.collection("case").document(cid).update()
        if(imgs==1)
        {
           // Log.d("aisehi",imageurl1)
            db.collection("case").document(cid).update(tt,FieldValue.arrayUnion(imageurl1))
        }
        else if(imgs==2)
        {
            db.collection("case").document(cid).update(tt,FieldValue.arrayUnion(imageurl1))
            db.collection("case").document(cid).update(tt,FieldValue.arrayUnion(imageurl2))
        }
        else if(imgs==3)
        {
            db.collection("case").document(cid).update(tt,FieldValue.arrayUnion(imageurl1))
            db.collection("case").document(cid).update(tt,FieldValue.arrayUnion(imageurl2))
            db.collection("case").document(cid).update(tt,FieldValue.arrayUnion(imageurl3))
        }
        else if(imgs==4)
        {
            db.collection("case").document(cid).update(tt,FieldValue.arrayUnion(imageurl1))
            db.collection("case").document(cid).update(tt,FieldValue.arrayUnion(imageurl2))
            db.collection("case").document(cid).update(tt,FieldValue.arrayUnion(imageurl3))
            db.collection("case").document(cid).update(tt,FieldValue.arrayUnion(imageurl4))
        }
        else if(imgs==5)
        {
            db.collection("case").document(cid).update(tt,FieldValue.arrayUnion(imageurl1))
            db.collection("case").document(cid).update(tt,FieldValue.arrayUnion(imageurl2))
            db.collection("case").document(cid).update(tt,FieldValue.arrayUnion(imageurl3))
            db.collection("case").document(cid).update(tt,FieldValue.arrayUnion(imageurl4))
            db.collection("case").document(cid).update(tt,FieldValue.arrayUnion(imageurl5))
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode==PICK_IMAGE_REQUEST && data!=null && resultCode== Activity.RESULT_OK && data.data!=null)
        {
            //selectedPhotoUri1=data.data
            if(imgs==0)
            {
                selectedPhotoUri1=data.data;
                if(selectedPhotoUri1!=null)
                {
                    img_btn1.text=""
                    img_btn11.visibility=View.VISIBLE
                    img_btn1.isClickable=false
                    img_btn1.visibility=View.INVISIBLE
                    img_btn2.isClickable=true
                    img_btn2.visibility=View.VISIBLE
//                    Toast.makeText(this,selectedPhotoUri1.toString(),Toast.LENGTH_SHORT).show()
                }
                this.imgs++

            }
            else if(imgs==1)
            {
                selectedPhotoUri2=data.data;
                if(selectedPhotoUri2!=null)
                {
                    img_btn2.text=""
                    img_btn22.visibility=View.VISIBLE
                    img_btn2.isClickable=false
                    img_btn2.visibility=View.INVISIBLE
                    img_btn3.isClickable=true
                    img_btn3.visibility=View.VISIBLE
//                    Toast.makeText(this,selectedPhotoUri2.toString(),Toast.LENGTH_SHORT).show()
                }
                this.imgs++;

            }
            else if(imgs==2)
            {
                selectedPhotoUri3=data.data
                if(selectedPhotoUri3!=null)
                {
                    img_btn3.text=""
                    img_btn33.visibility=View.VISIBLE
                    img_btn3.isClickable=false
                    img_btn3.visibility=View.INVISIBLE
                    img_btn4.isClickable=true
                    img_btn4.visibility=View.VISIBLE
                }
                this.imgs++;
            }
            else if(imgs==3)
            {
                selectedPhotoUri4=data.data
                if(selectedPhotoUri4!=null)
                {
                    img_btn4.text=""
                    img_btn44.visibility=View.VISIBLE
                    img_btn4.isClickable=false
                    img_btn4.visibility=View.INVISIBLE
                    img_btn5.isClickable=true
                    img_btn5.visibility=View.VISIBLE
                }
                this.imgs++;
            }
            else if(imgs==4)
            {
                selectedPhotoUri5=data.data
                if(selectedPhotoUri5!=null)
                {
                    img_btn5.text=""
                    img_btn55.visibility=View.VISIBLE
                    img_btn5.isClickable=false
                    img_btn5.visibility=View.INVISIBLE
                }
                this.imgs++;
            }
        }
    }

    private fun just_start()
    {
        img_btn2.isClickable = false
        img_btn2.visibility= View.INVISIBLE

        img_btn3.isClickable = false
        img_btn3.visibility= View.INVISIBLE

        img_btn4.isClickable = false
        img_btn4.visibility= View.INVISIBLE

        img_btn5.isClickable = false
        img_btn5.visibility= View.INVISIBLE


        img_btn11.visibility=View.INVISIBLE
        img_btn22.visibility=View.INVISIBLE
        img_btn33.visibility=View.INVISIBLE
        img_btn44.visibility=View.INVISIBLE
        img_btn55.visibility=View.INVISIBLE
    }

    private fun perform_upload()
    {
        if(imgs==1)
        {
            val filename=UUID.randomUUID().toString()
            val ref=FirebaseStorage.getInstance().getReference("/images/$filename")

            ref.putFile(selectedPhotoUri1!!).addOnSuccessListener {
                val result = it.metadata!!.reference!!.downloadUrl;
                result.addOnSuccessListener{
                    imageurl1=it.toString()
                    Log.d("image",imageurl1)
                }
                    .addOnFailureListener {
                        Toast.makeText(this,"Failed to upload image",Toast.LENGTH_SHORT).show()
                    }
            }

        }
        else if(imgs==2)
        {
            val filename=UUID.randomUUID().toString()
            val ref=FirebaseStorage.getInstance().getReference("/images/$filename")

            ref.putFile(selectedPhotoUri1!!).addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener {
                    imageurl1=it.toString()
                }
                    .addOnFailureListener {
                        Toast.makeText(this,"Failed to upload image",Toast.LENGTH_SHORT).show()
                    }
            }

            val fn2=UUID.randomUUID().toString()
            val ref2=FirebaseStorage.getInstance().getReference("/images/$fn2")
            ref2.putFile(selectedPhotoUri2!!).addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener {
                    imageurl2=it.toString()
                }.addOnFailureListener {
                    Toast.makeText(this,"Failed to upload image",Toast.LENGTH_SHORT).show()
                }
            }

        }
        else if(imgs==3)
        {
            val filename=UUID.randomUUID().toString()
            val ref=FirebaseStorage.getInstance().getReference("/images/$filename")

            ref.putFile(selectedPhotoUri1!!).addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener {
                    imageurl1=it.toString()
                }.addOnFailureListener {
                    Toast.makeText(this,"Failed o upload image",Toast.LENGTH_SHORT).show()
                }
            }

            val fn2=UUID.randomUUID().toString()
            val ref2=FirebaseStorage.getInstance().getReference("/images/$fn2")
            ref2.putFile(selectedPhotoUri2!!).addOnSuccessListener {
                ref2.downloadUrl.addOnSuccessListener {
                    imageurl2=it.toString()
                }.addOnFailureListener {
                    Toast.makeText(this,"Failed o upload image",Toast.LENGTH_SHORT).show()
                }
            }

            val fn3=UUID.randomUUID().toString()
            val ref3=FirebaseStorage.getInstance().getReference("/images/$fn3")

            ref3.putFile(selectedPhotoUri3!!).addOnSuccessListener {
                ref3.downloadUrl.addOnSuccessListener {
                    imageurl3=it.toString()
                }.addOnFailureListener {
                    Toast.makeText(this,"Failed o upload image",Toast.LENGTH_SHORT).show()
                }
            }

        }
        else if(imgs==4)
        {
            val filename=UUID.randomUUID().toString()
            val ref=FirebaseStorage.getInstance().getReference("/images/$filename")

            ref.putFile(selectedPhotoUri1!!).addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener {
                    imageurl1=it.toString()
                }
            }

            val fn2=UUID.randomUUID().toString()
            val ref2=FirebaseStorage.getInstance().getReference("/images/$fn2")

            ref2.putFile(selectedPhotoUri2!!).addOnSuccessListener {
                ref2.downloadUrl.addOnSuccessListener {
                    imageurl2=it.toString()
                }
            }


            val fn3=UUID.randomUUID().toString()
            val ref3=FirebaseStorage.getInstance().getReference("/images/$fn3")
            ref3.putFile(selectedPhotoUri3!!).addOnSuccessListener {
                ref3.downloadUrl.addOnSuccessListener {
                    imageurl3=it.toString()
                }
            }

            val fn4=UUID.randomUUID().toString()
            val ref4=FirebaseStorage.getInstance().getReference("/images/$fn4")
            ref4.putFile(selectedPhotoUri4!!).addOnSuccessListener {
                ref4.downloadUrl.addOnSuccessListener {
                    imageurl4=it.toString()
                }
            }

        }
        else if(imgs==5)
        {
            val filename=UUID.randomUUID().toString()
            val ref=FirebaseStorage.getInstance().getReference("/images/$filename")

            ref.putFile(selectedPhotoUri1!!).addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener {
                    imageurl1=it.toString()
                }
            }

            val fn2=UUID.randomUUID().toString()
            val ref2=FirebaseStorage.getInstance().getReference("/images/$fn2")
            ref2.putFile(selectedPhotoUri2!!).addOnSuccessListener {
                ref2.downloadUrl.addOnSuccessListener {
                    imageurl2=it.toString()
                }
            }

            val fn3=UUID.randomUUID().toString()
            val ref3=FirebaseStorage.getInstance().getReference("/images/$fn3")
            ref3.putFile(selectedPhotoUri3!!).addOnSuccessListener {
                ref3.downloadUrl.addOnSuccessListener {
                    imageurl3=it.toString()
                }
            }

            val fn4=UUID.randomUUID().toString()
            val ref4=FirebaseStorage.getInstance().getReference("/images/$fn4")
            ref4.putFile(selectedPhotoUri4!!).addOnSuccessListener {
                ref4.downloadUrl.addOnSuccessListener {
                    imageurl4=it.toString()
                }
            }

            val fn5=UUID.randomUUID().toString()
            val ref5=FirebaseStorage.getInstance().getReference("/images/$fn5")
            ref5.putFile(selectedPhotoUri5!!).addOnSuccessListener {
                ref5.downloadUrl.addOnSuccessListener {
                    imageurl5=it.toString()
                }
            }

        }
    }
}

