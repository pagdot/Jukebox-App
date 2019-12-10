package com.ise.virtualjukebox

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.ise.virtualjukebox.jukeboxApi.httpApi.HttpApi
import com.ise.virtualjukebox.jukeboxApi.JukeboxApi
import java.io.IOException

import com.ise.virtualjukebox.LoginHandler;

class MainActivity : AppCompatActivity() {

    var api = JukeboxApi("193.170.132.206")

    fun sendToast(ToBeSent: String){
        Toast.makeText(this, ToBeSent, Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        var mainl : MainHandler = MainHandler(this);
        var loginh : LoginHandler = LoginHandler(mainl);
        var settingsh : SettingsHandler = SettingsHandler(mainl);
        var playh : PlayHandler = PlayHandler(mainl);
        var searchh : SearchHandler = SearchHandler(mainl, 10);
        searchh.SearchSong("Neger");



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


        api.searchTracks

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
