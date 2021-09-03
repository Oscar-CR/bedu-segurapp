package org.bedu.segurapp.adapters

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_contact.view.*
import org.bedu.segurapp.R
import org.bedu.segurapp.models.Contacts
import org.bedu.segurapp.ui.home.AddContactActivity
import org.bedu.segurapp.ui.home.DetailContactActivity
import java.util.stream.Collector
import java.util.stream.Stream

const val USER_NAME = "USER_NAME"
const val USER_PHONE = "USER_PHONE"

class ContactsAdapter(
    private var contacts: MutableList<Contacts>,
) :
    RecyclerView.Adapter<ContactsAdapter.ViewHolder>() {

    //private lateinit var contactList:MutableList<Contacts>


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.item_contact, parent, false))
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       holder.bind(contacts[position])

    }
    /*
    public fun filter(strSearch: String){
        if (strSearch.length ==0){
            contacts.clear()
            contacts.addAll(contactList)

        }else{

            var contactSearch: Stream<Contacts>? = contacts.stream().filter{ it -> it.name.contains(strSearch)}
            contacts.clear()
            contacts.addAll(contactSearch)
            notifyDataSetChanged()
        }
    }

     */

    override fun getItemCount(): Int {
        return contacts.size
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(contact: Contacts) {
            view.tvNombre.text = contact.name
            view.tvPhone.text = contact.phone
            view.imgPorfile.setImageResource(contact.photo)

            view.btn_call.setOnClickListener{
                Toast.makeText(view.context, "Llamando a ${contact.name}",Toast.LENGTH_LONG).show()
            }

            view.setOnClickListener {

                var bundle = Bundle()

                bundle.putString(USER_NAME, contact.name)
                bundle.putString(USER_PHONE,contact.phone)

                 val intent=Intent(view.context, DetailContactActivity::class.java).apply {
                  putExtras(bundle)
              }
                view.context.startActivity(intent)
            }

        }

    }

    /*
    fun  ContactsAdapter(list: List<Contacts>){
        contacts=  list as MutableList<Contacts>

    }

     */




}








