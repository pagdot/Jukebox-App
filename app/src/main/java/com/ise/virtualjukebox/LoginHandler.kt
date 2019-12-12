package com.ise.virtualjukebox

class LoginHandler(MainInstance : MainHandler) {
    private var mainHandler: MainHandler = MainInstance

    private var ip:String = ""
    private var name:String = ""

    private fun notifyMainClass() : Boolean{
        return mainHandler.CreateNewServer(name, ip)
    }

    fun setServerIP(IP:String) : Boolean{
        if(IP.isEmpty()){
            return false
        }
        ip = IP
        return true
    }
    fun setUserName(Name:String) : Boolean{
        if(Name.isEmpty())
            return false
        name = Name
        return true
    }

    fun createConnection() : Boolean {
        return notifyMainClass()
    }
}
