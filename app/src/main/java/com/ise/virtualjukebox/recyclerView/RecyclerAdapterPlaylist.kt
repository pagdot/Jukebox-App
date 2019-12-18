package com.ise.virtualjukebox.recyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.ise.virtualjukebox.PlayHandler
import com.ise.virtualjukebox.R
import com.ise.virtualjukebox.activity.PlaylistFragment
import com.ise.virtualjukebox.jukeboxApi.dataStructure.VoteTrack

/**
 * Recycler adapter for Playlist
 *
 * @property playlist playlist
 * @property hPlay handler
 * @property listener listener
 */
class RecyclerAdapterPlaylist(private val playlist: MutableList<VoteTrack>, private val hPlay : PlayHandler, private val listener : PlaylistFragment.OnClickListener) :

    RecyclerView.Adapter<RecyclerAdapterPlaylist.ViewHolderVoteTrack>() {

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapterPlaylist.ViewHolderVoteTrack {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.playlistitem_layout, parent, false) // inflate layout
        return ViewHolderVoteTrack(view) // Create your ViewHolder here
    }

    override fun onBindViewHolder(holder: ViewHolderVoteTrack, position: Int) {
        // set your data
        holder.txvTitle.text        = playlist.get(position).title
        holder.txvArtist.text       = playlist.get(position).artist
        holder.txvUsername.text     = playlist.get(position).addedBy
        holder.txvVotes.text        = ""

        if(playlist.get(position).votes < 0) {
            holder.btnUpvote.visibility = View.INVISIBLE
            holder.btnDownvote.visibility = View.INVISIBLE
        }
        else {
            holder.btnUpvote.visibility = View.VISIBLE
            holder.btnDownvote.visibility = View.VISIBLE
            holder.txvVotes.text        = playlist.get(position).votes.toString()

            if(playlist.get(position).hasVoted == 0) {
                holder.btnUpvote.isEnabled = true
                holder.btnUpvote.isClickable = true
                holder.btnUpvote.alpha = 1f
                holder.btnDownvote.isEnabled = false
                holder.btnDownvote.isClickable = false
                holder.btnDownvote.alpha = 0.5f
            }
            else {
                holder.btnDownvote.isEnabled = true
                holder.btnDownvote.isClickable = true
                holder.btnDownvote.alpha = 1f
                holder.btnUpvote.isEnabled = false
                holder.btnUpvote.isClickable = false
                holder.btnUpvote.alpha = 0.5f
            }
        }

        // do some more stuff like listening to clicks (use holder.itemView)
        holder.btnUpvote.setOnClickListener{
            holder.btnDownvote.isEnabled = true
            holder.btnDownvote.isClickable = true
            holder.btnDownvote.alpha = 1f
            holder.btnUpvote.isEnabled = false
            holder.btnUpvote.isClickable = false
            holder.btnUpvote.alpha = 0.5f

            hPlay.vote(playlist.get(position))
            val text = "Song Upvoted."
            val duration = Toast.LENGTH_SHORT
            val toast = Toast.makeText(it!!.context, text, duration)
            toast.show()
            listener.onVoteClick()
        }

        holder.btnDownvote.setOnClickListener{
            holder.btnUpvote.isEnabled = true
            holder.btnUpvote.isClickable = true
            holder.btnUpvote.alpha = 1f
            holder.btnDownvote.isEnabled = false
            holder.btnDownvote.isClickable = false
            holder.btnDownvote.alpha = 0.5f

            hPlay.unvote(playlist.get(position))
            val text = "Song Upvote revoked!."
            val duration = Toast.LENGTH_SHORT
            val toast = Toast.makeText(it!!.context, text, duration)
            toast.show()
            listener.onVoteClick()
        }
}

// return the size of your data
override fun getItemCount() = playlist.size

// provide a reference to the views of the item layout
    class ViewHolderVoteTrack(view: View) : RecyclerView.ViewHolder(view) {
        val txvTitle    : TextView  = view.findViewById(R.id.txvTitle)
        val txvArtist   : TextView  = view.findViewById(R.id.txvArtist)
        val txvUsername : TextView  = view.findViewById(R.id.txvUsername)
        val txvVotes    : TextView  = view.findViewById(R.id.txvVotes)
        val btnUpvote   : ImageButton = view.findViewById(R.id.btnUpvote)
        val btnDownvote : ImageButton    = view.findViewById(R.id.btnDownvote)
    }
}