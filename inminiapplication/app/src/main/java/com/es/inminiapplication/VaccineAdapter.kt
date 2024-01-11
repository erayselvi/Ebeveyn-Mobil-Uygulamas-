package com.es.inminiapplication

import android.app.DatePickerDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.es.inminiapplication.model.VaccineInfo
import java.text.SimpleDateFormat
import java.util.*

class VaccineAdapter(private val vaccineInfos: List<VaccineInfo>) :
    RecyclerView.Adapter<VaccineAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        val checkBox: CheckBox = itemView.findViewById(R.id.checkBox)
        val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_vaccine, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val vaccineInfo = vaccineInfos[position]
        holder.nameTextView.text = vaccineInfo.name
        holder.descriptionTextView.text = vaccineInfo.description
        holder.checkBox.isChecked = vaccineInfo.isTaken
        holder.dateTextView.text = vaccineInfo.date

        // CheckBox durumu değiştiğinde listener ile durumu güncelle
        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            vaccineInfo.isTaken = isChecked
            // CheckBox durumu değiştiğinde gerçekleştirilecek diğer işlemleri ekleyebilirsiniz.
        }

        // Tarih bilgisini göstermek için TextView'e tarih ekleyin
        holder.dateTextView.setOnClickListener {
            showDatePicker(holder.adapterPosition, holder.dateTextView)
        }
    }

    private fun showDatePicker(position: Int, dateTextView: TextView) {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            dateTextView.context,
            { _, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, month, dayOfMonth)

                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val formattedDate = dateFormat.format(selectedDate.time)

                vaccineInfos[position].date = formattedDate
                dateTextView.text = formattedDate
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    override fun getItemCount(): Int {
        return vaccineInfos.size
    }
}

