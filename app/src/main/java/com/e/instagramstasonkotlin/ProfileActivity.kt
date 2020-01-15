package com.e.instagramstasonkotlin

import android.os.Bundle
import android.util.Log
import com.alexbezhan.instagram.activities.BaseActivity

class ProfileActivity : BaseActivity(4) {
    private val TAG = "ProfileActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        Log.d(TAG, "onCreate")
        setupBottomNavigation()
    }
}
