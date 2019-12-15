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
import kotlinx.android.synthetic.main.fragment_login.*


class PlaylistFragment : Fragment() {
    private var isPlaying : Boolean = false
    private var pStatus : Int = 0
    private var fragmentDestroyed : Boolean = false

    interface OnClickListener {
        fun onVoteClick()
    }

    companion object {
        fun newInstance(): PlaylistFragment {
            return PlaylistFragment()
        }
    }

    fun run() {
        (activity as MainActivity).test()
        Thread(Runnable {
            while (!fragmentDestroyed)
            {
                if (isPlaying) {
                    pStatus += 1
                    barPlaytime!!.progress = pStatus
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

    fun voteChanged() {
        (activity as MainActivity).mainl.refreshTracks()
        (activity as MainActivity).playh.updatePreviousPlaylistAndCurrentTrack()
        playlistContentChanged()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_playlist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvPlaylist.layoutManager = LinearLayoutManager(context)
        val playlist = (activity as MainActivity).playh.getPlaylist()
        if(playlist != null){
            rvPlaylist.adapter = RecyclerAdapterPlaylist(playlist, (activity as MainActivity).playh, object: OnClickListener {
                override fun onVoteClick() {
                    voteChanged()
                }
            })
        }

        val currentSong     = (activity as MainActivity).playh.getCurrentTrack()
        if(currentSong != null) // current song changed
        {
            // set played song context
            txvTitle.text       = currentSong.title
            txvArtist.text      = currentSong.artist
            txvUsername.text    = currentSong.addedBy
            Glide.with(this).load(currentSong.iconUri).into(imgCover) // load album cover image with Glide

            // reset progress bar
            barPlaytime.max = currentSong.duration*10
            pStatus = currentSong.playingFor*10 // progress bar = 1800 ms, playingFor = x sec
            barPlaytime.progress = pStatus
            isPlaying = currentSong.playing
            //isPlaying = true // for testing purposes
        }
        run()
    }

    fun playlistContentChanged() {
        rvPlaylist.layoutManager = LinearLayoutManager(context)
        val playlist = (activity as MainActivity).playh.getPlaylist()
        if(playlist != null) {
            rvPlaylist.adapter = RecyclerAdapterPlaylist(playlist, (activity as MainActivity).playh, object: OnClickListener {
                override fun onVoteClick() {
                    voteChanged()
                }
            })
        }
    }

    fun currentTrackChanged() {
        val currentSong     = (activity as MainActivity).playh.getCurrentTrack()
        if(currentSong != null) // current song changed
        {
            // set played song context
            txvTitle.text       = currentSong.title
            txvArtist.text      = currentSong.artist
            txvUsername.text    = currentSong.addedBy
            Glide.with(this).load(currentSong.iconUri).into(imgCover) // load album cover image with Glide

            // reset progress bar
            barPlaytime.max = currentSong.duration*10
            pStatus = currentSong.playingFor*10 // progress bar = 1800 ms, playingFor = x sec
            barPlaytime.progress = pStatus
            // todo set progressbar.max to length of song (currently not available in song info)
            isPlaying = currentSong.playing
            //isPlaying = true // for testing purposes
        }
    }


    override fun onDetach ()
    {
        fragmentDestroyed = true
        super.onDetach()
    }
}