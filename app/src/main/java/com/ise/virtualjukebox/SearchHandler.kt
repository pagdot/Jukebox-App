package com.ise.virtualjukebox

import com.ise.virtualjukebox.jukeboxApi.dataStructure.Track
import com.ise.virtualjukebox.jukeboxApi.dataStructure.VoteTrack

class SearchHandler(MainInstance : MainHandler, searchListLength : Int) {
    private var _mainHandler: MainHandler = MainInstance
    private val _listLength : Int = searchListLength

    fun searchSong(Song:String) : MutableList<Track>? {
        return _mainHandler.searchTrack(Song, _listLength)
    }
    fun addToPlaylist( Song: Track) : MutableList<VoteTrack>?   {

        if(_mainHandler.addOnTrack(Song)){
            _mainHandler.sendToast("Added To Playlist.")
        }else{
            _mainHandler.sendToast("Adding To Playlist Failed.")
        }
        return _mainHandler.getTracks()
    }
}