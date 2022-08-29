package com.luswetee.handyman.activities

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.luswetee.handyman.R
import com.luswetee.handyman.databinding.ActivitySignupBinding
import com.luswetee.handyman.models.EmployeeModel

class SignupActivity : AppCompatActivity() {

    //ViewBinding
    private lateinit var binding: ActivitySignupBinding

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
        binding = ActivitySignupBinding.inflate(layoutInflater)
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
        binding.signup.setOnClickListener {
            val fullname= binding.fullname.text.toString()
            val email = binding.sEmail.text.toString()
            val phonenumber= binding.phonenumber.text.toString()
            val specialty= binding.specialty.text.toString()
            val location = binding.location.text.toString()

            dbref = FirebaseDatabase.getInstance().getReference("Employees")

            val empID = dbref.push().key!!

            val employee = EmployeeModel(empID,fullname,email,phonenumber,specialty,location)
            dbref.child(empID).setValue(employee).addOnSuccessListener {
                Toast.makeText(this,"Successfully saved",Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this,"Failed",Toast.LENGTH_SHORT).show()
            }
            //validate data
            validateData()
        }

    }

    private fun validateData() {
        //get data
        email = binding.sEmail.text.toString().trim()
        password = binding.sPassword.text.toString().trim()

        //validate data
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            //invalid email entered
            binding.sEmail.error = "Invalid Email Format"
        }else if (TextUtils.isEmpty(password)){
            //no password entered
            binding.sPassword.error = "Password required"
        }else if (password.length<6){
            //password is too short
            binding.sPassword.error = "Password must be at least 6 characters"
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