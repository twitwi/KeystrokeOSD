

package com.heeere.osd.keystrokeosd;

import java.util.concurrent.BlockingQueue;
import static com.heeere.osd.keystrokeosd.QueueTools.*;
import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Robot;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

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
    
    protected void process(Event e) {
        switch (e.type) {
            case KEY_TYPED:
                addKey(e.asKeyTyped().ev.getKeyChar()+"");
                break;
        }
    }
    
    JFrame f;
    JPanel content;
    
    private void initComponents() {
        f = new JFrame("Key OSD");
        initFrameThings();
        // now the panel
        content = new JPanel();
        f.setLayout(new BorderLayout());
        f.setContentPane(content);
        content.setLayout(new BoxLayout(content, BoxLayout.LINE_AXIS));
        content.setOpaque(false);
        int marginX = 10, marginY = 10;
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
    
    private void addKey(String keyChar) {
        JLabel l = new JLabel(keyChar);
        l.setOpaque(true);
        l.setBackground(Color.LIGHT_GRAY);
        //aaaaaabibibaebbbl.setBorder(new BevelBorder(BevelBorder.LOWERED, Color.BLACK, Color.DARK_GRAY));
        l.setPreferredSize(new Dimension(30, 30));
        content.add(l);
        content.invalidate();
        f.pack();
    }
    
}
