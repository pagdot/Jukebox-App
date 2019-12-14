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
    private var _previousServerList: MutableList<MainHandler.ServerPair>? = null

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
    private fun compareItem(ServerA: MainHandler.ServerPair, ServerB: MainHandler.ServerPair): Boolean {
        if(ServerA.isInit != ServerB.isInit || ServerA.ip != ServerB.ip || ServerA.name != ServerB.name)
            return false
        return true
    }
    private fun compareServerLists(ListA: MutableList<MainHandler.ServerPair>?,
                                   ListB: MutableList<MainHandler.ServerPair>?): Boolean {
        var retVal = true
        if (ListA == null || ListB == null || ListA.size != ListB.size) {
            return false
        }
        ListA.forEachIndexed { i, value ->
            if (!compareItem(ListB[i], value)) {
                retVal = false
            }
        }
        return retVal
    }
    private fun copyList(ListToCpy: MutableList<MainHandler.ServerPair>?): MutableList<MainHandler.ServerPair>? {
        val newList = mutableListOf<MainHandler.ServerPair>()
        if (ListToCpy != null) {
            for (item in ListToCpy) {
                newList.add(item)
            }
            return newList
        }
        return null
    }
    fun checkIfServerListChanged(): MutableList<MainHandler.ServerPair>? {
        val current = _mainHandler.getActualServerList()
        if (!compareServerLists(current, _previousServerList)) {
            _previousServerList?.clear()
            _previousServerList = copyList(current)
            return current
        }
        return null
    }
}