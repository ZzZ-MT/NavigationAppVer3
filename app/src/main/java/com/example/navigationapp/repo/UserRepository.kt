package com.example.navigationapp.repo

import com.example.navigationapp.model.User
import com.google.firebase.auth.FirebaseUser
import com.example.navigationapp.utils.Result
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

interface UserRepository {
    suspend fun registerUserFromAuthWithEmailAndPassword(email: String, password: String): Result<FirebaseUser?>
    suspend fun createUserInFirestore(user: User): Result<Void?>
    suspend fun loginUserFromAuthWithEmailAndPassword(email:String, password: String): Result<FirebaseUser?>
    suspend fun getCurrentUser(): FirebaseUser?
    suspend fun logoutUser()
    suspend fun sendPasswordResetEmail(email:String): Result<Void?>
    suspend fun readUserInformation(uid:String):Result<DocumentSnapshot?>
}