package com.example.myfirebase


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myfirebase.databinding.ActivityGoogleLoginBinding
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.auth.GoogleAuthProvider


class GoogleLoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityGoogleLoginBinding
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var authStateListener: AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityGoogleLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {

        firebaseAuth = com.google.firebase.auth.FirebaseAuth.getInstance();

        authStateListener = AuthStateListener { firebaseAuth ->
            // Get signedIn user
            val user = firebaseAuth.currentUser

            //if user is signed in, we call a helper method to save the user details to Firebase
            if (user != null) {
                // User is signed in
                // you could place other firebase code
                //logic to save the user details to Firebase
                Log.d("TAG", "onAuthStateChanged:signed_in:" + user.uid)
            } else {
                // User is signed out
                Log.d("TAG", "onAuthStateChanged:signed_out")
            }
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_client_id)) //you can also use R.string.default_web_client_id
            .requestEmail()
            .build()

       var googleApiClient = GoogleApiClient.Builder(this)
            .enableAutoManage(this, object : GoogleApiClient.OnConnectionFailedListener{
                override fun onConnectionFailed(p0: ConnectionResult) {

                }

            })
            .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
            .build()

        binding.signInButton.setOnClickListener {
            val intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient)
            startActivityForResult(intent, 100)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === 100 && resultCode == RESULT_OK) {
            val result = data?.let { Auth.GoogleSignInApi.getSignInResultFromIntent(it) }
            if (result != null) {
                handleSignInResult(result)
            }
        }
    }

    private fun handleSignInResult(result: GoogleSignInResult) {
        if (result.isSuccess) {
            val account = result.signInAccount
            var idToken = account!!.idToken
           var name = account.displayName
           var email = account.email
            // you can store user data to SharedPreference
            Log.e("TAG", "handleSignInResult: ===>"+idToken+"    "+name+"    "+email )
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            firebaseAuthWithGoogle(credential)
        } else {
            // Google Sign In failed, update UI appropriately
            Log.e("TAG", "Login Unsuccessful. $result")
            Toast.makeText(this, "Login Unsuccessful", Toast.LENGTH_SHORT).show()
        }
    }

    private fun firebaseAuthWithGoogle(credential: AuthCredential) {
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(
                this
            ) { task ->
                Log.d("TAG", "signInWithCredential:onComplete:" + task.isSuccessful)
                if (task.isSuccessful) {
                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                    gotoProfile()
                } else {
                    Log.w("TAG", "signInWithCredential" + task.exception?.message)
                    task.exception?.printStackTrace()
                    Toast.makeText(
                        this, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun gotoProfile() {
        val intent = Intent(this, ProfileActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    override fun onStart() {
        super.onStart()
        if (authStateListener != null) {
            FirebaseAuth.getInstance().signOut()
        }
        firebaseAuth.addAuthStateListener(authStateListener)
    }

    override fun onStop() {
        super.onStop()
        if (authStateListener != null) {
            firebaseAuth.removeAuthStateListener(authStateListener)
        }
    }
}