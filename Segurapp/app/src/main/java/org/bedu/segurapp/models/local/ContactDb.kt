package org.bedu.segurapp.models.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.bedu.segurapp.interfaces.ContactDao


@Database(entities = arrayOf(Contact::class), version = 1)
abstract class ContactDb : RoomDatabase(){

    companion object {
        private var contactInstance: ContactDb? = null

        const val DB_NAME = "CONTACT_DB"

        fun getInstance(context: Context) : ContactDb?{
            if(contactInstance==null){

                synchronized(ContactDb::class){
                    Room.databaseBuilder(
                        context.applicationContext,
                        ContactDb::class.java,
                        DB_NAME)
                        .fallbackToDestructiveMigration()
                        .build()
                        .also { contactInstance = it }
                }
            }

            return contactInstance
        }
    }

    abstract fun contactDao(): ContactDao

}