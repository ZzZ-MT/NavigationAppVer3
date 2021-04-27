package com.example.navigationapp.viewmodel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.navigationapp.R
import com.example.navigationapp.model.User
import com.example.navigationapp.repo.UserRepository
import com.example.navigationapp.repo.implementation.UserRepositoryImpl
import com.example.navigationapp.utils.Event
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import com.example.navigationapp.utils.Result
import com.example.navigationapp.view.ui.LoginFragment
import com.example.navigationapp.view.ui.RegisterFragment

class FirebaseViewModel: ViewModel() {

    private val _toast = MutableLiveData<String?>()
    val toast: LiveData<String?>
        get() = _toast

    private val _spinner = MutableLiveData<Boolean>(false)
    val spinner: LiveData<Boolean>
        get() = _spinner

    private val _currentUserMLD = MutableLiveData<User>(User())
    val currentUserLD: LiveData<User>
        get() = _currentUserMLD

    private val _navigateScreen = MutableLiveData<Event<Int>>()
    val navigateScreen: LiveData<Event<Int>> = _navigateScreen

    private val userRepository: UserRepository = UserRepositoryImpl()

    private fun onClickButton(view: Int) {
        _navigateScreen.value = Event(view)
    }

    //Email
    fun registerUserFromAuthWithEmailAndPassword(name: String, email: String, password: String,fragment: Fragment) {
        launchDataLoad {
            viewModelScope.launch {
                when(val result = userRepository.registerUserFromAuthWithEmailAndPassword(email, password)) {
                    is Result.Success -> {
                        Log.e(TAG, "Result.Success")
                        result.data?.let {firebaseUser ->
                            createUserInFirestore(createUserObject(firebaseUser, name,email),fragment)
                            onClickButton(R.id.loginFragment)
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

    fun loginUserFromAuthWithEmailAndPassword(email:String,password: String,fragment: Fragment) {
        launchDataLoad {
            viewModelScope.launch {
                when(val result = userRepository.loginUserFromAuthWithEmailAndPassword(email,password)) {
                    is Result.Success -> {
                        Log.d(TAG,"Result.Success")
                        _toast.value = result.data.displayName
                        onClickButton(R.id.homeFragment)
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
        return viewModelScope.launch {
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