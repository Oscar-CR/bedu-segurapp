package org.bedu.segurapp.networking

import org.bedu.segurapp.interfaces.INotificationAPI
import org.bedu.segurapp.utils.Constants.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {
    companion object {
        private val retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        val api by lazy{
            retrofit.create(INotificationAPI::class.java)
        }
    }
}