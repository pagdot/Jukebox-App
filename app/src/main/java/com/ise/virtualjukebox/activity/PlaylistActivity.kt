package com.ise.virtualjukebox.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.ise.virtualjukebox.R
import kotlinx.android.synthetic.main.activity_playlist.*

class PlaylistActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist)

        btnSearch.setOnClickListener{
            val intent = Intent(this, SearchActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent)
        }

        btnSettings.setOnClickListener{
            val intent = Intent(this, SettingsActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent)
        }
    }
}