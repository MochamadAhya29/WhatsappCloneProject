package com.ahyadroid.whatsappclone.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.ahyadroid.whatsappclone.MainActivity
import com.ahyadroid.whatsappclone.fragment.CallFragment
import com.ahyadroid.whatsappclone.fragment.CameraFragment
import com.ahyadroid.whatsappclone.fragment.ChatsFragment
import com.ahyadroid.whatsappclone.fragment.StatusFragment

class SectionPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private var cameraFragment = CameraFragment()
    private var chatsFragment = ChatsFragment()
    private var statusFragment = StatusFragment()
    private var panggilanFragment = CallFragment()

    override fun getItem(position: Int): Fragment {
        return when(position){
            0 -> cameraFragment
            1 -> chatsFragment
            2 -> statusFragment
            3 -> panggilanFragment
            else -> chatsFragment
        }
    }
    override fun getCount(): Int {
        return 4
    }
}