package com.example.todo.authentication.signup.model

data class SignupDataResult(
    val userData  :UserData?,
    val erroMessage: String?
)
data class UserData(
    val userId :String ,
    val userName : String?,
    val profilePicture:String?
)
