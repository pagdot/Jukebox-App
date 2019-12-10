package com.ise.virtualjukebox.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.ise.virtualjukebox.R
import com.ise.virtualjukebox.jukeboxApi.JukeboxApi
import kotlinx.android.synthetic.main.activity_login.*
import java.io.IOException

class LoginActivity : Activity() {

    var api = JukeboxApi("pagdot.tk")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        var getSessionFlag = false

        api.getSessionID("test_name", object : JukeboxApi.JukeboxApiCallback {
            override fun onSuccess() {
                getSessionFlag = true
            }

            override fun onFailure(statusCode: String?, exception: IOException?) {
            }
        })

        while(!getSessionFlag){}

        api.getTracks("test", 20, object : JukeboxApi.JukeboxApiCallback {
            override fun onSuccess() {
            }

            override fun onFailure(statusCode: String?, exception: IOException?) {
            }
        })

        api.getCurrentQueues(object : JukeboxApi.JukeboxApiCallback {
            override fun onSuccess() {
            }

            override fun onFailure(statusCode: String?, exception: IOException?) {
            }
        })

        api.addTrackToQueue("strange_test_id", object : JukeboxApi.JukeboxApiCallback {
            override fun onSuccess() {
            }

            override fun onFailure(statusCode: String?, exception: IOException?) {
            }
        })

        api.voteTrack("strange_test_id", 1, object : JukeboxApi.JukeboxApiCallback {
            override fun onSuccess() {
            }

            override fun onFailure(statusCode: String?, exception: IOException?) {
            }
        })

        var test = 1

        btnConnect.setOnClickListener {
            val intent = Intent(this, PlaylistActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // clear top for avoid returning to login screen
            startActivity(intent)
        }
    }
}
