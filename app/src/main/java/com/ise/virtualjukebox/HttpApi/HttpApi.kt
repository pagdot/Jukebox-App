package com.ise.virtualjukebox.httpApi

import android.util.Log
import okhttp3.Response
import java.io.IOException

import org.json.JSONArray
import java.util.*
import kotlin.collections.HashMap

class HttpApi(var serverName : String) {
    private var baseScheme : String = "http"
    private var baseUrlSegments = mutableListOf("api", "v1")
    private var restClient = RestClient()

    var sessionID : String? = "124234dgd"//null

    fun getSessionID(nickname : String) {
        val segments = this.baseUrlSegments.toMutableList()
        segments.add("generateSession")

        val hashMap : HashMap<String, String> = HashMap()
        hashMap.put("nickname", nickname)

        restClient.quePostCall(this.baseScheme, this.serverName, segments, hashMap, object : RestClient.HttpCallback {
            override fun onSuccess(response: Response) {
                val jsonDataString = response.body?.string()
                val jsonDataArray = JSONArray(jsonDataString)

                sessionID = jsonDataArray.getJSONObject(0)["session_id"].toString()
            }

            override fun onFailure(response: Response?, exception: IOException?) {
                Log.e("test", "test_failure")
            }
        })
    }

    fun getTracks(searchPattern : String, max_entries : Int) {
        val segments = this.baseUrlSegments.toMutableList()
        segments.add("querryTracks")

        val hashMap : HashMap<String, String> = HashMap()
        hashMap.put("session_id", sessionID.toString())
        hashMap.put("pattern", searchPattern)
        if(max_entries != 0)
            hashMap.put("max_entries", max_entries.toString())

        restClient.queueGetCall(this.baseScheme, this.serverName, segments, hashMap, object : RestClient.HttpCallback {
            override fun onSuccess(response: Response) {
                val jsonDataString = response.body?.string()
                val jsonDataArray = JSONArray(jsonDataString)

                sessionID = jsonDataArray.getJSONObject(0)["session_id"].toString()
            }

            override fun onFailure(response: Response?, exception: IOException?) {
                Log.e("test", "test_failure")
            }
        })
    }
//"https://api.github.com/users/MADITT/repos"
    fun getGithub() {
        val segments = mutableListOf("users", "MADITT", "repos")

        val hashMap : HashMap<String, String> = HashMap()
        hashMap.put("", "")

        restClient.queueGetCall("https", "api.github.com", segments, hashMap, object : RestClient.HttpCallback {
            override fun onSuccess(response: Response) {
                val jsonDataString = response.body?.string()
                val jsonDataArray = JSONArray(jsonDataString)
            }

            override fun onFailure(response: Response?, exception: IOException?) {
                Log.e("test", "test_failure")
            }
        })
    }
}