package com.e.instagramstasonkotlin.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.alexbezhan.instagram.activities.BaseActivity
import com.bumptech.glide.Glide
import com.e.instagramstasonkotlin.R
import com.e.instagramstasonkotlin.utils.CameraPictureTaker
import com.e.instagramstasonkotlin.utils.FirebaseHelper
import kotlinx.android.synthetic.main.activity_share.*

class ShareActivity : BaseActivity(2) {
    private val TAG = "ShareActivity"
    private lateinit var mCamera: CameraPictureTaker
    private lateinit var mFirebase: FirebaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share)
        Log.d(TAG, "onCreate")

        mFirebase = FirebaseHelper(this)
        mCamera = CameraPictureTaker(this)
        mCamera.takeCameraPicture()

        back_image.setOnClickListener { finish() }
        share_text.setOnClickListener { share() }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == mCamera.REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Glide.with(this).load(mCamera.imageUri).centerCrop().into(post_image)
            } else {
                finish()
            }
        }
    }

      private fun share() {
        val imageUri = mCamera.imageUri
        if (imageUri != null) {
            val uid = mFirebase.auth.currentUser!!.uid
            var ref = mFirebase.storage.child("users").child(uid).child("images")
                .child(imageUri.lastPathSegment.toString())
            ref.putFile(imageUri).addOnCompleteListener{
                    if (it.isSuccessful) {
                        ref.downloadUrl.addOnCompleteListener {
                            var res = it.result.toString()
                            Log.d(TAG, "ref $res")
                            mFirebase.database.child("images").child(uid).push()
                                .setValue(it.result.toString())
                                .addOnCompleteListener{
                                    if (it.isSuccessful) {
                                        startActivity(Intent(this,
                                            ProfileActivity::class.java))
                                        finish()
                                    } else {
                                        showToast(it.exception!!.message!!)
                                    }
                                }
                        }



                    } else {
                        showToast(it.exception!!.message!!)
                    }
                }
        }
    }
}
