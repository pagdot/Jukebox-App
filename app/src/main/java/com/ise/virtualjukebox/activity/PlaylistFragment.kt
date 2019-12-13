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



class PlaylistFragment : Fragment() {
    private var isPlaying : Boolean = false
    private var pStatus : Int = 0

    companion object {
        fun newInstance(): PlaylistFragment {
            return PlaylistFragment()
        }
    }

    fun run() {
        Thread(Runnable {
            while (true)
            {
                if (isPlaying) {
                    pStatus += 1
                    barPlaytime.progress = pStatus
                }
                // sleep for 100 ms
                try {
                    Thread.sleep(100)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }).start()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_playlist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvPlaylist.layoutManager = LinearLayoutManager(context)
        val playlist = (activity as MainActivity).playh.playlistChanged()
        if(playlist != null){
            rvPlaylist.adapter = RecyclerAdapterPlaylist(playlist, (activity as MainActivity).playh)
        }
        run()
    }

    fun playlistContentChanged() {
        val currentSong     = (activity as MainActivity).playh.currentSongChanged()
        if(currentSong != null) // current song changed
        {
            // set played song context
            txvTitle.text       = currentSong.title
            txvArtist.text      = currentSong.artist
            txvVotes.text       = "0" // TODO votes not available in PlayingTrack
            txvUsername.text    = currentSong.addedBy
            Glide.with(this).load(currentSong.iconUri).into(imgCover) // load album cover image with Glide

            // reset progress bar
            barPlaytime.progress = currentSong.playingFor
            pStatus = currentSong.playingFor*10 // progress bar = 1800 ms, playingFor = x sec
            // todo set progressbar.max to length of song (currently not available in song info)
            isPlaying = currentSong.playing
            //isPlaying = true // for testing purposes
        }

        // update playlist content
        val playlist = (activity as MainActivity).playh.playlistChanged()
        if(playlist != null){
            rvPlaylist.adapter = RecyclerAdapterPlaylist(playlist, (activity as MainActivity).playh)
        }
    }
}