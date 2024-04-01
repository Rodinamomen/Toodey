package com.example.todo.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.todo.R
import com.example.todo.home.model.Task

class HomeRecyclerViewAdapter(val data: List<Task>) : RecyclerView.Adapter<HomeRecyclerViewAdapter.myHolder>() {
    class myHolder(row : View) :ViewHolder(row){
        var taskTitle = row.findViewById<RadioButton>(R.id.rd_task)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myHolder {
        val row = LayoutInflater.from(parent.context).inflate(R.layout.task_item,parent,false)
        return  myHolder(row)
    }

    override fun getItemCount(): Int {
       return data.size
    }

    override fun onBindViewHolder(holder: myHolder, position: Int) {
        holder.taskTitle.text= data[position].taskTitle
    }
}