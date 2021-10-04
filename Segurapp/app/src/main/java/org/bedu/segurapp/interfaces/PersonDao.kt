package org.bedu.segurapp.interfaces

import androidx.room.*
import org.bedu.segurapp.models.local.data.Person

@Dao
interface PersonDao {

    @Insert
    suspend fun insertPerson(person: Person)

    @Insert
    suspend fun insertAll(person: List<Person>)

    @Query("SELECT * FROM Person")
    fun getPerson(): List<Person>

}