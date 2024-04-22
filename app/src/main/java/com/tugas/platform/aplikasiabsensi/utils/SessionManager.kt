package com.tugas.platform.aplikasiabsensi.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.tugas.platform.aplikasiabsensi.R
import com.tugas.platform.aplikasiabsensi.models.User

class SessionManager (context: Context) {
    private var prefs: SharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
    private val editor = prefs.edit()
    private val gson: Gson = Gson()

    companion object {
        const val USER_TOKEN = "user_token"
        const val IS_LOGIN = "is_login"
        const val USER = "user"
    }

    /**
     * Function to save auth token
     */
    fun saveAuthToken(token: String) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.putBoolean(IS_LOGIN, true)
        editor.apply()
    }

    fun saveUser(user: User) {
        val userJson: String = gson.toJson(user)
        editor.putString(USER, userJson)
        editor.apply()
    }

    /**
     * Function to fetch auth token
     */
    fun fetchAuthToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

    fun getUser(): User? {
        val userJson: String? = prefs.getString(USER, null)

        return gson.fromJson(userJson, User::class.java)
    }

    fun isLogin(): Boolean {
        return prefs.getBoolean(IS_LOGIN, false)
    }
}