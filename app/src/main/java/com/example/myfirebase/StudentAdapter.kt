package com.example.myfirebase

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StudentAdapter(
    var studentData: ArrayList<StudentModelClass>,
    var onEditClick: ((String,String,String,String,String,String,String) -> Unit),var onDeleteClick:((String)->Unit)
) : RecyclerView.Adapter<StudentAdapter.MyViewHolder>() {
    class MyViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        var txtFirstname: TextView = ItemView.findViewById(R.id.txtFirstname)
        var txtMidname: TextView = ItemView.findViewById(R.id.txtMidname)
        var txtLastname: TextView = ItemView.findViewById(R.id.txtLastname)
        var txtEmailId: TextView = ItemView.findViewById(R.id.txtEmailId)
        var txtNumber: TextView = ItemView.findViewById(R.id.txtNumber)
        var txtFees: TextView = ItemView.findViewById(R.id.txtFees)
        var imgEdit: ImageView = ItemView.findViewById(R.id.imgEdit)
        var imgDelete: ImageView = ItemView.findViewById(R.id.imgDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentAdapter.MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.student_list_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: StudentAdapter.MyViewHolder, position: Int) {
        holder.txtFirstname.text = studentData[position].firstname
        holder.txtMidname.text = studentData[position].midname
        holder.txtLastname.text = studentData[position].lastname
        holder.txtEmailId.text = studentData[position].email
        holder.txtNumber.text = studentData[position].number
        holder.txtFees.text = studentData[position].fees
        holder.imgEdit.setOnClickListener {
            Log.e("TAG", "onBindViewHolder: ===>" + studentData[position].key)
            onEditClick.invoke(
                studentData[position].key,
                studentData[position].firstname,
                studentData[position].midname,
                studentData[position].lastname,
                studentData[position].email,
                studentData[position].number,
                studentData[position].fees
            )
        }
        holder.imgDelete.setOnClickListener {
            onDeleteClick.invoke(studentData[position].key)
        }

    }

    override fun getItemCount(): Int {
        return studentData.size
    }

    fun updateData(studentData: ArrayList<StudentModelClass>) {
        this.studentData = ArrayList()
        this.studentData.addAll(studentData)
        notifyDataSetChanged()
    }
}