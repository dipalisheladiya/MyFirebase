package com.example.myfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.myfirebase.databinding.ActivityCreateAccountBinding
import com.example.myfirebase.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class CreateAccountActivity : AppCompatActivity() {
    lateinit var binding: ActivityCreateAccountBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityCreateAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {

        binding.btnRegister.setOnClickListener {

            val email = binding.edtEma.text.toString()
            val password = binding.edtPass.text.toString()

            auth = Firebase.auth

            auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this){
                if (it.isSuccessful)
                {
                    Toast.makeText(this, "Create Account Successfully", Toast.LENGTH_LONG).show()
                    var intent = Intent(this, DashboardActivity::class.java)
                    startActivity(intent)
                }else{
                    Toast.makeText(this, it.exception?.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}