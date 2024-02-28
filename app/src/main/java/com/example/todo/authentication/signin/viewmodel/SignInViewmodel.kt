package com.example.todo.authentication.signin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch

class SignInViewmodel(): ViewModel() {
    private val _userSignedIn = MutableLiveData<Boolean>()
    val userSignedIn :LiveData<Boolean> = _userSignedIn
    val auth = Firebase.auth
    fun isUserSignedIn(email :String , password: String){
        viewModelScope.launch {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                _userSignedIn.value = it.isSuccessful
            }
        }
    }
}