package org.bedu.segurapp.models

import com.google.gson.annotations.SerializedName
import org.bedu.segurapp.interfaces.IJsonConverter

data class User(
    @SerializedName("nickname") var nickname: String,
    @SerializedName("email") var email: String,
    @SerializedName("password") var password: String,
    @SerializedName("alertMessage") var alertMessage: String = "",
    @SerializedName("telephoneNumber") var telephoneNumber: String = "911"
) : IJsonConverter