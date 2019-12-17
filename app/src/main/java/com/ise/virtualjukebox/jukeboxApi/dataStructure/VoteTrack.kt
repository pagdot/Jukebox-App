package com.ise.virtualjukebox.jukeboxApi.dataStructure

/**
 * Track with votes
 *
 */
class VoteTrack  : Track() {
    var votes : Int = 0
    var hasVoted : Int = 0
}