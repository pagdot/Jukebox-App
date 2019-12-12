package com.ise.virtualjukebox

class LoginHandler {
    private var _MainHandler: MainHandler

    constructor(MainInstance : MainHandler){
        _MainHandler = MainInstance
    }

    private var _IP:String = ""
    private var _Name:String = ""

    private fun notifyMainClass() : Boolean{
        return _MainHandler.CreateNewServer(_Name, _IP)
    }

    fun setServerIP(IP:String) : Boolean{
        if(IP.isEmpty()){
            return false
        }
        _IP = IP
        return true
    }
    fun setUserName(Name:String) : Boolean{
        if(Name.isEmpty())
            return false
        _Name = Name
        return true
    }

    fun createConnection() : Boolean {
        return notifyMainClass()
    }
}
