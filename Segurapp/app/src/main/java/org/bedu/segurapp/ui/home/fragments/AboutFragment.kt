package org.bedu.segurapp.ui.home.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_about.*
import org.bedu.segurapp.R
import org.bedu.segurapp.adapters.PersonAdapter
import org.bedu.segurapp.models.Person


class AboutFragment : Fragment() {

    private lateinit var vAdapter : PersonAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_about, container, false)

    }

    private fun getPeople(): MutableList<Person>{
        val persons:MutableList<Person> = ArrayList()

        persons.add(Person("Antonio", "GTO", "Ing. Software y Sistemas Computacionales", "https://www.linkedin.com/in/antonio-labra-0ab639119/", "https://github.com/its7ony"))
        persons.add(Person("Astrid", "CDMX", "Ing. Computación","https://www.linkedin.com/in/astrid-g-bb14171bb/", "https://github.com/asguen3"))
        persons.add(Person("Oscar", "CDMX", "Ing. TI y Comunicaciones", "https://www.linkedin.com/in/oscar-ch%C3%A1vez-rosales-b131b31b1/", "https://github.com/Oscar-CR"))
        persons.add(Person("Regina", "CDMX", "Lic. Diseño y Comunicación Visual", "https://www.linkedin.com/in/regina-bernal-182187222", "https://github.com/Reginaowo"))

        return persons
    }

    private fun setUpRecyclerView(){
        recyclerPersons.setHasFixedSize(true)
        recyclerPersons.layoutManager = LinearLayoutManager(activity)
        vAdapter = PersonAdapter(requireActivity(), getPeople()) { person, key ->
            when (key) {
                "Linkedin" -> sendToWeb(person.linkedinLink)
                "GitHub" -> sendToWeb(person.githubLink)
            }
        }
            recyclerPersons.adapter = vAdapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUpRecyclerView()
    }


    private fun sendToWeb(linkChoose: String) {
        val uri = Uri.parse(linkChoose)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

}