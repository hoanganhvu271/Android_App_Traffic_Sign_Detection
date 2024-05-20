package com.hav.group3.Adapter

import com.hav.group3.R
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hav.group3.Helper.DatabaseHelper
import com.hav.group3.Model.DetectionHistory


class HistoryAdapter(
    private val historyDataArrayList: ArrayList<DetectionHistory>,
    private var mcontext: Context,
    private val listener: ItemClickListener,
    private val db : DatabaseHelper
) : RecyclerView.Adapter<HistoryAdapter.RecyclerViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        // Inflate Layout
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.historyrecycler, parent, false)
        return RecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val history: DetectionHistory = historyDataArrayList[position]
        holder.id.setText((position + 1).toString())
        val name = db.getTrafficSign(history.signId)
        if (name != null) {
            holder.name.setText(name.getName().toString() ?: "")
        }
        holder.time.setText(history.time)
    }

    override fun getItemCount(): Int {
        return historyDataArrayList.size
    }

    inner class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        public val id: TextView
        public val name: TextView
        public val time: TextView

        init {
            id = itemView.findViewById<TextView>(R.id.idHistoryId)
            name = itemView.findViewById<TextView>(R.id.idHistoryName)
            time = itemView.findViewById<TextView>(R.id.idHistoryTime)
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position: Int = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }
    }
    interface ItemClickListener {
        fun onItemClick(position: Int)
    }
}
