package com.e.instagramstasonkotlin.activities

import android.content.Context
import android.content.Intent
import android.net.sip.SipSession
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.e.instagramstasonkotlin.Models.User
import com.e.instagramstasonkotlin.R
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_register_email.*
import kotlinx.android.synthetic.main.fragment_register_namepass.*
private val TAG = "RegisterActivity"
class RegisterActivity : AppCompatActivity(), EmailFragment.Listener, NamePassFragment.Listener {
    private var mEmail: String? = null
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDatabase: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().reference

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().add(R.id.frame_layout, EmailFragment()).commit()
        }

    }

    override fun onNext(email: String) {
        if (email.isNotEmpty()) {
            mEmail = email
            supportFragmentManager.beginTransaction().replace(R.id.frame_layout, NamePassFragment())
                .addToBackStack(null).commit()
        } else {
            showToast("Please enter email")
        }
    }

    override fun onRegister(fullname: String, password: String) {
        if (fullname.isNotEmpty() && password.isNotEmpty()) {
            val email = mEmail
            if (email != null) {
                mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener{
                        if (it.isSuccessful) {
                            val user = mkUser(fullname, email)
                            val reference = mDatabase.child("users").child(it.result!!.user!!.uid)
                            reference.setValue(user).addOnCompleteListener{

                                if (it.isSuccessful) {
                                    startHomeActivity()
                                } else {
                                    unknownRegisterError(it)
                                }
                            }
                        } else {
                            unknownRegisterError(it)
                        }
                    }
            } else {
                Log.e(TAG, "onRegister: email is null")
                showToast("Please enter email")
                supportFragmentManager.popBackStack()
            }
        } else {
            showToast("Please enter full name and password")
        }
    }
    private fun unknownRegisterError(it: Task<*>) {
        Log.e(TAG, "failed to create user profile", it.exception)
        showToast("Something wrong happened. Please try again later")
    }

    private fun startHomeActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun mkUser(fullName: String, email: String): User {
        val username = mkUsername(fullName)
        return User(name = fullName, username = username, email = email)
    }

    private fun mkUsername(fullName: String) =
        fullName.toLowerCase().replace(" ", ".")
}

// Email
class EmailFragment : Fragment() {
    private lateinit var mListener: Listener
    interface Listener{
        fun onNext(email: String)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        next_btn.setOnClickListener{
            val email = email_input.text.toString()
            mListener.onNext(email)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = context as Listener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register_email, container, false)
    }
}


// Name,Password
class NamePassFragment: Fragment() {
    private lateinit var mListener: Listener
    interface Listener {
        fun onRegister(fullname: String, password: String)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        register_btn.setOnClickListener{
            val fullname = full_name_input.text.toString()
            val password = password_input.text.toString()
            mListener.onRegister(fullname, password)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = context as Listener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register_namepass, container, false)
    }
}