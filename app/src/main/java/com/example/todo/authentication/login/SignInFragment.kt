package com.example.todo.authentication.login

import android.os.Bundle
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
import com.example.todo.authentication.login.viewmodel.SignInViewmodel
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth


class SignInFragment : Fragment() {
    private lateinit var registerTv: TextView
    private lateinit var emailEt : TextInputLayout
    private lateinit var passwordEt: TextInputLayout
    private lateinit var signIn :Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var auth = Firebase.auth
        emailEt = view.findViewById(R.id.et_email)
        passwordEt= view.findViewById(R.id.et_password)
        signIn = view.findViewById(R.id.btn_signin)
        val signInViewmodel = SignInViewmodel()
        signInViewmodel.userSignedIn.observe(requireActivity()){ data ->
            if(data){
                findNavController().navigate(R.id.action_signInFragment_to_homeFragment)
            }else{
                Toast.makeText(requireContext(), "user doesnot exist", Toast.LENGTH_SHORT).show()
            }

        }
        signIn.setOnClickListener {
           if( checkAllFields(emailEt,passwordEt)){
               signInViewmodel.isUserSignedIn(emailEt.editText?.text.toString(),passwordEt.editText?.text.toString())
           }

        }
        registerTv= view.findViewById(R.id.tv_register)
        registerTv.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_signupFragment)
        }
    }
    private fun checkAllFields(emailEt :TextInputLayout, passwordEt: TextInputLayout): Boolean{
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
        return true
    }
}