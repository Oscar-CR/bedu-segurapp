package org.bedu.segurapp.adapters

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_message.view.*
import org.bedu.segurapp.R
import org.bedu.segurapp.models.Messages
import org.bedu.segurapp.ui.home.DetailContactActivity
import org.bedu.segurapp.ui.home.DetailMessageActivity


const val MESSAGE_NAME = "MESSAGE_NAME"
const val MESSAGE_TEXT = "MESSAGE_PHONE"

class MessageAdapter(
    private val sms: MutableList<Messages>
) :
    RecyclerView.Adapter<MessageAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.item_message, parent,false )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(sms[position])
    }

    override fun getItemCount(): Int {
        return sms.size
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(message: Messages) {
            view.sms_tvNombre.text= message.name
            view.sms_tvMessage.text= message.message
            view.sms_tvTime.text=message.time
            view.sms_imgPorfile.setImageResource(message.photo)

            view.setOnClickListener {
                var bundle = Bundle()

                bundle.putString(MESSAGE_NAME, message.name )
                bundle.putString(MESSAGE_TEXT, message.message)

                val intent=Intent(view.context, DetailMessageActivity::class.java).apply {
                    putExtras(bundle)
                }
                view.context.startActivity(intent)
            }



        }

    }

}
