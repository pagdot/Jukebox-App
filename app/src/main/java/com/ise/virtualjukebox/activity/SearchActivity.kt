package com.ise.virtualjukebox.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclerview.RecyclerAdapterSearch
import com.ise.virtualjukebox.R
import com.ise.virtualjukebox.jukeboxApi.JukeboxApi
import com.ise.virtualjukebox.jukeboxApi.dataStructure.Track
import kotlinx.android.synthetic.main.activity_playlist.*
import kotlinx.android.synthetic.main.activity_playlist.btnPlaylist
import kotlinx.android.synthetic.main.activity_playlist.btnSettings
import kotlinx.android.synthetic.main.activity_search.*
import java.io.IOException

class SearchActivity : Activity() {

    var api = JukeboxApi("193.170.132.206")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        rvSearch.layoutManager = LinearLayoutManager(this)

        btnSearchSearch.setOnClickListener{
            //TODO: replace with real API/TOBI call
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

            onSearchSuccess(res)
        }


        btnPlaylist.setOnClickListener{
            val intent = Intent(this, PlaylistActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent)
        }

        btnSettings.setOnClickListener{
            val intent = Intent(this, SettingsActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent)
        }
    }

    private fun onSearchSuccess(results : MutableList<Track>){
        rvSearch.adapter = RecyclerAdapterSearch(results)
    }
}