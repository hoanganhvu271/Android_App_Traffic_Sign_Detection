package com.hav.group3.Adapter

import com.hav.group3.R
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hav.group3.Model.TrafficSign


class SignAdapter(
    private val signDataArrayList: ArrayList<TrafficSign>,
    private var mcontext: Context,
    private val listener: ItemClickListener
) : RecyclerView.Adapter<SignAdapter.RecyclerViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        // Inflate Layout
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.signrecycle, parent, false)
        return RecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val sign: TrafficSign = signDataArrayList[position]
        holder.name.setText(sign.getName())
        holder.img.setImageResource(sign.getImgId())
    }

    override fun getItemCount(): Int {
        return signDataArrayList.size
    }

    inner class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        public val name: TextView
        public val img: ImageView

        init {
            name = itemView.findViewById<TextView>(R.id.idSignText)
            img = itemView.findViewById<ImageView>(R.id.idSignImage)
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
