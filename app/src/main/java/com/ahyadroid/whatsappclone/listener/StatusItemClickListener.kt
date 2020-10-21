package com.ahyadroid.whatsappclone.listener

import com.ahyadroid.whatsappclone.model.StatusListElement

interface StatusItemClickListener {
    fun onItemClicked(statusElement: StatusListElement)
}