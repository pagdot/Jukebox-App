package com.ise.virtualjukebox

import com.ise.virtualjukebox.jukeboxApi.dataStructure.PlayingTrack
import com.ise.virtualjukebox.jukeboxApi.dataStructure.VoteTrack

class PlayHandler(mainInstance : MainHandler) {

    private var _mainHandler: MainHandler = mainInstance
    private var _previousState : MutableList<VoteTrack>? = null
    private var _currentTrack : PlayingTrack? = PlayingTrack()

    private fun compareItemWithoutVote(TrackA: VoteTrack, TrackB : VoteTrack) : Boolean {
        return TrackA.trackId == TrackB.trackId
    }

    private fun compareValues(ListA : MutableList<VoteTrack>?, ListB : MutableList<VoteTrack>?): Boolean{
        var retVal = true
        if(ListA == null || ListB == null || ListA.size != ListB.size){
            return false
        }
        ListA.forEachIndexed { i, value ->
            if (!compareItemWithoutVote(ListB[i], value))
            {
                retVal = false
            }
        }
        return retVal
    }

    fun playlistChanged() : MutableList<VoteTrack>?{
        if(_previousState != null){
            val buffer = _mainHandler.getTracks()
            if(buffer == _previousState){
                return null
            }
            return buffer
        }
        return _mainHandler.getTracks()
    }
    private fun compareCurrentSong(TrackA: PlayingTrack?, TrackB: PlayingTrack?) : Boolean{
        return TrackA?.trackId == TrackB?.trackId
    }
    fun currentSongChanged() : PlayingTrack?{
        val current = _mainHandler.currentTrack()
        if(!compareCurrentSong(current, _currentTrack)){
            return current
        }
        _currentTrack = current
        return null
    }
    fun songPaused() : Boolean?{
        val retVal = _mainHandler.currentTrack()
        if(retVal == null){
            return false
        }
        return retVal.playing
    }
    fun songResumed() : Boolean?{
        val retVal = _mainHandler.currentTrack()
        if(retVal == null){
            return false
        }
        return retVal.playing
    }

    fun vote(Song : VoteTrack) : Boolean{
        return _mainHandler.voteOnTrack(Song)
    }
}