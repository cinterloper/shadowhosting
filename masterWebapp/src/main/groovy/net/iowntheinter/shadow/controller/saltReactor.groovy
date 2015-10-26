/**
 * Created by grant on 10/20/15.
 */
import com.suse.saltstack.netapi.event.EventListener
import javax.websocket.CloseReason;
import com.suse.saltstack.netapi.datatypes.Event;

class saltReactor implements EventListener {

    saltReactor(Vertx v, String c)
    {

    }

    @Override
    void notify(Event event){

    }

    /**
     * Notify the listener that the backing event stream was closed.  Listener may
     * need to recreate the event stream or take other actions.
     * @param closeReason the close reason
     */
    @Override
    eventStreamClosed(CloseReason closeReason){
        println("closed")
    }
}