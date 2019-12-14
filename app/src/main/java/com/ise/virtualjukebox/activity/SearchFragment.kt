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

class SearchFragment : Fragment() {

    interface OnClickListener {
        fun onAddClick()
    }

    companion object {
        fun newInstance(): SearchFragment {
            return SearchFragment()
        }
    }

    fun trackAdded() {
        (activity as MainActivity).mainl.refreshTracks()
        (activity as MainActivity).playh.updatePreviousPlaylist()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

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