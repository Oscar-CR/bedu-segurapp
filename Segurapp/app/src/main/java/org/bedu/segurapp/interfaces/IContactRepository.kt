package org.bedu.segurapp.interfaces

import androidx.lifecycle.LiveData
import org.bedu.segurapp.models.local.Contact

//For testing purposes
interface IContactRepository {
    fun getContact(): LiveData<List<Contact>>
    suspend fun removeContact(contact: Contact)
    suspend fun addContact(contact: Contact)
    fun populateContacts(contacts: List<Contact>)
}