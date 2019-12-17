package com.ise.virtualjukebox.jukeboxApi

import org.junit.Test

import org.junit.Assert.*

class JsonParserTest {

    private val jsonParser = JsonParser()

    @Test
    fun parseQueuesFromResponse() {
        val input = "{\"admin_queue\":[{\"added_by\":\"me\",\"album\":\"album1\",\"artist\":\"artist1\",\"current_vote\":1,\"duration\":456,\"icon_uri\":\"adminuri1\",\"title\":\"admintitle1\",\"track_id\":\"adminid1\",\"votes\":1234},{\"added_by\":\"me\",\"album\":\"album2\",\"artist\":\"artist2\",\"current_vote\":1,\"duration\":336,\"icon_uri\":\"adminuri2\",\"title\":\"admintitle2\",\"track_id\":\"adminid2\",\"votes\":1234},{\"added_by\":\"me\",\"album\":\"album3\",\"artist\":\"artist3\",\"current_vote\":1,\"duration\":8888,\"icon_uri\":\"adminuri3\",\"title\":\"admintitle3\",\"track_id\":\"adminid3\",\"votes\":1234},{\"added_by\":\"me\",\"album\":\"album4\",\"artist\":\"artist4\",\"current_vote\":1,\"duration\":0,\"icon_uri\":\"adminuri4\",\"title\":\"admintitle4\",\"track_id\":\"adminid4\",\"votes\":1234}],\"currently_playing\":{\"added_by\":\"me\",\"album\":\"album1\",\"artist\":\"artist1\",\"duration\":456,\"icon_uri\":\"adminuri1\",\"playing\":false,\"playing_for\":10,\"title\":\"admintitle1\",\"track_id\":\"adminid1\"},\"normal_queue\":[{\"added_by\":\"me\",\"album\":\"album1\",\"artist\":\"artist1\",\"current_vote\":1,\"duration\":123,\"icon_uri\":\"uri1\",\"title\":\"title1\",\"track_id\":\"id1\",\"votes\":1234},{\"added_by\":\"me\",\"album\":\"album2\",\"artist\":\"artist2\",\"current_vote\":1,\"duration\":223,\"icon_uri\":\"uri2\",\"title\":\"title2\",\"track_id\":\"id2\",\"votes\":1234},{\"added_by\":\"me\",\"album\":\"album3\",\"artist\":\"artist3\",\"current_vote\":1,\"duration\":323,\"icon_uri\":\"uri3\",\"title\":\"title3\",\"track_id\":\"id3\",\"votes\":1234},{\"added_by\":\"me\",\"album\":\"album4\",\"artist\":\"artist4\",\"current_vote\":1,\"duration\":423,\"icon_uri\":\"uri4\",\"title\":\"title4\",\"track_id\":\"id4\",\"votes\":1234}]}"

        val output = jsonParser.parseQueuesFromResponse(input)

        //current
        //admin queue
        assert(output.adminQueue.size == 4)
        assert(output.adminQueue[0].addedBy == "me")
        assert(output.adminQueue[0].album == "album1")
        assert(output.adminQueue[0].artist == "artist1")
        assert(output.adminQueue[0].duration == 456)
        assert(output.adminQueue[0].iconUri == "adminuri1")
        assert(output.adminQueue[0].title == "admintitle1")
        assert(output.adminQueue[0].trackId == "adminid1")
        //---
        assert(output.adminQueue[1].addedBy == "me")
        assert(output.adminQueue[1].album == "album2")
        assert(output.adminQueue[1].artist == "artist2")
        assert(output.adminQueue[1].duration == 336)
        assert(output.adminQueue[1].iconUri == "adminuri2")
        assert(output.adminQueue[1].title == "admintitle2")
        assert(output.adminQueue[1].trackId == "adminid2")
        //---
        assert(output.adminQueue[2].addedBy == "me")
        assert(output.adminQueue[2].album == "album3")
        assert(output.adminQueue[2].artist == "artist3")
        assert(output.adminQueue[2].duration == 8888)
        assert(output.adminQueue[2].iconUri == "adminuri3")
        assert(output.adminQueue[2].title == "admintitle3")
        assert(output.adminQueue[2].trackId == "adminid3")
        //---
        assert(output.adminQueue[3].addedBy == "me")
        assert(output.adminQueue[3].album == "album4")
        assert(output.adminQueue[3].artist == "artist4")
        assert(output.adminQueue[3].duration == 0)
        assert(output.adminQueue[3].iconUri == "adminuri4")
        assert(output.adminQueue[3].title == "admintitle4")
        assert(output.adminQueue[3].trackId == "adminid4")

        //normal queue
        assert(output.normalQueue.size == 4)
        assert(output.normalQueue[0].addedBy == "me")
        assert(output.normalQueue[0].album == "album1")
        assert(output.normalQueue[0].artist == "artist1")
        assert(output.normalQueue[0].hasVoted == 1)
        assert(output.normalQueue[0].duration == 123)
        assert(output.normalQueue[0].iconUri == "uri1")
        assert(output.normalQueue[0].title == "title1")
        assert(output.normalQueue[0].trackId == "id1")
        assert(output.normalQueue[0].votes == 1234)
        //---
        assert(output.normalQueue[1].addedBy == "me")
        assert(output.normalQueue[1].album == "album2")
        assert(output.normalQueue[1].artist == "artist2")
        assert(output.normalQueue[1].hasVoted == 1)
        assert(output.normalQueue[1].duration == 223)
        assert(output.normalQueue[1].iconUri == "uri2")
        assert(output.normalQueue[1].title == "title2")
        assert(output.normalQueue[1].trackId == "id2")
        assert(output.normalQueue[1].votes == 1234)
        //---
        assert(output.normalQueue[2].addedBy == "me")
        assert(output.normalQueue[2].album == "album3")
        assert(output.normalQueue[2].artist == "artist3")
        assert(output.normalQueue[2].hasVoted == 1)
        assert(output.normalQueue[2].duration == 323)
        assert(output.normalQueue[2].iconUri == "uri3")
        assert(output.normalQueue[2].title == "title3")
        assert(output.normalQueue[2].trackId == "id3")
        assert(output.normalQueue[2].votes == 1234)
        //---
        assert(output.normalQueue[3].addedBy == "me")
        assert(output.normalQueue[3].album == "album4")
        assert(output.normalQueue[3].artist == "artist4")
        assert(output.normalQueue[3].hasVoted == 1)
        assert(output.normalQueue[3].duration == 423)
        assert(output.normalQueue[3].iconUri == "uri4")
        assert(output.normalQueue[3].title == "title4")
        assert(output.normalQueue[3].trackId == "id4")
        assert(output.normalQueue[3].votes == 1234)
    }

