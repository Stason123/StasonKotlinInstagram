package com.e.instagramstasonkotlin.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.alexbezhan.instagram.activities.BaseActivity
import com.bumptech.glide.Glide
import com.e.instagramstasonkotlin.Models.User
import com.e.instagramstasonkotlin.R
import com.e.instagramstasonkotlin.utils.CameraPictureTaker
import com.e.instagramstasonkotlin.utils.FirebaseHelper
import com.e.instagramstasonkotlin.utils.ValueEventListenerAdapter
import com.google.firebase.database.ServerValue
import kotlinx.android.synthetic.main.activity_share.*
import org.w3c.dom.Comment
import java.util.*

class ShareActivity : BaseActivity(2) {
    private val TAG = "ShareActivity"
    private lateinit var mCamera: CameraPictureTaker
    private lateinit var mFirebase: FirebaseHelper
    private lateinit var mUser: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share)
        Log.d(TAG, "onCreate")

        mFirebase = FirebaseHelper(this)
        mCamera = CameraPictureTaker(this)
        mCamera.takeCameraPicture()

        back_image.setOnClickListener { finish() }
        share_text.setOnClickListener { share() }

        mFirebase.currentUserReference().addValueEventListener(ValueEventListenerAdapter{
            mUser = it.asUser()!!
        })

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
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
                            val imageDownloadUrl = it.result.toString()
                            Log.d(TAG, "ref $imageDownloadUrl")
                            mFirebase.database.child("images").child(uid).push()
                                .setValue(imageDownloadUrl)
                                .addOnCompleteListener {
                                    if (it.isSuccessful) {
                                        mFirebase.database.child("feed-post").child(uid)
                                            .push()
                                            .setValue(mkFeedPost(uid, imageDownloadUrl))
                                            .addOnCompleteListener{
                                                if (it.isSuccessful) {
                                                    startActivity(Intent(this,
                                                        ProfileActivity::class.java))
                                                    finish()
                                                }
                                            }
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
    private fun mkFeedPost(uid: String, imageDownloadUrl: String): FeedPost{
        Log.d(TAG, "$uid and $imageDownloadUrl")
        return FeedPost(
            uid = uid,
            username = mUser.username,
            image = imageDownloadUrl,
            caption = caption_input.text.toString(),
            photo = mUser.photo
        )
    }
}



data class FeedPost(val uid: String = "", val username : String = "",
                    val image : String = "", val likeCount: Int  = 0, val comentCount: Int = 0,
                    val caption: String = "", val comments: List<Comment> = emptyList(),
                    val timestamp: Any = ServerValue.TIMESTAMP, val photo: String? = null) {
    fun timestampDate(): Date = Date(timestamp as Long)
}

data class Comment(val uid: String, val username: String, val text: String)
