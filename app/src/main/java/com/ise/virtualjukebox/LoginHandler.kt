package com.ise.virtualjukebox

class LoginHandler(MainInstance : MainHandler) {
    private var _mainHandler: MainHandler = MainInstance

    private var _ip:String = ""
    private var _name:String = ""

    private fun notifyMainClass() : MainHandler.PublicRetClass{
        var rval =_mainHandler.createNewServer(_name, _ip)
        return rval;
    }

    fun setServerIP(IP:String) : Boolean{
        if(IP.isEmpty()){
            return false
        }
        _ip = IP
        return true
    }
    fun setUserName(Name:String) : Boolean{
        if(Name.isEmpty())
            return false
        _name = Name
        return true
    }

    fun createConnection() : MainHandler.PublicRetClass {
        return notifyMainClass()
    }
}
