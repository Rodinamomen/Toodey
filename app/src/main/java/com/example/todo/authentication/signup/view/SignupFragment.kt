package com.example.todo.authentication.signup.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.todo.R
import com.example.todo.authentication.signup.viewModel.SignUpViewModel
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore


class signupFragment : Fragment() {
     private lateinit var auth: FirebaseAuth
    private lateinit var emailEt: TextInputLayout
    private lateinit var passwordEt: TextInputLayout
    private lateinit var signupBtn: Button
    private lateinit var signinTv: TextView
    private lateinit var fullNameEt: TextInputLayout
    private lateinit var reEnterPasswordEt: TextInputLayout


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup, container, false)
    }
    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    //    FirebaseApp.initializeApp(requireContext())
        var signUpViewModel= SignUpViewModel(requireContext())
        emailEt= view.findViewById(R.id.et_email)
        passwordEt= view.findViewById(R.id.et_password)
        signupBtn = view.findViewById(R.id.btn_signup)
        signinTv= view.findViewById(R.id.tv_sign_in)
        reEnterPasswordEt=view.findViewById(R.id.et_re_enter_password)
        fullNameEt= view.findViewById(R.id.et_full_name)
        signinTv.setOnClickListener {
            findNavController().navigate(R.id.action_signupFragment_to_signInFragment)
        }
        signUpViewModel.userAdded.observe(requireActivity()){ data ->
            if(data==true){
                    findNavController().navigate(R.id.action_signupFragment_to_homeFragment)
            } else {
                Toast.makeText(context,"user already exists",Toast.LENGTH_SHORT).show()
            }
        }
        signupBtn.setOnClickListener {
            var email = emailEt.editText?.text.toString()
            var password= passwordEt.editText?.text.toString()
            var fullName= fullNameEt.editText?.text.toString()
            if(checkAllFields(emailEt, passwordEt,fullNameEt,reEnterPasswordEt)){
                signUpViewModel.emailAuth(email,password,fullName)
            }
        }
    }
    private fun checkAllFields(emailEt :TextInputLayout, passwordEt: TextInputLayout, fullNameEt: TextInputLayout, reEnterPasswordEt: TextInputLayout): Boolean{
         var email = emailEt.editText?.text.toString()
        if(email==""){
            emailEt.error = "This field is required"
            return false
        }else{
            emailEt.error = null
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEt.error = "Check email fromat"
            return false
        }

        if(passwordEt.editText?.text.toString() ==""){
            emailEt.error = "This field is required"
            return false
        }else{
            emailEt.error= null
        }
        if(passwordEt.editText?.text?.length!!<8){
            passwordEt.error = "password should be 8 characters at least "
            return false
        }else{
            passwordEt.error= null
        }
        if(fullNameEt.editText?.text.toString()==""){
            fullNameEt.error = "This field is required"
            return false
        }else{
            fullNameEt.error = null
        }
        if(reEnterPasswordEt.editText?.text.toString()==""){
            reEnterPasswordEt.error = "This field is required"
            return false
        }else if(reEnterPasswordEt.editText?.text.toString()!=passwordEt.editText?.text.toString()){
            reEnterPasswordEt.error = "Password doesn't match"
        }else{
            reEnterPasswordEt.error=null
        }
        return true
    }
}