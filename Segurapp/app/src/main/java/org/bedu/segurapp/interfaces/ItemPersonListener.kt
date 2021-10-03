package org.bedu.segurapp.interfaces

import org.bedu.segurapp.models.local.data.Person

interface ItemPersonListener {
    fun onEdit(person: Person)

    fun onDelete(person: Person)

}
