package com.ise.virtualjukebox.jukeboxApi

import android.util.Log
import com.ise.virtualjukebox.jukeboxApi.httpApi.HttpApi
import com.ise.virtualjukebox.jukeboxApi.dataStructure.Queues
import com.ise.virtualjukebox.jukeboxApi.dataStructure.Track
import com.ise.virtualjukebox.jukeboxApi.httpApi.RestClient

import okhttp3.Response
import org.json.JSONArray
import org.json.JSONException
import java.io.IOException

class JukeboxApi(hostName : String) {
    private var api = HttpApi(hostName)
    var queues = Queues()
    var searchTracks : MutableList<Track>? = null

    fun getSessionID(nickname :String) {
        api.getSessionID(nickname, object : RestClient.HttpCallback {
            override fun onSuccess(response: Response) {
                val jsonDataString = response.body?.string()
                val jsonDataArray = JSONArray(jsonDataString)
                val tmp = jsonDataArray.getJSONObject(0)["session_id"].toString()
                api.sessionID = tmp
                Log.d("getSessionID success", ("SessionID: $tmp"))
            }

            override fun onFailure(response: Response?, exception: IOException?) {
                if(exception == null)
                    Log.e("getSessionID code fail", "Code: ${response?.code}; Message: ${response?.message.toString()}")
                else
                    Log.e("getSessionID exception", "${exception.message}")
            }
        })
    }

   fun getTracks(searchPattern : String, maxEntries : Int) {
        api.getTracks(searchPattern, maxEntries, object : RestClient.HttpCallback {
            override fun onSuccess(response: Response) {
                searchTracks?.clear()
                val jsonDataString = response.body?.string()
                val jsonDataArray = JSONArray(jsonDataString)
                for (i in 0 until jsonDataArray.length()) {
                    try {
                        val tmpTrack = Track()
                        val jsonObj = jsonDataArray.getJSONObject(i)
                        if(jsonObj != null) {
                            tmpTrack.trackId = jsonObj["track_id"].toString().toInt()
                            tmpTrack.title = jsonObj["title"].toString()
                            tmpTrack.album = jsonObj["album"].toString()
                            tmpTrack.artist = jsonObj["artist"].toString()
                            tmpTrack.duration = jsonObj["duration"].toString().toInt()
                            tmpTrack.icon_uri = jsonObj["icon_uri"].toString()

                            searchTracks?.add(tmpTrack)
                            Log.d("getTracks successful", searchTracks.toString())
                        }
                    } catch (e: JSONException) { // If there is an error then output this to the logs.
                        Log.e("getTracks JSON ex", "Invalid JSON Object.")
                    }
                }
            }

            override fun onFailure(response: Response?, exception: IOException?) {
                if(exception == null)
                    Log.e("getTracks code fail", "Code: ${response?.code}; Message: ${response?.message.toString()}")
                else
                    Log.e("getTracks exception", "${exception.message}")
            }
        })
    }

    fun getCurrentQueues()  {
        api.getCurrentQueues(object : RestClient.HttpCallback {
            override fun onSuccess(response: Response) {
                val jsonDataString = response.body?.string()
                val jsonDataArray = JSONArray(jsonDataString)
                val tmp = jsonDataArray.getJSONObject(0)["name"].toString()

                

                Log.d("getCurQueues success", ("SessionID: $tmp"))
            }

            override fun onFailure(response: Response?, exception: IOException?) {
                if(exception == null)
                    Log.e("getCurQueues code fail", "Code: ${response?.code}; Message: ${response?.message.toString()}")
                else
                    Log.e("getCurQueues exception", "${exception.message}")
            }
        })
    }

    fun addTrackToQueue(trackID : String) {
        api.addTrackToQueue(trackID, object : RestClient.HttpCallback {
            override fun onSuccess(response: Response) {
                Log.d("addTrackToQ success", "empty response")
            }

            override fun onFailure(response: Response?, exception: IOException?) {
                if(exception == null)
                    Log.e("addTrackToQ code fail", "Code: ${response?.code}; Message: ${response?.message.toString()}")
                else
                    Log.e("addTrackToQ exception", "${exception.message}")
            }
        })
    }

    fun voteTrack(trackID : String, vote : Int) {
        api.voteTrack(trackID, vote, object : RestClient.HttpCallback {
            override fun onSuccess(response: Response) {
                Log.d("addTrackToQ success", "empty response")
            }

            override fun onFailure(response: Response?, exception: IOException?) {
                if(exception == null)
                    Log.e("voteTrack code fail", "Code: ${response?.code}; Message: ${response?.message.toString()}")
                else
                    Log.e("voteTrack exception", "${exception.message}")
            }
        })
    }

    fun getGithub() {
        api.getGithub(object : RestClient.HttpCallback {
            override fun onSuccess(response: Response) {
                val jsonDataString = response.body?.string()
                val jsonDataArray = JSONArray(jsonDataString)
                val tmp = jsonDataArray.getJSONObject(0)["name"].toString()

                Log.d("getGithub success", ("Repo Name: $tmp"))
            }

            override fun onFailure(response: Response?, exception: IOException?) {
                if(exception == null)
                    Log.e("getGithub code fail", "Code: ${response?.code}; Message: ${response?.message.toString()}")
                else
                    Log.e("getGithub exception", "${exception.message}")
            }
        })
    }
}