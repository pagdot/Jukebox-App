package com.ise.virtualjukebox.jukeboxApi.dataStructure

class Queues {
    var current : PlayingTrack? = null
    var normal_que : MutableList<VoteTrack>? = null
    var admin_que :MutableList<Track>? = null
}