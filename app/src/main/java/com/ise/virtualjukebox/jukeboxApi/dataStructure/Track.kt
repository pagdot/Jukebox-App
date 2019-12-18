package com.ise.virtualjukebox.jukeboxApi.dataStructure

/**
 * Track
 *
 */
open class Track {
    var trackId : String = ""
    var title : String = ""
    var album : String = ""
    var artist : String = ""
    var duration : Int = 0
    var iconUri : String = ""
    var addedBy : String = ""
}