package com.ise.virtualjukebox.jukeboxApi.httpApi

import okhttp3.RequestBody.Companion.toRequestBody

import java.io.IOException
import android.util.Log
import okhttp3.*

open class RestClient {

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

    fun close() {
        client.dispatcher.executorService.shutdown()
        client.connectionPool.evictAll()
        client.cache?.close()
    }

    fun queueGetCall(scheme : String, host : String, segment : List<String>, parameters : HashMap<String, String>?, bodyPayload : String?, cb : HttpCallback) {
        this.call("GET", scheme, host, segment, parameters, bodyPayload, cb)
    }

    fun quePostCall(scheme : String, host : String, segment : List<String>, parameters : HashMap<String, String>?, bodyPayload : String?, cb : HttpCallback) {
        this.call("POST", scheme, host, segment, parameters, bodyPayload, cb)
    }

    fun quePutCall(scheme : String, host : String, segment : List<String>, parameters : HashMap<String, String>?, bodyPayload : String?, cb : HttpCallback) {
        this.call("PUT", scheme, host, segment, parameters, bodyPayload, cb)
    }

    private fun call(method : String, scheme : String, host : String, segments : List<String>, parameters : HashMap<String, String>?, bodyPayload : String?, cb : HttpCallback) {
        var request : Request
        val urlBuilder = HttpUrl.Builder().scheme(scheme).host(host).port(8888)
        segments.forEach { urlBuilder.addPathSegments(it) }

        if(method == "GET")
        {
            if(parameters != null)
            {
                val it = parameters.entries.iterator()
                while (it.hasNext()) {
                    val pair = it.next() as Map.Entry<*, *>
                    if(pair.key != "" && pair.value != "")
                        urlBuilder.addEncodedQueryParameter(pair.key.toString(), pair.value.toString())
                }
            }

            val builtUrl = urlBuilder.build()

            request = Request.Builder()
                .method(method, null)
                .url(builtUrl)
                .build()
        }
        else
        {
            val body = bodyPayload?.toRequestBody()
            val builtUrl = urlBuilder.build()
            request = Request.Builder()
                .method(method, body)
                .url(builtUrl)
                .build()
        }

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("Rest: $method Call Exception:", e.toString())
                cb.onFailure(null, e)
                return
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.code == 200) {
                    cb.onSuccess(response)
                    return
                } else {
                    Log.e("Rest: $method code exception:", "Code: ${response.code}")
                    cb.onFailure(response, null)
                    return
                }
            }
        })
    }
}