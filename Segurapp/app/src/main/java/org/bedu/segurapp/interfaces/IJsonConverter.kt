package org.bedu.segurapp.interfaces
import com.google.gson.Gson

interface IJsonConverter {
    fun toJSON(): String = Gson().toJson(this)
}

inline fun <reified T: IJsonConverter> String.toObject(): T = Gson().fromJson(this, T::class.java)