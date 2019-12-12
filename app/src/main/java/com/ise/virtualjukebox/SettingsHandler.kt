package com.ise.virtualjukebox

import java.io.IOException

class SettingsHandler(MainInstance : MainHandler) {
    private var mainHandler: MainHandler = MainInstance

    interface SettingsHandlerCallback {
        /**
         *
         * @param statusCode - contains the rest status code if the response was not OK (200). exception is then null
         * @param exception - contains the exception. statusCode is then null
         */
        fun onFailure(status : String?, exception : IOException?)

        fun onSuccess()
    }

    fun connect(ServerIP: String) : Boolean{
        mainHandler.DisconnectAllServer()
        return mainHandler.ConnectToExistingServer(ServerIP)
    }
    fun fetchServerList() : MutableList<MainHandler.ServerPair>?{
        return mainHandler.ServList
    }
    fun AddServer(Name: String, ServerIP: String, CB:SettingsHandlerCallback) : MutableList<MainHandler.ServerPair>?{
        mainHandler.DisconnectAllServer()
        if(mainHandler.CreateNewServer(Name, ServerIP)) {
            mainHandler.sendToast("Succeeded to Add Server")
            CB.onSuccess()
        }else{
            mainHandler.sendToast("Failed to Add Server")
            CB.onFailure("Failed to Add Server", null)
        }
        return fetchServerList()
    }
}