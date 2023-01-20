package com.example.myfirebase

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.myfirebase.databinding.ActivityProfileBinding
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResultCallback
import com.google.firebase.auth.FirebaseAuth

//import jdk.internal.util.StaticProperty.userName


class ProfileActivity : AppCompatActivity() {

    lateinit var binding: ActivityProfileBinding
    lateinit var googleApiClient: GoogleApiClient
    lateinit var gso: GoogleSignInOptions
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {


        var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        googleApiClient = GoogleApiClient.Builder(this)
            .enableAutoManage(this, object : GoogleApiClient.OnConnectionFailedListener {
                override fun onConnectionFailed(p0: ConnectionResult) {

                }

            })
            .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
            .build()

        binding.logoutBtn.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback { status ->
                if (status.isSuccess()) {
                    gotoMainActivity()
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Session not close",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

    }

    override fun onStart() {
        super.onStart()
        val opr = Auth.GoogleSignInApi.silentSignIn(googleApiClient)
        if (opr.isDone) {
            val result = opr.get()
            handleSignInResult(result)
        } else {
            opr.setResultCallback(object : ResultCallback<GoogleSignInResult?> {
                override fun onResult(googleSignInResult: GoogleSignInResult) {
                    handleSignInResult(googleSignInResult)
                }
            })
        }
    }

    private fun handleSignInResult(result: GoogleSignInResult) {
        if (result.isSuccess) {
            val account = result.signInAccount
            binding.name.setText(account!!.displayName)
            binding.email.setText(account.email)
            binding.userId.setText(account.id)
            try {
                Glide.with(this).load(account?.photoUrl).into(binding.profileImage)
            } catch (e: NullPointerException) {
                Toast.makeText(applicationContext, "image not found", Toast.LENGTH_LONG).show()
            }
        } else {
            gotoMainActivity()
        }
    }

    private fun gotoMainActivity() {
        val intent = Intent(this, GoogleLoginActivity::class.java)
        startActivity(intent)
    }

    fun onConnectionFailed(connectionResult: ConnectionResult) {

    }
}