

package com.heeere.osd.keystrokeosd;

import java.util.concurrent.BlockingQueue;
import static com.heeere.osd.keystrokeosd.QueueTools.*;
import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import org.jnativehook.keyboard.NativeKeyEvent;

/**
 *
 * @author @twitwi
 */
public class KeyboardOSD {
    
    protected KeyboardOSD() {
    }
    
    // constructor
    
    public static KeyboardOSD showFrom(final BlockingQueue<Event> q) {
        final KeyboardOSD res = new KeyboardOSD();
        res.initComponents();
        consume(q, new F1<Void, Event>() {
            public Void apply(Event e) {
                res.process(e);
                return null;
            }
        });
        return res;
    }
    
    // real class content
    
    private int lastPressedCode;
    protected void process(Event e) {
        JPanel tmp;
        NativeKeyEvent ev;
        String str;
        switch (e.type) {
            case KEY_TYPED:
                tmp = addKey(e.asKeyTyped().ev, lastPressedCode);
                removeKey(fadeAfter, fadeFor, tmp);
                break;
            case KEY_PRESSED: {
                ev = e.asKeyPressed().ev;
                String mod = getMod(ev);
                if (mod != null) {
                    tmp = addKey(mod);
                    if (modifiersKeyPanels.containsKey(mod)) {
                        throw new IllegalStateException("add:"+mod);
                    }
                    modifiersKeyPanels.put(mod, tmp);
                } else {
                    lastPressedCode = ev.getKeyCode();
                }
                break;
            }
            case KEY_RELEASED: {
                ev = e.asKeyReleased().ev;
                String mod = getMod(ev);
                if (mod != null) {
                    tmp = modifiersKeyPanels.remove(mod);
                    if (tmp==null) {
                        throw new IllegalStateException("rem:"+mod);
                    }
                    removeKey(0, 0, tmp);
                }
                break;
            }
        }
    }
    
    JFrame f;
    JPanel content;
    int fadeAfter = 3000;
    int fadeFor = 2000;
    Map<String, JPanel> modifiersKeyPanels = new HashMap<String, JPanel>();
    
    private void initComponents() {
        f = new JFrame("Key OSD");
        initFrameThings();
        // now the panel
        content = new JPanel();
        f.setLayout(new BorderLayout());
        f.setContentPane(content);
        content.setLayout(new BoxLayout(content, BoxLayout.LINE_AXIS));
        content.setOpaque(false);
        int marginX = 5, marginY = 5;
        Border margin = new EmptyBorder(marginY, marginX, marginY, marginX);
        content.setBorder(new CompoundBorder(new BevelBorder(BevelBorder.LOWERED, Color.BLACK, Color.DARK_GRAY), margin));
        
        // show etc
        f.setVisible(true);
        f.pack();
    }
    
    
    private void initFrameThings() {
        /*
        f.addComponentListener(new ComponentAdapter() {
        @Override
        public void componentResized(ComponentEvent e) {
        setShape(new Ellipse2D.Double(0,0,getWidth(),getHeight()));
        }
        });*/
        f.setFocusableWindowState(false);
        f.setUndecorated(true);
        f.setBackground(new Color(0, 0, 0, 100));
        f.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setAlwaysOnTop(true);
        
        // workaround for click-through
        f.addMouseListener(new MouseAdapter() {
            
            private Robot robot;
            {
                try {
                    robot = new Robot();
                } catch (AWTException ex) {
                    Logger.getLogger(ShapedWindowDemo.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            @Override
            public void mouseClicked(final MouseEvent e) {
                FocusListener listener = new FocusListener() {
                    
                    public void focusGained(FocusEvent fe) {
                    }
                    
                    public void focusLost(FocusEvent fe) {
                        robot.mousePress(InputEvent.getMaskForButton(e.getButton()));
                        robot.mouseRelease(InputEvent.getMaskForButton(e.getButton()));
                        f.setFocusableWindowState(false);
                        f.setAlwaysOnTop(true);
                        f.removeFocusListener(this);
                    }
                };
                f.setFocusableWindowState(true);
                f.requestFocus();
                f.addFocusListener(listener);
                f.setAlwaysOnTop(false);
                f.toBack();
            }
            
        });
        
    }
    
    private Map<String, String> keyPatch = new HashMap<String, String>() {{
        put("1 9", "↹");
    }};
    private JPanel addKey(NativeKeyEvent ev, int lastPressedCode) {
        String keyChar = ev.getKeyChar()+"";
        int code = ev.getKeyCode() == 0 ? lastPressedCode : ev.getKeyCode();
        String patched = keyPatch.get(ev.getKeyLocation()+" "+code);
        if (patched != null) keyChar = patched;
        return addKey(keyChar);
    }
    private JPanel addKey(String keyText) {
        JLabel l = new JLabel(keyText);
        JPanel k = new JPanel(new BorderLayout());
        // k.setOpaque(false);
        //l.setOpaque(false);
        k.setBackground(Color.LIGHT_GRAY);
        int marginX = 5, marginY = 5;
        Border margin = new EmptyBorder(marginY, marginX, marginY, marginX);
        k.setBorder(new CompoundBorder(new BevelBorder(BevelBorder.LOWERED, Color.BLACK, Color.DARK_GRAY), margin));
        l.setPreferredSize(new Dimension(40, 40));
        l.setHorizontalAlignment(JLabel.CENTER);
        k.add(l, BorderLayout.CENTER);
        content.add(k);
        content.invalidate();
        f.pack();
        return k;
    }

    private void removeKey(int fadeAfter, final int fadeFor, final JPanel keyPanel) {
        final long start = System.currentTimeMillis();
        final long fadeAt = start+fadeAfter;
        final Timer t = new Timer(50, null);
        t.addActionListener(new ActionListener() {
            int r = keyPanel.getComponent(0).getBackground().getRed();
            int g = keyPanel.getComponent(0).getBackground().getGreen();
            int b = keyPanel.getComponent(0).getBackground().getBlue();
            public void actionPerformed(ActionEvent e) {
                long now = System.currentTimeMillis();
                if (now - fadeAt > 0) {
                    content.remove(keyPanel);
                    f.pack();
                    t.stop();
                } else {
                    //float af = (fadeAt - now) / (float)(fadeFor);
                    //int a = Math.max(0, Math.min((int)(af*255), 255));
                    //System.err.println(r+" "+g+" "+b+" "+a);
                    //keyPanel.setBackground(new Color(r,g,b,a));
                    //keyPanel.setBackground(new Color(r,g,b));
                }
            }
        });
        t.start();
    }

    private boolean is(NativeKeyEvent ev, int i1, int i2) {
        return ev.getKeyLocation()==i1 && ev.getKeyCode()==i2;
    }

    private String getMod(NativeKeyEvent ev) {
        if (is(ev, 2, 16)) return "lshift";
        if (is(ev, 3, 16)) return "rshift";
        if (is(ev, 2, 17)) return "lctrl";
        if (is(ev, 3, 17)) return "rctrl";
        if (is(ev, 2, 18)) return "lalt";
        if (is(ev, 3, 18)) return "ralt";
        return null; 
    }
    
}
