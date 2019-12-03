package com.ise.virtualjukebox

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ise.virtualjukebox.jukeboxApi.httpApi.HttpApi
import com.ise.virtualjukebox.jukeboxApi.JukeboxApi

class MainActivity : AppCompatActivity() {

    var api = JukeboxApi("193.170.132.206")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        api.getSessionID("test_name")

        var i = 10000
        while(i >= 0)
        {
            i--
        }
        api.getTracks("test", 20)
        api.getCurrentQueues()
        api.addTrackToQueue("strange_test_id")
        api.voteTrack("strange_test_id", 1)
        var test = 1
    }
}
