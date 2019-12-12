package com.ise.virtualjukebox.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ise.virtualjukebox.PlayHandler
import com.ise.virtualjukebox.R
import com.ise.virtualjukebox.jukeboxApi.dataStructure.VoteTrack
import com.ise.virtualjukebox.recyclerView.RecyclerAdapterPlaylist
import kotlinx.android.synthetic.main.fragment_playlist.*


class PlaylistFragment() : Fragment(){

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

        rvPlaylist.layoutManager = LinearLayoutManager(context)

        //TODO: Aufruf getTrack
        val playlist = ArrayList<VoteTrack>()
        val track1 = VoteTrack()
        val track2 = VoteTrack()
        val track3 = VoteTrack()
        track1.title = "title 1"
        track1.artist = "artist 1"

        track2.title = "title 2"
        track2.artist = "artist 2"

        track3.title = "title 3"
        track3.artist = "artist 3"

        playlist.add(track1)
        playlist.add(track2)
        playlist.add(track3)

        rvPlaylist.adapter = RecyclerAdapterPlaylist(playlist, (activity as MainActivity).playh)

        //btnUpvote.setOnClickListener {
        //    //(activity as MainActivityFragment).playh.Vote()
        //}
//
        //btnDownvote.setOnClickListener{
        //    //(activity as MainActivityFragment).playh.Vote()
        //}

        // todo set list contents
    }

    //private var listener: PlaylistContentChangedListener? = null
//
//
    //override fun onAttach(activity: Activity?) {
    //    super.onAttach(activity)
    //    try {
    //        listener = activity as PlaylistContentChangedListener?
    //    } catch (castException: ClassCastException) {
    //        /** The activity does not implement the listener.  */
    //    }
//
    //}
//
    fun playlistContentChanged() {
        // todo update playlist content
    }
}