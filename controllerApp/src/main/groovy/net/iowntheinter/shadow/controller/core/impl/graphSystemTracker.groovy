package net.iowntheinter.shadow.controller.core.impl

import net.iowntheinter.shadow.controller.core.serviceGroupSpec
import net.iowntheinter.shadow.controller.core.systemTracker
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
    TinkerGraph g

    graphSystemTracker() {
        g= TinkerGraphFactory
        g = TinkerGraph.open()
    }
    @Override
    boolean addServer(String sid) {
        g.addVertex(id)
        return true
    }

    @Override
    boolean removeServer(String sid) {
        g.vertices(sid).remove()
        return true
    }
    boolean setServerProperty(sid, String k, val){

    }


    List getServersFromDeployment(UUID did){

    }
    Map findServersForDeployment(serviceGroupSpec s){

    }
}
