package com.luswetee.handyman.activities

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.luswetee.handyman.R
import com.luswetee.handyman.databinding.ActivitySignup2Binding
import com.luswetee.handyman.databinding.ActivitySignupBinding
import com.luswetee.handyman.models.EmployeeModel
import com.luswetee.handyman.models.EmployerModel

class SignupActivity2 : AppCompatActivity() {

    //ViewBinding
    private lateinit var binding: ActivitySignup2Binding

    //ActionBar
    private lateinit var actionBar: ActionBar

    //ProgressDialog
    private lateinit var progressDialog: ProgressDialog

    //FirebaseAuth
    private lateinit var firebaseAuth: FirebaseAuth
    private var email = ""
    private var password = ""

    //FirebaseDB
    private lateinit var dbref : DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignup2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        //Configure ActionBar
        actionBar = supportActionBar!!
        actionBar.title = "Sign Up"
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setDisplayShowHomeEnabled(true)

        //Configure progress dialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setMessage("Signing Up...")
        progressDialog.setCanceledOnTouchOutside(false)

        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance()

        //handle click, begin signup
        binding.signupR.setOnClickListener {
            val fullname = binding.fullnameR.text.toString()
            val email = binding.sEmailR.text.toString()
            val phonenumber = binding.phonenumberR.text.toString()

            dbref = FirebaseDatabase.getInstance().getReference("Employers")

            val empID = dbref.push().key!!

            val employer = EmployerModel(empID,fullname,email,phonenumber)
            dbref.child(empID).setValue(employer).addOnSuccessListener {
                Toast.makeText(this,"Successfully saved", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this,"Failed", Toast.LENGTH_SHORT).show()
            }
            //validate data
            validateData()

        }
    }

    private fun validateData() {
        //get data
        email = binding.sEmailR.text.toString().trim()
        password = binding.sPasswordR.text.toString().trim()

        //validate data
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            //invalid email entered
            binding.sEmailR.error = "Invalid Email Format"
        }else if (TextUtils.isEmpty(password)){
            //no password entered
            binding.sPasswordR.error = "Password required"
        }else if (password.length<6){
            //password is too short
            binding.sPasswordR.error = "Password must be at least 6 characters"
        }else{
            firebaseSignUp()
        }

    }

    private fun firebaseSignUp() {
        //show progress
        progressDialog.show()

        //create account
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener{
                //signup success
                progressDialog.dismiss()
                //get current user
                val firebaseUser = firebaseAuth.currentUser
                val email = firebaseUser!!.email
                Toast.makeText(this,"Account created with email $email",Toast.LENGTH_SHORT).show()

                //open main activity
                startActivity(Intent(this,MainActivity::class.java))
                finish()
            }
            .addOnFailureListener { e->
                //signup failed
                progressDialog.dismiss()
                Toast.makeText(this,"${e.message}",Toast.LENGTH_SHORT).show()
            }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}