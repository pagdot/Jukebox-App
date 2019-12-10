package com.ise.virtualjukebox

import android.app.Activity
import java.io.IOException

class SettingsHandler : Activity{
    private var _MainHandler: MainHandler;

    constructor(MainInstance : MainHandler){
        _MainHandler = MainInstance;
    }


    interface SettingsHandlerCallback {
        /**
         *
         * @param statusCode - contains the rest status code if the response was not OK (200). exception is then null
         * @param exception - contains the exception. statusCode is then null
         */
        fun onFailure(status : String?, exception : IOException?)

        fun onSuccess()
    }

    fun Connect(ServerIP: String) : Boolean{
        _MainHandler.DisconnectAllServer();
        return _MainHandler.ConnectToExistingServer(ServerIP);
    }
    fun FetchServerList() : MutableList<MainHandler.ServerPair>?{
        return _MainHandler.ServList;
    }
    fun AddServer(Name: String, ServerIP: String, CB:SettingsHandlerCallback) : MutableList<MainHandler.ServerPair>?{
        _MainHandler.DisconnectAllServer();
        if(_MainHandler.CreateNewServer(Name, ServerIP)) {
            CB.onSuccess();
        }else{
            CB.onFailure("Failed to Add Server", null);
        }
        return FetchServerList();
    }
}