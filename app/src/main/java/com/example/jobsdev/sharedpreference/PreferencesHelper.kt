package com.example.jobsdev.sharedpreference

import android.content.Context
import android.content.SharedPreferences

class PreferencesHelper(context: Context) {

    private val SharedPreferenceName = "sharedpreferenceJobSDev"
    private val sharedPref : SharedPreferences
    val editor : SharedPreferences.Editor

    init {
        sharedPref = context.getSharedPreferences(SharedPreferenceName, Context.MODE_PRIVATE)
        editor = sharedPref.edit()
    }

    fun putValue(key : String, value : String) {
        editor.putString(key, value).apply()
    }

    fun getValueString(key : String) : String? {
        return sharedPref.getString(key, null)
    }

    fun putValue(key : String, value : Int) {
        editor.putInt(key, value).apply()
    }

    fun getValueInt(key : String) : Int? {
        return sharedPref.getInt(key, 0)
    }

    fun putValue(key : String, value : Boolean) {
        editor.putBoolean(key, value).apply()
    }

    fun getValueBoolean(key : String) : Boolean? {
        return sharedPref.getBoolean(key, false)
    }

    fun clear() {
        editor.clear().apply()
    }
}