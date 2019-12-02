package com.ise.virtualjukebox.jukeboxApi.dataStructure

class Queues {
    var current = PlayingTrack(false, 0)
    var normal_que = mutableListOf(VoteTrack(0, false))
    var admin_que = mutableListOf(Track())
}