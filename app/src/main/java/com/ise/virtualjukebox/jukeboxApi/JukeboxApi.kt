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
                var test = jsonDataArray.getJSONObject(0)["name"].toString()
                Log.e("test", "test_failure")
            }

            override fun onFailure(response: Response?, exception: IOException?) {
                Log.e("test", "test_failure")
            }
        })
    }

   fun getTracks(searchPattern : String, maxEntries : Int) {
        api.getTracks(searchPattern, maxEntries, object : RestClient.HttpCallback {
            override fun onSuccess(response: Response) {
                val jsonDataString = response.body?.string()
                val jsonDataArray = JSONArray(jsonDataString)
                var test = jsonDataArray.getJSONObject(0)["name"].toString()
                Log.e("test", "test_failure")
            }

            override fun onFailure(response: Response?, exception: IOException?) {
                Log.e("test", "test_failure")
            }
        })
    }

    fun getCurrentQueues()  {
        api.getCurrentQueues(object : RestClient.HttpCallback {
            override fun onSuccess(response: Response) {
                val jsonDataString = response.body?.string()
                val jsonDataArray = JSONArray(jsonDataString)
                var test = jsonDataArray.getJSONObject(0)["name"].toString()
                Log.e("test", "test_failure")
            }

            override fun onFailure(response: Response?, exception: IOException?) {
                Log.e("test", "test_failure")
            }
        })
    }

    fun addTrackToQueue(trackID : String) {
        api.addTrackToQueue(trackID, object : RestClient.HttpCallback {
            override fun onSuccess(response: Response) {
                val jsonDataString = response.body?.string()
                val jsonDataArray = JSONArray(jsonDataString)
                var test = jsonDataArray.getJSONObject(0)["name"].toString()
                Log.e("test", "test_failure")
            }

            override fun onFailure(response: Response?, exception: IOException?) {
                Log.e("test", "test_failure")
            }
        })
    }

    fun voteTrack(trackID : String, vote : Int) {
        api.voteTrack(trackID, vote, object : RestClient.HttpCallback {
            override fun onSuccess(response: Response) {
                val jsonDataString = response.body?.string()
                val jsonDataArray = JSONArray(jsonDataString)
                var test = jsonDataArray.getJSONObject(0)["name"].toString()
                Log.e("test", "test_failure")
            }

            override fun onFailure(response: Response?, exception: IOException?) {
                Log.e("test", "test_failure")
            }
        })
    }

    fun getGithub() {
        api.getGithub(object : RestClient.HttpCallback {
            override fun onSuccess(response: Response) {
                val jsonDataString = response.body?.string()
                val jsonDataArray = JSONArray(jsonDataString)
                var test = jsonDataArray.getJSONObject(0)["name"].toString()
                Log.e("test", "test_failure")
            }

            override fun onFailure(response: Response?, exception: IOException?) {
                Log.e("test", "test_failure")
            }
        })
    }
}