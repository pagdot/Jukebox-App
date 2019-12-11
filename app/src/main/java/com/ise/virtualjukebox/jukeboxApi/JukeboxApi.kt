package com.ise.virtualjukebox.jukeboxApi

import android.util.Log
import com.ise.virtualjukebox.jukeboxApi.httpApi.HttpApi
import com.ise.virtualjukebox.jukeboxApi.dataStructure.Queues
import com.ise.virtualjukebox.jukeboxApi.dataStructure.Track
import com.ise.virtualjukebox.jukeboxApi.dataStructure.apiError
import com.ise.virtualjukebox.jukeboxApi.httpApi.RestClient

import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class JukeboxApi(hostName : String) {
    private var api = HttpApi(hostName)
    private var jsonParser = JsonParser()
    var queues = Queues()
    var searchTracks : MutableList<Track> = mutableListOf(Track())

    init {
        searchTracks.clear()
    }

    interface JukeboxApiCallback {

        /**
         * called when the server response was not 2xx or when an exception was thrown in the process
         * @param statusCode - contains the rest status code if the response was not OK (200). exception is then null
         * @param exception - contains the exception. statusCode is then null
         */
        fun onFailure(errorClass : apiError, exception : Exception?)

        fun onSuccess()
    }

    fun disconnectClient() {
        api.disconnectClient()
    }

    fun getSessionID(nickname :String, cb : JukeboxApiCallback) {
        api.getSessionID(nickname, object : RestClient.HttpCallback {
            override fun onSuccess(response: Response) {
                try {
                    val jsonDataString = response.body?.string()
                    val jsonDataObject = JSONObject(jsonDataString.toString())
                    val tmp = jsonDataObject["session_id"].toString()
                    api.sessionID = tmp
                    Log.d("getSessionID success", ("SessionID: $tmp"))
                } catch (e : Exception) {
                    cb.onFailure(apiError(null, null), e)
                }

                cb.onSuccess()
            }

            override fun onFailure(response: Response?, exception: IOException?) {
                val errorClass = apiError(null, null)
                if(exception == null) {
                    Log.e("getSessionID code fail", "Body: ${response?.body?.string()}")
                    val jsonDataString = response?.body?.string()
                    val jsonDataObject = JSONObject(jsonDataString.toString())
                    errorClass.code = jsonDataObject["status"].toString()
                    errorClass.message = jsonDataObject["error"].toString()
                }
                else
                    Log.e("getSessionID exception", "${exception.message}")

                cb.onFailure(errorClass, exception)
            }
        })
    }

   fun getTracks(searchPattern : String, maxEntries : Int, cb : JukeboxApiCallback) {
        api.getTracks(searchPattern, maxEntries, object : RestClient.HttpCallback {
            override fun onSuccess(response: Response) {
                searchTracks.clear()
                try {
                    val jsonDataString = response.body?.string()
                    searchTracks.addAll(jsonParser.parseTrackListFromResponse(jsonDataString))
                } catch (e : Exception) {
                    cb.onFailure(apiError(null, null), e)
                }

                cb.onSuccess()
            }

            override fun onFailure(response: Response?, exception: IOException?) {
                val errorClass = apiError(null, null)
                if(exception == null) {
                    Log.e("getTracks code fail", "Code: ${response?.code}; Message: ${response?.message.toString()}")
                    val jsonDataString = response?.body?.string()
                    val jsonDataObject = JSONObject(jsonDataString.toString())
                    errorClass.code = jsonDataObject["status"].toString()
                    errorClass.message = jsonDataObject["error"].toString()
                }
                else
                    Log.e("getTracks exception", "${exception.message}")

                cb.onFailure(errorClass, exception)
            }
        })
    }

    fun getCurrentQueues(cb : JukeboxApiCallback)  {
        api.getCurrentQueues(object : RestClient.HttpCallback {
            override fun onSuccess(response: Response) {

                try {
                    val jsonDataString = response.body?.string()
                    queues = jsonParser.parseQueuesFromResponse(jsonDataString)
                } catch (e : Exception) {
                    cb.onFailure(apiError(null, null), e)
                }

                cb.onSuccess()
            }

            override fun onFailure(response: Response?, exception: IOException?) {
                val errorClass = apiError(null, null)
                if(exception == null) {
                    Log.e("getCurQueues code fail", "Code: ${response?.code}; Message: ${response?.message.toString()}")
                    val jsonDataString = response?.body?.string()
                    val jsonDataObject = JSONObject(jsonDataString.toString())
                    errorClass.code = jsonDataObject["status"].toString()
                    errorClass.message = jsonDataObject["error"].toString()
                }
                else
                    Log.e("getCurQueues exception", "${exception.message}")

                cb.onFailure(errorClass, exception)
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
                val errorClass = apiError(null, null)
                if(exception == null) {
                    Log.e("addTrackToQ code fail", "Code: ${response?.code}; Message: ${response?.message.toString()}")
                    val jsonDataString = response?.body?.string()
                    val jsonDataObject = JSONObject(jsonDataString.toString())
                    errorClass.code = jsonDataObject["status"].toString()
                    errorClass.message = jsonDataObject["error"].toString()
                }
                else
                    Log.e("addTrackToQ exception", "${exception.message}")

                cb.onFailure(errorClass, exception)
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
                val errorClass = apiError(null, null)
                if(exception == null) {
                    Log.e("voteTrack code fail", "Code: ${response?.code}; Message: ${response?.message.toString()}")
                    val jsonDataString = response?.body?.string()
                    val jsonDataObject = JSONObject(jsonDataString.toString())
                    errorClass.code = jsonDataObject["status"].toString()
                    errorClass.message = jsonDataObject["error"].toString()
                }
                else
                    Log.e("voteTrack exception", "${exception.message}")

                cb.onFailure(errorClass, exception)
            }
        })
    }
}