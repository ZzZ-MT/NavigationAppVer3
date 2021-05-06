package com.example.navigationapp.repo

import com.example.navigationapp.model.User
import com.google.firebase.auth.FirebaseUser
import com.example.navigationapp.utils.Result

interface UserRepository {
    suspend fun registerUserFromAuthWithEmailAndPassword(email: String, password: String): Result<FirebaseUser?>
    suspend fun createUserInFirestore(user: User): Result<Void?>
    suspend fun loginUserFromAuthWithEmailAndPassword(email:String, password: String): Result<FirebaseUser?>
    suspend fun checkUserLoggedIn(): FirebaseUser?
    suspend fun logoutUser()
    suspend fun sendPasswordResetEmail(email:String): Result<Void?>
}