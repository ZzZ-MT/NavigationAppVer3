package com.example.navigationapp.repo.implementation

import android.util.Log
import com.example.navigationapp.model.User
import com.example.navigationapp.repo.UserRepository
import com.example.navigationapp.utils.extension.await
import com.example.navigationapp.utils.Result
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class UserRepositoryImpl : UserRepository
{
    private val TAG = "UserRepositoryImpl"
    //CONST
    private val USER_COLLECTION_NAME = "users"

    private val firestoreInstance = FirebaseFirestore.getInstance()
    private val userCollection = firestoreInstance.collection(USER_COLLECTION_NAME)
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override suspend fun registerUserFromAuthWithEmailAndPassword(email: String, password: String): Result<FirebaseUser?> {
        try {
            return when(val resultDocumentSnapshot = firebaseAuth.createUserWithEmailAndPassword(email, password).await()) {
                is Result.Success -> {
                    Log.i(TAG, "Result.Success")
                    val firebaseUser = resultDocumentSnapshot.data.user
                    Result.Success(firebaseUser)
                }
                is Result.Error -> {
                    Log.e(TAG, "${resultDocumentSnapshot.exception}")
                    Result.Error(resultDocumentSnapshot.exception)
                }
                is Result.Canceled ->  {
                    Log.e(TAG, "${resultDocumentSnapshot.exception}")
                    Result.Canceled(resultDocumentSnapshot.exception)
                }
            }
        }
        catch (exception: Exception) {
            return Result.Error(exception)
        }
    }

    override suspend fun createUserInFirestore(user: User): Result<Void?> {
        return try {
            userCollection.document(user.id).set(user).await()
        }
        catch (exception: Exception) {
            Result.Error(exception)
        }
    }


    override suspend fun loginUserFromAuthWithEmailAndPassword(email:String, password:String) :Result<FirebaseUser?> {
        try {
            return when(val resultDocumentSnapshot = firebaseAuth.signInWithEmailAndPassword(email,password).await()) {
                is Result.Success -> {
                    Log.i(TAG, "Result.Success")
                    val firebaseUser = resultDocumentSnapshot.data.user
                    Result.Success(firebaseUser)
                }
                is Result.Error -> {
                    Log.e(TAG, "${resultDocumentSnapshot.exception}")
                    Result.Error(resultDocumentSnapshot.exception)
                }
                is Result.Canceled ->  {
                    Log.e(TAG, "${resultDocumentSnapshot.exception}")
                    Result.Canceled(resultDocumentSnapshot.exception)
                }
            }
        } catch (exception:Exception) {
            return  Result.Error(exception)
        }
    }

    override suspend fun getCurrentUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

    override suspend fun logoutUser() {
        firebaseAuth.signOut()
    }

    override suspend fun sendPasswordResetEmail(email: String): Result<Void?> {
        return try {
            firebaseAuth.sendPasswordResetEmail(email).await()
        } catch (e:Exception) {
            Result.Error(e)
        }
    }

    override suspend fun readUserInformation(uid: String): Result<FirebaseFirestore> {
        try{
            return when (val resultDocumentSnapshot = userCollection.document(uid).get().await()) {
                is Result.Success -> {
                    Log.i(TAG, "Result.Success")

                    Result.Success()
                }
                is Result.Error -> {
                    Log.e(TAG, "${resultDocumentSnapshot.exception}")
                    Result.Error(resultDocumentSnapshot.exception)
                }
                is Result.Canceled ->  {
                    Log.e(TAG, "${resultDocumentSnapshot.exception}")
                    Result.Canceled(resultDocumentSnapshot.exception)
                }
            }
        } catch (exception:Exception) {
            return Result.Error(exception)
        }
    }
}