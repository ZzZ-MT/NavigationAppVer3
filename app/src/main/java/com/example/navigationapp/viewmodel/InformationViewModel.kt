package com.example.navigationapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.navigationapp.repo.UserRepository
import com.example.navigationapp.repo.implementation.UserRepositoryImpl
import com.example.navigationapp.utils.Event
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class InformationViewModel: ViewModel() {
    private val TAG = "InformationViewModel"

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

    fun getCurrentUserInformation() : FirebaseUser? {
        var currentUser: FirebaseUser? = null
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
}