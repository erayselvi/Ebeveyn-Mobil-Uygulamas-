package com.es.inminiapplication

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.es.inminiapplication.model.VaccineInfo
import com.es.inminiapplication.viewmodel.VaccineViewModel

class VaccineAdapter(private val vaccineViewModel: VaccineViewModel) :
    ListAdapter<VaccineInfo, VaccineAdapter.ViewHolder>(VaccineInfoDiffCallback()) {

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
        val vaccineInfo = getItem(position)

        Log.d("VaccineAdapter", "Position: $position, Name: ${vaccineInfo.name}, isTaken: ${vaccineInfo.Taken}")

        holder.nameTextView.text = vaccineInfo.name
        holder.descriptionTextView.text = vaccineInfo.description
        holder.checkBox.isChecked = vaccineInfo.Taken
        holder.doseTextView.text = "Doz: ${vaccineInfo.dose}"

        // CheckBox durumu değiştiğinde, bu durumu VaccineInfo nesnesine de yansıtalım.
        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            Log.d("Checkbox", "Toggled: $isChecked")
            vaccineInfo.Taken = isChecked
            Log.d("VaccineAdapter", "Position: $position, Name: ${vaccineInfo.name}, isTaken: ${vaccineInfo.Taken}")

            // VaccineViewModel üzerinden güncelleme yapalım
            vaccineViewModel.updateVaccineTakenStatus(position, isChecked)
        }
    }
}

class VaccineInfoDiffCallback : DiffUtil.ItemCallback<VaccineInfo>() {
    override fun areItemsTheSame(oldItem: VaccineInfo, newItem: VaccineInfo): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: VaccineInfo, newItem: VaccineInfo): Boolean {
        return oldItem == newItem
    }
}
