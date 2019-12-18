package com.example.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ise.virtualjukebox.R
import com.ise.virtualjukebox.SearchHandler
import com.ise.virtualjukebox.activity.SearchFragment
import com.ise.virtualjukebox.jukeboxApi.dataStructure.Track

/**
 * Recycler adapter for tracks of search
 *
 * @property data tracklist
 * @property handler handler
 * @property listener listener
 */
class RecyclerAdapterSearch(private val data: MutableList<Track>, private val handler: SearchHandler, private val listener : SearchFragment.OnClickListener) :
    RecyclerView.Adapter<RecyclerAdapterSearch.ViewHolderTrack>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderTrack {
        return ViewHolderTrack(LayoutInflater.from(parent.context).inflate((R.layout.searchitem_layout), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolderTrack, position: Int) {
        holder.title.text = data[position].title
        holder.artist.text = data[position].artist

        holder.btnAdd.setOnClickListener{
            val tmpRetClass = handler.addToPlaylist(data[position])
            if(tmpRetClass.success)
                listener.onAddClick()
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