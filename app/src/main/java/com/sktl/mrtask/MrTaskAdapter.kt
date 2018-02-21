package com.sktl.mrtask


import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.ImageView
import android.widget.TextView


/**
 * Created by USER-PC on 18.02.2018.
 */

class MrTaskAdapter internal constructor(internal var processes: List<Process>) : RecyclerView.Adapter<MrTaskAdapter.ProcessViewHolder>() {


    class ProcessViewHolder//        ProcessViewHolder(View  itemView) {
    internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {


        internal var cv: CardView
        internal var processName: TextView
        internal var processButton: TextView
        internal var processPhoto: ImageView


        init {//потребовало
            cv = itemView.findViewById<View>(R.id.cv) as CardView
            processName = itemView.findViewById<View>(R.id.process_name) as TextView
            processButton = itemView.findViewById<View>(R.id.process_button) as TextView
            processPhoto = itemView.findViewById<View>(R.id.process_photo) as ImageView


            itemView.setOnClickListener {
                if (MrTaskAdapter.listener != null) {
                    MrTaskAdapter.listener!!.onItemClick(itemView, layoutPosition)
                }
            }


        }
    }


    override fun onAttachedToRecyclerView(recyclerView: RecyclerView?) {
        super.onAttachedToRecyclerView(recyclerView)

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ProcessViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_view, viewGroup, false)
        return ProcessViewHolder(v)
    }

    override fun onBindViewHolder(processViewHolder: ProcessViewHolder, i: Int) {
        processViewHolder.processName.text = processes[i].name

        processViewHolder.processButton.setOnClickListener {
            //                processViewHolder.processButton.setText("killed");
        }

    }


    override fun getItemCount(): Int {
        return processes.size
    }

    interface OnItemClickListener {
        fun onItemClick(itemView: View, position: Int)
    }

    fun setOnItemClickListener(listnr: OnItemClickListener) {
        listener = listnr
    }

    companion object {


        //все что ниже - для клика

        var listener: OnItemClickListener? = null
    }

}
