package com.e.instagramstasonkotlin.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.alexbezhan.instagram.activities.BaseActivity
import com.e.instagramstasonkotlin.Models.User
import com.e.instagramstasonkotlin.R
import com.e.instagramstasonkotlin.utils.FirebaseHelper
import com.e.instagramstasonkotlin.utils.ValueEventListenerAdapter
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : BaseActivity(4) {
    private val TAG = "ProfileActivity"
    private lateinit var mFirebaseHelper: FirebaseHelper
    private lateinit var mUser : User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        Log.d(TAG, "onCreate")
        setupBottomNavigation()
        edit_profile_btn.setOnClickListener{
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivity(intent)
        }

        mFirebaseHelper = FirebaseHelper(this)
        mFirebaseHelper.currentUserReference().addValueEventListener(ValueEventListenerAdapter{
            mUser = it.getValue(User::class.java)!!
            profile_image.loadUserPhoto(mUser.photo)
            username_text.text = mUser.username
        })
    }
}
