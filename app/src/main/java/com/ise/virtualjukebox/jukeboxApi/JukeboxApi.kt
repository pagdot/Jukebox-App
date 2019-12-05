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
    private var jsonParser = JsonParser()
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
                try {
                    searchTracks.addAll(jsonParser.parseTrackListFromResponse(response))
                } catch (e : Exception) {

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

                try {
                    queues = jsonParser.parseQueuesFromResponse(response)
                } catch (e : java.lang.Exception) {

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