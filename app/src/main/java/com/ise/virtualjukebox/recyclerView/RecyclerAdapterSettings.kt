package com.ise.virtualjukebox.recyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ise.virtualjukebox.MainHandler
import com.ise.virtualjukebox.R
import com.ise.virtualjukebox.SettingsHandler


class RecyclerAdapterSettings(private val data: MutableList<MainHandler.ServerPair>, private val handler: SettingsHandler) :

    RecyclerView.Adapter<RecyclerAdapterSettings.ViewHolderServer>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderServer{
        return ViewHolderServer(LayoutInflater.from(parent.context).inflate((R.layout.serveritem_layout), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolderServer, position: Int) {
        holder.serverIP.text = data[position].IP
        holder.btnConnect.setOnClickListener{
            //TODO: add callback
            handler.Connect(data[position].IP.toString())
        }
    }

    // return the size of your data
    override fun getItemCount() = data.size

    class ViewHolderServer(view: View) : RecyclerView.ViewHolder(view) {
        val serverIP : TextView = view.findViewById(R.id.txvServerIP)
        val btnConnect : Button = view.findViewById(R.id.btnConnect)
    }
}