package com.ise.virtualjukebox.jukeboxApi

import android.util.Log
import com.ise.virtualjukebox.jukeboxApi.httpApi.HttpApi
import com.ise.virtualjukebox.jukeboxApi.dataStructure.Queues
import com.ise.virtualjukebox.jukeboxApi.dataStructure.Track
import com.ise.virtualjukebox.jukeboxApi.dataStructure.VoteTrack
import com.ise.virtualjukebox.jukeboxApi.httpApi.RestClient

import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class JukeboxApi(hostName : String) {
    private var api = HttpApi(hostName)
    var queues = Queues()
    var searchTracks : MutableList<Track> = mutableListOf(Track())

    interface JukeboxApiCallback {

        /**
         * called when the server response was not 2xx or when an exception was thrown in the process
         * @param statusCode - contains the rest status code if the response was not OK (200). exception is then null
         * @param exception - contains the exception. statusCode is then null
         */
        fun onFailure(statusCode : String?, exception : IOException?)

        fun onSuccess()
    }

    fun getSessionID(nickname :String, cb : JukeboxApiCallback) {
        api.getSessionID(nickname, object : RestClient.HttpCallback {
            override fun onSuccess(response: Response) {
                val jsonDataString = response.body?.string()
                val jsonDataObject = JSONObject(jsonDataString)
                val tmp = jsonDataObject["session_id"].toString()
                api.sessionID = tmp
                Log.d("getSessionID success", ("SessionID: $tmp"))

                cb.onSuccess()
            }

            override fun onFailure(response: Response?, exception: IOException?) {
                if(exception == null) {
                    var jsonDataString = response?.body?.string()
                    Log.e(
                        "getSessionID code fail",
                        "Code: ${response?.code}; Message: ${response?.message.toString()}"
                    )
                }
                else
                    Log.e("getSessionID exception", "${exception.message}")

                cb.onFailure(response?.code.toString(), exception)
            }
        })
    }

   fun getTracks(searchPattern : String, maxEntries : Int, cb : JukeboxApiCallback) {
        api.getTracks(searchPattern, maxEntries, object : RestClient.HttpCallback {
            override fun onSuccess(response: Response) {
                searchTracks.clear()
                val jsonDataString = response.body?.string()
                val jsonDataObject = JSONObject(jsonDataString)
                val jsonDataArray = jsonDataObject.getJSONArray("tracks")
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
                            tmpTrack.addedBy = ""  //TODO:put it to queues since it is not used

                            searchTracks.add(tmpTrack)
                            Log.d("getTracks successful", searchTracks.toString())
                        }
                    } catch (e: JSONException) { // If there is an error then output this to the logs.
                        Log.e("getTracks JSON ex", "Invalid JSON Object.")
                    }
                }

                cb.onSuccess()
            }

            override fun onFailure(response: Response?, exception: IOException?) {
                if(exception == null)
                    Log.e("getTracks code fail", "Code: ${response?.code}; Message: ${response?.message.toString()}")
                else
                    Log.e("getTracks exception", "${exception.message}")

                cb.onFailure(response?.code.toString(), exception)
            }
        })
    }

    fun getCurrentQueues(cb : JukeboxApiCallback)  {
        api.getCurrentQueues(object : RestClient.HttpCallback {
            override fun onSuccess(response: Response) {
                queues.normalQueue.clear()
                queues.adminQueue.clear()

                val jsonDataString = response.body?.string()
                val jsonDataObject = JSONObject(jsonDataString)

                val jsonObj = jsonDataObject.getJSONObject("currently_playing")
                queues.current.trackId = jsonObj["track_id"].toString()
                queues.current.title = jsonObj["title"].toString()
                queues.current.album = jsonObj["album"].toString()
                queues.current.artist = jsonObj["artist"].toString()
                queues.current.duration = jsonObj["duration"].toString().toInt()
                queues.current.iconUri = jsonObj["icon_uri"].toString()
                queues.current.addedBy = jsonObj["added_by"].toString()
                queues.current.playing = jsonObj["playing"].toString().toBoolean()
                queues.current.playingFor = jsonObj["playing_for"].toString().toInt()

                var jsonDataArray = jsonDataObject.getJSONArray("normal_queue")
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

                            queues.normalQueue.add(tmpVoteTrack)
                            Log.d("getTracks successful", searchTracks.toString())
                        }
                    } catch (e: JSONException) { // If there is an error then output this to the logs.
                        Log.e("getTracks JSON ex", "Invalid JSON Object.")
                    }
                }
                jsonDataArray = jsonDataObject.getJSONArray("admin_queue")
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
                            tmpTrack.addedBy = jsonObj["added_by"].toString()

                            queues.adminQueue.add(tmpTrack)
                            Log.d("getTracks successful", searchTracks.toString())
                        }
                    } catch (e: JSONException) { // If there is an error then output this to the logs.
                        Log.e("getTracks JSON ex", "Invalid JSON Object.")
                    }
                }

                cb.onSuccess()
            }

            override fun onFailure(response: Response?, exception: IOException?) {
                if(exception == null)
                    Log.e("getCurQueues code fail", "Code: ${response?.code}; Message: ${response?.message.toString()}")
                else
                    Log.e("getCurQueues exception", "${exception.message}")

                cb.onFailure(response?.code.toString(), exception)
            }
        })
    }

    fun addTrackToQueue(trackID : String, cb : JukeboxApiCallback) {
        api.addTrackToQueue(trackID, object : RestClient.HttpCallback {
            override fun onSuccess(response: Response) {
                Log.d("addTrackToQ success", "empty response")

                cb.onSuccess()
            }

            override fun onFailure(response: Response?, exception: IOException?) {
                if(exception == null)
                    Log.e("addTrackToQ code fail", "Code: ${response?.code}; Message: ${response?.message.toString()}")
                else
                    Log.e("addTrackToQ exception", "${exception.message}")

                cb.onFailure(response?.code.toString(), exception)
            }
        })
    }

    fun voteTrack(trackID : String, vote : Int, cb : JukeboxApiCallback) {
        api.voteTrack(trackID, vote, object : RestClient.HttpCallback {
            override fun onSuccess(response: Response) {
                Log.d("addTrackToQ success", "empty response")

                cb.onSuccess()
            }

            override fun onFailure(response: Response?, exception: IOException?) {
                if(exception == null)
                    Log.e("voteTrack code fail", "Code: ${response?.code}; Message: ${response?.message.toString()}")
                else
                    Log.e("voteTrack exception", "${exception.message}")

                cb.onFailure(response?.code.toString(), exception)
            }
        })
    }
}