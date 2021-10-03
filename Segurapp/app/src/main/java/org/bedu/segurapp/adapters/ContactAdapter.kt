package org.bedu.segurapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.bedu.segurapp.databinding.ItemContactBinding
import org.bedu.segurapp.models.ContactViewModel
import org.bedu.segurapp.models.Messages
import org.bedu.segurapp.models.local.Contact

class ContactAdapter(
    private val viewModel: ContactViewModel

) :
    ListAdapter<Contact, ContactAdapter.ViewHolder>(ContactDiffCallback()) {
    private lateinit var contact: MutableList<Contact>
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(viewGroup)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item = getItem(position)
        viewHolder.bind(viewModel,item)
    }

    class ViewHolder private constructor(val binding: ItemContactBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(viewModel: ContactViewModel, item: Contact) {

            binding.viewModel = viewModel
            binding.contact = item
            binding.executePendingBindings()

        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemContactBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }

    }
    fun filterListC(filteredListC: MutableList<Contact>) {
        contact = filteredListC
        notifyDataSetChanged()
    }

}

class ContactDiffCallback : DiffUtil.ItemCallback<Contact>() {
    override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
        return oldItem == newItem
    }
}
