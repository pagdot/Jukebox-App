package com.ise.virtualjukebox.jukeboxApi.httpApi

import android.util.Log
import okhttp3.Response
import java.io.IOException

import org.json.JSONArray
import java.util.*
import kotlin.collections.HashMap

open class HttpApi(var serverName : String) : RestClient() {
    private var baseScheme : String = "http"
    private var baseUrlSegments = mutableListOf("api", "v1")
    private var restClient = RestClient()

    var sessionID : String? = "124234dgd"//null

    open fun getSessionID(nickname : String, cb : HttpCallback) {
        val segments = this.baseUrlSegments.toMutableList()
        segments.add("generateSession")

        val hashMap : HashMap<String, String> = HashMap()
        hashMap.put("nickname", nickname)

        restClient.quePostCall(this.baseScheme, this.serverName, segments, hashMap, cb)
    }

    fun getTracks(searchPattern : String, max_entries : Int, cb : HttpCallback) {
        val segments = this.baseUrlSegments.toMutableList()
        segments.add("querryTracks")

        val hashMap : HashMap<String, String> = HashMap()
        hashMap.put("session_id", sessionID.toString())
        hashMap.put("pattern", searchPattern)
        if(max_entries != 0)
            hashMap.put("max_entries", max_entries.toString())

        restClient.queueGetCall(this.baseScheme, this.serverName, segments, hashMap, cb)
    }

    fun getCurrentQueues(cb : HttpCallback) {
        val segments = this.baseUrlSegments.toMutableList()
        segments.add("getCurrentQueues")

        val hashMap : HashMap<String, String> = HashMap()
        hashMap.put("session_id", sessionID.toString())

        restClient.queueGetCall(this.baseScheme, this.serverName, segments, hashMap, cb)
    }

    //queue-Type is always set to normal since we don't support any admin feature
    fun addTrackToQueue(trackID : String, cb : HttpCallback) {
        val segments = this.baseUrlSegments.toMutableList()
        segments.add("addTrackToQueue")

        val hashMap : HashMap<String, String> = HashMap()
        hashMap.put("session_id", sessionID.toString())
        hashMap.put("track_id", trackID)
        hashMap.put("queue_type", "normal")

        restClient.quePostCall(this.baseScheme, this.serverName, segments, hashMap, cb)
    }

    //TODO:Check vote parameter it should not be a string.
    fun voteTrack(trackID : String, vote : Int, cb : HttpCallback) {
        val segments = this.baseUrlSegments.toMutableList()
        segments.add("voteTrack")

        val hashMap : HashMap<String, String> = HashMap()
        hashMap.put("session_id", sessionID.toString())
        hashMap.put("track_id", trackID)
        hashMap.put("vote", vote.toString())

        restClient.quePutCall(this.baseScheme, this.serverName, segments, hashMap, cb)
    }

    //"https://api.github.com/users/MADITT/repos"
    open fun getGithub(cb : HttpCallback) {
        val segments = mutableListOf("users", "MADITT", "repos")

        val hashMap : HashMap<String, String> = HashMap()
        hashMap.put("", "")

        restClient.queueGetCall("https", "api.github.com", segments, hashMap, cb)
    }
}