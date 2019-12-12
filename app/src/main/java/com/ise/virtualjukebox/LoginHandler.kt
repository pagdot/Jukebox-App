package com.ise.virtualjukebox
import com.ise.virtualjukebox.MainHandler;


class LoginHandler {
    private var _MainHandler: MainHandler;

    constructor(MainInstance : MainHandler){
        _MainHandler = MainInstance;
    }
    enum class IP_Type {
        IP4, IP6, None
    }

    private var _IP:String = "";
    private var _Name:String = "";


    private fun notifyMainClass() : Boolean{
        return _MainHandler.CreateNewServer(_IP, _Name);
    }


    public fun setServerIP(IP:String) : Boolean{
        _IP = IP;
        if(_Name.length == 0){
            return false;
        }
        return notifyMainClass();
    }
    public fun setUserName(Name:String){
        _Name = Name;
    }
}
