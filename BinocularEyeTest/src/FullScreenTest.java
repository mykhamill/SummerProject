import java.awt.BufferCapabilities;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageFilter;
import java.awt.image.LookupOp;
import java.awt.image.ShortLookupTable;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

/** @see http://stackoverflow.com/questions/7456227 */
public class FullScreenTest extends JPanel implements ActionListener, Runnable {

    private JFrame f = new JFrame("FullScreenTest");
    private BufferStrategy bs;
    private BufferedImage[] img = new BufferedImage[2];
    private int flip = 0;
    private Timer tick = new Timer(8, this);
    private long framerate;
    private Thread animation;
    private long reportedFramerate = 0;
    private boolean running;
    private int screenWidth, screenHeight;
    
    public void actionPerformed(ActionEvent e) {
        renderLoop();
    }
    //private JButton b = new JButton(exit);

    public FullScreenTest() {
        //this.add(b);
        //f.getRootPane().setDefaultButton(b);

        //this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_Q, 0), EXIT);
        //this.getActionMap().put(EXIT, exit);
        framerate = 1000/120;
    }
    
    private void loadImages() {
    	try {
    		img[0] = ImageIO.read(new File("./left.png"));
    		img[1] = ImageIO.read(new File("./right.png"));
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }
    private void generateImages() {
    	img[0] = ImageGenerator.generateImage(screenWidth, screenHeight);
    	short[] invArray = new short[256];
    	for (short i = 0; i < invArray.length; i++) {
        	invArray[i] = (short)(invArray.length - (i + 1));
    	}
    	BufferedImageFilter invImg = new BufferedImageFilter(new LookupOp(new ShortLookupTable(0, invArray), null));
    	img[1] = invImg.getBufferedImageOp().filter(img[0], null);
    }
    
    private void display() {
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice dev = env.getDefaultScreenDevice();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setBackground(Color.white);
        f.setResizable(false);
        f.setUndecorated(true);
        f.addKeyListener(new KeyListener() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_Q) {
                    //tick.stop();
                	running = false;
                    f.dispatchEvent(new WindowEvent(f, WindowEvent.WINDOW_CLOSING));
                }
                /*if (e.getKeyCode() == KeyEvent.VK_S) {
                    if (tick.isRunning()) {
                        tick.stop();
                    } else {
                        tick.restart();
                    }
                }
                if (e.getKeyCode() == KeyEvent.VK_N) {
                    if (!tick.isRunning()) {
                        flip = (flip + 1) % img.length;
                    }
                    renderLoop();
                }
                if (e.getKeyCode() == KeyEvent.VK_P) {
                    if (!tick.isRunning()) {
                        if (flip == 0) {
                            flip = img.length - 1;
                        } else {
                            flip--;
                        }
                    }
                    renderLoop();
                }*/
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
        BufferCapabilities bc = dev.getDefaultConfiguration().getBufferCapabilities();
        System.out.println("Is fullscreen required? " + (bc.isFullScreenRequired() ? "Yes" : "No") +
                           ", is Multibuffer available? " + (bc.isMultiBufferAvailable() ? "Yes" : "No") +
                           ", capable of Page flipping? " + (bc.isPageFlipping() ? "Yes" : "No"));
        f.createBufferStrategy(2);
        bs = f.getBufferStrategy();
        screenWidth = dev.getDisplayMode().getWidth();
        screenHeight = dev.getDisplayMode().getHeight();
        //loadImages();
        generateImages();
        running = true;
        animation = new Thread(this);
        animation.start();
        //tick.setRepeats(true);
        //tick.start();
        //renderLoop();
    }
    
    private void render() {
    	BufferedImage stereoImg = new BufferedImage(screenWidth * 2, screenHeight + 1, BufferedImage.TYPE_INT_ARGB);
    	Graphics strG = stereoImg.createGraphics();
    	strG.drawImage(img[0], 0, 0, screenWidth, screenHeight / 2, 0, 0, img[0].getWidth(), img[0].getHeight(), null);
    	strG.drawImage(img[1], 0, screenHeight / 2, screenWidth, screenHeight, 0, 0, img[1].getWidth(), img[1].getHeight(), null);
    	
    	Graphics g = bs.getDrawGraphics();
        if (!bs.contentsLost()) {
        	g.drawImage(stereoImg, 0, 0, null);
        	bs.show();
            g.dispose();
        }
        flip = (flip + 1) % img.length;
    }
    
    private void renderLoop() {
        //while(true) {
// time the frame began
		    }
    
    public static void main(String[] args) {
    	FullScreenTest fst = new FullScreenTest();
    	fst.display();
    }
	@Override
	public void run() {
		// TODO Auto-generated method stub
		long frameStart;
		
		// number of frames counted this second
		long frameCount = 0;
		// time elapsed during one frame
		long elapsedTime;
		// accumulates elapsed time over multiple frames
		long totalElapsedTime = 0;
		
		while (running) {
			// the actual calculated framerate reported
			frameStart = System.currentTimeMillis();
			// paint the frame
			render();
			// calculate the time it took to render the frame
			elapsedTime = System.currentTimeMillis() - frameStart;
			// sync the framerate
			try {
				if(elapsedTime < framerate) {
					Thread.sleep(framerate - elapsedTime);
				}
				else {
					// don't starve the garbage collector
					Thread.sleep(2);
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			++ frameCount;
			totalElapsedTime += (System.currentTimeMillis() - frameStart);
			if(totalElapsedTime > 1000)
			{
				reportedFramerate = (long)((double) frameCount /
						(double) totalElapsedTime * 1000.0);
				// show the framerate in the applet status window
				//System.out.println(reportedFramerate);
				frameCount = 0;
				totalElapsedTime = 0;
			}
		}
		System.out.println("Exiting");
        //}
        /*if (!tick.isRunning()) {
            tick.start();
        }*/

	}
}
