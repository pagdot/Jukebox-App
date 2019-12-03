package com.ise.virtualjukebox.jukeboxApi.dataStructure

class Queues {
    var current : PlayingTrack = PlayingTrack()
    var normal_que : MutableList<VoteTrack> = mutableListOf(VoteTrack())
    var admin_que :MutableList<Track> = mutableListOf(Track())
}