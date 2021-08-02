package com.example.navigationapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.navigationapp.R
import com.example.navigationapp.repo.UserRepository
import com.example.navigationapp.repo.implementation.UserRepositoryImpl
import com.example.navigationapp.utils.Event
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SplashViewModel: ViewModel() {
    private val TAG = "SplashViewModel"

    private val _toast = MutableLiveData<String?>()
    val toast: LiveData<String?>
        get() = _toast

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
                onClickButton(R.id.main_nav_graph)
                _toast.value = "Login successful"
            } else {
                onClickButton(R.id.loginFragment)
                _toast.value = "ready to login"
            }
        }
        return firebaseUser
    }

    fun onToastShown() {
        _toast.value = null
    }

}