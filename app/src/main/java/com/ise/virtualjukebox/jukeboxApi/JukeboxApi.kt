package com.ise.virtualjukebox.jukeboxApi

import com.ise.virtualjukebox.jukeboxApi.httpApi.HttpApi
import com.ise.virtualjukebox.jukeboxApi.dataStructure.Queues

class JukeboxApi(var serverName : String) {
    private var api =
        HttpApi(serverName)
    var queues = Queues()

    fun getSessionID(nickname :String) {
        api.getSessionID(nickname)
    }

    fun getTracks(searchPattern : String, maxEntries : Int) {
        api.getTracks(searchPattern, maxEntries)
    }

    fun getCurrentQueues()  {
        api.getCurrentQueues()
    }

    fun addTrackToQueue(trackID : String) {
        api.addTrackToQueue(trackID)
    }

    fun voteTrack(trackID : String, vote : Int) {
        api.voteTrack(trackID, vote)
    }

    fun getGithub() {
        api.getGithub()
    }
}