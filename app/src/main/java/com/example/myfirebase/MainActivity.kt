package com.example.myfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.myfirebase.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {

        binding.btnCreateAccount.setOnClickListener {
            var intent = Intent(this, CreateAccountActivity::class.java)
            startActivity(intent)
        }

        binding.btnlogin.setOnClickListener {
            var intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }
}