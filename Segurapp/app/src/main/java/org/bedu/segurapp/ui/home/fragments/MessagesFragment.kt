package org.bedu.segurapp.ui.home.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_messages.*
import org.bedu.segurapp.R
import org.bedu.segurapp.models.Messages
import org.bedu.segurapp.adapters.MessageAdapter

class MessagesFragment : Fragment() {
    private lateinit var smsAdapter : MessageAdapter
    private var smsListener : (Messages) ->Unit = {}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_messages, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUpRecyclerView()
    }

    fun setListener(l: (Messages) ->Unit){
        smsListener = l
    }

    //configuramos lo necesario para desplegar el RecyclerView
    private fun setUpRecyclerView(){
        recyclerSms.setHasFixedSize(true)
        recyclerSms.layoutManager = LinearLayoutManager(context)
        val adapter = MessageAdapter(getMessages())
        //asignando el Adapter al RecyclerView
        recyclerSms.adapter = adapter
    }

    //Generando datos
    private fun getMessages(): MutableList<Messages>{
        var message:MutableList<Messages> = ArrayList()
        message.add(Messages(R.drawable.unknown,"Oscar","Este es un mensaje de prueba", "3:53"))
        message.add(Messages(R.drawable.unknown,"Diego","Este es otro mensaje de prueba, pero mas largo 12346534", "1:02"))
        message.add(Messages(R.drawable.unknown,"Andres","Este un mensaje de prueba", "12:12"))
        message.add(Messages(R.drawable.unknown,"Ricardo","Hola", "16:32"))
        message.add(Messages(R.drawable.unknown,"David","Adios", "22:44"))
        message.add(Messages(R.drawable.unknown,"Roberto","Buenas", "23:00"))
        return message
    }
}