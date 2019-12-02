package com.ise.virtualjukebox

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ise.virtualjukebox.jukeboxApi.httpApi.HttpApi
import com.ise.virtualjukebox.jukeboxApi.JukeboxApi

class MainActivity : AppCompatActivity() {

    var api = JukeboxApi("127.0.0.1")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        api.getGithub()
        api.getSessionID("test_name")
        api.getTracks("test", 20)
        api.getCurrentQueues()
        api.addTrackToQueue("strange_test_id")
        api.voteTrack("strange_test_id", 1)
        var test = 1
    }
}
