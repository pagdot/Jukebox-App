package com.ise.virtualjukebox

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ise.virtualjukebox.httpApi.HttpApi

class MainActivity : AppCompatActivity() {

    var api = HttpApi("127.0.0.1")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        api.getGithub()
        api.getSessionID("test_name")
        api.getTracks("test", 20)
        var test = 1
    }
}
