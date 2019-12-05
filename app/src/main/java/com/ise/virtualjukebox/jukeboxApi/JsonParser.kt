package com.ise.virtualjukebox.jukeboxApi

import android.util.Log
import com.ise.virtualjukebox.jukeboxApi.dataStructure.PlayingTrack
import com.ise.virtualjukebox.jukeboxApi.dataStructure.Queues
import com.ise.virtualjukebox.jukeboxApi.dataStructure.Track
import com.ise.virtualjukebox.jukeboxApi.dataStructure.VoteTrack
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class JsonParser {
    private fun parsePlayingTrackFromJsonObject(jsonObj : JSONObject) : PlayingTrack {
        var tmpPlayingTrack = PlayingTrack()

        try {
            tmpPlayingTrack.trackId = jsonObj["track_id"].toString()
            tmpPlayingTrack.title = jsonObj["title"].toString()
            tmpPlayingTrack.album = jsonObj["album"].toString()
            tmpPlayingTrack.artist = jsonObj["artist"].toString()
            tmpPlayingTrack.duration = jsonObj["duration"].toString().toInt()
            tmpPlayingTrack.iconUri = jsonObj["icon_uri"].toString()
            tmpPlayingTrack.addedBy = jsonObj["added_by"].toString()
            tmpPlayingTrack.playing = jsonObj["playing"].toString().toBoolean()
            tmpPlayingTrack.playingFor = jsonObj["playing_for"].toString().toInt()

            Log.d("getQueues successful", tmpPlayingTrack.toString())
        } catch (e : Exception) {
            Log.e("getQueues JSON ex", "Invalid JSON Object: $jsonObj")
            throw e
        }

        return tmpPlayingTrack
    }

    private fun parseTrackListFromJsonArray(jsonDataArray : JSONArray) : MutableList<Track>{
        var tmpList : MutableList<Track> = mutableListOf(Track())
        tmpList.clear()

        for (i in 0 until jsonDataArray.length()) {
            try {
                val tmpTrack = Track()
                val jsonObj = jsonDataArray.getJSONObject(i)
                if(jsonObj != null) {
                    tmpTrack.trackId = jsonObj["track_id"].toString()
                    tmpTrack.title = jsonObj["title"].toString()
                    tmpTrack.album = jsonObj["album"].toString()
                    tmpTrack.artist = jsonObj["artist"].toString()
                    tmpTrack.duration = jsonObj["duration"].toString().toInt()
                    tmpTrack.iconUri = jsonObj["icon_uri"].toString()
                    tmpTrack.addedBy = ""
                    if(jsonObj["added_by"] != null) {
                        tmpTrack.addedBy = jsonObj["added_by"].toString()
                    }

                    tmpList.add(tmpTrack)
                    Log.d("getTracks successful", tmpList.toString())
                }
            } catch (e: JSONException) { // If there is an error then output this to the logs.
                Log.e("getTracks JSON ex", "Invalid JSON Object: $jsonDataArray")
                throw e
            }
        }
        return tmpList
    }

    private fun parseVoteTrackListFromJsonArray (jsonDataArray : JSONArray) :  MutableList<VoteTrack> {
        val tmpList : MutableList<VoteTrack> = mutableListOf(VoteTrack())
        tmpList.clear()

        for (i in 0 until jsonDataArray.length()) {
            try {
                val tmpVoteTrack = VoteTrack()
                val jsonObj = jsonDataArray.getJSONObject(i)
                if(jsonObj != null) {
                    tmpVoteTrack.trackId = jsonObj["track_id"].toString()
                    tmpVoteTrack.title = jsonObj["title"].toString()
                    tmpVoteTrack.album = jsonObj["album"].toString()
                    tmpVoteTrack.artist = jsonObj["artist"].toString()
                    tmpVoteTrack.duration = jsonObj["duration"].toString().toInt()
                    tmpVoteTrack.iconUri = jsonObj["icon_uri"].toString()
                    tmpVoteTrack.addedBy = jsonObj["added_by"].toString()
                    tmpVoteTrack.votes = jsonObj["votes"].toString().toInt()
                    tmpVoteTrack.hasVoted = jsonObj["current_vote"].toString().toBoolean()

                    tmpList.add(tmpVoteTrack)
                    Log.d("getQueues successful", tmpVoteTrack.toString())
                }
            } catch (e: JSONException) { // If there is an error then output this to the logs.
                Log.e("getQueues JSON ex", "Invalid JSON Object: $jsonDataArray")
                throw e
            }
        }

        return tmpList
    }

    fun parseQueuesFromResponse (response : Response) : Queues {
        if(response == null)
            throw Exception("Response is NULL")

        var tmpQueues = Queues()
        tmpQueues.adminQueue.clear()
        tmpQueues.normalQueue.clear()

        var jsonDataString : String?
        var jsonDataObject : JSONObject
        try {
            jsonDataString = response.body?.string()
            jsonDataObject = JSONObject(jsonDataString)
        } catch (e : JSONException) {
            Log.e("getQueues response ex", "Invalid response: $response")
            throw e
        }

        try {
            val jsonObj = jsonDataObject.getJSONObject("currently_playing")
            tmpQueues.current = parsePlayingTrackFromJsonObject(jsonObj)
        } catch (e : JSONException) {
            throw e
        }

        try {
            val jsonDataArray = jsonDataObject.getJSONArray("normal_queue")
            tmpQueues.normalQueue.addAll(parseVoteTrackListFromJsonArray(jsonDataArray))
        } catch (e : JSONException) {
            throw e
        }

        try {
            val jsonDataArray = jsonDataObject.getJSONArray("admin_queue")
            tmpQueues.adminQueue.addAll(parseTrackListFromJsonArray(jsonDataArray))
        } catch (e : JSONException) {
            throw e
        }

        return tmpQueues
    }

    fun parseTrackListFromResponse (response : Response) : MutableList<Track> {
        if(response == null)
            throw Exception("Response is NULL")

        var tmpList : MutableList<Track> = mutableListOf(Track())
        tmpList.clear()

        var jsonDataString : String?
        var jsonDataObject : JSONObject
        var jsonDataArray : JSONArray

        try {
            jsonDataString = response.body?.string()
            jsonDataObject = JSONObject(jsonDataString)
            jsonDataArray = jsonDataObject.getJSONArray("tracks")
        } catch (e : Exception) {
            Log.e("getTracks response ex", "Invalid response: $response")
            throw e
        }

        try {
            tmpList.addAll(parseTrackListFromJsonArray(jsonDataArray))
        } catch (e : JSONException) {
            throw e
        }
        
        return tmpList
    }
}