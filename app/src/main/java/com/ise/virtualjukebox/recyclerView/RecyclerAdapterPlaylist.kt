package com.ise.virtualjukebox.recyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.ise.virtualjukebox.PlayHandler
import com.ise.virtualjukebox.R
import com.ise.virtualjukebox.jukeboxApi.JukeboxApi
import com.ise.virtualjukebox.jukeboxApi.dataStructure.VoteTrack


class RecyclerAdapterPlaylist(private val data: List<VoteTrack>, private val hPlay : PlayHandler) :

    RecyclerView.Adapter<RecyclerAdapterPlaylist.ViewHolderVoteTrack>() {

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapterPlaylist.ViewHolderVoteTrack {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.playlistitem_layout, parent, false) // inflate layout
        return ViewHolderVoteTrack(view) // Create your ViewHolder here
    }


    override fun onBindViewHolder(holder: ViewHolderVoteTrack, position: Int) {
        // set your data
        holder.txvTitle.text        = data.get(position).title
        holder.txvArtist.text       = data.get(position).artist
        holder.txvUsername.text     = data.get(position).addedBy
        holder.txvVotes.text        = data.get(position).votes.toString()

        // do some more stuff like listening to clicks (use holder.itemView)
        holder.btnUpvote.setOnClickListener{
            //api.voteTrack(data.get(position).trackId, api)

            val text = "Song Upvoted"
            val duration = Toast.LENGTH_SHORT
            val toast = Toast.makeText(it!!.context, text, duration)
            toast.show()
        }

        holder.btnDownvote.setOnClickListener{
            //api.voteTrack(data.get(position).trackId, api)
            //hPlay.Vote()

            val text = "Song Downvoted"
            val duration = Toast.LENGTH_SHORT
            val toast = Toast.makeText(it!!.context, text, duration)
            toast.show()
        }
}


// return the size of your data
override fun getItemCount() = data.size


// provide a reference to the views of the item layout
    class ViewHolderVoteTrack(view: View) : RecyclerView.ViewHolder(view) {
        val txvTitle    : TextView  = view.findViewById(R.id.txvTitle)
        val txvArtist   : TextView  = view.findViewById(R.id.txvArtist)
        val txvUsername : TextView  = view.findViewById(R.id.txvUsername)
        val txvVotes    : TextView  = view.findViewById(R.id.txvVotes)
        val btnUpvote   : Button    = view.findViewById(R.id.btnUpvote)
        val btnDownvote : Button    = view.findViewById(R.id.btnDownvote)
    }
}