    @Test
    fun parseTrackListFromResponse() {
        val input = "{\"tracks\":[{\"added_by\":\"me\",\"album\":\"album1\",\"artist\":\"artist1\",\"duration\":123,\"icon_uri\":\"uri1\",\"title\":\"title1\",\"track_id\":\"id1\"},{\"added_by\":\"me\",\"album\":\"album2\",\"artist\":\"artist2\",\"duration\":223,\"icon_uri\":\"uri2\",\"title\":\"title2\",\"track_id\":\"id2\"},{\"added_by\":\"me\",\"album\":\"album3\",\"artist\":\"artist3\",\"duration\":323,\"icon_uri\":\"uri3\",\"title\":\"title3\",\"track_id\":\"id3\"},{\"added_by\":\"me\",\"album\":\"album4\",\"artist\":\"artist4\",\"duration\":423,\"icon_uri\":\"uri4\",\"title\":\"title4\",\"track_id\":\"id4\"}]}"

        val output = jsonParser.parseTrackListFromResponse(input)

        assert(output.size == 4)

        assert(output[0].addedBy == "me")
        assert(output[0].album == "album1")
        assert(output[0].artist == "artist1")
        assert(output[0].duration == 123)
        assert(output[0].iconUri == "uri1")
        assert(output[0].title == "title1")
        assert(output[0].trackId == "id1")

        assert(output[1].addedBy == "me")
        assert(output[1].album == "album2")
        assert(output[1].artist == "artist2")
        assert(output[1].duration == 223)
        assert(output[1].iconUri == "uri2")
        assert(output[1].title == "title2")
        assert(output[1].trackId == "id2")

        assert(output[2].addedBy == "me")
        assert(output[2].album == "album3")
        assert(output[2].artist == "artist3")
        assert(output[2].duration == 323)
        assert(output[2].iconUri == "uri3")
        assert(output[2].title == "title3")
        assert(output[2].trackId == "id3")

        assert(output[3].addedBy == "me")
        assert(output[3].album == "album4")
        assert(output[3].artist == "artist4")
        assert(output[3].duration == 423)
        assert(output[3].iconUri == "uri4")
        assert(output[3].title == "title4")
        assert(output[3].trackId == "id4")
    }
}