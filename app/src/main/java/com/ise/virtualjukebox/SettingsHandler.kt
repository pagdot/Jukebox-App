/**
 * Management Class to provide necessary functionality to the Settings Screen
 * @author Tobias Egger
 * @author Matthias Dittrich
 * @version 1.0
 */

package com.ise.virtualjukebox

import java.io.IOException

class SettingsHandler(MainInstance : MainHandler) {
    private var _mainHandler: MainHandler = MainInstance

    /**
     * Callback Functions to signal error/succes of a function
     */
    interface SettingsHandlerCallback {
        /**
         *
         * @param status - contains the rest status code if the response was not OK (200). exception is then null
         * @param exception - contains the exception. statusCode is then null
         */
        fun onFailure(status : String?, exception : IOException?)

        fun onSuccess()
    }
    /**
     * Connects to an existing server.
     * Internally checks if Object for Server exists and Connects to this Server
     * Disconnects all currently active Servers
     * Meant to be used in conjunction to a Server List.
     * @param ServerIP The IP of the Server. Object for this IP must exist in Server List
     * @return Reports on Success or Failure.
     */
    fun connect(ServerIP: String) : MainHandler.PublicRetClass{
        _mainHandler.disconnectAllServer()
        return _mainHandler.connectToExistingServer(ServerIP)
    }
    /**
     * Returns the Internal Server List
     */
    fun fetchServerList() : MutableList<MainHandler.ServerPair>?{
        return _mainHandler.serverList
    }
    /**
     * Adds a on-existent Server to the internal Server List and connects to it
     * @param Name User Name
     * @param ServerIP Server IP
     * @param CB Callbacks to report Success or Failure
     * @return Returns a Server List.
     */
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