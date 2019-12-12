package com.ise.virtualjukebox
import android.os.Handler
import android.os.Looper
import com.ise.virtualjukebox.activity.MainActivity
import com.ise.virtualjukebox.jukeboxApi.JukeboxApi
import com.ise.virtualjukebox.jukeboxApi.dataStructure.PlayingTrack
import com.ise.virtualjukebox.jukeboxApi.dataStructure.Track
import com.ise.virtualjukebox.jukeboxApi.dataStructure.VoteTrack
import com.ise.virtualjukebox.jukeboxApi.dataStructure.apiError
import java.util.concurrent.CountDownLatch

class MainHandler(private var _MainHandler: MainActivity) {
    private var fname = "ICC"
    class ServerPair{
        var Net: JukeboxApi? = null
        var Name: String? = null
        var IP : String? = null
        var IsInit : Boolean = false
    }
    class Retval{
        var Net: JukeboxApi? = null
        var Success : Boolean = false
    }
    var ServList : MutableList<ServerPair> = mutableListOf<ServerPair>()
    var TrackList : MutableList<VoteTrack>? = null//mutableListOf<VoteTrack>()
    var CurrTrack : PlayingTrack? = null

    private var Core : ServerPair? = null

    fun CreateNewServerWithoutConnect(ServIP:String, Name:String) : Boolean{
        _MainHandler.sendToast("Created")
        var Pair = ServerPair()
        Pair.Name = Name
        Pair.IP = ServIP
        Pair.Net = JukeboxApi(ServIP)
        Pair.IsInit = false
        if(ServList.find { it.IP == Pair.IP } == null) {
            ServList.add(Pair)
            return true
        }
        return false
    }
    fun BackProcess(){
        val handler = Handler(Looper.getMainLooper())
        handler.post(object : Runnable {
            override fun run() {
                RefreshTracks()
                if(_MainHandler.playh.PlaylistChanged() != null){
                    //val fragment = fragmentManager.findFragmentById(R.layout.fragment_playlist) as PlaylistFragment
                    //fragment.playlistContentChanged()
                }
                handler.postDelayed(this, 5000)
            }
        })
    }


