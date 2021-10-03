package org.bedu.segurapp.interfaces

import androidx.lifecycle.LiveData
import androidx.room.*
import org.bedu.segurapp.models.local.Contact

@Dao
interface ContactDao {

    @Insert
    suspend fun insertContact(contact: Contact)

    @Insert
    fun insertAll(vehicle: List<Contact>)

    @Update
    suspend fun updateContact(contact: Contact)

    @Delete
    suspend fun removeContact(contact: Contact)

    @Query("DELETE FROM Contact WHERE id=:id")
    suspend fun removeContactById(id: Int)

    @Delete
    suspend fun removeContact(vararg contact: Contact)

    @Query("SELECT * FROM Contact")
    fun getContact(): LiveData<List<Contact>>

    @Query("SELECT * FROM Contact WHERE id = :id")
    suspend fun getContactById(id: Int): Contact

    @Query("SELECT * FROM Contact WHERE name = :name")
    suspend fun getContactByName(name: String) : Contact
}
