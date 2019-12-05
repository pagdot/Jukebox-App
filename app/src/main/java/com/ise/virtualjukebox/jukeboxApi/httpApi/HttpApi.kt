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

        var bodyPayload = "{\"nickname\": \"$nickname\"}"

        restClient.quePostCall(this.baseScheme, this.serverName, segments, null, bodyPayload, cb)
    }

    fun getTracks(searchPattern : String, max_entries : Int, cb : HttpCallback) {
        val segments = this.baseUrlSegments.toMutableList()
        segments.add("queryTracks")

        val hashMap : HashMap<String, String> = HashMap()
        hashMap.put("session_id", sessionID.toString())
        hashMap.put("pattern", searchPattern)
        if(max_entries != 0)
            hashMap.put("max_entries", max_entries.toString())

        restClient.queueGetCall(this.baseScheme, this.serverName, segments, hashMap, null, cb)
    }

    fun getCurrentQueues(cb : HttpCallback) {
        val segments = this.baseUrlSegments.toMutableList()
        segments.add("getCurrentQueues")

        val hashMap : HashMap<String, String> = HashMap()
        hashMap.put("session_id", sessionID.toString())

        restClient.queueGetCall(this.baseScheme, this.serverName, segments, hashMap, null, cb)
    }

    //queue-Type is always set to normal since we don't support any admin feature
    fun addTrackToQueue(trackID : String, cb : HttpCallback) {
        val segments = this.baseUrlSegments.toMutableList()
        segments.add("addTrackToQueue")

        var bodyPayload = "{\"session_id\": \"${this.sessionID}\", \"track_id\": \"$trackID\", \"queue_type\": \"normal\"}"

        restClient.quePostCall(this.baseScheme, this.serverName, segments, null, bodyPayload, cb)
    }

    fun voteTrack(trackID : String, vote : Int, cb : HttpCallback) {
        val segments = this.baseUrlSegments.toMutableList()
        segments.add("voteTrack")

        var bodyPayload = "{\"session_id\": \"${this.sessionID}\", \"track_id\": \"$trackID\", \"vote\": $vote}"

        restClient.quePutCall(this.baseScheme, this.serverName, segments, null, bodyPayload, cb)
    }
}