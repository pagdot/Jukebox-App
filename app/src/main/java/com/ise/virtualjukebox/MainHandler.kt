package com.ise.virtualjukebox
import android.os.Environment
import com.ise.virtualjukebox.jukeboxApi.JukeboxApi
import java.io.File
import java.io.IOException
import android.content.SharedPreferences;
import android.content.Context;
import android.content.ContextWrapper


class MainHandler{
    private var fname = "/Storage";
    class ServerPair{
        var Net: JukeboxApi? = null;
        var Name: String? = null;
        var IP : String? = null;
        var IsInit : Boolean = false;
    }
    class Retval{
        var Net: JukeboxApi? = null;
        var Success : Boolean = false;
    }
    var ServList : MutableList<ServerPair> = mutableListOf<ServerPair>();


    fun CreateNewServerWithoutConnect(ServIP:String, Name:String) : Boolean{
        var Pair : ServerPair = ServerPair();
        Pair.Name = Name;
        Pair.IP = ServIP;
        Pair.Net = JukeboxApi(ServIP);
        Pair.IsInit = false;
        if(ServList.find { it.IP == Pair.IP } == null) {
            ServList.add(Pair);
            return true;
        }
        return false;
    }
    fun CreateNewServer(Name:String, ServIP:String) : Boolean{
        val rval : Retval = ConnectToServer(ServIP, Name);
        if(rval.Success){
                val Pair : ServerPair = ServerPair();
                Pair.Name = Name;
                Pair.IP = ServIP;
                Pair.Net = rval.Net;
                Pair.IsInit = true;
                if(ServList.find { it.IP == Pair.IP } == null) {
                    ServList.add(Pair);
                }
        }
        return rval.Success;
    }
    fun StoreServerList(){
    }
    fun ReadServerList(){

    }
    fun ConnectToServer(Name:String, ServIP : String) : Retval{
        val NetCore : JukeboxApi = JukeboxApi(ServIP);
        val rval : Retval = Retval();
        NetCore.getSessionID(Name, object : JukeboxApi.JukeboxApiCallback {
            override fun onSuccess() {
                rval.Success = true;
                rval.Net = NetCore;
            }
            override fun onFailure(statusCode: String?, exception: IOException?) {
                rval.Success = false;
            }
        })

        return rval;
    }
    fun ConnectToExistingServer(ServIP: String) : Boolean{
        val found = ServList.find { it.IP ==  ServIP};
        if(found == null){
            return false;
        }
        var rval = false;
        found.Net?.getSessionID(found.Name!!, object : JukeboxApi.JukeboxApiCallback {
            override fun onSuccess() {
                rval = true;
                found.IsInit = true;
            }
            override fun onFailure(statusCode: String?, exception: IOException?) {
                rval = false;
            }
        })
        return rval;
    }
    fun DisconnectAllServer(){
        for(item in ServList){
            if(item.IsInit == true){
                // That Language... Wtf???
                DisconnectFromServer(item.IP!!);
            }
        }
    }
    fun DisconnectFromServer(ServIP: String){
        var found = ServList.find { it.IP == ServIP };
        if(found != null){
            found.Net?.Disconnect();
        };
    }
}
