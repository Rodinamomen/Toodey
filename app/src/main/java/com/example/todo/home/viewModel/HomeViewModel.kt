package com.example.todo.home.viewModel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo.home.model.Task
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.launch

class HomeViewModel(): ViewModel() {
   private val _newTaskAdded = MutableLiveData<Boolean>()
    val newTaskAdded :LiveData<Boolean> = _newTaskAdded
    private val _allTasks= MutableLiveData<List<Task>>()
    val allTask :LiveData<List<Task>> = _allTasks
    val db = Firebase.firestore
    val user= FirebaseAuth.getInstance().currentUser
    fun addTask(taskTitle:String , taskDescription:String){
        viewModelScope.launch {
            var task = hashMapOf(
                "taskTitle" to taskTitle,
                "taskDesc" to taskDescription
            )
            db.collection("Users")
                .document(user!!.uid)
                .collection("Tasks")
                .add(task)
                .addOnCompleteListener {
                    _newTaskAdded.value=true
                }.addOnFailureListener {
                    _newTaskAdded.value=false
                }
        }
    }
    fun getTasks(){
        viewModelScope.launch {
            var tasks : MutableList<Task> =mutableListOf()
           db.collection("Users")
                .document(user!!.uid)
                .collection("Tasks")
                .get()
               .addOnSuccessListener { result ->
                   for (document in result) {
                       val task = Task(
                           document.data["taskTitle"] as String,
                           document.data["taskDesc"] as String
                       )
                       tasks.add(task)
                   }
                   _allTasks.value=tasks
               }

        }
    }
}