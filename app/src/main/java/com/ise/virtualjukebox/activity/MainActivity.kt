/*
package com.ise.virtualjukebox

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ise.virtualjukebox.activity.PlaylistFragment
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T




class MainActivity : AppCompatActivity() {

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

        handler.post(object : Runnable {
            override fun run() {
               // mainl.sendToast("Neger");
                mainl.RefreshTracks();
                handler.postDelayed(this, 10000)
            }
        })
    }
    override fun onDestroy(){
        mainl.StoreServerList();
        super.onDestroy()
    }



}

 */

package com.ise.virtualjukebox.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.ise.virtualjukebox.*
import kotlinx.android.synthetic.main.fragment_layout.*

class MainActivity : AppCompatActivity() {

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

    private val fragmentManager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_layout)

        //mainl.ReadServerList()

        //switchFragment(Screens.Search)

        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainer, LoginFragment.newInstance())
        fragmentTransaction.commit()

        btnPlaylist.setOnClickListener{
            switchFragment(Screens.Playlist)
        }

        btnSearch.setOnClickListener{
            switchFragment(Screens.Search)
        }

        btnSettings.setOnClickListener{
            switchFragment(Screens.Settings)
        }
        //btnOne.setOnClickListener{
        //    val fragmentTransaction = fragmentManager.beginTransaction()
        //    fragmentTransaction.replace(R.id.myFragment, testFragment)
        //    fragmentTransaction.commit()
        //}

        //val fragmentTransaction = fragmentManager.beginTransaction()
        //fragmentTransaction.replace(R.id.myFragment, testFragment)
        //fragmentTransaction.commit()
    }


    // switch current fragment on screen with other fragment
    fun switchFragment(screenName: Screens) {

        val fragmentTransaction = fragmentManager.beginTransaction()
        //val transaction = supportFragmentManager.beginTransaction()

        when (screenName) {
            Screens.Login -> {
                // Replace whatever is in the fragment_container view with this fragment
                fragmentTransaction.replace(R.id.fragmentContainer, LoginFragment.newInstance())
                Toast.makeText(this, "login", Toast.LENGTH_SHORT).show()
            }
            Screens.Playlist -> {
                // Replace whatever is in the fragment_container view with this fragment
                fragmentTransaction.replace(R.id.fragmentContainer, PlaylistFragment.newInstance())
                Toast.makeText(this, "playlist", Toast.LENGTH_SHORT).show()
            }
            Screens.Search -> {
                // Replace whatever is in the fragment_container view with this fragment
                fragmentTransaction.replace(R.id.fragmentContainer, SearchFragment.newInstance())
                Toast.makeText(this, "search", Toast.LENGTH_SHORT).show()
            }
            Screens.Settings -> {
                // Replace whatever is in the fragment_container view with this fragment
                fragmentTransaction.replace(R.id.fragmentContainer, SettingsFragment.newInstance())
            }
            else -> {
                // Replace whatever is in the fragment_container view with this fragment
                //transaction.replace(R.id.fragmentContainer, LoginFragment())
            }
        }

        // Commit the transaction
        fragmentTransaction.commit()
    }
}

