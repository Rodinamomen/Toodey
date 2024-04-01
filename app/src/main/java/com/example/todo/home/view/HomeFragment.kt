package com.example.todo.home.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.R
import com.example.todo.home.adapter.HomeRecyclerViewAdapter
import com.example.todo.home.viewModel.HomeViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore


class HomeFragment : Fragment() {
    private lateinit var addTaskButton: FloatingActionButton
    private lateinit var tasksRecyclerView: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        addTaskButton= view.findViewById(R.id.btn_add_task)
        tasksRecyclerView=view.findViewById(R.id.rv_tasks)
        val HomeViewModel = HomeViewModel()
        HomeViewModel.getTasks()
        HomeViewModel.allTask.observe(requireActivity()){ it ->
            Log.d("Tasks", "onViewCreated: ${it}")
            tasksRecyclerView.adapter= HomeRecyclerViewAdapter(it)
            tasksRecyclerView.layoutManager = LinearLayoutManager(requireContext(),  RecyclerView.VERTICAL, false)
        }
        addTaskButton.setOnClickListener {
            val dialog = BottomSheetDialog(requireContext())
            val view = layoutInflater.inflate(R.layout.new_task, null)
            val taskTitleEt: EditText = view.findViewById(R.id.et_new_task_title)
            val taskDescEt:EditText= view.findViewById(R.id.et_new_task_description)
            val saveBtn = view.findViewById<ImageView>(R.id.iv_save_new_task)
            saveBtn.setOnClickListener {
                HomeViewModel.addTask(taskTitleEt.text.toString(),taskDescEt.text.toString())
                HomeViewModel.getTasks()
                dialog.dismiss()
            }
            dialog.setCancelable(true)
            dialog.setContentView(view)
            dialog.show()
        }
        super.onViewCreated(view, savedInstanceState)
    }
}