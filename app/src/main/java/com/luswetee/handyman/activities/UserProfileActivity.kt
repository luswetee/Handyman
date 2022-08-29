package com.luswetee.handyman.activities

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.luswetee.handyman.R
import com.luswetee.handyman.databinding.ActivityUserProfileBinding
import com.luswetee.handyman.models.EmployeeModel

class UserProfileActivity : AppCompatActivity() {

    private lateinit var binding:ActivityUserProfileBinding
    private lateinit var auth:FirebaseAuth
    private lateinit var databasereference: DatabaseReference
    private lateinit var user:EmployeeModel
    private lateinit var uid:String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid.toString()

        databasereference = FirebaseDatabase.getInstance().getReference("Employee")


    }

}