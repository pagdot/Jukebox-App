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

    fun connect(ServerIP: String) : MainHandler.PublicRetClass{
        _mainHandler.disconnectAllServer()
        return _mainHandler.connectToExistingServer(ServerIP)
    }
    fun fetchServerList() : MutableList<MainHandler.ServerPair>?{
        return _mainHandler.serverList
    }
    fun AddServer(Name: String, ServerIP: String, CB:SettingsHandlerCallback) : MutableList<MainHandler.ServerPair>?{
        _mainHandler.disconnectAllServer()
        var tmpRetClass = _mainHandler.createNewServer(Name, ServerIP)
        if(tmpRetClass.success) {
            _mainHandler.sendToast("Successfully added Server.")
            CB.onSuccess()
        }else{
            _mainHandler.sendToast("Failed to add Server (${tmpRetClass.errorMessage}).")
            CB.onFailure("Failed to add Server (${tmpRetClass.errorMessage}).", null)
        }
        return fetchServerList()
    }
}