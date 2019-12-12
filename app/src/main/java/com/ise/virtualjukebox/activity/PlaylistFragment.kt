package com.ise.virtualjukebox.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ise.virtualjukebox.R
import com.ise.virtualjukebox.recyclerView.RecyclerAdapterPlaylist
import kotlinx.android.synthetic.main.fragment_playlist.*
import com.bumptech.glide.Glide


class PlaylistFragment() : Fragment() {
    private var isPlaying : Boolean = false

    companion object {
        fun newInstance(): PlaylistFragment {
            return PlaylistFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_playlist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // configure recyclerview
        rvPlaylist.layoutManager = LinearLayoutManager(context)
        rvPlaylist.adapter = RecyclerAdapterPlaylist((activity as MainActivity).playh)
    }

    fun playlistContentChanged() {
        val currentSong     = (activity as MainActivity).playh.CurrentSongChanged()
        if(currentSong != null) // current song changed
        {
            // set played song content
            txvTitle.text       = currentSong.title
            txvArtist.text      = currentSong.artist
            txvVotes.text       = "0" // TODO votes not available in PlayingTrack
            txvUsername.text    = currentSong.addedBy
            Glide.with(this).load(currentSong.iconUri).into(imgCover) // load album cover image with Glide

            // reset progress bar
            barPlaytime.progress = currentSong.playingFor
        }

        // update playlist content
        rvPlaylist.adapter = RecyclerAdapterPlaylist((activity as MainActivity).playh)

        // song was paused and is resumed now
        if(currentSong!!.playing && !isPlaying)
        {
            // continue porgressing progress bar
            // todo simulate progress of bar
            // https://stackoverflow.com/questions/30841419/pause-and-resume-circle-progress-bar-in-android/30843307#30843307
            barPlaytime.progress = currentSong.playingFor
            isPlaying = currentSong.playing
        }
        // song was playing and is paused now
        else if(!currentSong.playing && isPlaying)
        {
            // stop progressing progress bar
            barPlaytime.progress = currentSong.playingFor
            isPlaying = currentSong.playing
        }
    }
}