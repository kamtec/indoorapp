package com.estimote.indoorapp

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.estimote.indoorsdk_module.cloud.Location

/**
  * Adapter para la lista de ubicaciones
  *
 */

class LocationListAdapter(var mLocations: List<Location>) : androidx.recyclerview.widget.RecyclerView.Adapter<LocationListAdapter.LocationHolder>() {

    var mListener: ((String) -> Unit)? = null

    class LocationHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.location_name) as TextView
        val id: TextView = itemView.findViewById(R.id.location_id) as TextView
        fun setOnClickListener(listener: View.OnClickListener) {
            itemView.setOnClickListener(listener)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): LocationListAdapter.LocationHolder {
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.location_list_item, parent,false)
        val vh = LocationHolder(v)
        return vh
    }

    override fun onBindViewHolder(holder: LocationHolder, position: Int) {
        holder.name.text = mLocations[position].name
        holder.id.text = mLocations[position].identifier
        holder.setOnClickListener(View.OnClickListener { mListener?.invoke(mLocations[position].identifier) })
    }

    override fun getItemCount(): Int {
        return mLocations.size
    }

    fun setLocations(list: List<Location>) {
        this.mLocations = list
        notifyDataSetChanged()
    }


    fun setOnClickListener(listener: (String) -> Unit) {
        mListener = listener
    }

}