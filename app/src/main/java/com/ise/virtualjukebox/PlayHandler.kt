package com.ise.virtualjukebox

import com.ise.virtualjukebox.jukeboxApi.dataStructure.PlayingTrack
import com.ise.virtualjukebox.jukeboxApi.dataStructure.VoteTrack

class PlayHandler {

    private var _MainHandler: MainHandler;
    private var _PreviousState : MutableList<VoteTrack>? = null;
    private var _currentTrack : PlayingTrack? = PlayingTrack();


    constructor(MainInstance : MainHandler){
        _MainHandler = MainInstance;
    }
    private fun compareItemWithoutVote(TrackA: VoteTrack, TrackB : VoteTrack) : Boolean{
        return TrackA.trackId == TrackB.trackId;
    }
    private fun compareValues(ListA : MutableList<VoteTrack>?, ListB : MutableList<VoteTrack>?):Boolean{
        var retval : Boolean = true;
        if(ListA == null || ListB == null || ListA.size != ListB.size){
            return false;
        }
        ListA.forEachIndexed { i, value ->
            if (!compareItemWithoutVote(ListB[i], value))
            {
                retval = false;
            }
        }
        return retval;
    }

    fun PlaylistChanged() : MutableList<VoteTrack>?{
        if(_PreviousState != null){
            var buffer = _MainHandler.getTracks();
            if(buffer == _PreviousState){
                return null;
            }else{
                return buffer;
            }
        }
        return _MainHandler.getTracks();
    }
    private fun compareCurrentSong(TrackA: PlayingTrack?, TrackB: PlayingTrack?) : Boolean{
        return TrackA?.trackId == TrackB?.trackId;
    }
    fun CurrentSongChanged() : PlayingTrack?{
        var current = _MainHandler.currentTrack();
        if(!compareCurrentSong(current, _currentTrack)){
            return current;
        }
        _currentTrack = current;
        return null;
    }
    fun SongPaused() : Boolean?{
        var retval = _MainHandler.currentTrack();
        if(retval == null){
            return false
        }
        return retval.playing;
    }
    fun SongResumed() : Boolean?{
        var retval = _MainHandler.currentTrack();
        if(retval == null){
            return false
        }
        return retval.playing;
    }
    fun Vote(Song : VoteTrack) : Boolean{
        return _MainHandler.voteOnTrack(Song);
    }
}