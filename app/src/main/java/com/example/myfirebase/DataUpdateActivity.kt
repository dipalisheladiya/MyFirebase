package com.example.myfirebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.myfirebase.databinding.ActivityDashboardBinding
import com.example.myfirebase.databinding.ActivityDataUpdateBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class DataUpdateActivity : AppCompatActivity() {
    lateinit var binding: ActivityDataUpdateBinding
    lateinit var reference: DatabaseReference
    lateinit var key : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityDataUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        reference = FirebaseDatabase.getInstance().reference

        if (intent != null) {

            key = intent.getStringExtra("key").toString()
            var firstname = intent.getStringExtra("firstname").toString()
            var midname = intent.getStringExtra("midname").toString()
            var lastname = intent.getStringExtra("lastname").toString()
            var email = intent.getStringExtra("email").toString()
            var number = intent.getStringExtra("number").toString()
            var fees = intent.getStringExtra("fees").toString()

            Log.e("TAG", "initView: ===>"+key )
            binding.edtfirstname.setText(firstname)
            binding.edtmidname.setText(midname)
            binding.edtlastname.setText(lastname)
            binding.edtemail.setText(email)
            binding.edtnumber.setText(number)
            binding.edtfees.setText(fees)

            binding.btnUpdate.setOnClickListener {

                var firstname = binding.edtfirstname.text.toString()
                var midname = binding.edtmidname.text.toString()
                var lastname = binding.edtlastname.text.toString()
                var email = binding.edtemail.text.toString()
                var number = binding.edtnumber.text.toString()
                var fees = binding.edtfees.text.toString()


                var studentData = StudentModelClass(firstname, midname, lastname, email, number, fees,key)

                reference.root.child("StudentTb").child(key).setValue(studentData).addOnCompleteListener(this){
                    if (it.isSuccessful) {
                        Toast.makeText(this, "Success", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this, it.exception?.message, Toast.LENGTH_LONG).show()
                        Log.e("TAG", "initVew: ===>" + it.exception?.message)
                    }
                    onBackPressed()
                }
            }
        }
    }
}