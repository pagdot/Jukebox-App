package com.ise.virtualjukebox.recyclerView

import android.graphics.Color
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
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
        holder.serverIP.text = data[position].ip
        if(data[position].isInit) {
            holder.connectStatus.setColorFilter(Color.parseColor("#00ff00"), PorterDuff.Mode.SRC)
            holder.btnConnect.isEnabled = false
            holder.btnConnect.isClickable = false
            holder.btnConnect.alpha = 0.5f
        }
        else {
            holder.connectStatus.clearColorFilter()
            holder.btnConnect.isEnabled = true
            holder.btnConnect.isClickable = true
            holder.btnConnect.alpha = 1f
        }

        holder.btnConnect.setOnClickListener{
            val tmpRetClass = handler.connect(data[position].ip.toString())
            if(tmpRetClass.success) {
                Toast.makeText(it!!.context, "Connection successfully created.", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(it!!.context, "Connection failed to create (${tmpRetClass.errorMessage}).", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // return the size of your data
    override fun getItemCount() = data.size

    class ViewHolderServer(view: View) : RecyclerView.ViewHolder(view) {
        val serverIP : TextView = view.findViewById(R.id.txvServerIP)
        val connectStatus : ImageView = view.findViewById(R.id.imvConnectStatus)
        val btnConnect : Button = view.findViewById(R.id.btnConnect)
    }
}