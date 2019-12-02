package com.ise.virtualjukebox.jukeboxApi

import android.util.Log
import com.ise.virtualjukebox.jukeboxApi.httpApi.HttpApi
import com.ise.virtualjukebox.jukeboxApi.dataStructure.Queues
import com.ise.virtualjukebox.jukeboxApi.httpApi.RestClient

import okhttp3.Response
import org.json.JSONArray
import java.io.IOException

class JukeboxApi(var hostName : String) {
    private var api = HttpApi(hostName)
    var queues = Queues()

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
                val jsonDataString = response.body?.string()
                val jsonDataArray = JSONArray(jsonDataString)
                var tmp = jsonDataArray.getJSONObject(0)["name"].toString()

                Log.d("getTracks success", ("SessionID: $tmp"))
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
                var tmp = jsonDataArray.getJSONObject(0)["name"].toString()

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
                val jsonDataString = response.body?.string()
                val jsonDataArray = JSONArray(jsonDataString)
                var tmp = jsonDataArray.getJSONObject(0)["name"].toString()

                Log.d("addTrackToQ success", ("SessionID: $tmp"))
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
                val jsonDataString = response.body?.string()
                val jsonDataArray = JSONArray(jsonDataString)
                var tmp = jsonDataArray.getJSONObject(0)["name"].toString()

                Log.d("voteTrack success", ("SessionID: $tmp"))
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
                var tmp = jsonDataArray.getJSONObject(0)["name"].toString()

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