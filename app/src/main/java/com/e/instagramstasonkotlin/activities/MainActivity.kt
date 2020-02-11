package com.e.instagramstasonkotlin.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.alexbezhan.instagram.activities.BaseActivity
import com.e.instagramstasonkotlin.R
import com.e.instagramstasonkotlin.utils.FirebaseHelper
import com.e.instagramstasonkotlin.utils.ValueEventListenerAdapter
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_home.*

class MainActivity : BaseActivity(0) {

    private val TAG = "MainActivity"
    private lateinit var mFirebase: FirebaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        Log.d(TAG, "onCreate")
        setupBottomNavigation()
        mFirebase = FirebaseHelper(this)
        sign_out_text.setOnClickListener{
            mFirebase.auth.signOut()
        }

        mFirebase.auth.addAuthStateListener {
            if (it.currentUser == null) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }

        mFirebase.database.child("feed-post").child(mFirebase.auth.currentUser!!.uid)
            .addValueEventListener(ValueEventListenerAdapter{
                val posts = it.children.map {it.getValue(FeedPost::class.java)!!}
//                Log.d(TAG, "feedPost ${posts.joinToString("\n", "\n")}")
//                Log.d(TAG, "feedPost ${posts.first().timestampDate()}")
            })

    }

    override fun onStart(){
        super.onStart()
        if (mFirebase.auth.currentUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