    fun CreateNewServer(Name:String, ServIP:String) : Boolean{
        val rval : Retval = ConnectToServer(Name, ServIP)
        if(rval.Success){
                val Pair = ServerPair()

                Pair.Name = Name
                Pair.IP = ServIP
                Pair.Net = rval.Net
                Pair.IsInit = true
                if(ServList.find { it.IP == Pair.IP } == null) {
                    ServList.add(Pair)
                    Core?.Net?.disconnectClient()
                    Core = Pair
                }else{
                    ServList.find { it.IP == Pair.IP }?.Name = Name;
                    ServList.find { it.IP == Pair.IP }?.IP = ServIP;
                    ServList.find { it.IP == Pair.IP }?.Net = rval.Net;
                    ServList.find { it.IP == Pair.IP }?.IsInit = true;
                }
        }
        return rval.Success;
    }
    fun StoreServerList(){
        if(ServList != null){
            _MainHandler.storePrefs(ServList.size.toString(), fname, "size")
            var it = 0
            for(item in ServList){
                if(item != null && item.Name != null && item.IP != null){
                    _MainHandler.storePrefs(item.Name.toString(), fname, "Name" + it.toString())
                    _MainHandler.storePrefs(item.IP.toString(), fname, "IP" + it.toString())
                }
                it++
            }
        }
    }
    fun ReadServerList(){
        ServList.clear();
        val size =_MainHandler.loadPrefs(fname, "size")?.toInt()
        if(size != null){
            for(i in 0 until size step 1){
                val Name  = _MainHandler.loadPrefs(fname, "Name"+i.toString())
                val IP =  _MainHandler.loadPrefs(fname, "IP"+i.toString())
                if(Name != null && IP != null){
                    CreateNewServerWithoutConnect(IP, Name)
                }
            }
        }
    }
    fun ConnectToServer(Name:String, ServIP : String) : Retval{
        val NetCore : JukeboxApi = JukeboxApi(ServIP)
        val rval : Retval = Retval()
        val countDownLatch = CountDownLatch(1)
        NetCore.getSessionID(Name, object : JukeboxApi.JukeboxApiCallback {
            override fun onSuccess() {
                rval.Success = true
                rval.Net = NetCore
                countDownLatch.countDown()
            }
            override fun onFailure(errorClass: apiError, exception: Exception?) {
                rval.Success = false
                countDownLatch.countDown()
            }
        })
        countDownLatch.await()
        return rval;
    }
    fun ConnectToExistingServer(ServIP: String) : Boolean{
        val found = ServList.find { it.IP ==  ServIP}
        if(found == null){
            return false
        }
        return this.CreateNewServer(found.Name!!, found.IP!!)
    }
    fun DisconnectAllServer(){
        for(item in ServList){
            if(item.IsInit){
                DisconnectFromServer(item.IP!!)
            }
        }
    }
    fun DisconnectFromServer(ServIP: String){
        var found = ServList.find { it.IP == ServIP }
        if(found != null && found.IsInit){
            found.Net?.disconnectClient()
            found.IsInit = false
            found.Net?.searchTracks
        };
    }
    fun SearchTrack(Input: String, ListSize : Int) :  MutableList<Track>?{
        val found = Core
        var list : MutableList<Track>? = null
        val countDownLatch = CountDownLatch(1)
        if(found != null) {
            found.Net?.getTracks(Input, ListSize, object : JukeboxApi.JukeboxApiCallback {
                override fun onSuccess() {
                    list = found.Net?.searchTracks
                    countDownLatch.countDown()
                }

                override fun onFailure(errorClass: apiError, exception: Exception?) {
                    list = null
                    countDownLatch.countDown()
                }
            })
        }

        countDownLatch.await()
        return list;
    }
    private fun convertToVoteTrack(list : MutableList<Track>?) : MutableList<VoteTrack>?{
        val newlist : MutableList<VoteTrack>? = mutableListOf<VoteTrack>()
        if(list == null){
            return newlist
        }
        for(item in list){
            val buf = VoteTrack()
            buf.trackId = item.trackId
            buf.title = item.title
            buf.album  = item.album
            buf.artist = item.artist
            buf.duration = item.duration
            buf.iconUri = item.iconUri
            buf.addedBy = item.addedBy
            buf.votes = -1
            newlist?.add(buf)
        }
        return newlist
    }
    fun RefreshTracks(){
        val found = Core
        if(found != null) {
            val countDownLatch = CountDownLatch(1)
            TrackList?.clear()
            found.Net?.getCurrentQueues(object : JukeboxApi.JukeboxApiCallback {
                override fun onSuccess() {
                    if(found.Net?.queues?.adminQueue != null){
                        TrackList = convertToVoteTrack(found.Net?.queues?.adminQueue)
                    }
                    if(found.Net?.queues?.normalQueue != null){
                        TrackList?.addAll(found.Net?.queues!!.normalQueue)
                    }
                    CurrTrack = found.Net?.queues?.current
                    countDownLatch.countDown()
                }
                override fun onFailure(errorClass: apiError, exception: Exception?) {
                    TrackList = null
                    CurrTrack = null
                    countDownLatch.countDown()
                }
            })
            countDownLatch.await()
        }
    }
    fun GetTracks() :  MutableList<VoteTrack>?{
        return TrackList
    }

    fun VoteOnTrack(Song:Track):Boolean{
        val found = Core
        var retval = false

        val countDownLatch = CountDownLatch(1)
        found?.Net?.voteTrack(Song.trackId, 1, object : JukeboxApi.JukeboxApiCallback {
            override fun onSuccess() {
                retval = true
                countDownLatch.countDown()
            }

            override fun onFailure(errorClass: apiError, exception: Exception?) {
                retval = false
                countDownLatch.countDown()
            }
        })

        countDownLatch.await()
        return retval
    }
    fun AddOnTrack(Song:Track):Boolean{
        val found = Core
        var retval = false

        val countDownLatch = CountDownLatch(1)
        found?.Net?.addTrackToQueue(Song.trackId,  object : JukeboxApi.JukeboxApiCallback {
            override fun onSuccess() {
                retval = true
                countDownLatch.countDown()
            }

            override fun onFailure(errorClass: apiError, exception: Exception?) {
                retval = false
                countDownLatch.countDown()
            }
        })

        countDownLatch.await()
        return retval
    }
    fun CurrentTrack() : PlayingTrack? {
        return CurrTrack
    }
    fun sendToast(ToBeSent: String){
        _MainHandler.sendToast(ToBeSent)
    }
}
