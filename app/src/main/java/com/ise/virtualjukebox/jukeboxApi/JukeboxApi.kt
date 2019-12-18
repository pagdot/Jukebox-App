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

/**
 * Jukebox API class
 *
 * @constructor
 *
 * @param hostName server address
 */
class JukeboxApi(hostName : String) {
    private var api = HttpApi(hostName)
    private var jsonParser = JsonParser()
    var queues = Queues()
    var searchTracks : MutableList<Track> = mutableListOf(Track())
    var getQueuesBlcoked = false

    init {
        searchTracks.clear()
    }

    /**
     * API callback declaration
     *
     */
    interface JukeboxApiCallback {

        /**
         * called when the server response was not 2xx or when an exception was thrown in the process
         * @param statusCode - contains the rest status code if the response was not OK (200). exception is then null
         * @param exception - contains the exception. statusCode is then null
         */
        fun onFailure(errorClass : apiError, exception : Exception?)

        /**
         * Called on successfull response
         *
         */
        fun onSuccess()
    }

    /**
     * Disconnect
     *
     */
    fun disconnectClient() {
        api.disconnectClient()
    }

    /**
     * Retrieve session ID
     *
     * @param nickname User nickname
     * @param cb callback on completion
     */
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
                    val jsonDataString = response?.body?.string()
                    val jsonDataObject = JSONObject(jsonDataString.toString())
                    errorClass.code = jsonDataObject["status"].toString()
                    errorClass.message = jsonDataObject["error"].toString()
                    Log.e("addTrackToQ code fail", "Code: ${errorClass.code}; Message: ${errorClass.message}")
                }
                else
                    Log.e("getSessionID exception", "${exception.message}")

                cb.onFailure(errorClass, exception)
            }
        })
    }

    /**
     * search for tracks on server
     *
     * @param searchPattern pattern to search for
     * @param maxEntries max entries in search
     * @param cb callback on completion
     */
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
                    val jsonDataString = response?.body?.string()
                    val jsonDataObject = JSONObject(jsonDataString.toString())
                    errorClass.code = jsonDataObject["status"].toString()
                    errorClass.message = jsonDataObject["error"].toString()
                    Log.e("addTrackToQ code fail", "Code: ${errorClass.code}; Message: ${errorClass.message}")
                }
                else
                    Log.e("getTracks exception", "${exception.message}")

                cb.onFailure(errorClass, exception)
            }
        })
    }

    /**
     * retrieve current queues from server
     *
     * @param cb
     */
    fun getCurrentQueues(cb : JukeboxApiCallback)  {
        if(getQueuesBlcoked) {
            cb.onFailure(apiError(null, null), null)
        }
        else {
            getQueuesBlcoked = true
            api.getCurrentQueues(object : RestClient.HttpCallback {
                override fun onSuccess(response: Response) {

                    try {
                        val jsonDataString = response.body?.string()
                        queues = jsonParser.parseQueuesFromResponse(jsonDataString)
                    } catch (e : Exception) {
                        cb.onFailure(apiError(null, null), e)
                    }

                    getQueuesBlcoked = false
                    cb.onSuccess()
                }

                override fun onFailure(response: Response?, exception: IOException?) {
                    val errorClass = apiError(null, null)
                    if(exception == null) {
                        val jsonDataString = response?.body?.string()
                        val jsonDataObject = JSONObject(jsonDataString.toString())
                        errorClass.code = jsonDataObject["status"].toString()
                        errorClass.message = jsonDataObject["error"].toString()
                        Log.e("addTrackToQ code fail", "Code: ${errorClass.code}; Message: ${errorClass.message}")
                    }
                    else
                        Log.e("getCurQueues exception", "${exception.message}")

                    getQueuesBlcoked = false
                    cb.onFailure(errorClass, exception)
                }
            })
        }
    }

    /**
     * Add track to normal queue
     *
     * @param trackID id of track
     * @param cb callback on completion
     */
    fun addTrackToQueue(trackID : String, cb : JukeboxApiCallback) {
        api.addTrackToQueue(trackID, object : RestClient.HttpCallback {
            override fun onSuccess(response: Response) {
                Log.d("addTrackToQ success", "empty response")

                cb.onSuccess()
            }

            override fun onFailure(response: Response?, exception: IOException?) {
                val errorClass = apiError(null, null)
                if(exception == null) {
                    val jsonDataString = response?.body?.string()
                    val jsonDataObject = JSONObject(jsonDataString.toString())
                    errorClass.code = jsonDataObject["status"].toString()
                    errorClass.message = jsonDataObject["error"].toString()
                    Log.e("addTrackToQ code fail", "Code: ${errorClass.code}; Message: ${errorClass.message}")
                }
                else
                    Log.e("addTrackToQ exception", "${exception.message}")

                cb.onFailure(errorClass, exception)
            }
        })
    }

    /**
     * Set/cancel vote on track
     *
     * @param trackID id of track
     * @param vote Set/cancel vote
     * @param cb callback on completion
     */
    fun voteTrack(trackID : String, vote : Int, cb : JukeboxApiCallback) {
        api.voteTrack(trackID, vote, object : RestClient.HttpCallback {
            override fun onSuccess(response: Response) {
                Log.d("addTrackToQ success", "empty response")

                cb.onSuccess()
            }

            override fun onFailure(response: Response?, exception: IOException?) {
                val errorClass = apiError(null, null)
                if(exception == null) {
                    val jsonDataString = response?.body?.string()
                    val jsonDataObject = JSONObject(jsonDataString.toString())
                    errorClass.code = jsonDataObject["status"].toString()
                    errorClass.message = jsonDataObject["error"].toString()
                    Log.e("addTrackToQ code fail", "Code: ${errorClass.code}; Message: ${errorClass.message}")
                }
                else
                    Log.e("voteTrack exception", "${exception.message}")

                cb.onFailure(errorClass, exception)
            }
        })
    }
}