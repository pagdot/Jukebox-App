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

class MainHandler(private var _mainHandler: MainActivity) {
    private var _fName = "ICC"

    class ServerPair{
        var net: JukeboxApi? = null
        var name: String? = null
        var ip : String? = null
        var isInit : Boolean = false
    }

    class RetVal{
        var net: JukeboxApi? = null
        var success : Boolean = false
        var errorMessage : String = ""
    }

    class PublicRetClass {
        var success : Boolean = false
        var errorMessage : String = ""
    }

    var serverList : MutableList<ServerPair> = mutableListOf<ServerPair>()
    var trackList : MutableList<VoteTrack>? = null
    var currTrack : PlayingTrack? = null

    private var core : ServerPair? = null

    var anyServerConnection = false

    private fun createNewServerWithoutConnect(serverIp : String, name : String) : Boolean{
        if(!checkIfValid(serverIp)){
            return false
        }
        _mainHandler.sendToast("Created")
        val pair = ServerPair()
        pair.name = name
        pair.ip = serverIp
        pair.net = JukeboxApi(serverIp)
        pair.isInit = false
        if(serverList.find { it.ip == pair.ip } == null) {
            serverList.add(pair)
            return true
        }
        return false
    }

    fun backProcess(){
        val handler = Handler(Looper.getMainLooper())
        handler.post(object : Runnable {
            override fun run() {
                refreshTracks()
                if(_mainHandler.playh.playlistChanged() != null){
                    //val fragment = fragmentManager.findFragmentById(R.layout.fragment_playlist) as PlaylistFragment
                    //fragment.playlistContentChanged()
                }
                handler.postDelayed(this, 5000)
            }
        })
    }

    fun createNewServer(name : String, serverIp : String) : PublicRetClass{
        val tmpRetClass = PublicRetClass()
        if(!checkIfValid(serverIp)){
            tmpRetClass.errorMessage = "Invalid Address"
            tmpRetClass.success = false
            return tmpRetClass
        }
        val rVal : RetVal = connectToServer(name, serverIp)
        if(rVal.success){
            val pair = ServerPair()

            pair.name = name
            pair.ip = serverIp
            pair.net = rVal.net
            pair.isInit = true
            if(serverList.find { it.ip == pair.ip } == null) {
                serverList.add(pair)
                core?.net?.disconnectClient()
                core = pair
            }else{
                serverList.find { it.ip == pair.ip }?.name = name
                serverList.find { it.ip == pair.ip }?.ip = serverIp
                serverList.find { it.ip == pair.ip }?.net = rVal.net
                serverList.find { it.ip == pair.ip }?.isInit = true
                core = pair
            }
            anyServerConnection = true
        }

        tmpRetClass.success = rVal.success
        tmpRetClass.errorMessage = rVal.errorMessage
        return tmpRetClass
    }

    private fun checkIfValid(serverIp: String): Boolean {
        val ip4 = "^(?:[0-9]{1,3}\\.){3}[0-9]{1,3}\$".toRegex()
        val ip6 = "^(([0-9a-fA-F]{1,4}:){7,7}[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,7}:|([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|:((:[0-9a-fA-F]{1,4}){1,7}|:)|fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]{1,}|::(ffff(:0{1,4}){0,1}:){0,1}((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])|([0-9a-fA-F]{1,4}:){1,4}:((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]))".toRegex()
        val adr = "^[a-zA-Z0-9\\-\\.]+\\.[a-zA-Z]{2,3}(\\/\\S*)?\$".toRegex()


       return (serverIp.matches(ip4) || serverIp.matches(ip6) || serverIp.matches(adr));
    }

    fun storeServerList(){
        if(serverList.size != 0){
            _mainHandler.storePrefs(serverList.size.toString(), _fName, "size")
            var it = 0
            for(item in serverList){
                if(/*item != null &&*/ item.name != null && item.ip != null){
                    _mainHandler.storePrefs(item.name.toString(), _fName, "name$it")
                    _mainHandler.storePrefs(item.ip.toString(), _fName, "ip$it")
                }
                it++
            }
        }
    }

    fun readServerList(){
        serverList.clear()
        val size =_mainHandler.loadPrefs(_fName, "size")?.toInt()
        if(size != null){
            for(i in 0 until size step 1){
                val name  = _mainHandler.loadPrefs(_fName, "name$i")
                val ip =  _mainHandler.loadPrefs(_fName, "ip$i")
                if(name != null && ip != null){
                    createNewServerWithoutConnect(ip, name)
                }
            }
        }
    }

    private fun _connectToServerSubHandler(Net : JukeboxApi? , Name : String) : RetVal{
        val countDownLatch = CountDownLatch(1)
        val retVal = RetVal()
        Net?.getSessionID(Name, object : JukeboxApi.JukeboxApiCallback {
            override fun onSuccess() {
                retVal.net = Net
                retVal.errorMessage = ""
                retVal.success = true
                countDownLatch.countDown()
            }
            override fun onFailure(errorClass: apiError, exception: Exception?) {
                retVal.success = false
                if(exception == null)
                    retVal.errorMessage = errorClass.message.toString()
                else
                    retVal.errorMessage = exception.message.toString()
                countDownLatch.countDown()
            }
        })
        countDownLatch.await()
        return retVal
    }

