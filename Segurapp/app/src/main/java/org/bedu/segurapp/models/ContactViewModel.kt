package org.bedu.segurapp.models

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.bedu.segurapp.models.local.Contact
import org.bedu.segurapp.models.local.ContactRepository

class ContactViewModel(private val contactRepository: ContactRepository): ViewModel() {


    val contactList = contactRepository.getContact()

    private var _editContactId = MutableLiveData<Int?>()
    val eventEditContact = _editContactId
    var contacts = contactRepository.getContact()


    fun removeContact(contact: Contact) = viewModelScope.launch{
        contactRepository.removeContact(contact)
    }

    fun onEdit(contactId: Int){
        eventEditContact.value = contactId
    }


    fun prepopulate(){

        val contacts = listOf(
            Contact(name = "Oscar",phone = "123456789"),
            Contact(name = "Diego",phone = "987654321")
        )
        contactRepository.populateContacts(contacts)

    }



}