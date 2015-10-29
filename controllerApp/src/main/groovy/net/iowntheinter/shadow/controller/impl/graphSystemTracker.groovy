package net.iowntheinter.shadow.controller.impl

import net.iowntheinter.shadow.controller.serviceGroupSpec
import net.iowntheinter.shadow.controller.systemTracker
import org.apache.tinkerpop.gremlin.structure.Graph
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph

/**
 * Created by grant on 10/27/15.
 * should keep track of
 *  - what servers are running
 *   - what their real resource utilization is
 *    -this should be established through periodic polling
 *  - what is actually deployed on them
 *   - what the max resource commit should be for these deployments
 */
class graphSystemTracker implements systemTracker {
    Graph g
    graphSystemTracker(){
        g = TinkerGraph.open()
    }
    boolean addServer(){

    }
    boolean removeServer(){

    }
    List getServersFromDeployment(UUID did){

    }
    Map findServersForDeployment(serviceGroupSpec s){

    }
}