    private fun connectToServer(name:String, serverIp : String) : RetVal{
        val netCore  = JukeboxApi(serverIp)
        return _connectToServerSubHandler(netCore, name)
    }

    fun connectToExistingServer(serverIp: String) : PublicRetClass{
        val tmpRetClass = PublicRetClass()
        val found = serverList.find { it.ip ==  serverIp}
        if(found == null ){
            return tmpRetClass
        }
        return createNewServer(found.name!!, found.ip!!)
    }

    fun disconnectAllServer(){
        for(item in serverList){
            if(item.isInit){
                disconnectFromServer(item.ip!!)
            }
        }
        core = null
        anyServerConnection = false
    }

    private fun disconnectFromServer(ServerIp: String){
        val found = serverList.find { it.ip == ServerIp }
        if(found != null && found.isInit){
            found.net?.disconnectClient()
            found.isInit = false
            found.net?.searchTracks
        }
    }

    fun getActualServerList() :  MutableList<ServerPair>{
        val tmp = mutableListOf<ServerPair>()
        serverList.forEachIndexed { i, value ->
            var tmpServerPair = ServerPair()
            tmpServerPair.ip = value.ip
            tmpServerPair.isInit = value.isInit
            tmpServerPair.name = value.name
            tmpServerPair.net = value.net
            tmp.add(tmpServerPair)
        }
        return tmp
    }

    fun searchTrack(input: String, listSize : Int) :  MutableList<Track>?{
        val found = core
        var list : MutableList<Track>? = null
        if(found != null) {
            val countDownLatch = CountDownLatch(1)
            found.net?.getTracks(input, listSize, object : JukeboxApi.JukeboxApiCallback {
                override fun onSuccess() {
                    list = found.net?.searchTracks
                    countDownLatch.countDown()
                }

                override fun onFailure(errorClass: apiError, exception: Exception?) {
                    list = null
                    countDownLatch.countDown()
                }
            })
        }

        countDownLatch.await()
        return list
    }
    private fun convertToVoteTrack(list : MutableList<Track>?) : MutableList<VoteTrack>?{
        val newList : MutableList<VoteTrack>? = mutableListOf<VoteTrack>()
        if(list == null){
            return newList
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
            newList?.add(buf)
        }
        return newList
    }
    fun refreshTracks() : PublicRetClass{
        val found = core
        val retClass = PublicRetClass()

        if(found != null) {
            val countDownLatch = CountDownLatch(1)
            trackList?.clear()
            found.net?.getCurrentQueues(object : JukeboxApi.JukeboxApiCallback {
                override fun onSuccess() {
                    if(found.net?.queues?.adminQueue != null){
                        trackList = convertToVoteTrack(found.net?.queues?.adminQueue)
                    }
                    if(found.net?.queues?.normalQueue != null){
                        trackList?.addAll(found.net?.queues!!.normalQueue)
                    }
                    currTrack = found.net?.queues?.current
                    retClass.success = true
                    countDownLatch.countDown()
                }
                override fun onFailure(errorClass: apiError, exception: Exception?) {
                    retClass.success = false
                    if(exception != null) {
                        retClass.errorMessage = exception.message.toString()
                    }
                    else {
                        retClass.errorMessage = errorClass.message.toString()
                    }
                    trackList = null
                    currTrack = null
                    countDownLatch.countDown()
                }
            })
            countDownLatch.await()
        }
        return retClass
    }
    fun getTracks() :  MutableList<VoteTrack>?{
        return trackList
    }

    fun voteOnTrack(song : Track, UpDown : Int) : Boolean{
        val found = core
        var retVal = false

        if(found != null) {
            val countDownLatch = CountDownLatch(1)
            found.net?.voteTrack(song.trackId, UpDown, object : JukeboxApi.JukeboxApiCallback {
                override fun onSuccess() {
                    retVal = true
                    countDownLatch.countDown()
                }

                override fun onFailure(errorClass: apiError, exception: Exception?) {
                    retVal = false
                    countDownLatch.countDown()
                }
            })
            countDownLatch.await()
        }
        return retVal
    }
    fun addOnTrack(song : Track): PublicRetClass{
        val found = core
        val retClass = PublicRetClass()

        if(found != null) {
            val countDownLatch = CountDownLatch(1)
            found.net?.addTrackToQueue(song.trackId, object : JukeboxApi.JukeboxApiCallback {
                override fun onSuccess() {
                    retClass.success = true
                    countDownLatch.countDown()
                }

                override fun onFailure(errorClass: apiError, exception: Exception?) {
                    retClass.success = false
                    if (exception == null)
                        retClass.errorMessage = errorClass.message.toString()
                    else
                        retClass.errorMessage = exception.message.toString()
                    countDownLatch.countDown()
                }
            })
            countDownLatch.await()
        }
        return retClass
    }
    fun currentTrack() : PlayingTrack? {
        return currTrack
    }
    fun sendToast(ToBeSent: String){
        _mainHandler.sendToast(ToBeSent)
    }
}
