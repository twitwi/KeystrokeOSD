

package com.heeere.osd.keystrokeosd;

import java.util.concurrent.BlockingQueue;
import static com.heeere.osd.keystrokeosd.QueueTools.*;

/**
 *
 * @author @twitwi
 */
public class KeyboardOSD {

    protected KeyboardOSD() {
    }
    
    public static KeyboardOSD showFrom(final BlockingQueue<Event> q) {
        final KeyboardOSD res = new KeyboardOSD();
        consume(q, new F1<Void, Event>() {
            public Void apply(Event e) {
                res.process(e);
                return null;
            }
        });
        return res;
    }
    
    protected void process(Event e) {
    }
}
