package com.luswetee.handyman.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.luswetee.handyman.R

class SignupInterface : AppCompatActivity() {

    private lateinit var btnemployer : Button
    private lateinit var btnemployee : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup_interface)

        btnemployer = findViewById(R.id.employer)
        btnemployee = findViewById(R.id.employee)

        btnemployee.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }

        btnemployer.setOnClickListener {
            val intent = Intent(this, SignupActivity2::class.java)
            startActivity(intent)
        }

    }
}