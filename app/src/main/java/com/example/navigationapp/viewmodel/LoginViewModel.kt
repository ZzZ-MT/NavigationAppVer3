package com.example.navigationapp.viewmodel

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.navigationapp.R
import com.example.navigationapp.repo.UserRepository
import com.example.navigationapp.repo.implementation.UserRepositoryImpl
import com.example.navigationapp.utils.Event
import com.example.navigationapp.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class LoginViewModel: ViewModel() {
    private val TAG = "LoginViewModel"

    private val _toast = MutableLiveData<String?>()
    val toast: LiveData<String?>
        get() = _toast

    private val _spinner = MutableLiveData(false)
    val spinner: LiveData<Boolean>
        get() = _spinner

    private val _navigateScreen = MutableLiveData<Event<Int>>()
    val navigateScreen: LiveData<Event<Int>> = _navigateScreen

    private val userRepository: UserRepository = UserRepositoryImpl()

    //Handle navigate view by button click
    private fun onClickButton(view: Int) {
        _navigateScreen.value = Event(view)
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

    //Login User
    fun loginUserFromAuthWithEmailAndPassword(email:String,password: String,fragment: Fragment) {
        launchDataLoad {
            viewModelScope.launch(Dispatchers.Main) {
                when(val result = userRepository.loginUserFromAuthWithEmailAndPassword(email,password)) {
                    is Result.Success -> {
                        Log.d(TAG,"Result.Success")
                        _toast.value = result.data?.displayName
                        onClickButton(R.id.main_nav_graph)
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