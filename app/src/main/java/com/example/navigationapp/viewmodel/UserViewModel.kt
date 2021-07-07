package com.example.navigationapp.viewmodel

import android.app.Activity
import android.content.ContentValues.TAG
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.navigationapp.R
import com.example.navigationapp.model.User
import com.example.navigationapp.repo.UserPreferences
import com.example.navigationapp.repo.UserRepository
import com.example.navigationapp.repo.implementation.UserRepositoryImpl
import com.example.navigationapp.utils.Event
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import com.example.navigationapp.utils.Result
import com.example.navigationapp.view.LoginFragment
import com.example.navigationapp.view.RegisterFragment
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.Dispatchers

class   UserViewModel: ViewModel() {
    private val TAG ="FirebaseViewModel"

    private val _toast = MutableLiveData<String?>()
    val toast: LiveData<String?>
        get() = _toast

    private val _spinner = MutableLiveData<Boolean>(false)
    val spinner: LiveData<Boolean>
        get() = _spinner

    private val _currentUserMLD = MutableLiveData<User>(User())
    val currentUserLD: LiveData<User>
        get() = _currentUserMLD

    private val _userInformation = MutableLiveData<User>(User())
    val userInformation:LiveData<User>
        get() = _userInformation

    private val _navigateScreen = MutableLiveData<Event<Int>>()
    val navigateScreen: LiveData<Event<Int>> = _navigateScreen

    private val userRepository: UserRepository = UserRepositoryImpl()

    //Handle navigate view by button click
    private fun onClickButton(view: Int) {
        _navigateScreen.value = Event(view)
    }

    //Check login state
    fun getCurrentUser(): FirebaseUser? {
        var firebaseUser: FirebaseUser? = null
        viewModelScope.launch(Dispatchers.Main) {
            firebaseUser = userRepository.getCurrentUser()
            Log.i(TAG,"${firebaseUser?.uid}")
            if(firebaseUser != null) {
                onClickButton(R.id.tabFragment)
            } else {
                ///action_splashFragment_to_tabFragment cannot be found from the current destination NavGraph
//                onClickButton(R.id.action_splashFragment_to_loginFragment)
                onClickButton(R.id.loginFragment)

            }
        }
        return firebaseUser
    }

    fun getCurrentUserInformation() :FirebaseUser? {
        var currentUser:FirebaseUser? = null
        viewModelScope.launch {
            currentUser = userRepository.getCurrentUser()
            Log.i(TAG,"${currentUser?.uid}")
        }
        return currentUser
    }

    //Logout User
    fun logOutUser() {
        viewModelScope.launch(Dispatchers.Main) {
            userRepository.logoutUser()
        }
    }

    //Send confirm email for reset password
    fun sendPasswordResetEmail(email: String, fragment: Fragment) {
        viewModelScope.launch(Dispatchers.IO) {
            when(val result = userRepository.sendPasswordResetEmail(email))
            {
                is Result.Success -> {
                    _toast.value = "Check email to reset your password!"
                }
                is Result.Error -> {
                    _toast.value = result.exception.message
                }
                is Result.Canceled -> {
                    _toast.value = fragment.getString(R.string.request_canceled)
                }
            }
        }
    }

    //register by email
    fun registerUserFromAuthWithEmailAndPassword(name: String, email: String, password: String,fragment: Fragment) {
        launchDataLoad {
            viewModelScope.launch(Dispatchers.Main) {
                when(val result = userRepository.registerUserFromAuthWithEmailAndPassword(email, password)) {
                    is Result.Success -> {
                        Log.e(TAG, "Result.Success")
                        var user = userRepository.getCurrentUser()
                        val profileUpdates = userProfileChangeRequest {
                            displayName = name
                        }
                        user!!.updateProfile(profileUpdates)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Log.d(TAG, "User profile updated.")
                                }
                            }
                        result.data?.let {firebaseUser ->
                            createUserInFirestore(createUserObject(firebaseUser, name,email),fragment)
                            onClickButton(R.id.action_registerFragment_to_loginFragment)
                        }
                    }
                    is Result.Error -> {
                        Log.e(TAG, "${result.exception.message}")
                        _toast.value = result.exception.message
                    }
                    is Result.Canceled -> {
                        Log.e(TAG, "${result.exception!!.message}")
                        _toast.value = fragment.getString(R.string.request_canceled)
                    }
                }
            }
        }
    }

    //Login User
    fun loginUserFromAuthWithEmailAndPassword(email:String,password: String,fragment: Fragment) {
        launchDataLoad {
            viewModelScope.launch(Dispatchers.Main) {
                when(val result = userRepository.loginUserFromAuthWithEmailAndPassword(email,password)) {
                    is Result.Success -> {
                        Log.d(TAG,"Result.Success")
                        _toast.value = result.data?.displayName
                        onClickButton(R.id.tabFragment)
                    }
                    is Result.Error -> {
                        Log.e(TAG, "${result.exception.message}")
                        _toast.value = result.exception.message
                    }
                    is Result.Canceled -> {
                        Log.e(TAG, "${result.exception!!.message}")
                        _toast.value = fragment.getString(R.string.request_canceled)
                    }
                }
            }
        }
    }

    //Read user information
//    fun readUserInformationInFirestore(uid:String, fragment: Fragment): DocumentSnapshot{
//        val user:DocumentSnapshot
//        viewModelScope.launch(Dispatchers.Main) {
//            user = userRepository.readUserInformation(uid)
//
//        }
//        return user
//    }

    //Create User information in Firestore
    private suspend fun createUserInFirestore(user: User, fragment:Fragment) {
        Log.d(TAG, "Result - ${user.name}")
        when(val result = userRepository.createUserInFirestore(user))
        {
            is Result.Success -> {
                Log.d(TAG, fragment::class.java.simpleName)
                when(fragment) {
                    is RegisterFragment -> {
                        Log.d(TAG,"")
                        _toast.value = fragment.getString(R.string.registration_successful)
                    }
                    is LoginFragment -> {
                        Log.d(TAG, "Result - ${user.name}")
                        _toast.value = fragment.getString(R.string.login_successful)
                    }
                }
                Log.d(TAG, "Result.Error - ${user.name}")
                _currentUserMLD.value = user
            }
            is Result.Error -> {
                _toast.value = result.exception.message
            }
            is Result.Canceled -> {
                _toast.value = fragment.getString(R.string.request_canceled)
            }
        }
    }




    private fun createUserObject(firebaseUser: FirebaseUser, name: String, email: String): User {

        return User(
            id =  firebaseUser.uid,
            name = name,
            email = email
            //profilePicture = profilePicture
        )
    }

    fun onToastShown() {
        _toast.value = null
    }

    private fun launchDataLoad(block: suspend () -> Unit): Job {
        return viewModelScope.launch(Dispatchers.Main) {
            try
            {
                _spinner.value = true
                block()
            }
            catch (error: Throwable)
            {
                _toast.value = error.message
            }
            finally
            {
                _spinner.value = false
            }
        }
    }
}