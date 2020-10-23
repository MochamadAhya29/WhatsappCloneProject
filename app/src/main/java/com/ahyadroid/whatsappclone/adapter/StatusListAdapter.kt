package com.ahyadroid.whatsappclone.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ahyadroid.whatsappclone.R
import com.ahyadroid.whatsappclone.listener.StatusItemClickListener
import com.ahyadroid.whatsappclone.model.StatusListElement
import com.ahyadroid.whatsappclone.util.populateImage
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_status.*

class StatusListAdapter (val statusList: ArrayList<StatusListElement>) :
    RecyclerView.Adapter<StatusListAdapter.StatusListViewHolder>(){

    private var clickListener: StatusItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =  StatusListViewHolder (
        LayoutInflater.from(parent.context).inflate(R.layout.item_status, parent, false)
    )

    override fun onBindViewHolder(holder: StatusListAdapter.StatusListViewHolder, position: Int) {
       holder.bindItem(statusList[position], clickListener)
    }

    override fun getItemCount() = statusList.size


    class StatusListViewHolder (override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bindItem(statusElement: StatusListElement, listener: StatusItemClickListener?){
            populateImage(
                img_status_photo.context,
                statusElement.userUrl,
                img_status_photo,
                R.drawable.ic_user
            )

            txt_status_name.text = statusElement.userName
            txt_status_time.text = statusElement.statusTime
            itemView.setOnClickListener {
                listener?.onItemClicked(statusElement)
            }
        }

    }

    fun onRefresh(){
        statusList.clear()
        notifyDataSetChanged()
    }
    fun addElement(element: StatusListElement){
        statusList.add(element)
        notifyDataSetChanged()
    }
    fun setOnClickListener(listener: StatusItemClickListener){
        clickListener= listener
        notifyDataSetChanged()
    }
}