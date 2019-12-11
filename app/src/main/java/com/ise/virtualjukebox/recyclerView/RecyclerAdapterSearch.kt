package com.example.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ise.virtualjukebox.R
import com.ise.virtualjukebox.jukeboxApi.dataStructure.Track
import com.ise.virtualjukebox.jukeboxApi.dataStructure.VoteTrack
import kotlin.reflect.KFunction1

//class RecyclerAdapter(private val data: List<RecyclerItem>) :
class RecyclerAdapterSearch(private val data: MutableList<Track>, private val cbSearch: KFunction1<@ParameterName(
    name = "Song"
) Track, MutableList<VoteTrack>?>
) :
    RecyclerView.Adapter<RecyclerAdapterSearch.ViewHolderTrack>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderTrack {
        return ViewHolderTrack(LayoutInflater.from(parent.context).inflate((R.layout.searchitem_layout), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolderTrack, position: Int) {
        holder.title.text = data[position].title
        holder.artist.text = data[position].artist

        holder.btnAdd.setOnClickListener{
            //TODO: return ignored
            //cbSearch.invoke(data[position])
        }
    }

    // return the size of your data
    override fun getItemCount() = data.size

    class ViewHolderTrack(view: View) : RecyclerView.ViewHolder(view) {
        val artist : TextView = view.findViewById(R.id.txvArtistSearch)
        val title : TextView = view.findViewById(R.id.txvTitleSearch)
        val btnAdd : Button = view.findViewById(R.id.btnAddSearch)
    }
}