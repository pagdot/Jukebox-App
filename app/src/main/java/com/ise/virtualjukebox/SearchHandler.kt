/**
 * Management Class to provide necessary network functionality to the Search Screen
 * @author Tobias Egger
 * @author Matthias Dittrich
 * @version 1.0
 */

package com.ise.virtualjukebox

import com.ise.virtualjukebox.jukeboxApi.dataStructure.Track
import com.ise.virtualjukebox.jukeboxApi.dataStructure.VoteTrack

class SearchHandler(MainInstance : MainHandler, searchListLength : Int) {
    private var _mainHandler: MainHandler = MainInstance
    private val _listLength : Int = searchListLength

    /**
     * Queries the Server for a specific Song
     * @param Song The Song to be looked after
     * @return Returns a List of Matches sent from the Server
     */
    fun searchSong(Song:String) : MutableList<Track>? {
        return _mainHandler.searchTrack(Song, _listLength)
    }
    /**
     * Adds a Song to Servers Playlist and synchronizes Local and Public Playlist
     * @param Song The Song to be looked after
     * @return Returns the new Playlist. On failure, Playlist is null
     */
    fun addToPlaylist( Song: Track) : MainHandler.PublicRetClass   {

        val tmpRetClass = _mainHandler.addOnTrack(Song)
        if(tmpRetClass.success){
            _mainHandler.sendToast("Added To Playlist.")
        }else{
            _mainHandler.sendToast("Adding To Playlist Failed. (${tmpRetClass.errorMessage})")
        }
        return tmpRetClass
    }
}