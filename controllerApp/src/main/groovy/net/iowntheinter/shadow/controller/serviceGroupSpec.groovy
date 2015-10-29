package net.iowntheinter.shadow.controller

/**
 * Created by grant on 10/27/15.
 */
interface serviceGroupSpec {

    boolean deployable() //becomes yes when reserved

    boolean parse()
    /*this reservation id should probably expire*/
    UUID reserveResources(deploymentManager dm)


}
