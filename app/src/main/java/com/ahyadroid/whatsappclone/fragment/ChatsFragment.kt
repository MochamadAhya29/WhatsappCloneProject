package com.ahyadroid.whatsappclone.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahyadroid.whatsappclone.MainActivity
import com.ahyadroid.whatsappclone.R
import com.ahyadroid.whatsappclone.activities.ConversationActivity
import com.ahyadroid.whatsappclone.adapter.ChatsAdapter
import com.ahyadroid.whatsappclone.listener.ChatsClickListener
import com.ahyadroid.whatsappclone.listener.FailureCallback
import com.ahyadroid.whatsappclone.model.Chat
import com.ahyadroid.whatsappclone.util.DATA_CHATS
import com.ahyadroid.whatsappclone.util.DATA_USERS
import com.ahyadroid.whatsappclone.util.DATA_USERS_CHATS
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_chats.*

class ChatsFragment : Fragment(), ChatsClickListener{


    private var chatsAdapter = ChatsAdapter(arrayListOf())
    private var failureCallback: FailureCallback? = null

    private val firebaseDb = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chats, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (userId.isNullOrEmpty()){
            failureCallback?.onUserError()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        chatsAdapter.setOnItemClickListener(this)
        rv_chats.apply {
            setHasFixedSize(false)
            layoutManager = LinearLayoutManager(context)
            adapter = chatsAdapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }

        firebaseDb.collection(DATA_USERS).document(userId!!)
            .addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException == null){
                    refreshChats()
                }
            }

    }

    fun setFailureCallbackListener(listener: FailureCallback){
        failureCallback = listener
    }

    override fun onChatClicked(
        chatId: String?,
        otherUserId: String?,
        chatsImageUrl: String?,
        chatsName: String?
    ) {
       startActivity(ConversationActivity.newIntent(context, chatId, chatsImageUrl, otherUserId, chatsName))
    }

    private fun refreshChats(){
        firebaseDb.collection(DATA_USERS).document(userId!!).get()
            .addOnSuccessListener {
                if (it.contains(DATA_USERS_CHATS)){
                    val partners = it[DATA_USERS_CHATS]
                    val chats = arrayListOf<String>()
                    for (partner in(partners as HashMap<String, String>).keys){
                        if (partners[partner] != null){
                            chats.add(partners[partner]!!)
                        }
                    }
                    chatsAdapter.updateChats(chats)
                }
            }
            .addOnFailureListener{ e ->
                e.printStackTrace()
            }
    }

    fun newChat(partnerId: String){
        firebaseDb.collection(DATA_USERS).document(userId!!).get()
            .addOnSuccessListener { userDocument ->
                val userChatPartners = hashMapOf<String, String>()
                if (userDocument[DATA_USERS_CHATS] != null &&
                        userDocument[DATA_USERS_CHATS] is HashMap<*, *>){
                    val userDocumentMap = userDocument[DATA_USERS_CHATS] as HashMap<String, String>
                    if (userDocumentMap.containsKey(partnerId)){
                        return@addOnSuccessListener
                    } else {
                        userChatPartners.putAll(userDocumentMap)
                    }
                }

                firebaseDb.collection(DATA_USERS)
                    .document(partnerId)
                    .get()
                    .addOnSuccessListener { partnerDocument ->
                        val partnerChatsPartners = hashMapOf<String, String>()
                        if (partnerDocument[DATA_USERS_CHATS] != null &&
                            partnerDocument[DATA_USERS_CHATS] is HashMap<*, *>){

                            val partnerDocumentMap =
                                partnerDocument[DATA_USERS_CHATS] as HashMap<String, String>
                            partnerChatsPartners.putAll(partnerDocumentMap)
                        }

                        val chatParticipants = arrayListOf(userId, partnerId)
                        val chat = Chat(chatParticipants)
                        val chatRef = firebaseDb.collection(DATA_CHATS).document()
                        val userRef = firebaseDb.collection(DATA_USERS).document(userId)
                        val partnerRef = firebaseDb.collection(DATA_USERS).document(partnerId)
                        userChatPartners[partnerId] = chatRef.id
                        partnerChatsPartners[userId] = chatRef.id

                        val batch = firebaseDb.batch()
                        batch.set(chatRef, chat)
                        batch.update(userRef, DATA_USERS_CHATS, userChatPartners)
                        batch.update(partnerRef,  DATA_USERS_CHATS, partnerChatsPartners)
                        batch.commit()

                    }
                    .addOnFailureListener{e ->
                        e.printStackTrace()
                    }
            }
            .addOnFailureListener{e ->
                e.printStackTrace()
            }
    }

}