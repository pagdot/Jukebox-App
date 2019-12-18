package com.ise.virtualjukebox.jukeboxApi.dataStructure

/**
 * current playing track
 *
 */
class PlayingTrack: Track() {
    var playing : Boolean = false
    var playingFor : Int = 0
}