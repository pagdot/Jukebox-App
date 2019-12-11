package com.ise.virtualjukebox.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ise.virtualjukebox.R
import kotlinx.android.synthetic.main.playlistitem_layout.*

class PlaylistFragment : Fragment(){

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

        btnUpvote.setOnClickListener {
            //(activity as MainActivityFragment).playh.Vote()
        }

        btnDownvote.setOnClickListener{
            //(activity as MainActivityFragment).playh.Vote()
        }

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_playlist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // todo set list contents
    }
}