package org.bedu.segurapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.bedu.segurapp.R
import org.bedu.segurapp.interfaces.ItemPersonListener
import org.bedu.segurapp.models.local.data.Person

class PersonAdapter(
    private val personArray: MutableList<Person>?,
    val itemListener : ItemPersonListener
) :
    RecyclerView.Adapter<PersonAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        lateinit var photo : ImageView
        val nombre: TextView
        val ciudad: TextView
        val carrera: TextView


        init {
            // Define click listener for the ViewHolder's View.
            nombre = view.findViewById(R.id.tvName)
            ciudad  = view.findViewById(R.id.tvCity)
            carrera  = view.findViewById(R.id.tvCareer)
            photo = view.findViewById(R.id.imgPerson)

        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_person, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        personArray?.get(position)?.photo?.let { viewHolder.photo.setImageResource(it) }
        viewHolder.nombre.text = personArray?.get(position)?.name ?: ""
        viewHolder.ciudad.text = personArray?.get(position)?.city?:""
        viewHolder.carrera.text = personArray?.get(position)?.career?:""

    }


    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = personArray?.size ?: 0

}
