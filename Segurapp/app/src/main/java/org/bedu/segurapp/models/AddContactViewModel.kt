package org.bedu.segurapp.models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.bedu.segurapp.models.local.Contact
import org.bedu.segurapp.models.local.ContactRepository

class AddContactViewModel(private val contactRepository: ContactRepository): ViewModel() {


    private var _contactDone = MutableLiveData<Boolean>(false)
    val contactDone = _contactDone


    var name: String? = null
    var phone: String? = null


    fun setName(s: CharSequence, start:Int, before: Int, count:Int){
        name = s.toString()
    }

    fun setPhone(s: CharSequence, start:Int, before: Int, count:Int){
        phone = s.toString()
    }


    fun newContact() = viewModelScope.launch{
        if ( !name.isNullOrBlank() && !phone.isNullOrBlank() ){
            val contact = Contact(
                name = name,
                phone = phone,
            )

            contactRepository.addContact(contact)

            _contactDone.value = true
        }
    }

}

