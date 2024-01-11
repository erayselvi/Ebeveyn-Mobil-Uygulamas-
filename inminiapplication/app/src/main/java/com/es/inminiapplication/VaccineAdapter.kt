package com.es.inminiapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.es.inminiapplication.model.VaccineInfo

class VaccineAdapter(private val vaccineInfos: List<VaccineInfo>) :
    RecyclerView.Adapter<VaccineAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        val checkBox: CheckBox = itemView.findViewById(R.id.checkBox)
        val doseTextView: TextView = itemView.findViewById(R.id.doseTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_vaccine, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val vaccineInfo = vaccineInfos[position]
        holder.nameTextView.text = vaccineInfo.name
        holder.descriptionTextView.text = vaccineInfo.description
        holder.checkBox.isChecked = vaccineInfo.Taken
        holder.doseTextView.text = "Doz: ${vaccineInfo.dose}"

        // CheckBox durumu değiştiğinde, bu durumu VaccineInfo nesnesine de yansıtalım.
        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            vaccineInfos[position].Taken = isChecked
        }
    }

    override fun getItemCount(): Int {
        return vaccineInfos.size
    }
}
