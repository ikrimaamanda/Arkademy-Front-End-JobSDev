package com.example.jobsdev.remote

import android.content.Context
import com.example.jobsdev.login.LoginResponse
import com.example.jobsdev.sharedpreference.Constant
import com.example.jobsdev.sharedpreference.PreferencesHelper
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor(private val context : Context) : Interceptor {

    private lateinit var sharedPref : PreferencesHelper

    override fun intercept(chain: Interceptor.Chain): Response = chain.run {
        sharedPref = PreferencesHelper(context = context)

        val tokenAuth = sharedPref.getValueString(Constant.prefToken)
        proceed(
            request().newBuilder()
                .addHeader("Authorization", "Bearer $tokenAuth")
                .build()
        )
    }
}