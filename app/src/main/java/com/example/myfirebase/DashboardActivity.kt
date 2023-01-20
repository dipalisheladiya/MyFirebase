package com.example.myfirebase

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myfirebase.databinding.ActivityDashboardBinding
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*


class DashboardActivity : AppCompatActivity() {
    lateinit var binding: ActivityDashboardBinding
    lateinit var reference: DatabaseReference
    var studentData: ArrayList<StudentModelClass> = ArrayList()
    lateinit var studentAdapter: StudentAdapter
    lateinit var uri : Uri
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initVew()
    }

    private fun initVew() {

        binding.btnUpload.setOnClickListener {
            selectImage()
        }

        reference = FirebaseDatabase.getInstance().reference

        binding.btnDisplay.setOnClickListener {
            studentData.clear()
            reference.root.child("StudentTb").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
//                    studentData.clear()
                    for (child in snapshot.children) {

                        var studentdata = child.getValue(StudentModelClass::class.java)

                        Log.e("TAG", "onDataChange: ====>"+studentdata )

                        studentData.add(studentdata as StudentModelClass)

                    }

                    studentAdapter.updateData(studentData)

                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
        }

        studentAdapter = StudentAdapter(studentData,
            onEditClick = { key, firstname, midname, lastname, email, number, fees ->
                studentData.clear()

                var i = Intent(this, DataUpdateActivity::class.java)
                i.putExtra("key", key)
                i.putExtra("firstname", firstname)
                i.putExtra("midname", midname)
                i.putExtra("lastname", lastname)
                i.putExtra("email", email)
                i.putExtra("number", number)
                i.putExtra("fees", fees)
                startActivity(i)

            },
            onDeleteClick = {
                studentData.clear()
                val databaseReference =
                    FirebaseDatabase.getInstance().reference.child("StudentTb").child(it)
                databaseReference.removeValue()
            })
        val manager =
            LinearLayoutManager(this@DashboardActivity, LinearLayoutManager.VERTICAL, false)
        binding.rcvStudentList.layoutManager = manager
        binding.rcvStudentList.adapter = studentAdapter

        binding.btnInsert.setOnClickListener {

            studentData.clear()
            var firstname = binding.edtFirstname.text.toString()
            var midname = binding.edtMidname.text.toString()
            var lastname = binding.edtLastname.text.toString()
            var email = binding.edtEmail.text.toString()
            var number = binding.edtNumber.text.toString()
            var fees = binding.edtFees.text.toString()


            var key = reference.root.child("StudentTb").push().key ?: ""
            var studentData = StudentModelClass(firstname, midname, lastname, email, number, fees, key)
            reference.root.child("StudentTb").child(key).setValue(studentData)
                .addOnCompleteListener(this) {
                    if (it.isSuccessful) {
                        Toast.makeText(this, "Data insert Success", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this, it.exception?.message, Toast.LENGTH_LONG).show()
                        Log.e("TAG", "initVew: ===>" + it.exception?.message)
                    }
                }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK)
        {
            if (data != null)
            {
                uri = data.data!!
                UploadImage()
            }
        }
    }

    private fun UploadImage() {
        if (uri != null){
            var progressDialog = ProgressDialog(this)
            progressDialog.setTitle("Uploading...")
            progressDialog.show();

            val ref: StorageReference = FirebaseStorage.getInstance().getReference().child("images/" + UUID.randomUUID().toString())
            ref.putFile(uri)
                .addOnSuccessListener {
                    progressDialog.dismiss()
                    Toast.makeText(this, "Uploaded", Toast.LENGTH_LONG).show()
                }
                .addOnFailureListener {
                    progressDialog.dismiss()
                    Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                }
                .removeOnProgressListener {taskSnapshot->
                    val progress: Double = (100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount)
                    progressDialog.setMessage("Uploaded " + progress.toInt() + "%"
                    )
                }
        }
    }

    private fun selectImage() {
        var intent = Intent()
        intent.type = "image/*"
        intent.action=Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),100)
    }
}