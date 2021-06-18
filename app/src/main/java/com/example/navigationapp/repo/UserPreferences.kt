package com.example.navigationapp.repo

import android.content.Context
import android.content.SharedPreferences

object UserPreferences {
    private const val NAME = "CurrentUser"
    private const val MODE = Context.MODE_PRIVATE
    private lateinit var preferences: SharedPreferences

    //List of preferences
    private val USER_STATUS = Pair("status", false)
    private val USER_UID = Pair("uid", null)
    private val USER_NAME = Pair("name", null)
    private val USER_EMAIL = Pair("email", null)
    private val USER_CURRENT_LAT = Pair("lat", null)
    private val USER_CURRENT_LNG = Pair("lng", null)


    fun init(context: Context) {
        preferences = context.getSharedPreferences(NAME,MODE)
    }

    private inline fun SharedPreferences
            .edit(operation: (SharedPreferences.Editor) -> Unit){
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    var changeUserStatus: Boolean
        get() = preferences.getBoolean(USER_STATUS.first, USER_STATUS.second)
        set(value) = preferences.edit {
            it.putBoolean(USER_STATUS.first, value)
        }

    var changeUserUid: String?
        get() = preferences.getString(USER_UID.first, USER_UID.second)
        set(value) = preferences.edit{
            it.putString(USER_UID.first, value)
        }

    var changeUserName: String?
        get() = preferences.getString(USER_NAME.first, USER_NAME.second)
        set(value) = preferences.edit {
            it.putString(USER_NAME.first, value)
        }

    var changeUserEmail: String?
        get() = preferences.getString(USER_EMAIL.first, USER_EMAIL.second)
        set(value) = preferences.edit{
            it.putString(USER_EMAIL.first, value)
        }

    var changeUserCurrentLat: String?
        get() = preferences.getString(USER_CURRENT_LAT.first, USER_CURRENT_LAT.second)
        set(value) = preferences.edit {
            it.putString(USER_CURRENT_LAT.first,value)
        }

    var changeUserCurrentLng: String?
        get() = preferences.getString(USER_CURRENT_LNG.first, USER_CURRENT_LNG.second)
        set(value) = preferences.edit {
            it.putString(USER_CURRENT_LNG.first, value)
        }


}