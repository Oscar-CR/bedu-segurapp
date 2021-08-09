package org.bedu.segurapp.ui.home.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.bedu.segurapp.R
import org.bedu.segurapp.models.Messages

class MessageAdapter(
    private val context: Context,
    private val sms: MutableList<Messages>,
    private val clickListener: (Messages) -> Unit
) :
    RecyclerView.Adapter<MessageAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.item_message, parent,false )
        )
    }

    override fun onBindViewHolder(holder: MessageAdapter.ViewHolder, position: Int) {
        val message = sms.get(position)
        holder.bind(message, context)

        holder.itemView.setOnClickListener {
            clickListener(message)
        }
    }


    override fun getItemCount(): Int {
        return sms.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val name = view.findViewById<TextView>(R.id.sms_tvNombre)
        private val chat = view.findViewById<TextView>(R.id.sms_tvMessage)
        private val picture = view.findViewById<ImageView>(R.id.sms_imgPorfile)
        private val time = view.findViewById<TextView>(R.id.sms_tvTime)

        fun bind(mess: Messages, context: Any?) {
            name.text = mess.name
            chat.text = mess.message
            time.text = mess.time
            picture.setImageResource(mess.photo)
        }
    }

}
