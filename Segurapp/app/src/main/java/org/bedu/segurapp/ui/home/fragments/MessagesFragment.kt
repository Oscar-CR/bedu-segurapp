package org.bedu.segurapp.ui.home.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_messages.*
import org.bedu.segurapp.R
import org.bedu.segurapp.models.Messages
import org.bedu.segurapp.adapters.MessageAdapter

class MessagesFragment : Fragment() {
    private lateinit var adapterM : MessageAdapter
    private var smsListener : (Messages) ->Unit = {}
    private lateinit var etSearchMessage: EditText
    private lateinit var messageList: MutableList<Messages>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_messages, container, false)

        initComponents(view)
        initListener(view)
        return view
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
        adapterM = MessageAdapter(getMessages())
        //asignando el Adapter al RecyclerView
        recyclerSms.adapter = adapterM
    }

    //Generando datos
    private fun getMessages(): MutableList<Messages>{
        messageList = ArrayList()
        messageList.add(Messages(R.drawable.unknown,"Oscar","Este es un mensaje de prueba", "3:53"))
        messageList.add(Messages(R.drawable.unknown,"Diego","Este es otro mensaje de prueba, pero mas largo 12346534", "1:02"))
        messageList.add(Messages(R.drawable.unknown,"Andres","Este un mensaje de prueba", "12:12"))
        messageList.add(Messages(R.drawable.unknown,"Ricardo","Hola", "16:32"))
        messageList.add(Messages(R.drawable.unknown,"David","Adios", "22:44"))
        messageList.add(Messages(R.drawable.unknown,"Roberto","Buenas", "23:00"))
        return messageList
    }

    private fun initComponents(view: View){
        etSearchMessage = view.findViewById(R.id.etSearchMessage)
    }

    private fun initListener(view: View){

        etSearchMessage.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {


            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }
            override fun afterTextChanged(s: Editable) {
                filter(s.toString())
            }
        })

    }

    private fun filter(text:String){

        val filteredListM: MutableList<Messages> = ArrayList()
        for (item in messageList) {
            if (item.name.lowercase().contains(text.lowercase())) {
                filteredListM.add(item)

            }
        }
        adapterM.filterListM(filteredListM);
    }

}