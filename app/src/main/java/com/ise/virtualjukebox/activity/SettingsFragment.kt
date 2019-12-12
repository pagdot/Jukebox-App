package com.ise.virtualjukebox.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ise.virtualjukebox.MainHandler
import com.ise.virtualjukebox.R
import com.ise.virtualjukebox.recyclerView.RecyclerAdapterSettings
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : Fragment() {

    companion object {
        fun newInstance(): SettingsFragment {
            return SettingsFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //TODO: create real function to add new server list
        rvServer.layoutManager = LinearLayoutManager(context)

        val serverList = (activity as MainActivity).mainl.ServList
        /*
        val server1 = MainHandler.ServerPair()
        val server2 = MainHandler.ServerPair()
        val server3 = MainHandler.ServerPair()

        server1.IP = "192.168.1.123"
        server1.Name = "test1"

        server2.IP = "192.168.2.123"
        server2.Name = "test2"

        server3.IP = "192.168.3.123"
        server3.Name = "test3"

        val serverList = ArrayList<MainHandler.ServerPair>()
        serverList.add(server1)
        serverList.add(server2)
        serverList.add(server3)
         */
        rvServer.adapter = RecyclerAdapterSettings(serverList, (activity as MainActivity).settingsh)
    }
}