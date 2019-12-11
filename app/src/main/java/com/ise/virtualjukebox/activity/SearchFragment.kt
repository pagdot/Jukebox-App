package com.ise.virtualjukebox.activity

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclerview.RecyclerAdapterSearch
import com.ise.virtualjukebox.R     //ACHTUNG  ACHTUNG
import com.ise.virtualjukebox.jukeboxApi.dataStructure.Track
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.fragment1_layout.*

class SearchFragment : Fragment() {

    companion object {
        fun newInstace(): SearchFragment {
            return SearchFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment1_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        first_fragment_text.text = "first fragment"
    }

    /*
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        rvSearch.layoutManager = LinearLayoutManager(activity)
        btnSearch.setOnClickListener{
            val res = ArrayList<Track>()
            val track1 = Track()
            val track2 = Track()
            val track3 = Track()
            track1.title = "title 1"
            track1.artist = "artist 1"

            track2.title = "title 2"
            track2.artist = "artist 2"

            track3.title = "title 3"
            track3.artist = "artist 3"

            res.add(track1)
            res.add(track2)
            res.add(track3)

            rvSearch.adapter = RecyclerAdapterSearch(res)
        }

        return inflater.inflate(R.layout.activity_search, container, false)
    }
     */
}