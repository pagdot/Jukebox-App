package com.ise.virtualjukebox.httpApi

import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.Call
import okhttp3.FormBody

import okhttp3.RequestBody.Companion.toRequestBody

import java.io.IOException
import android.util.Log

class RestClient {

    private var client = OkHttpClient()

    interface HttpCallback {

        /**
         * called when the server response was not 2xx or when an exception was thrown in the process
         * @param response - in case of server error (4xx, 5xx) this contains the server response
         *                 in case of IO exception this is null
         * @param exception - contains the exception. in case of server error (4xx, 5xx) this is null
         */
        fun onFailure(response: Response?, exception: IOException?)

        /**
         * contains the server response
         * @param response
         */
        fun onSuccess(response: Response)
    }

    fun queueGetCall(url: String, cb: HttpCallback) {
        val hashMap : HashMap<String, String> = HashMap()
        hashMap.put("", "")
        this.call("GET", url, hashMap, cb)
    }

    fun quePostCall(url: String, parameters: HashMap<String, String>, cb: HttpCallback) {
        //call("POST", url, cb)
    }

    fun quePutCall(url: String, parameters: HashMap<String, String>, cb: HttpCallback) {
        //call("PUT", url, cb)
    }

    private fun call(method: String, url: String, parameters: HashMap<String, String>, cb: HttpCallback) {
        var request : Request
        if(method == "GET")
        {
            request = Request.Builder()
                .method(method, null)
                .url(url)
                .build()
        }
        else
        {
            val restBodyBuilder = FormBody.Builder()
            val it = parameters.entries.iterator()
            while (it.hasNext()) {
                val pair = it.next() as Map.Entry<*, *>
                restBodyBuilder.add(pair.key.toString(), pair.value.toString())
            }
            val body = restBodyBuilder.build()
            //val payload : String = "test = 2"
            //val body = payload.toRequestBody()
            request = Request.Builder()
                .method(method, body)
                .url(url)
                .build()
        }

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("Rest" + method + "Call Exception:", e.toString())
                cb.onFailure(null, e)
                return
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.code == 200) {
                    cb.onSuccess(response)
                    return
                } else {
                    cb.onFailure(response, null)
                    return
                }
            }
        })
    }
}