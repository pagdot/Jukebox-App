package com.ise.virtualjukebox.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ise.virtualjukebox.R
import com.ise.virtualjukebox.recyclerView.RecyclerAdapterSettings
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : Fragment() {

    interface OnClickListener {
        fun onConnectClick()
    }

    companion object {
        fun newInstance(): SettingsFragment {
            return SettingsFragment()
        }
    }

    fun serverChanged() {
        serverListChanged()
        (activity as MainActivity).mainl.refreshTracks()
        (activity as MainActivity).playh.updatePreviousPlaylist()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvServer.layoutManager = LinearLayoutManager(context)

        val serverList = (activity as MainActivity).mainl.getActualServerList()
        rvServer.adapter = RecyclerAdapterSettings(serverList, (activity as MainActivity).settingsh, object: OnClickListener {
            override fun onConnectClick() {
                serverChanged()
            }
        })

        btnAddServer.setOnClickListener{
            (activity as MainActivity).switchFragment(Screens.Login)
        }
    }

    private fun serverListChanged() {
        val serverList = (activity as MainActivity).mainl.getActualServerList()
        rvServer.adapter = RecyclerAdapterSettings(serverList, (activity as MainActivity).settingsh, object: OnClickListener {
            override fun onConnectClick() {
                serverChanged()
            }
        })
    }
}