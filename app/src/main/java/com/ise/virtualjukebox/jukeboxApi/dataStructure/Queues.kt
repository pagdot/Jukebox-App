package com.ise.virtualjukebox.jukeboxApi.dataStructure

/**
 * current queues
 *
 */
class Queues {
    var current : PlayingTrack? = PlayingTrack()
    var normalQueue : MutableList<VoteTrack> = mutableListOf(VoteTrack())
    var adminQueue : MutableList<Track> = mutableListOf(Track())

    constructor() {
        normalQueue.clear()
        adminQueue.clear()
        current = null
    }
}