package org.bedu.segurapp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.bedu.segurapp.models.local.Contact


private var  observableContacts = MutableLiveData<List<Contact>>()


class FakeContactRepository: IContactRepository {

    override fun getContact(): LiveData<List<Contact>> {
        return observableContacts
    }

    override suspend fun removeContact(contact: Contact) {
        val newList: MutableList<Contact> = observableContacts.value?.toMutableList() ?: mutableListOf()
        newList.remove(contact)
        observableContacts.value = newList
    }

    override suspend fun addContact(contact: Contact) {
        val newList: MutableList<Contact> = observableContacts.value?.toMutableList() ?: mutableListOf()
        newList.add(contact)
        observableContacts.value = newList
    }

    override fun populateContacts(contacts: List<Contact>) {
        observableContacts.value = contacts
    }
}