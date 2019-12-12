package com.ise.virtualjukebox.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recyclerview.RecyclerAdapterSearch
import com.ise.virtualjukebox.R     //ACHTUNG  ACHTUNG
import com.ise.virtualjukebox.jukeboxApi.dataStructure.Track
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment : Fragment() {

    companion object {
        fun newInstance(): SearchFragment {
            return SearchFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //TODO: check if right interface
        val hSearch = (activity as MainActivity).searchh

        rvSearch.layoutManager = LinearLayoutManager(context)
        rvSearch.adapter = RecyclerAdapterSearch(ArrayList<Track>(), hSearch)

        btnSearchSearch.setOnClickListener{
            val searchResults = hSearch.SearchSong(txeSearch.text.toString())
            if(searchResults != null){
                rvSearch.adapter = RecyclerAdapterSearch(searchResults, hSearch)
            }
        }
    }
}