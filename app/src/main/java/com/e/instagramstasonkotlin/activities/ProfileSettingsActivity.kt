package com.e.instagramstasonkotlin.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.e.instagramstasonkotlin.R
import com.e.instagramstasonkotlin.utils.FirebaseHelper
import kotlinx.android.synthetic.main.activity_profile_settings.*

class ProfileSettingsActivity : AppCompatActivity() {
    private lateinit var mFirebase: FirebaseHelper
    private val TAG =  "ProfileSettingsActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, " 0")
        setContentView(R.layout.activity_profile_settings)
        mFirebase = FirebaseHelper(this)
        Log.d(TAG, " 1")
        sign_out_text.setOnClickListener {
            mFirebase.auth.signOut()
        }
        Log.d(TAG, " 2")
        close_image.setOnClickListener {
            finish()
        }
    }
}
