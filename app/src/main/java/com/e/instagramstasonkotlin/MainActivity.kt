package com.e.instagramstasonkotlin

import android.os.Bundle
import android.util.Log
import com.alexbezhan.instagram.activities.BaseActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : BaseActivity(0) {

    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        Log.d(TAG, "onCreate")
        setupBottomNavigation()

        val auth = FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword("stasonnoris321@gmail.com", "123456789")
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d(TAG, "signIn: Successful")
                } else {
                    Log.e(TAG, "signIn: Failed", it.exception)
                }
            }

    } 
}
