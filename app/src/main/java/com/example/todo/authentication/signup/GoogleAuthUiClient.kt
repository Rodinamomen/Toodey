package com.example.todo.authentication.signup

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import com.example.todo.R
import com.example.todo.authentication.signup.model.SignupDataResult
import com.example.todo.authentication.signup.model.UserData
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest.GoogleIdTokenRequestOptions
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CancellationException

class GoogleAuthUiClient(private val context: Context, private val onTapClient : SignInClient) {
    private lateinit var auth: FirebaseAuth
    suspend fun signIn(): IntentSender?
    {
        val result= try{
                onTapClient.beginSignIn(
                        buildSignInRequest(context)
                ).await()
        }catch(e :Exception){
                e.printStackTrace()
                if(e is CancellationException) throw e
                    null
                }
        return result?.pendingIntent?.intentSender
        }
    suspend fun getFromIntent(intent: Intent): SignupDataResult {
        val credential= onTapClient.getSignInCredentialFromIntent(intent)
        val googleIdToken = credential.googleIdToken
        val googleAuthCredential= GoogleAuthProvider.getCredential(googleIdToken,null)
        return try {
            val user = auth.signInWithCredential(googleAuthCredential).await().user
            SignupDataResult(
                userData = user?.run {
                    UserData(
                        userId = uid,
                        userName = displayName,
                        profilePicture = photoUrl?.toString()
                    )
                }, erroMessage = null
            )
        }catch(e :Exception){
            e.printStackTrace()
            if(e is CancellationException) throw e
            SignupDataResult(
                userData = null,
                erroMessage = e.message
            )
        }
    }
    suspend fun signOut(){
        try {
            onTapClient.signOut().await()
            auth.signOut()
        }catch (e:Exception) {
            e.printStackTrace()
            if(e is CancellationException) throw e

        }
        fun getUserData() : UserData?= auth.currentUser?.run{
            return  UserData(
                userId = uid,
                userName = displayName,
                profilePicture = photoUrl?.toString()
            )
        }
    }

    }
    private fun buildSignInRequest(context: Context): BeginSignInRequest{
        return BeginSignInRequest.builder()

            .setGoogleIdTokenRequestOptions(
                GoogleIdTokenRequestOptions.Builder().setSupported(true).setFilterByAuthorizedAccounts(false).setServerClientId(context.getString(
                    R.string.web_default_id)).build()
            ).setAutoSelectEnabled(true)
            .build()
    }

