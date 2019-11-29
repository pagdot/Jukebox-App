package com.ise.virtualjukebox

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnConnect.setOnClickListener {
            val intent = Intent(this, PlaylistActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // clear top for avoid returning to login screen
            startActivity(intent)
        }
    }
}
