package com.ahyadroid.whatsappclone.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class User(
    val email: String? = "", // Model merupakan layer yang menunjuk pada objek dan
    val phone: String? = "", // data yang ada pada aplikasi
    val name: String? = "", // sehingga User disini akan memiliki data-data disamping
    val imageUrl: String? = "",
    val status: String? = "",
    val statusUrl: String? = "",
    val statusTime: String? = ""
)

data class Contact(
    val name: String?,
    val phone: String?
)

data class Chat(
    val chatParticipants: ArrayList<String>
)

data class Message(
    val sentBy: String? = "",
    val message: String? = "",
    val messageTime: Long? = 0
)

@Parcelize
data class StatusListElement(
    val userName: String?,
    val userUrl: String?,
    val status: String?,
    val statusUrl: String?,
    val statusTime: String?,
) : Parcelable