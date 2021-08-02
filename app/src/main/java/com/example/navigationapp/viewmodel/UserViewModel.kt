package com.example.navigationapp.viewmodel

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.example.navigationapp.R
import com.example.navigationapp.model.User
import com.example.navigationapp.repo.UserRepository
import com.example.navigationapp.repo.implementation.UserRepositoryImpl
import com.example.navigationapp.utils.Event
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import com.example.navigationapp.utils.Result
import com.example.navigationapp.view.LoginFragment
import com.example.navigationapp.view.RegisterFragment
import com.example.navigationapp.view.SplashFragmentDirections
import com.google.firebase.auth.ktx.userProfileChangeRequest
import kotlinx.coroutines.Dispatchers

class UserViewModel: ViewModel() {
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

//
//    //Check login state
//    fun getCurrentUser(): FirebaseUser? {
//        var firebaseUser: FirebaseUser? = null
//        viewModelScope.launch(Dispatchers.Main) {
//            firebaseUser = userRepository.getCurrentUser()
//            Log.i(TAG,"${firebaseUser?.uid}")
//            if(firebaseUser != null) {
//                onClickButton(R.id.main_nav_graph)
//            } else {
//                ///action_splashFragment_to_tabFragment cannot be found from the current destination NavGraph
////                onClickButton(R.id.action_splashFragment_to_loginFragment)
//                onClickButton(R.id.loginFragment)
//
//            }
//        }
//        return firebaseUser
//    }

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

//    //Send confirm email for reset password
//    fun sendPasswordResetEmail(email: String, fragment: Fragment) {
//        viewModelScope.launch(Dispatchers.IO) {
//            when(val result = userRepository.sendPasswordResetEmail(email))
//            {
//                is Result.Success -> {
//                    _toast.value = "Check email to reset your password!"
//                }
//                is Result.Error -> {
//                    _toast.value = result.exception.message
//                }
//                is Result.Canceled -> {
//                    _toast.value = fragment.getString(R.string.request_canceled)
//                }
//            }
//        }
//    }



//    //Login User
//    fun loginUserFromAuthWithEmailAndPassword(email:String,password: String,fragment: Fragment) {
//        launchDataLoad {
//            viewModelScope.launch(Dispatchers.Main) {
//                when(val result = userRepository.loginUserFromAuthWithEmailAndPassword(email,password)) {
//                    is Result.Success -> {
//                        Log.d(TAG,"Result.Success")
//                        _toast.value = result.data?.displayName
//                        onClickButton(R.id.main_nav_graph)
//                    }
//                    is Result.Error -> {
//                        Log.e(TAG, "${result.exception.message}")
//                        _toast.value = result.exception.message
//                    }
//                    is Result.Canceled -> {
//                        Log.e(TAG, "${result.exception!!.message}")
//                        _toast.value = fragment.getString(R.string.request_canceled)
//                    }
//                }
//            }
//        }
//    }

    //Read user information
//    fun readUserInformationInFirestore(uid:String, fragment: Fragment): DocumentSnapshot{
//        val user:DocumentSnapshot
//        viewModelScope.launch(Dispatchers.Main) {
//            user = userRepository.readUserInformation(uid)
//
//        }
//        return user
//    }









}