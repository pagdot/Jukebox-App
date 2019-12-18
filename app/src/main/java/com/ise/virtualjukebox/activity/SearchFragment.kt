package com.ise.virtualjukebox.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recyclerview.RecyclerAdapterSearch
import com.ise.virtualjukebox.R
import com.ise.virtualjukebox.jukeboxApi.dataStructure.Track
import kotlinx.android.synthetic.main.fragment_search.*

/**
 * Track search GUI fragment
 *
 */
class SearchFragment : Fragment() {

    /**
     * Click-listener for votes
     *
     */
    interface OnClickListener {
        fun onAddClick()
    }

    /**
     * Constructor
     */
    companion object {
        fun newInstance(): SearchFragment {
            return SearchFragment()
        }
    }

    /**
     * On Track added refresh track- and playlist
     *
     */
    fun trackAdded() {
        (activity as MainActivity).mainl.refreshTracks()
        (activity as MainActivity).playh.updatePreviousPlaylistAndCurrentTrack()
    }

    /**
     * on view creation pass arguments
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    /**
     * After view creation create empty tracklist
     *
     * @param view
     * @param savedInstanceState
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val hSearch = (activity as MainActivity).searchh

        rvSearch.layoutManager = LinearLayoutManager(context)
        rvSearch.adapter = RecyclerAdapterSearch(ArrayList<Track>(), hSearch, object: OnClickListener {
            override fun onAddClick() {
                trackAdded()
            }
        })

        btnSearchSearch.setOnClickListener{
            val searchResults = hSearch.searchSong(txeSearch.text.toString())
            if(searchResults != null){
                rvSearch.adapter = RecyclerAdapterSearch(searchResults, hSearch, object: OnClickListener {
                    override fun onAddClick() {
                        trackAdded()
                    }
                })
            }
        }
    }
}