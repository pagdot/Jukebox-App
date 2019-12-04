package com.ise.virtualjukebox.jukeboxApi.dataStructure

class PlayingTrack: Track() {
    var playing : Boolean = false
    var playingFor : Int = 0
}