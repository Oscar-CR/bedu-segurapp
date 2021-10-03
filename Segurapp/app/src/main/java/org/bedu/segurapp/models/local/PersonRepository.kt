package org.bedu.segurapp.models.local

import android.app.Person
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.bedu.segurapp.interfaces.PersonDao


class PersonRepository(private val personDao: PersonDao,
                       private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO) {

    fun getPerson(): List<org.bedu.segurapp.models.local.data.Person> {
        return personDao.getPerson()
    }

    suspend fun populatePersons(persons: List<org.bedu.segurapp.models.local.data.Person>) = withContext(ioDispatcher){
        return@withContext personDao.insertAll(persons)
    }

}