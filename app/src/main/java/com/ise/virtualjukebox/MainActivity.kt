package com.ise.virtualjukebox

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ise.virtualjukebox.jukeboxApi.httpApi.HttpApi
import com.ise.virtualjukebox.jukeboxApi.JukeboxApi
import java.io.IOException

class MainActivity : AppCompatActivity() {

    var api = JukeboxApi("193.170.132.206")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
    }
}
