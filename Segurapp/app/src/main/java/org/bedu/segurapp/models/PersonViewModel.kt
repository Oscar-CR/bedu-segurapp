package org.bedu.segurapp.models


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.bedu.segurapp.R
import org.bedu.segurapp.models.local.PersonRepository
import org.bedu.segurapp.models.local.data.Person

class PersonViewModel(private val personRepository: PersonRepository): ViewModel(){
    private var _persons: List<Person> = listOf()

    init{
        prepopulate()
    }

    fun getPersons(): List<Person>{
        _persons = personRepository.getPerson()
        return _persons
    }
    fun prepopulate(){
        val persons = listOf(
            Person(name = "Antonio Labra Guerrero", city = "León, GTO", career = "Ing. Software y Sistemas Computacionales",linkedinLink =  "https://www.linkedin.com/in/antonio-labra-0ab639119/", githubLink = "https://github.com/its7ony", photo = R.drawable.antonio),
            Person(name = "Astrid Guerrero Niño",city =  "CDMX, Mexico", career = "Computer Engineering",linkedinLink = "https://www.linkedin.com/in/astrid-g-bb14171bb/", githubLink = "https://github.com/asguen3",photo = R.drawable.astrid),
            Person(name ="Oscar Chávez Rosales",city =  "CDMX", career = "Ing. Tecnologías de la Información y Comunicaciones", linkedinLink = "https://www.linkedin.com/in/oscar-ch%C3%A1vez-rosales-b131b31b1/", githubLink = "https://github.com/Oscar-CR", photo = R.drawable.oscar ),
            Person(name = "Regina Bernal Galicia", city = "CDMX, Mexico",career =  "Lic. Diseño y Comunicación Visual", linkedinLink = "https://www.linkedin.com/home", githubLink = "https://github.com/Reginaowo", photo = R.drawable.regina)
        )

        viewModelScope.launch {
            personRepository.populatePersons(persons)
        }
    }
}