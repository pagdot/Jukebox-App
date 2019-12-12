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

    private val _IP4Regex  = "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\$".toRegex();
    private val _IP6Regex  = "^([0-9A-Fa-f]{1,4}:){7}[0-9A-Fa-f]{1,4}\$".toRegex();

    private var iptypevar : IP_Type = IP_Type.None;

    private fun notifyMainClass() : Boolean{
        return _MainHandler.CreateNewServer(_Name, _IP);
    }

    private fun checkIfIP4() : IP_Type{
        if(_IP4Regex.containsMatchIn(_IP)){
            return IP_Type.IP4;
        }
        return IP_Type.None;
    }
    private fun checkIfIP6() : IP_Type{
        if(_IP6Regex.containsMatchIn(_IP)){
            return IP_Type.IP6;
        }
        return IP_Type.None;
    }

    private fun checkIfIP(){
        if(checkIfIP4() == IP_Type.IP4){
            iptypevar = IP_Type.IP4;
        }else if(checkIfIP6() == IP_Type.IP6){
            iptypevar =  IP_Type.IP6;
        }else{
            iptypevar = IP_Type.None;
        }
    }
    private fun checkIfBoolWrapper() : Boolean{
        checkIfIP();
        if(iptypevar != IP_Type.None){
            return true;
        }
        return false;
    }

    public fun setServerIP(IP:String) : Boolean{
        _IP = IP;
        if(checkIfBoolWrapper()!=true){
            return false;
        }
        return notifyMainClass();
    }
    public fun setUserName(Name:String){
        _Name = Name;
    }
}
