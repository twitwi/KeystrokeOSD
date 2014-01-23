package com.heeere.osd.keystrokeosd;

import java.util.concurrent.BlockingQueue;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import static com.heeere.osd.keystrokeosd.QueueTools.*;

/**
 * 
 * @author @twitwi
 */
public class MagicOSD {
    
    public static void main(String[] arSsgs) throws NativeHookException {
        
        final boolean quitOnEsc = true;
        
        final BlockingQueue<Event> qKeybord = q();
        final BlockingQueue<Event> qEscapeDetector = q();
        final BlockingQueue<Event> qFilterEvents = q();
        final BlockingQueue<Event> qOSDShow = q();
        
        final BlockingQueue<Event> qLog = q();

        KeyboardMonitor keyboardMonitor = KeyboardMonitor.produceIn(qKeybord);
        KeyboardOSD osd = KeyboardOSD.showFrom(qOSDShow);
        
        // fork
        drain(qKeybord, qFilterEvents, qEscapeDetector, qLog);
        
        // filter and send to OSD
        filter(qFilterEvents, qOSDShow, new F1<Event, Event>() {
            @Override
            public Event apply(Event e) {
                return e;
            }
        });
        // escape
        consume(qEscapeDetector, new F1<Void, Event>() {
            @Override public Void apply(Event p1) {
                if (quitOnEsc && p1.type == Event.Type.KEY_PRESSED && p1.asKeyPressed().ev.getKeyCode() == NativeKeyEvent.VK_ESCAPE) {
                    GlobalScreen.unregisterNativeHook();
                    //System.exit(0);
                    return null;
                }
                return null;
            }
        });
        // logging
        consume(qLog, new F1<Void, Event>() {

            @Override
            public Void apply(Event e) {
                switch (e.type) {
                    case KEY_PRESSED: System.err.println("PRESSED: "+ toString(e.asKeyPressed().ev)); break;
                    case KEY_RELEASED: System.err.println("RELEASED: "+ toString(e.asKeyReleased().ev)); break;
                    case KEY_TYPED: System.err.println("TYPED: "+ toString(e.asKeyTyped().ev)); break;
                }
                return null;
            }

            private String toString(NativeKeyEvent ev) {
                return ev.getKeyLocation() + " "+ev.getKeyCode()+" "+ev.getKeyChar();
            }
        });
    }
    
}
