package com.e.instagramstasonkotlin.activities

import android.os.Bundle
import android.util.Log
import com.alexbezhan.instagram.activities.BaseActivity
import com.e.instagramstasonkotlin.R

class ShareActivity : BaseActivity(2) {
    private val TAG = "ShareActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        Log.d(TAG, "onCreate")
        setupBottomNavigation()
    }
}
