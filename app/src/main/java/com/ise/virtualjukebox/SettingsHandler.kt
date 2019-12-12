package com.ise.virtualjukebox

import java.io.IOException

class SettingsHandler(MainInstance : MainHandler) {
    private var _mainHandler: MainHandler = MainInstance

    interface SettingsHandlerCallback {
        /**
         *
         * @param status - contains the rest status code if the response was not OK (200). exception is then null
         * @param exception - contains the exception. statusCode is then null
         */
        fun onFailure(status : String?, exception : IOException?)

        fun onSuccess()
    }

    fun connect(ServerIP: String) : Boolean{
        _mainHandler.disconnectAllServer()
        return _mainHandler.connectToExistingServer(ServerIP)
    }
    fun fetchServerList() : MutableList<MainHandler.ServerPair>?{
        return _mainHandler.serverList
    }
    fun AddServer(Name: String, ServerIP: String, CB:SettingsHandlerCallback) : MutableList<MainHandler.ServerPair>?{
        _mainHandler.disconnectAllServer()
        if(_mainHandler.createNewServer(Name, ServerIP)) {
            _mainHandler.sendToast("Succeeded to Add Server")
            CB.onSuccess()
        }else{
            _mainHandler.sendToast("Failed to Add Server")
            CB.onFailure("Failed to Add Server", null)
        }
        return fetchServerList()
    }
}