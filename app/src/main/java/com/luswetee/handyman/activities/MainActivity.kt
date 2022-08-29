package com.luswetee.handyman.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import androidx.appcompat.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.luswetee.handyman.R
import com.luswetee.handyman.adapters.EmpAdapter
import com.luswetee.handyman.databinding.ActivityMainBinding
import com.luswetee.handyman.models.EmployeeModel

class MainActivity : AppCompatActivity() {


    //ViewBinding
    private lateinit var binding: ActivityMainBinding

    //ActionBar
    private lateinit var actionBar: ActionBar

    //FirebaseAuth
    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var employeeRecyclerView : RecyclerView
    private lateinit var tvloadingdata : TextView
    private lateinit var tempArrayList: ArrayList<EmployeeModel>
    private lateinit var empList: ArrayList<EmployeeModel>
    private lateinit var dbRef : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Configure Actionbar
        actionBar = supportActionBar!!
        actionBar.title = "Home"

        //init Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        employeeRecyclerView = findViewById(R.id.rvEmployees)
        employeeRecyclerView.layoutManager = LinearLayoutManager(this)
        employeeRecyclerView.setHasFixedSize(true)
        tvloadingdata = findViewById(R.id.tvLoadingData)

        empList = arrayListOf<EmployeeModel>()
        tempArrayList = arrayListOf<EmployeeModel>()

        getEmployeesData()


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.nav_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.logout -> {
                firebaseAuth.signOut()
                checkUser()
            }

            R.id.profile -> {
                Toast.makeText(this, "Profile button clicked", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this,UserProfileActivity::class.java))
            }
            R.id.search -> {

            }

        }
        return super.onOptionsItemSelected(item)
    }

    private fun getEmployeesData() {
        employeeRecyclerView.visibility = View.GONE
        tvloadingdata.visibility = View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("Employees")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                empList.clear()
                if (snapshot.exists()){
                    for (empSnap in snapshot.children){
                        val empData = empSnap.getValue(EmployeeModel::class.java)
                        empList.add(empData!!)
                    }
                    val mAdapter = EmpAdapter(empList)
                    employeeRecyclerView.adapter = mAdapter

                    employeeRecyclerView.visibility = View.VISIBLE
                    tvloadingdata.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        tempArrayList.addAll(empList)

    }

    private fun checkUser() {
        //check user is logged in or not
        val firebaseUser = firebaseAuth.currentUser
        if(firebaseUser!=null){
            //user is logged in
            val email = firebaseUser.email
        }else{
            //user is not logged in
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }
    }
}