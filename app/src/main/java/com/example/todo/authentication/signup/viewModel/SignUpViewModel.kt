package com.example.todo.authentication.signup.viewModel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.launch

class SignUpViewModel(val context:Context) : ViewModel(){
    private val _userAdded = MutableLiveData<Boolean>()
    val userAdded : LiveData<Boolean> = _userAdded
    private val _emailExists = MutableLiveData<Boolean>()
    val emailExist : LiveData<Boolean> = _emailExists
    val auth = FirebaseAuth.getInstance()
    val firebaseDatabase= Firebase.firestore
    fun emailAuth(email:String , password:String , fullName: String){
        viewModelScope.launch {
            auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(){ task ->
                    if (task.isSuccessful){
                        val user = auth.currentUser
                        hashMapOf(
                            "userEmail" to user?.email,
                            "userFullName" to fullName
                        )
                        firebaseDatabase.collection("Users")
                            .document(user!!.uid)
                            .set(hashMapOf(
                                "userEmail" to user?.email,
                                "userFullName" to fullName,
                                "userId" to user?.uid
                            ))
                           .addOnSuccessListener {
                                _userAdded.value= true
                            }
                            .addOnFailureListener {
                                _userAdded.value= false
                            }
                    }
                    else{
                        _userAdded.value= false
                    }
                }
        }
    }
    fun isEmailExist(email: String){
        firebaseDatabase.collection("Users")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    _emailExists.value = email== document.get("userEmail")

                }
            }
    }
}