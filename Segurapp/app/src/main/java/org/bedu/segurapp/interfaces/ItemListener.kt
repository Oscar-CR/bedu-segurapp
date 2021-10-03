package org.bedu.segurapp.interfaces

import org.bedu.segurapp.models.local.Contact

interface ItemListener {
    fun onEdit(contact: Contact)

    fun onDelete(contact: Contact)

}
