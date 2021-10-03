package org.bedu.segurapp.models.local.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.bedu.segurapp.interfaces.PersonDao


@Database(entities = [Person::class], version = 2)
abstract class PersonDB : RoomDatabase(){

    companion object {
        private var personInstance: PersonDB? = null

        const val DB_NAME = "DEVELOPERS_DB"

        fun getInstance(context: Context) : PersonDB?{
            if(personInstance==null){

                synchronized(PersonDB::class){
                    personInstance = Room.databaseBuilder(
                        context.applicationContext,
                        PersonDB::class.java,
                        DB_NAME)
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }

            return personInstance
        }
    }

    abstract fun personDao(): PersonDao

}