package com.ise.virtualjukebox

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity()  {
    var mainl : MainHandler = MainHandler(this);
    var loginh : LoginHandler = LoginHandler(mainl);
    var settingsh : SettingsHandler = SettingsHandler(mainl);
    var playh : PlayHandler = PlayHandler(mainl);
    var searchh : SearchHandler = SearchHandler(mainl, 10);

    val Store = "JukeBox"

    fun sendToast(ToBeSent: String){
        Toast.makeText(this, ToBeSent, Toast.LENGTH_SHORT).show()
    }

    fun storePrefs(Prefs : String, Name: String, Store : String){
        val pref = getSharedPreferences(Name, Context.MODE_PRIVATE)
        val edit = pref.edit()
        edit.putString(Store, Prefs);
        edit.apply();
    }

    fun loadPrefs(Name: String, Store : String) : String?{
        val pref = getSharedPreferences(Name, Context.MODE_PRIVATE);
        return pref.getString(Store, null);
    }



    override fun onCreate(savedInstanceState: Bundle?) {

        //searchh.SearchSong("   ");

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainl.ReadServerList();

        val test = mainl.ConnectToServer("test", "pagdot.tk")
        val handler = Handler(Looper.getMainLooper())

        mainl.BackProcess()
    }
    override fun onDestroy(){
        mainl.StoreServerList();
        super.onDestroy()
    }

}
