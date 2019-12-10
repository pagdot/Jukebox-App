package com.ise.virtualjukebox.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.ise.virtualjukebox.R
import kotlinx.android.synthetic.main.activity_playlist.*

class SettingsActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        btnPlaylist.setOnClickListener{
            val intent = Intent(this, PlaylistActivity::class.java)
            startActivity(intent)
        }

        btnSearch.setOnClickListener{
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }
    }
}