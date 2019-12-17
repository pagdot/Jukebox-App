/**
 * Management Class to provide necessary functionality to the Login Screen
 * @author Tobias Egger
 * @author Matthias Dittrich
 * @version 1.0
 */

package com.ise.virtualjukebox

class LoginHandler(MainInstance : MainHandler) {
    private var _mainHandler: MainHandler = MainInstance

    private var _ip:String = ""
    private var _name:String = ""

    /**
     * Notifies the Main Handler on the request to create a new Server
     * @return reports on success
     */
    private fun notifyMainClass() : MainHandler.PublicRetClass{
        return _mainHandler.createNewServer(_name, _ip)
    }

    /**
     * Sets the Server IP for internal Usage
     * @param IP The Server IP
     * @return reports on success
     */
    fun setServerIP(IP:String) : Boolean{
        if(IP.isEmpty()){
            return false
        }
        _ip = IP
        return true
    }
    /**
     * Sets the Server Name for internal Usage
     * @param Name The User Name
     * @return reports on success
     */
    fun setUserName(Name:String) : Boolean{
        if(Name.isEmpty())
            return false
        _name = Name
        return true
    }

    /**
     * Creates Connection
     * @return reports on success
     */
    fun createConnection() : MainHandler.PublicRetClass {
        return notifyMainClass()
    }
}
