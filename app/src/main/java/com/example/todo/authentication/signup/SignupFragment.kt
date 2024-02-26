package com.example.todo.authentication.signup

import android.os.Bundle
import android.os.PatternMatcher
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.todo.R
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth


class signupFragment : Fragment() {
     private lateinit var auth: FirebaseAuth
    private lateinit var emailEt: TextInputLayout
    private lateinit var passwordEt: TextInputLayout
    private lateinit var signupBtn: Button
    private lateinit var signupGoogleBtn: Button
 //   private lateinit var onTapSentClient : SignInClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        FirebaseApp.initializeApp(requireContext())
          auth = Firebase.auth
        emailEt= view.findViewById(R.id.et_email)
        passwordEt= view.findViewById(R.id.et_password)
        signupBtn = view.findViewById(R.id.btn_signup)
        signupGoogleBtn = view.findViewById(R.id.btn_google_signup)
        signupGoogleBtn.setOnClickListener {

        }
        signupBtn.setOnClickListener {
            if(checkAllFields(emailEt, passwordEt)){
                auth.createUserWithEmailAndPassword(emailEt.editText?.text.toString(), passwordEt.editText?.text.toString())
                    .addOnCompleteListener(){ task ->
                        if (task.isSuccessful){
                            val user = auth.currentUser
                            Toast.makeText(requireContext(), "done", Toast.LENGTH_SHORT).show()
                        }
                        else{
                            Toast.makeText(
                                requireContext(),
                                "user already has an account.",
                                Toast.LENGTH_SHORT,
                            ).show()
                        }
                    }
            }
        }
    }
    private fun checkAllFields(et_email :TextInputLayout, et_password: TextInputLayout): Boolean{
         var email = et_email.editText?.text.toString()
        if(email==""){
            et_email.error = "This field is required"
            return false
        }else{
            et_email.error = null
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            et_email.error = "Check email fromat"
            return false
        }

        if(et_password.editText?.text.toString() ==""){
            et_password.error = "This field is required"
            return false
        }else{
            et_password.error= null
        }
        if(et_password.editText?.text?.length!!<8){
            et_password.error = "password should be 8 characters at least "
            return false
        }

        return true
    }
}