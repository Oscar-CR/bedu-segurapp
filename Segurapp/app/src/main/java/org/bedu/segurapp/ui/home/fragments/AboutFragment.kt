package org.bedu.segurapp.ui.home.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.bedu.segurapp.R
import org.bedu.segurapp.UserLogin
import org.bedu.segurapp.adapters.PersonAdapter
import org.bedu.segurapp.interfaces.ItemPersonListener
import org.bedu.segurapp.models.PersonViewModel
import org.bedu.segurapp.models.local.data.Person
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class AboutFragment : Fragment() , ItemPersonListener {


    private lateinit var recyclerPerson: RecyclerView
    private lateinit var adapter: PersonAdapter

    private lateinit var viewModel: PersonViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    fun getListener(): ItemPersonListener {
        return this
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_about, container, false)


        viewModel = PersonViewModel(
            (requireContext().applicationContext as UserLogin).personRepository
        )

        recyclerPerson = view.findViewById(R.id.recyclerContacts)

        setupVehicleList()


        return view
    }


    private fun setupVehicleList() {
        if (viewModel != null) {
            val executor: ExecutorService = Executors.newSingleThreadExecutor()

            executor.execute(Runnable {

                val personArray = viewModel.getPersons()

                Handler(Looper.getMainLooper()).post(Runnable {
                    adapter = PersonAdapter(personArray?.toMutableList(), getListener())
                    recyclerPerson.adapter = adapter
                })
            })
        }
    }

    override fun onEdit(person: Person) {

    }

    override fun onDelete(person: Person) {

    }

}

