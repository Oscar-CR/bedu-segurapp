package org.bedu.segurapp.interfaces
import com.google.gson.Gson
import org.bedu.segurapp.models.CoordinatesInfo

interface IJsonConverter {
    fun toJSON(): String = Gson().toJson(this)
}

inline fun <reified T: IJsonConverter> String.toObject(java: Class<Array<CoordinatesInfo>>): T = Gson().fromJson(this, T::class.java)