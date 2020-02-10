package com.e.instagramstasonkotlin.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.alexbezhan.instagram.activities.BaseActivity
import com.bumptech.glide.Glide
import com.e.instagramstasonkotlin.R
import com.e.instagramstasonkotlin.utils.CameraPictureTaker
import kotlinx.android.synthetic.main.activity_share.*

class ShareActivity : BaseActivity(2) {
    private val TAG = "ShareActivity"
    private lateinit var mCameraHelper: CameraPictureTaker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share)
        Log.d(TAG, "onCreate")

        mCameraHelper = CameraPictureTaker(this)
        mCameraHelper.takeCameraPicture()

        back_image.setOnClickListener { finish() }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == mCameraHelper.REQUEST_CODE && resultCode == RESULT_OK) {
            Glide.with(this).load(mCameraHelper.imageUri).centerCrop().into(post_image)
        }
    }
}
