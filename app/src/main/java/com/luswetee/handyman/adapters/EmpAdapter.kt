package com.luswetee.handyman.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.luswetee.handyman.R
import com.luswetee.handyman.models.EmployeeModel
import java.text.FieldPosition

class EmpAdapter (private val empLIst : ArrayList<EmployeeModel>) : RecyclerView.Adapter<EmpAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup,viewType : Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.emp_list_item,parent,false)
        return ViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: ViewHolder,position:Int) {
        val currentEmp = empLIst[position]

        holder.tvfullName.text = currentEmp.fullname
        holder.tvEmail.text = currentEmp.email
        holder.tvPhoneNumber.text = currentEmp.phonenumber
        holder.specialty.text = currentEmp.specialty
        holder.location.text = currentEmp.location
    }

    override fun getItemCount(): Int {
        return empLIst.size
    }

    class ViewHolder (itemView: View): RecyclerView.ViewHolder(itemView) {
        val tvfullName : TextView = itemView.findViewById(R.id.tvfullname)
        val tvEmail : TextView = itemView.findViewById(R.id.tvemail)
        val tvPhoneNumber : TextView = itemView.findViewById(R.id.tvphone)
        val specialty : TextView = itemView.findViewById(R.id.tvspecialty)
        val location : TextView = itemView.findViewById(R.id.tvlocation)

    }

}