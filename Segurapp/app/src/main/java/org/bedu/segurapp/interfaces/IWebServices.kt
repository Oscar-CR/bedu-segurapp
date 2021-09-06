package org.bedu.segurapp.interfaces
import org.bedu.segurapp.models.EmergencyService
import retrofit2.Call
import retrofit2.http.GET

interface IWebServices {
    @GET("services/")
    fun getServices(): Call<List<EmergencyService>>
}