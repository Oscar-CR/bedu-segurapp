package org.bedu.segurapp.adapters

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_contact.view.*
import org.bedu.segurapp.R
import org.bedu.segurapp.models.Contacts
import org.bedu.segurapp.ui.home.DetailContactActivity

const val USER_NAME = "USER_NAME"
const val USER_PHONE = "USER_PHONE"

class ContactsAdapter(
    private val mListener: (Contacts) -> Unit,
    private var contacts: MutableList<Contacts>
) :
    RecyclerView.Adapter<ContactsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.item_contact, parent, false))
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = contacts[position]

        with(holder) {
            view.tvNombre.text = item.name
            view.tvPhone.text = item.phone
            view.imgPorfile.setImageResource(item.photo)
            view.btn_call.setOnClickListener {
                item.let { it1 -> mListener.invoke(it1) }
            }

            view.setOnClickListener {

                val bundle = Bundle()

                bundle.putString(USER_NAME, item.name)
                bundle.putString(USER_PHONE, item.phone)

                val intent = Intent(view.context, DetailContactActivity::class.java).apply {
                    putExtras(bundle)
                }
                view.context.startActivity(intent)
            }
        }
    }


    override fun getItemCount(): Int {
        return contacts.size
    }


    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {}


    fun filterList(filteredList: MutableList<Contacts>) {
        contacts = filteredList
        notifyDataSetChanged()
    }

}

