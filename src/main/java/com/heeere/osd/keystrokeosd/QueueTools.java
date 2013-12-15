
package com.heeere.osd.keystrokeosd;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author @twitwi
 */
public abstract class QueueTools {

    public static BlockingQueue<Event> q() {
        return new ArrayBlockingQueue<Event>(100);
    }

    public static void drain(final BlockingQueue<Event> src, final BlockingQueue<Event> ...targets) {
        consume(src, new F1<Void, Event>() {

            @Override
            public Void apply(Event taken) {
                if (taken == null) return null; // skip null elements (convention for removing elements)
                try {
                    for (BlockingQueue<Event> target : targets) {
                        target.put(taken);
                    }
                } catch (InterruptedException ex) {
                    Logger.getLogger(MagicOSD.class.getName()).log(Level.SEVERE, null, ex);
                }
                return null;
            }
        });
    }

    public static void consume(final BlockingQueue<Event> q, final F1<Void, Event> f1) {
        new Thread(new Runnable() {

            public void run() {
                try {
                    while (true) {
                        final Event taken = q.take();
                        f1.apply(taken);
                    }
                } catch (InterruptedException ex) {
                    Logger.getLogger(MagicOSD.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }).start();
    }

    public static void filter(BlockingQueue<Event> src, final BlockingQueue<Event> target, final F1<Event, Event> f1) {
        consume(src, new F1<Void, Event>() {
            @Override
            public Void apply(Event taken) {
                if (taken == null) return null; // skip null elements (convention for removing elements)
                taken = f1.apply(taken);
                if (taken == null) return null; // skip null elements (convention for removing elements)
                try {
                    target.put(taken);
                } catch (InterruptedException ex) {
                    Logger.getLogger(MagicOSD.class.getName()).log(Level.SEVERE, null, ex);
                }
                return null;
            }
        });
    }    
}
