package org.bedu.segurapp.models.local

import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.bedu.segurapp.interfaces.ContactDao


class ContactRepository(
    private val contactDao: ContactDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    fun getContact(): LiveData<List<Contact>> {
        return contactDao.getContact()
    }

    suspend fun removeContact(contact: Contact){
        coroutineScope {
            launch { with(contactDao) { removeContact(contact ) } }
        }
    }

    suspend fun addContact(contact: Contact){
        coroutineScope {
            launch { contactDao.insertContact(contact ) }
        }
    }

    fun populateContact(contacts: List<Contact>) {
        return contactDao.insertAll(contacts)
    }

}