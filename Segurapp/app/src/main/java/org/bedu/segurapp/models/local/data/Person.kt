package org.bedu.segurapp.models.local.data


import android.provider.ContactsContract
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity
data class Person constructor(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo (name="name") val name: String?,
    @ColumnInfo val city: String?,
    @ColumnInfo val career: String?,
    @ColumnInfo val linkedinLink: String?,
    @ColumnInfo val githubLink: String?,
    @ColumnInfo val photo: Int?

)


    /* : Parcelable{
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

     */