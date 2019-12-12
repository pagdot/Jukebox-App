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
    }
    var serverList : MutableList<ServerPair> = mutableListOf<ServerPair>()
    var trackList : MutableList<VoteTrack>? = null
    var currTrack : PlayingTrack? = null

    private var core : ServerPair? = null

    private fun createNewServerWithoutConnect(serverIp : String, name : String) : Boolean{
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

    fun createNewServer(name : String, serverIp : String) : Boolean{
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
            }
        }
        return rVal.success
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

    private fun connectToServer(name:String, serverIp : String) : RetVal{
        val netCore  = JukeboxApi(serverIp)
        val rVal  = RetVal()
        val countDownLatch = CountDownLatch(1)
        netCore.getSessionID(name, object : JukeboxApi.JukeboxApiCallback {
            override fun onSuccess() {
                rVal.success = true
                rVal.net = netCore
                countDownLatch.countDown()
            }
            override fun onFailure(errorClass: apiError, exception: Exception?) {
                rVal.success = false
                countDownLatch.countDown()
            }
        })
        countDownLatch.await()
        return rVal
    }

    fun connectToExistingServer(serverIp: String) : Boolean{
        val found = serverList.find { it.ip ==  serverIp}
        if(found == null){
            return false
        }
        return this.createNewServer(found.name!!, found.ip!!)
    }

    fun disconnectAllServer(){
        for(item in serverList){
            if(item.isInit){
                disconnectFromServer(item.ip!!)
            }
        }
    }

    private fun disconnectFromServer(ServerIp: String){
        val found = serverList.find { it.ip == ServerIp }
        if(found != null && found.isInit){
            found.net?.disconnectClient()
            found.isInit = false
            found.net?.searchTracks
        }
    }

    fun searchTrack(input: String, listSize : Int) :  MutableList<Track>?{
        val found = core
        var list : MutableList<Track>? = null
        val countDownLatch = CountDownLatch(1)
        if(found != null) {
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
    fun refreshTracks(){
        val found = core
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
                    countDownLatch.countDown()
                }
                override fun onFailure(errorClass: apiError, exception: Exception?) {
                    trackList = null
                    currTrack = null
                    countDownLatch.countDown()
                }
            })
            countDownLatch.await()
        }
    }
    fun getTracks() :  MutableList<VoteTrack>?{
        return trackList
    }

    fun voteOnTrack(song : Track) : Boolean{
        val found = core
        var retVal = false

        val countDownLatch = CountDownLatch(1)
        found?.net?.voteTrack(song.trackId, 1, object : JukeboxApi.JukeboxApiCallback {
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
        return retVal
    }
    fun addOnTrack(song : Track): Boolean{
        val found = core
        var retVal = false

        val countDownLatch = CountDownLatch(1)
        found?.net?.addTrackToQueue(song.trackId,  object : JukeboxApi.JukeboxApiCallback {
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
        return retVal
    }
    fun currentTrack() : PlayingTrack? {
        return currTrack
    }
    fun sendToast(ToBeSent: String){
        _mainHandler.sendToast(ToBeSent)
    }
}
