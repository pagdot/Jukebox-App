package com.ise.virtualjukebox.HttpApi

import android.util.Log
import okhttp3.Response
import org.json.JSONArray
import java.io.IOException

class HttpApi {
    private var baseUrl : String = "/api/v1/"
    private var sessionID = null
    private var restClient = RestClient()

    private fun getSessionID() {
        //restClient.queueGetCall()
    }

    fun getGithub() {
        restClient.queueGetCall("https://api.github.com/users/MADITT/repos", object : RestClient.HttpCallback {
            override fun onSuccess(response: Response) {
                Log.e("Volley", "Invalid JSON Object.")
                val jsonDataString = response.body?.string()
                val jsonDataArray = JSONArray(jsonDataString)
            }

            override fun onFailure(response: Response?, exception: IOException?) {
                Log.e("Volley", "Invalid JSON Object.")
            }
        })
    }
}