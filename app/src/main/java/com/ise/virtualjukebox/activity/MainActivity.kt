/**
 * The Main Activity of the App
 * @author Tobias Egger
 * @author Matthias Dittrich
 * @version 1.0
 */
package com.ise.virtualjukebox.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.ise.virtualjukebox.*
import kotlinx.android.synthetic.main.fragment_layout.*


class MainActivity : AppCompatActivity() {

    var mainl : MainHandler = MainHandler(this)
    var loginh : LoginHandler = LoginHandler(mainl)
    var settingsh : SettingsHandler = SettingsHandler(mainl)
    var playh : PlayHandler = PlayHandler(mainl)
    var searchh : SearchHandler = SearchHandler(mainl, 50)

    var activeScreen : Screens = Screens.Login
    var screenChange :Boolean = true

    val handler = Handler(Looper.getMainLooper())

    val Store = "JukeBox"

    var testvar = true
    /**
     * Sends a Toast
     * @param ToBeSent String to be displayed as Toast
     */
    fun sendToast(ToBeSent: String){
        Toast.makeText(this, ToBeSent, Toast.LENGTH_SHORT).show()
    }

    /**
     * Store Settings as Shared Preferences
     * @param Prefs The Preferences to be stored
     * @param Name The Name of the Shared Preference File
     * @param Store The Tag for the Preference within the File
     */
    fun storePrefs(Prefs : String, Name: String, Store : String){
        val pref = getSharedPreferences(Name, Context.MODE_PRIVATE)
        val edit = pref.edit()
        edit.putString(Store, Prefs)
        edit.apply()
    }

    /**
     * Store a Settings as Shared Preferences
     * @param Name The Name of the Shared Preference File
     * @param Store The Tag for the Preference within the File
     * @return Returns the Value read, if failed, returns null
     */
    fun loadPrefs(Name: String, Store : String) : String?{
        val pref = getSharedPreferences(Name, Context.MODE_PRIVATE)
        return pref.getString(Store, null)
    }

    private val fragmentManager = supportFragmentManager

    /**
     * Spawns a Background Thread that refreshes the Playlist every 1s
     */
    fun spawnBackgroundThread() {
        screenChange = false
        if (testvar) {
            testvar = false
            handler.post(object : Runnable {
                override fun run() {
                    var sleepTime: Long = 1000
                    if (activeScreen == Screens.Playlist && !screenChange && mainl.anyServerConnection) {
                        val fragmentPlaylist =
                            fragmentManager.findFragmentByTag(Screens.Playlist.toString()) as PlaylistFragment
                        val tmpRetClass = mainl.refreshTracks()
                        if(!tmpRetClass.success) {
                            mainl.sendToast("Sync Failed: $tmpRetClass.errorMessage")
                            mainl.disconnectAllServer()
                        }
                        if (playh.playlistChanged() != null) {
                            fragmentPlaylist.playlistContentChanged()
                        }
                        if (playh.currentSongChanged() != null) {
                            fragmentPlaylist.currentTrackChanged()
                        }
                    }
                    else {
                        screenChange = false
                    }
                    handler.postDelayed(this, sleepTime)
                }
            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_layout)

        mainl.readServerList()




        switchFragment(Screens.Login)

        btnPlaylist.setOnClickListener{
            switchFragment(Screens.Playlist)
        }

        btnSearch.setOnClickListener{
            switchFragment(Screens.Search)
        }

        btnSettings.setOnClickListener{
            switchFragment(Screens.Settings)
        }
    }

    override fun onPause() {
        super.onPause()
        testvar = true
    }

    override fun onStop() {
        handler.removeCallbacksAndMessages(null)
        mainl.storeServerList()
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    // Display different fragment on screen
    fun switchFragment(screenName: Screens) {
        screenChange = true
        val fragmentTransaction = fragmentManager.beginTransaction()
        when (screenName) {
            Screens.Login -> {
                activeScreen = Screens.Login
                fragmentTransaction.replace(R.id.fragmentContainer, LoginFragment.newInstance(), Screens.Login.toString())
            }
            Screens.Playlist -> {
                activeScreen = Screens.Playlist
                fragmentTransaction.replace(R.id.fragmentContainer, PlaylistFragment.newInstance(), Screens.Playlist.toString())
            }
            Screens.Search -> {
                activeScreen = Screens.Search
                fragmentTransaction.replace(R.id.fragmentContainer, SearchFragment.newInstance(), Screens.Search.toString())
            }
            Screens.Settings -> {
                activeScreen = Screens.Settings
                fragmentTransaction.replace(R.id.fragmentContainer, SettingsFragment.newInstance(), Screens.Settings.toString())
            }
        }
        fragmentTransaction.commit()
    }
}

