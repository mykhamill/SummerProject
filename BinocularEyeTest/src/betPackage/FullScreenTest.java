package betPackage;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.image.BufferedImage;
import java.awt.event.KeyListener;
import java.awt.event.ActionListener;
import java.awt.BufferCapabilities;
/** @see http://stackoverflow.com/questions/7456227 */
public class FullScreenTest extends JPanel implements ActionListener {

    private JFrame f = new JFrame("FullScreenTest");
    private BufferStrategy bs;
    private BufferedImage[] img = new BufferedImage[2];
    private BufferedImage buf;
    private int x, y, v_x, v_y;
    private int max_w, max_h;
    private int count = 0;
    private Timer tick = new Timer(25, this);
     
    public void actionPerformed(ActionEvent e) {
        count = (count + 1) % 30;
        renderLoop();
    }

    public FullScreenTest() {
        x = 50;
        y = 50;
        v_x = 5;
        v_y = 7;
    }

    private void display() {
        
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice dev = env.getDefaultScreenDevice();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setBackground(Color.red);
        f.setResizable(false);
        f.setUndecorated(true);
        f.addKeyListener(new KeyListener() {
            public void keyPressed(KeyEvent e) {
                System.out.println("Key pressed : " + e.getKeyChar());
                if (e.getKeyCode() == KeyEvent.VK_Q) {
                    tick.stop();
                    f.dispatchEvent(new WindowEvent(f, WindowEvent.WINDOW_CLOSING));
                }
                if (e.getKeyCode() == KeyEvent.VK_S) {
                    if (tick.isRunning()) {
                        tick.stop();
                    } else if(!tick.isRunning()) {
                        count = 0;
                        tick.restart();
                    }
                }
                if (e.getKeyCode() == KeyEvent.VK_N) {
                    if (!tick.isRunning()) {
                        count = (count + 1) % 30;
                        renderLoop();
                    }
                }
            };
            public void keyReleased(KeyEvent e) {};
            public void keyTyped(KeyEvent e) {};
        });
        f.addMouseMotionListener(new MouseAdapter() {

            @Override
            public void mouseMoved(MouseEvent e) {
                FullScreenTest.this.setToolTipText(
                    "("+ e.getX() + "," + e.getY() + ")");
            }
        });
        //f.add(this);
        f.pack();
        dev.setFullScreenWindow(f);
        max_h = dev.getDisplayMode().getHeight();
        max_w = dev.getDisplayMode().getWidth();
        buf = new BufferedImage(max_w, max_h, BufferedImage.TYPE_INT_RGB);
        buf.getGraphics().fillRect(0, 0, max_w, max_h);
        f.createBufferStrategy(2);
        bs = f.getBufferStrategy();
        Graphics tempg = bs.getDrawGraphics();
        tempg.drawImage(buf, 0,0, Color.gray, null);
        bs.show();
        tempg.dispose();
        BufferCapabilities bc = dev.getDefaultConfiguration().getBufferCapabilities();
        System.out.println("Is fullscreen required? " + (bc.isFullScreenRequired() ? "Yes" : "No") +
                           ", is Multibuffer available? " + (bc.isFullScreenRequired() ? "Yes" : "No") +
                           ", capable of Page flipping? " + (bc.isFullScreenRequired() ? "Yes" : "No"));
        tick.setRepeats(true);
        //tick.start();
        screenSetup();
        
    }
    
    private void update() {
        x = x + v_x;
        y = y + v_y;
        if (x < 10 || x > max_w - 10) {
            v_x = -v_x;
        }
        if (y < 10 || y > max_h - 10) {
            v_y = -v_y;
        }
        if (count == 1) {
            //System.out.format("center: (%d,%d) screen size:(%d,%d)\n", x, y, max_w, max_h);
        }
    }
    
    private void screenSetup() {
        Graphics tempg = bs.getDrawGraphics();
        tempg.drawImage(buf, 0,0, Color.gray, null);
        bs.show();
        tempg.dispose();
        tempg = bs.getDrawGraphics();
        tempg.drawImage(buf, 0,0, Color.gray, null);
        bs.show();
        tempg.dispose();
    }
    
    private void render() {
        Graphics buf_g = buf.getGraphics();
        buf_g.setColor(Color.blue);
        buf_g.drawOval(x - 10, y - 10, 20, 20);
    }
    
    private void derender() {
        Graphics buf_g = buf.getGraphics();
        buf_g.setColor(Color.gray);
        buf_g.drawOval(x - 10, y - 10, 20, 20);
    }
    
    private void renderLoop() {
        Graphics g = bs.getDrawGraphics();
        if (!bs.contentsLost()) {
            render();
            g.drawImage(buf, 0,0, Color.gray, null);
            bs.show();
            g.dispose();
            derender();
            update();
        }
    }
    
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {

            public void run() {
                new FullScreenTest().display();
            }
        });
    }
}
