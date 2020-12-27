package com.example.jobsdev.remote

import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response = chain.run {
        val tokenAuth = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhY19pZCI6NzEsImFjX2VtYWlsIjoicml6YWxAZ21haWwuY29tIiwiYWNfbGV2ZWwiOjEsImlhdCI6MTYwOTA5MTM0NiwiZXhwIjoxNjA5MDk0OTQ2fQ.uIdB1jCVj0wUGh34cwaG4im4BAGNYzZQn8PIGagpUhA"
        proceed(
            request().newBuilder()
                .addHeader("Authorization", "Bearer $tokenAuth")
                .build()
        )
    }
}