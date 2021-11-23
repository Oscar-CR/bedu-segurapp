package org.bedu.segurapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.bedu.segurapp.R
import org.bedu.segurapp.models.Person


class PersonAdapter(
    private val context: Context,
    private val persons: MutableList<Person>,
    private val clickListener: (Person, String) -> Unit): RecyclerView.Adapter<PersonAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return ViewHolder(layoutInflater.inflate(R.layout.item_person, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val person = persons[position]
        holder.bin(person, context)

        holder.view.findViewById<ImageButton>(R.id.imageBtnLinkd).setOnClickListener {
            clickListener(person, "Linkedin")
        }

        holder.view.findViewById<ImageButton>(R.id.imageBtnGit).setOnClickListener {
            clickListener(person, "GitHub")
        }

    }

    override fun getItemCount(): Int {
        return persons.size
    }


    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        private val nombre = view.findViewById<TextView>(R.id.tvName)
        private val ciudad = view.findViewById<TextView>(R.id.tvCity)
        private val carrera = view.findViewById<TextView>(R.id.tvCareer)


        fun bin(person: Person, context: Context) {
            nombre.text = person.name
            ciudad.text = person.city
            carrera.text = person.career

        }
    }
}


