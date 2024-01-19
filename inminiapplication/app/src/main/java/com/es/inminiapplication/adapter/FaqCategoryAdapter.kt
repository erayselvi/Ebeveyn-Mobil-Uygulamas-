package com.es.inminiapplication.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.es.inminiapplication.R
import com.es.inminiapplication.model.FAQCategory

class FaqCategoryAdapter(
    private var faqCategories: MutableList<FAQCategory>) :
    RecyclerView.Adapter<FaqCategoryAdapter.FaqCategoryViewHolder>() {

    private var selectedCategoryId: String? = null
    private var itemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(faqCategory: FAQCategory)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    inner class FaqCategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val categoryNameTextView: TextView = itemView.findViewById(R.id.textViewCategory)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FaqCategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false)
        return FaqCategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: FaqCategoryViewHolder, position: Int) {
        val category = faqCategories[position]
        holder.categoryNameTextView.text = category.categoryName

        holder.itemView.setOnClickListener{
            selectedCategoryId = category.firestoreId
            notifyDataSetChanged()

            itemClickListener?.onItemClick(category)
        }//

        Log.d("Checkbox", "Toggled: $faqCategories")
        // Seçili kategoriyi vurgulamak için
        if (selectedCategoryId == category.firestoreId) {
            holder.itemView.setBackgroundResource(R.drawable.selected_background)
        } else {
            holder.itemView.setBackgroundResource(R.drawable.normal_background)
        }
    }

    override fun getItemCount(): Int {
        return faqCategories.size
    }

    fun updateData(newFaqCategories: List<FAQCategory>) {
        faqCategories.clear()
        faqCategories.addAll(newFaqCategories)
        notifyDataSetChanged()
    }
}
