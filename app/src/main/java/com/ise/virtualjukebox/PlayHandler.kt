/**
 * Management Class to provide necessary functionality to the Playlist/Play Screen
 * @author Tobias Egger
 * @version 1.0
 */


package com.ise.virtualjukebox

import com.ise.virtualjukebox.jukeboxApi.dataStructure.PlayingTrack
import com.ise.virtualjukebox.jukeboxApi.dataStructure.VoteTrack
import kotlin.reflect.full.memberProperties

class PlayHandler(mainInstance : MainHandler) {

    private var _mainHandler: MainHandler = mainInstance
    private var _previousState: MutableList<VoteTrack>? = null
    private var _currentTrack: PlayingTrack? = PlayingTrack()

    /**
     * A Simple Predicate to compare items. Deprecated
     * @param TrackA Item to compare
     * @param TrackB item to compare
     * @return whether or not their unique trackId matches
     */
    private fun compareItemWithoutVote(TrackA: VoteTrack, TrackB: VoteTrack): Boolean {
        return TrackA.trackId == TrackB.trackId
    }

    /**
     * A Predicate to compare Tracks in accordance with their votes and trackid
     * @param TrackA Item to compare
     * @param TrackB item to compare
     * @return whether or not their unique trackId, or votes or hasVoted Flag doesnt Match
     */
    private fun compareItemWithVote(TrackA: VoteTrack, TrackB: VoteTrack): Boolean {
        if(TrackA.trackId != TrackB.trackId || TrackA.votes != TrackB.votes || TrackA.hasVoted != TrackB.hasVoted)
            return false
        return true
    }
    /**
     * Compares Values of two TrackLists
     * @param ListA List to compare
     * @param ListB List to compare
     * @return whether or not they are equal
     */
    private fun compareValues(
        ListA: MutableList<VoteTrack>?,
        ListB: MutableList<VoteTrack>?
    ): Boolean {
        var retVal = true
        if (ListA == null || ListB == null || ListA.size != ListB.size) {
            return false
        }
        ListA.forEachIndexed { i, value ->
            if (!compareItemWithVote(ListB[i], value)) {
                retVal = false
            }
        }
        return retVal
    }
    /**
     * Copies a mutable List of Type VoteTrack
     * @param ListToCpy Item to copy
     * @return Copied List on Success, null on Failure
     */
    fun copyList(ListToCpy: MutableList<VoteTrack>?): MutableList<VoteTrack>? {
        var newList = mutableListOf<VoteTrack>();
        if (ListToCpy != null) {
            for (item in ListToCpy) {
                newList.add(item);
            }
            return newList;
        }
        return null;
    }

    /**
     * Detects whether or not the Playlist has changed
     * @return returns null on no change, return new Playlist on change
     */
    fun playlistChanged(): MutableList<VoteTrack>? {
        val buffer = _mainHandler.getTracks()
        if (_previousState == null || !compareValues(_previousState, buffer)) {
            _previousState?.clear()
            _previousState = copyList(buffer)
            return buffer
        }
        return null
    }

    /**
     * Predicate to detect whether or not the current song has changed
     * @param TrackA Item to compare
     * @param TrackB item to compare
     * @return true when old and new song are equal, else false
     */
    private fun compareCurrentSong(TrackA: PlayingTrack?, TrackB: PlayingTrack?): Boolean {
        //return TrackA?.trackId == TrackB?.trackId
        val retVal = true
        if (TrackA == null || TrackB == null ) {
            return false
        }
        if(TrackA.trackId != TrackB.trackId || TrackA.playing != TrackB.playing || TrackA.playingFor != TrackB.playingFor) {
            return false
        }
        return retVal
    }
    /**
     * Detects a change in the current song
     * @return null on no change, new current song on change
     */
    fun currentSongChanged(): PlayingTrack? {
        val current = _mainHandler.currentTrack()
        if (!compareCurrentSong(current, _currentTrack)) {
            _currentTrack = current
            return current
        }
        return null
    }

    /**
     * Returns whether or not a Song is paused
     * @return true on pause, else false
     */
    fun songPaused(): Boolean? {
        val retVal = _mainHandler.currentTrack()
        if (retVal == null) {
            return false
        }
        return retVal.playing
    }

    /**
     * Returns whether or not a Song is resumed
     * @return true on song playing, else false
     */
    fun songResumed(): Boolean? {
        val retVal = _mainHandler.currentTrack()
        if (retVal == null) {
            return false
        }
        return retVal.playing
    }

    /**
     * Provides Functionality to upvote on a Song
     * @param Song Item to Vote on
     * @return true on success, false on failure
     */
    fun vote(Song: VoteTrack): Boolean {
        return _mainHandler.voteOnTrack(Song, 1)
    }

    /**
     * Provides Functionality to remove own vote on a Song
     * @param Song Item to Vote on
     * @return true on success, false on failure
     */
    fun unvote(Song: VoteTrack): Boolean {
        return _mainHandler.voteOnTrack(Song, 0)
    }

    /**
     * refreshes Playlist
     */
    fun refresh() {
        _mainHandler.refreshTracks()
    }

    /**
     * Updates Previous Playlist and Current track
     */
    fun updatePreviousPlaylistAndCurrentTrack() {
        _previousState?.clear()
        _previousState = copyList(_mainHandler.trackList)
        _currentTrack = _mainHandler.currentTrack()
    }

    /**
     * Returns the Current Playlist
     * @return current Playlist
     */
    fun getPlaylist(): MutableList<VoteTrack>? {
        return _previousState
    }

    /**
     * Returns the Current Song
     * @return current Song
     */
    fun getCurrentTrack(): PlayingTrack? {
        return _currentTrack
    }
}