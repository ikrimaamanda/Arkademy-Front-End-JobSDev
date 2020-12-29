package com.example.jobsdev.remote

import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response = chain.run {
        val tokenAuth = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhY19pZCI6NzQsImFjX2VtYWlsIjoieW9sYW5kYUBnbWFpbC5jb20iLCJhY19sZXZlbCI6MCwiaWF0IjoxNjA5MjA2ODMwLCJleHAiOjE2MDkyMTA0MzB9.HxwpgObumzlq1gZubXo6L850iOeuErlcZLxZHmse2l8"
        proceed(
            request().newBuilder()
                .addHeader("Authorization", "Bearer $tokenAuth")
                .build()
        )
    }
}