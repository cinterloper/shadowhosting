package net.iowntheinter.shadow.controller.auth

/**
 * Created by grant on 10/30/15.
 */
interface authManager {

    boolean addUser()

    boolean removeUser()

    boolean authenticate(user)

    boolean retrieveToken(user)

    boolean setKeypair(user,keypair)

    boolean getPubkey(user)

    String getPrivkey(user,token)


}
