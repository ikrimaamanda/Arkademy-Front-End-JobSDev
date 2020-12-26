package com.example.jobsdev.remote

import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response = chain.run {
        val tokenAuth = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhY19pZCI6NzEsImFjX2VtYWlsIjoicml6YWxAZ21haWwuY29tIiwiYWNfbGV2ZWwiOjEsImlhdCI6MTYwODk5NTUzMCwiZXhwIjoxNjA4OTk5MTMwfQ.Onk9GoNf_0JSXFNkg9vCjJmsS9d9W56iTOtsOwcOdZ8"
        proceed(
            request().newBuilder()
                .addHeader("Authorization", "Bearer $tokenAuth")
                .build()
        )
    }
}