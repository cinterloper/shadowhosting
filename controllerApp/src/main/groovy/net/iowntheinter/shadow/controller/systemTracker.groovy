package net.iowntheinter.shadow.controller
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;
import org.apache.tinkerpop.gremlin.structure.Graph
import org.apache.tinkerpop.gremlin.util.Gremlin;
/**
 * Created by grant on 10/27/15.
 */
interface systemTracker {

    boolean addServer()
    boolean removeServer()
    List getServersFromDeployment(UUID did)

}
