package com.e.instagramstasonkotlin.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.e.instagramstasonkotlin.Models.User
import com.e.instagramstasonkotlin.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_edit_profile.*

class EditProfileActivity : AppCompatActivity() {
    private val TAG =  "EditProfileActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        Log.d(TAG, "onCreate")

        close_image.setOnClickListener {
            finish()
        }

        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        val database = FirebaseDatabase.getInstance().reference
        database.child("users").child(user!!.uid)
            .addListenerForSingleValueEvent(ValueEventListenerAdapter {
                val userD = it.getValue(User::class.java)
                name_input.setText(userD!!.name, TextView.BufferType.EDITABLE)
                username_input.setText(userD.username, TextView.BufferType.EDITABLE)
                website_input.setText(userD.website, TextView.BufferType.EDITABLE)
                bio_input.setText(userD.bio, TextView.BufferType.EDITABLE)
                email_input.setText(userD.email, TextView.BufferType.EDITABLE)
                phone_input.setText(userD.phone.toString(), TextView.BufferType.EDITABLE)

        })
    }
}
