package com.ise.virtualjukebox

import android.app.Activity
import android.os.Bundle


// inherit from Actovity instead of AppCompatActivity to enable starting activity via Intents
class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
