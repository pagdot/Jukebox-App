package com.ise.virtualjukebox.httpApi

import android.util.Log
import okhttp3.Response
import java.io.IOException

import org.json.JSONArray

class HttpApi(var serverName : String) {
    private var baseUrl : String = "/api/v1/"
    private var sessionID : String? = null
    private var restClient = RestClient()

    private fun getSessionID(nickname : String) {
        val hashMap : HashMap<String, String> = HashMap()
        hashMap.put("nickname", nickname)

        restClient.quePostCall(this.serverName + this.baseUrl + "generateSession", hashMap, object : RestClient.HttpCallback {
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

    fun getGithub() {
        restClient.queueGetCall("https://api.github.com/users/MADITT/repos", object : RestClient.HttpCallback {
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