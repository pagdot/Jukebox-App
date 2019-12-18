package com.ise.virtualjukebox.jukeboxApi.httpApi

import kotlin.collections.HashMap

/**
 * Basic HTTP REST API
 *
 * @property serverName server address
 */
open class HttpApi(private var serverName : String) : RestClient() {
    private var baseScheme : String = "http"
    private var baseUrlSegments = mutableListOf("api", "v1")
    private var restClient = RestClient()

    var sessionID : String? = null

    /**
     * Close api on disconnect
     *
     */
    fun disconnectClient() {
        restClient.close()
    }

    /**
     * retrieve session ID from server
     *
     * @param nickname nickname to use
     * @param cb callback on completion
     */
    open fun getSessionID(nickname : String, cb : HttpCallback) {
        val segments = this.baseUrlSegments.toMutableList()
        segments.add("generateSession")

        val bodyPayload = "{\"nickname\": \"$nickname\"}"

        restClient.quePostCall(this.baseScheme, this.serverName, segments, null, bodyPayload, cb)
    }

    /**
     * search for tracks on server
     *
     * @param searchPattern pattern to search for
     * @param max_entries max count of returned entries
     * @param cb callback on completion
     */
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

    /**
     * retrieve current queues from server
     *
     * @param cb callback on completion
     */
    fun getCurrentQueues(cb : HttpCallback) {
        val segments = this.baseUrlSegments.toMutableList()
        segments.add("getCurrentQueues")

        val hashMap : HashMap<String, String> = HashMap()
        hashMap.put("session_id", sessionID.toString())

        restClient.queueGetCall(this.baseScheme, this.serverName, segments, hashMap, null, cb)
    }

    /**
     * Add track to normal queue
     *
     * @param trackID id of track
     * @param cb callback on completion
     */
    fun addTrackToQueue(trackID : String, cb : HttpCallback) {
        val segments = this.baseUrlSegments.toMutableList()
        segments.add("addTrackToQueue")

        //queue-Type is always set to normal since we don't support any admin feature
        val bodyPayload = "{\"session_id\": \"${this.sessionID}\", \"track_id\": \"$trackID\", \"queue_type\": \"normal\"}"

        restClient.quePostCall(this.baseScheme, this.serverName, segments, null, bodyPayload, cb)
    }

    /**
     * set/cancel vote and track
     *
     * @param trackID track to set/cancel vote
     * @param vote set/cancel vote
     * @param cb callback on completion
     */
    fun voteTrack(trackID : String, vote : Int, cb : HttpCallback) {
        val segments = this.baseUrlSegments.toMutableList()
        segments.add("voteTrack")

        val bodyPayload = "{\"session_id\": \"${this.sessionID}\", \"track_id\": \"$trackID\", \"vote\": $vote}"

        restClient.quePutCall(this.baseScheme, this.serverName, segments, null, bodyPayload, cb)
    }
}