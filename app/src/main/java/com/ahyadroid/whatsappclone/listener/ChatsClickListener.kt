package com.ahyadroid.whatsappclone.listener

interface ChatsClickListener {
    fun onChatClicked(name: String?, otherUserId: String?, chatsImageUrl: String?, chatsName: String?)
}