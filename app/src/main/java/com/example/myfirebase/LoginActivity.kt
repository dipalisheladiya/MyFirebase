package com.example.myfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.myfirebase.databinding.ActivityLoginBinding
import com.example.myfirebase.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {

        binding.btnLogin.setOnClickListener {

            val email = binding.edtEmail.text.toString()
            val password = binding.edtPassword.text.toString()

            auth = Firebase.auth

            auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this){
                if (it.isSuccessful)
                {
                    Toast.makeText(this, "Account Login Successfully", Toast.LENGTH_LONG).show()
                    var intent = Intent(this, DashboardActivity::class.java)
                    startActivity(intent)
                }else{
                    Toast.makeText(this, it.exception?.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
