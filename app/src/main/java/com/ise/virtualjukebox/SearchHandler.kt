package com.ise.virtualjukebox
import com.ise.virtualjukebox.jukeboxApi.dataStructure.Track
import com.ise.virtualjukebox.jukeboxApi.dataStructure.VoteTrack

class SearchHandler {
    private var _MainHandler: MainHandler;
    private val _llength : Int;

    constructor(MainInstance : MainHandler, listleght : Int){
        _MainHandler = MainInstance;
        _llength = listleght;
    }
    fun SearchSong(Song:String) : MutableList<Track>? {
        return _MainHandler.SearchTrack(Song, _llength);
    }
    fun AddToPlaylist( Song: Track) : MutableList<VoteTrack>?   {

        if(!_MainHandler.AddOnTrack(Song)){
            _MainHandler.sendToast("Added To Playlist");
        }else{
            _MainHandler.sendToast("Adding To Playlist Failed");
        }
        return _MainHandler.GetTracks();
    }
}