
package com.heeere.osd.keystrokeosd;

import org.jnativehook.keyboard.NativeKeyEvent;

/**
 *
 * @author @twitwi
 */
public abstract class Event {
    
    public static enum Type {KEY_PRESSED, KEY_RELEASED, KEY_TYPED}
    
    public final Type type;
    public Event(Type type) {
        this.type = type;
    }
    
    public static class KeyPressed extends Event {
        public final NativeKeyEvent ev;
        public KeyPressed(NativeKeyEvent ev) {
            super(Type.KEY_PRESSED);
            this.ev = ev;
        }
        @Override public KeyPressed asKeyPressed() { return this; }
    }
    public static class KeyReleased extends Event {
        public final NativeKeyEvent ev;
        public KeyReleased(NativeKeyEvent ev) {
            super(Type.KEY_RELEASED);
            this.ev = ev;
        }
        @Override public KeyReleased asKeyReleased() { return this; }
    }
    public static class KeyTyped extends Event {
        public final NativeKeyEvent ev;
        public KeyTyped(NativeKeyEvent ev) {
            super(Type.KEY_TYPED);
            this.ev = ev;
        }
        @Override public KeyTyped asKeyTyped() { return this; }
    }

    public KeyPressed asKeyPressed() {return null;}
    public KeyReleased asKeyReleased() { return null; }
    public KeyTyped asKeyTyped() { return null; }

}
