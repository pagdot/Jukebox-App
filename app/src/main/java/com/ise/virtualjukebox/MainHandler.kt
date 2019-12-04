package com.ise.virtualjukebox
import com.ise.virtualjukebox.jukeboxApi.JukeboxApi
import java.io.IOException


class MainHandler{

    class ServerPair{
        var Net: JukeboxApi? = null;
        var Name: String? = null;
    }
    var ServList : MutableList<ServerPair> = mutableListOf<ServerPair>();

    fun CreateNewServer(IP:String, Name:String) : Boolean{
        var NetCore : JukeboxApi = JukeboxApi(IP);
        var rval : Boolean = false;
        NetCore.getSessionID(Name, object : JukeboxApi.JukeboxApiCallback {
            override fun onSuccess() {
                var Pair : ServerPair = ServerPair();
                Pair.Name = Name;
                Pair.Net = NetCore;
                ServList.add(Pair);
                rval = true;
            }
            override fun onFailure(statusCode: String?, exception: IOException?) {
                rval = false;
            }
        })
        return rval;
    }
}
