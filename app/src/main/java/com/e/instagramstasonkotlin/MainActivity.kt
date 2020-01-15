package com.e.instagramstasonkotlin

import android.os.Bundle
import android.util.Log
import com.alexbezhan.instagram.activities.BaseActivity

class MainActivity : BaseActivity(0) {

    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        Log.d(TAG, "onCreate")
        setupBottomNavigation()

    } 
}
