package com.ise.virtualjukebox
import android.os.Environment
import com.ise.virtualjukebox.jukeboxApi.JukeboxApi
import java.io.File
import java.io.IOException
import android.content.SharedPreferences;
import android.content.Context;
import android.content.ContextWrapper
import com.ise.virtualjukebox.jukeboxApi.dataStructure.PlayingTrack
import com.ise.virtualjukebox.jukeboxApi.dataStructure.Track
import com.ise.virtualjukebox.jukeboxApi.dataStructure.VoteTrack


class MainHandler(private var _MainHandler: MainActivity){

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

    private var Core : JukeboxApi? = null;

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
            found.Net?.searchTracks
        };
    }
    fun SearchTrack(Input: String, ListSize : Int) :  MutableList<Track>?{
        val found = ServList.find{ it: ServerPair -> it.IsInit == true};
        var list : MutableList<Track>? = null;
        if(found != null) {
            found.Net?.getTracks(Input, ListSize, object : JukeboxApi.JukeboxApiCallback {
                override fun onSuccess() {
                    list = found.Net?.searchTracks;
                }

                override fun onFailure(statusCode: String?, exception: IOException?) {
                    list = null;
                }
            })
        }
        return list;
    }
    private fun convertToVoteTrack(list : MutableList<Track>?) : MutableList<VoteTrack>?{
        var newlist : MutableList<VoteTrack>? = mutableListOf<VoteTrack>();
        if(list == null){
            return null;
        }
        for(item in list){
            var buf : VoteTrack = VoteTrack();
            buf.trackId = item.trackId
            buf.title = item.title
            buf.album  = item.album
            buf.artist = item.artist
            buf.duration = item.duration
            buf.iconUri = item.iconUri
            buf.addedBy = item.addedBy
            newlist?.add(buf);
        }
        return newlist;
    }
    fun GetTracks() :  MutableList<VoteTrack>?{
        val found = ServList.find{ it: ServerPair -> it.IsInit == true};
        var list : MutableList<VoteTrack>? = mutableListOf<VoteTrack>();
        if(found != null) {
            found.Net?.getCurrentQueues(object : JukeboxApi.JukeboxApiCallback {
                override fun onSuccess() {
                    if(found.Net?.queues?.adminQueue != null){
                        list = convertToVoteTrack(found.Net?.queues?.adminQueue);
                    }
                    if(found.Net?.queues?.normalQueue != null){
                        list?.addAll(found.Net?.queues!!.normalQueue);
                    }
                }
                override fun onFailure(statusCode: String?, exception: IOException?) {
                    list = null;
                }
            })
        }
        return list;
    }

    fun VoteOnTrack(Song:Track):Boolean{
        val found = ServList.find{ it: ServerPair -> it.IsInit == true};
        var retval = false;
        found?.Net?.voteTrack(Song.trackId, 1, object : JukeboxApi.JukeboxApiCallback {
            override fun onSuccess() {
                retval = true;
            }

            override fun onFailure(statusCode: String?, exception: IOException?) {
                retval = false;
            }
        })
        return retval;
    }
    fun AddOnTrack(Song:Track):Boolean{
        val found = ServList.find{ it: ServerPair -> it.IsInit == true};
        var retval = false;
        found?.Net?.addTrackToQueue(Song.trackId,  object : JukeboxApi.JukeboxApiCallback {
            override fun onSuccess() {
                retval = true;
            }

            override fun onFailure(statusCode: String?, exception: IOException?) {
                retval = false;
            }
        })
        return retval;
    }
    fun CurrentTrack() : PlayingTrack? {
        val found = ServList.find{ it: ServerPair -> it.IsInit == true};
        var track : PlayingTrack? = PlayingTrack();
        if(found != null) {
            found.Net?.getCurrentQueues(object : JukeboxApi.JukeboxApiCallback {
                override fun onSuccess() {
                   track = found.Net?.queues?.current;
                }
                override fun onFailure(statusCode: String?, exception: IOException?) {
                    track = null;
                }
            })
        }
        return track;
    }
    fun sendToast(ToBeSent: String){
        _MainHandler.sendToast(ToBeSent);
    }
}
