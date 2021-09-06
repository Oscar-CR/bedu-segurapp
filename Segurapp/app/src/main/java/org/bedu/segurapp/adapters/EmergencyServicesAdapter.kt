package org.bedu.segurapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_emergency_service.view.*
import org.bedu.segurapp.R
import org.bedu.segurapp.models.EmergencyService

class EmergencyServicesAdapter(
    private val mListener: (EmergencyService) -> Unit,
    private var mEmergencyServiceList: MutableList<EmergencyService>
) :
    RecyclerView.Adapter<EmergencyServicesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.item_emergency_service, parent, false))
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mEmergencyServiceList[position]

        with(holder) {
            view.tv_title.text = item.name
            view.tv_description.text = item.description
            view.tv_tags.text = getTags(item.tags)
            Picasso.get().load(item.urlImage).into(view.img_service)
            view.btn_call.setOnClickListener {
                item.let { it1 -> mListener.invoke(it1) }
            }

        }
    }

    private fun getTags(tags: String):String{
        var newString = ""
            tags.split(",").forEach {
                newString += "#$it "
        }

        return newString
    }


    override fun getItemCount(): Int {
        return mEmergencyServiceList.size
    }


    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {}


    fun filterList(filteredList: MutableList<EmergencyService>) {
        mEmergencyServiceList = filteredList
        notifyDataSetChanged()
    }


}








