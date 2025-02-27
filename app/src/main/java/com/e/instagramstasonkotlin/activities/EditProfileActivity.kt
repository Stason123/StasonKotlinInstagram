package com.e.instagramstasonkotlin.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.e.instagramstasonkotlin.Models.User
import com.e.instagramstasonkotlin.R
import com.e.instagramstasonkotlin.utils.CameraPictureTaker
import com.e.instagramstasonkotlin.utils.FirebaseHelper
import com.e.instagramstasonkotlin.utils.ValueEventListenerAdapter
import com.e.instagramstasonkotlin.views.PasswordDialog
import com.google.firebase.auth.EmailAuthProvider
import kotlinx.android.synthetic.main.activity_edit_profile.*

class EditProfileActivity : AppCompatActivity(), PasswordDialog.Listener {
    private val TAG =  "EditProfileActivity"
    private lateinit var mUser: User
    private lateinit var mPendingUser: User
    private lateinit var mFirebaseHelper: FirebaseHelper
    private lateinit var cameraPictureTaker: CameraPictureTaker


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        Log.d(TAG, "onCreate")

        cameraPictureTaker = CameraPictureTaker(this)

        close_image.setOnClickListener {
            finish()
        }

        save_image.setOnClickListener { updateProfile() }
        change_photo_text.setOnClickListener { cameraPictureTaker.takeCameraPicture() }

        mFirebaseHelper = FirebaseHelper(this)

        mFirebaseHelper.currentUserReference()
            .addListenerForSingleValueEvent(ValueEventListenerAdapter {
                mUser = it.asUser()!!
                name_input.setText(mUser.name)
                username_input.setText(mUser.username)
                website_input.setText(mUser.website)
                bio_input.setText(mUser.bio)
                email_input.setText(mUser.email)
                phone_input.setText(mUser.phone?.toString())
                profile_image.loadUserPhoto(mUser.photo)

        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == cameraPictureTaker.REQUEST_CODE && resultCode == RESULT_OK) {
            mFirebaseHelper.uploadUserPhoto(cameraPictureTaker.imageUri!!) {
                var ref = mFirebaseHelper.getUploadPath()
                ref.downloadUrl.addOnCompleteListener {
                    val photoUrl = it.result.toString()
                    mFirebaseHelper.updateUserPhoto(photoUrl) {
                        mUser = mUser.copy(photo = photoUrl)
                        profile_image.loadUserPhoto(mUser.photo)
                    }
                }

            }
        }
    }

    private fun updateProfile() {
        mPendingUser = readInputs()
        val error = validate(mPendingUser)
        if(error == null) {
            if (mPendingUser.email ==  mUser.email) {
                updateUser(mPendingUser)
            } else {
                PasswordDialog().show(supportFragmentManager, "password_dialog")
            }
        } else {
            showToast(error)
        }
    }

    private fun readInputs(): User {
        return User(
            name = name_input.text.toString(),
            username = username_input.text.toString(),
            email = email_input.text.toString(),
            bio = bio_input.text.toStringOrNull(),
            phone = phone_input.text.toString().toLongOrNull(),
            website = website_input.text.toStringOrNull()
        )
    }

    override fun onPasswordConfirm(password: String) {
        if (password.isNotEmpty()) {
            val credential = EmailAuthProvider.getCredential(mUser.email, password)
            mFirebaseHelper.reauthenticate(credential) {
                mFirebaseHelper.updateEmail(mPendingUser.email) {
                    updateUser(mPendingUser)
                }
            }
        } else {
            showToast("You should enter your password")
        }

    }

    private fun updateUser(user: User) {
        val updatesMap = mutableMapOf<String, Any?>()
        if (user.name != mUser.name) updatesMap["name"] = user.name
        if (user.username != mUser.username) updatesMap["username"] = user.username
        if (user.website != mUser.website) updatesMap["website"] = user.website
        if (user.bio != mUser.bio) updatesMap["bio"] = user.bio
        if (user.email != mUser.email) updatesMap["email"] = user.email
        if (user.phone != mUser.phone) updatesMap["phone"] = user.phone

        mFirebaseHelper.updateUser(updatesMap) {
            showToast("Profile saved")
            finish()
        }
    }

    private fun validate(user: User): String?  =
        when {
            user.name.isEmpty() -> "Please enter name"
            user.username.isEmpty() -> "Please enter username"
            user.email.isEmpty() -> "Please enter email"
            else -> null
        }

}
