package org.bedu.segurapp.interfaces

import org.bedu.segurapp.models.PushNotification
import org.bedu.segurapp.utils.Constants.Companion.CONTENT_TYPE
import org.bedu.segurapp.utils.Constants.Companion.SERVER_KEY
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface INotificationAPI {
    @Headers("Authorization: key=$SERVER_KEY", "Content-Type:$CONTENT_TYPE")
    @POST("fcm/send")
    suspend fun postNotification(
        @Body notification: PushNotification
    ): Response<ResponseBody>
}