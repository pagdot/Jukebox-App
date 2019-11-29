package com.ise.virtualjukebox.HttpApi

import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.Call
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
        this.call("GET", url, cb)
    }

    public fun quePostCall(url: String, cb: HttpCallback) {
        call("POST", url, cb)
    }

    public fun quePutCall(url: String, cb: HttpCallback) {
        call("PUT", url, cb)
    }

    private fun call(method: String, url: String, cb: HttpCallback) {
        val request = Request.Builder()
            .method(method, null)
            .url(url)
            .build()  //TODO: Body for put or post

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