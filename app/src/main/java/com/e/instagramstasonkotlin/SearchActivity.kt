package com.e.instagramstasonkotlin

import android.os.Bundle
import android.util.Log
import com.alexbezhan.instagram.activities.BaseActivity

class SearchActivity : BaseActivity(1) {
    private val TAG = "SearchActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        Log.d(TAG, "onCreate")
        setupBottomNavigation()
    }
}
