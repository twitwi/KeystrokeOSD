

package com.heeere.osd.keystrokeosd;

import java.util.concurrent.BlockingQueue;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

/**
 *
 * @author @twitwi
 */
public abstract class KeyboardMonitor {
    
    public static KeyboardMonitor produceIn(final BlockingQueue<Event> q) throws NativeHookException {
        GlobalScreen.registerNativeHook();
        //Construct the example object and initialze native hook.
        GlobalScreen.addNativeKeyListener(new NativeKeyListener() {
            public void nativeKeyPressed(NativeKeyEvent e) {
                checkTrue(q.offer(new Event.KeyPressed(e)));
            }
            
            public void nativeKeyReleased(NativeKeyEvent e) {
                checkTrue(q.offer(new Event.KeyReleased(e)));
            }
            
            public void nativeKeyTyped(NativeKeyEvent e) {
                checkTrue(q.offer(new Event.KeyTyped(e)));
            }

        });
        return new KeyboardMonitor() {};
    };
    
    private static void checkTrue(boolean isOk) {
        if (!isOk) throw new RuntimeException("important assertion failed");
    }
}
