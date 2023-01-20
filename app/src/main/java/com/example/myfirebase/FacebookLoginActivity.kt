package com.example.myfirebase

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import java.util.*


class FacebookLoginActivity : AppCompatActivity() {

    var callbackManager: CallbackManager? = null
    lateinit var loginButton: LoginButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_facebook_login)
        initView()
    }

    private fun initView() {



        val EMAIL = "email"

        loginButton = findViewById<View>(R.id.login_button) as LoginButton
        loginButton.setReadPermissions(Arrays.asList(EMAIL))
        // If you are using in a fragment, call loginButton.setFragment(this);

        // Callback registration
        // If you are using in a fragment, call loginButton.setFragment(this);

        // Callback registration
        loginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult?> {
            @JvmName("onSuccess1")
            fun onSuccess(loginResult: LoginResult) {
                // App code
            }

            override fun onCancel() {
                // App code
            }

            override fun onError(exception: FacebookException) {
                // App code
            }

            override fun onSuccess(result: LoginResult?) {

            }
        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager!!.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
}