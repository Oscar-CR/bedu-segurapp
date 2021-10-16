package org.bedu.segurapp.models

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable


@SuppressLint("ParcelCreator")
class Person(
    var name: String,
    var city: String,
    var career: String,
    var linkedinLink: String,
    var githubLink: String,
    var idPhoto: Int

): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt()
    ){


    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(city)
        parcel.writeString(career)
        parcel.writeString(linkedinLink)
        parcel.writeString(githubLink)
        parcel.writeInt(idPhoto)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Person> {
        override fun createFromParcel(parcel: Parcel): Person {
            return Person(parcel)
        }

        override fun newArray(size: Int): Array<Person?> {
            return arrayOfNulls(size)
        }
    }

}