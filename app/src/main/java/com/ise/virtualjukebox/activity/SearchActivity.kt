package com.ise.virtualjukebox.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.ise.virtualjukebox.R
import kotlinx.android.synthetic.main.activity_playlist.*

class SearchActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        btnPlaylist.setOnClickListener{
            val intent = Intent(this, PlaylistActivity::class.java)
            startActivity(intent)
        }

        btnSettings.setOnClickListener{
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }
}