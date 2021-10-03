package org.bedu.segurapp.models.local

import androidx.lifecycle.LiveData
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.bedu.segurapp.interfaces.ContactDao
import org.bedu.segurapp.models.local.data.IContactRepository


class ContactRepository(
    private val contactDao: ContactDao
) : IContactRepository { // : IContactRepository for testing purposes

    override fun getContact(): LiveData<List<Contact>> {
        return contactDao.getContact()
    }

    override suspend fun removeContact(contact: Contact){
        coroutineScope {
            launch { with(contactDao) { removeContact(contact ) } }
        }
    }

    override suspend fun addContact(contact: Contact){
        coroutineScope {
            launch { contactDao.insertContact(contact ) }
        }
    }

    override fun populateContacts(contacts: List<Contact>) {
        return contactDao.insertAll(contacts)
    }

}