package com.es.inminiapplication.view.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.es.inminiapplication.R
import com.es.inminiapplication.model.FAQCategory
import com.es.inminiapplication.model.FAQDocument

class FaqListAdapter(
    private var faqList: MutableList<FAQDocument>
) : RecyclerView.Adapter<FaqListAdapter.FaqViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(faqDocument: FAQDocument)
    }

    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    inner class FaqViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FaqViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_faq, parent, false)
        return FaqViewHolder(view)
    }

    override fun onBindViewHolder(holder: FaqViewHolder, position: Int) {
        val faqDocument = faqList[position]
        holder.titleTextView.text = faqDocument.title

        holder.itemView.setOnClickListener {
            itemClickListener?.onItemClick(faqDocument)
        }
    }

    override fun getItemCount(): Int {
        return faqList.size
    }

    fun updateData(newFaqList: List<FAQDocument>) {
        faqList.clear()
        faqList.addAll(newFaqList)
        notifyDataSetChanged()
    }
}
