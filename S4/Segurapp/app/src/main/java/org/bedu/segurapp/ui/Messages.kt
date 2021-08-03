package org.bedu.segurapp.ui

import android.os.Parcel
import android.os.Parcelable

data class Messages (
    var photo: Int,
    var name: String,
    var message: String,
    var time: String
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(photo)
        parcel.writeString(name)
        parcel.writeString(message)
        parcel.writeString(time)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Messages> {
        override fun createFromParcel(parcel: Parcel): Messages {
            return Messages(parcel)
        }

        override fun newArray(size: Int): Array<Messages?> {
            return arrayOfNulls(size)
        }
    }
}