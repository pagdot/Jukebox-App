package com.ise.virtualjukebox

import com.ise.virtualjukebox.jukeboxApi.dataStructure.Track
import com.ise.virtualjukebox.jukeboxApi.dataStructure.VoteTrack

class SearchHandler(MainInstance : MainHandler, searchListLength : Int) {
    private var mainHandler: MainHandler = MainInstance
    private val listLength : Int = searchListLength

    fun searchSong(Song:String) : MutableList<Track>? {
        return mainHandler.SearchTrack(Song, listLength)
    }
    fun addToPlaylist( Song: Track) : MutableList<VoteTrack>?   {

        if(!mainHandler.AddOnTrack(Song)){
            mainHandler.sendToast("Added To Playlist")
        }else{
            mainHandler.sendToast("Adding To Playlist Failed")
        }
        return mainHandler.GetTracks()
    }
}