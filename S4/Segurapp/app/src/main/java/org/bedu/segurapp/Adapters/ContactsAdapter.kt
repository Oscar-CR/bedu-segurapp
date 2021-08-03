package org.bedu.segurapp.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.bedu.segurapp.R
import org.bedu.segurapp.ui.Contacts
import org.bedu.segurapp.ui.Messages


class ContactsAdapter(
    private val context: Context,
    private val contacts: MutableList<Contacts>,
    private val clickListener: (Contacts) -> Unit
) :
    RecyclerView.Adapter<ContactsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.item_contacts, parent, false))
    }

    override fun onBindViewHolder(holder: ContactsAdapter.ViewHolder, position: Int) {
        val contact = contacts.get(position)
        holder.bind(contact, context)

        holder.itemView.setOnClickListener {
            clickListener(contact)
        }
    }

    override fun getItemCount(): Int {
        return contacts.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val name = view.findViewById<TextView>(R.id.tvNombre)
        private val phone = view.findViewById<TextView>(R.id.tvPhone)
        private val picture = view.findViewById<ImageView>(R.id.imgPorfile)

        fun bind(contact: Contacts, context: Any?) {
            name.text = contact.name
            phone.text = contact.phone
            picture.setImageResource(contact.photo)
        }
    }

}


