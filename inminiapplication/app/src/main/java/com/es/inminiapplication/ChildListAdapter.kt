package com.es.inminiapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.es.inminiapplication.model.Child

class ChildListAdapter(private val children: MutableList<Child>) :
    RecyclerView.Adapter<ChildListAdapter.ChildViewHolder>() {

    private var selectedChildId: String? = null
    private var itemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(child: Child)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    inner class ChildViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val childNameTextView: TextView = itemView.findViewById(R.id.childNameTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_child, parent, false)
        return ChildViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ChildViewHolder, position: Int) {
        val child = children[position]
        holder.childNameTextView.text = child.name

        holder.itemView.setOnClickListener {
            selectedChildId = child.firestoreId
            notifyDataSetChanged()

            itemClickListener?.onItemClick(child)
        }

        // Seçili çocuğu vurgulamak için
        if (selectedChildId == child.firestoreId) {
            holder.itemView.setBackgroundResource(R.drawable.selected_background)
        } else {
            holder.itemView.setBackgroundResource(R.drawable.normal_background)
        }
    }

    override fun getItemCount(): Int {
        return children.size
    }

    fun updateData(newChildren: List<Child>) {
        children.clear()
        children.addAll(newChildren)
        notifyDataSetChanged()
    }
}
