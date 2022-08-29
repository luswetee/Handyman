package com.luswetee.handyman.activities

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.google.firebase.auth.FirebaseAuth
import com.luswetee.handyman.R
import com.luswetee.handyman.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    //ViewBinding
    private lateinit var binding: ActivityLoginBinding

    //ActionBar
    private lateinit var actionBar: ActionBar

    //ProgressDialog
    private lateinit var progressDialog: ProgressDialog

    //FirebaseAuth
    private lateinit var firebaseAuth: FirebaseAuth
    private var email = ""
    private var password = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Configure ActionBar
        actionBar = supportActionBar!!
        actionBar.title = "Login"

        //Configure progress dialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setMessage("Logging in...")
        progressDialog.setCanceledOnTouchOutside(false)

        //init firebase
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        //handle click signup
        binding.newUser.setOnClickListener {
            startActivity(Intent(this,SignupInterface::class.java))
        }
        //handle click,begin login
        binding.login.setOnClickListener {
            //before logging in validate data
            validateData()
        }

    }

    private fun validateData() {
        //get Data
        email = binding.email.text.toString().trim()
        password = binding.Password.text.toString().trim()

        //validate data
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            //invalid email entered
            binding.email.error = "Invalid Email Format"
        }else if (TextUtils.isEmpty(password)){
            //no password entered
            binding.Password.error = "Password required"
        }else{
            //data is valid
            firebaseLogin()
        }
    }

    private fun firebaseLogin() {
        //show progress
        progressDialog.show()
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                progressDialog.dismiss()
                //get user info
                val firebaseUser = firebaseAuth.currentUser
                val email = firebaseUser!!.email
                Toast.makeText(this,"You are logged in as $email", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this,MainActivity::class.java))
                finish()
            }
            .addOnFailureListener{ e->
                //login failed
                progressDialog.dismiss()
                Toast.makeText(this,"${e.message}", Toast.LENGTH_SHORT).show()

            }
    }

    private fun checkUser() {
        //if user is already logged in go to main activity
        //get current user
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser !=null){
            //user is logged in
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
    }
}