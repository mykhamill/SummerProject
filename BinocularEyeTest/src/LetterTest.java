import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.Timer;
import javax.swing.JFrame;

import betPackage.TestResult;
import betPackage.TestResults;




@SuppressWarnings("serial")
public class LetterTest extends JFrame implements KeyListener, ActionListener{
	
	private BufferedImage backBuffer;
	private String[] letters; 
	private int currentLetter;
	private int letterSize;
	private Timer timeout;
	private Timer animate;
	private int state;
	private TestResults totalTest;
	private long startTime;
	private long endTime;
	private int contrast;
	private int depth;
	private Point position;
	private Point velocity;
	private String alpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
	public LetterTest() {
		super("LetterTest");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addKeyListener(this);
		setSize(800, 600);
		setBackground(Color.CYAN);
		init();
		setVisible(true);
		timeout = new Timer(2000, this);
		timeout.setRepeats(false);
		animate = new Timer(17, this);
	}
	
	public void init() {
		letters = new String[26];
		for (int i = 0; i < letters.length; i++) {
			letters[i] = alpha.substring(i, i+1);
		}
		currentLetter = 0;
		letterSize = 90;
		state = 0;
		startTime = 0;
		endTime = 0;
		contrast = 0;
		depth = 0;
		position = new Point(0,0);
		velocity = new Point(5, 5);
		totalTest = new TestResults();
	}
	
	public void paint (Graphics g) {
		if (backBuffer == null) {
			backBuffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
		}
		if (backBuffer.getWidth() != getWidth() || backBuffer.getHeight() != getHeight()) {
			backBuffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
		}
		Graphics gBuff = backBuffer.getGraphics();
		gBuff.setColor(getBackground());
		gBuff.fillRect(0,  0, getWidth(), getHeight());
		gBuff.setColor(Color.BLACK);
		if (state == 0) {
			drawString(gBuff, "Press Enter to start the test", 40, position);
		} else if (state == 20) {
			drawString(gBuff, "End of the test.", 40, new Point(0, -60));
			drawString(gBuff, "Press Enter to save the results.", 40, new Point(0, 60));
		} else if (state == 21) {
			drawString(gBuff, "Results saved", 40, position);
		} else {
			if (state > 10 && state < 20) {
				gBuff.setColor(Color.RED);
				gBuff.fillRect((getWidth() / 2) - 5, (getHeight() / 2) - 5, 10, 10);
			}
 			drawString(gBuff, letters[currentLetter], letterSize, position);
		}
		g.drawImage(backBuffer, 0, 0, null);
	}
	
	public void drawString(Graphics g, String s, int fontSize, Point position) {
		g.setColor(Color.BLACK);
		Font font = new Font("Monospace", Font.PLAIN, fontSize);
		g.setFont(font);
		int ascent = g.getFontMetrics().getAscent();
		Rectangle2D letterBounds = g.getFontMetrics(font).getStringBounds(s, g);
		g.drawString(s, getWidth()/2 - (int)(letterBounds.getWidth()/2) + position.x, getHeight() / 2 + (int)((letterBounds.getHeight()  - (ascent / 2)) / 2) + position.y);
	}
	
	public void update() {
		int width = this.getWidth() / 2;
		int height = this.getHeight() / 2;
		position.x = position.x + velocity.x;
		position.y = position.y + velocity.y;
		if (position.x < -width) {
			position.x = -(2 * width + position.x) ;
			velocity.x = -velocity.x;
		}
		if (position.y < -height) {
			position.y = -(2 * height + position.y);
			velocity.y = -velocity.y;
		}
		if (position.x > width) {
			position.x = (2 * width - position.x);
			velocity.x = -velocity.x;
		}
		if (position.y > height) {
			position.y = (2 * height - position.y);
			velocity.y = -velocity.y;
		}
	}
	
	@Override
	public void keyPressed(KeyEvent arg0) {
		String text = KeyEvent.getKeyText(arg0.getKeyCode());
		System.out.println(text +" L:" + letters[currentLetter] + " S:" + letterSize);
		if(text == "Enter") {
			if (state == 0) {
				state++;
				timeout.start();
				startTime = System.currentTimeMillis();
			} else if (state == 20) {
				totalTest.saveResults("./testResults");
				state++;
			}
		}
		if(alpha.contains(text)) {
			timeout.restart();
			endTime = System.currentTimeMillis();
			saveResult(text);
			if (text.equals(letters[currentLetter])) {
				currentLetter = (int)Math.floor(Math.random() * 26);
				letterSize -= 10;
			} else {
				currentLetter = (int)Math.floor(Math.random() * 26);
				letterSize += 20;
			}
			state++;
			if (state == 11) {
				letterSize = 90;
			}
			if (state > 10 && state < 20) {
				if (!animate.isRunning()) {
					animate.start();
				}
			}
			if (state == 20) {
				animate.stop();
				position.x = 0;
				position.y = 0;
			}
			startTime = System.currentTimeMillis();
		}
		this.paint(this.getGraphics());
	}
	
	@Override
	public void keyReleased(KeyEvent arg0) {
	}
	
	@Override
	public void keyTyped(KeyEvent arg0) {
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource() == animate) {
			if (state > 10) {
				update();
			}
		}
		if (arg0.getSource() == timeout) {
			if (state < 20) {
				endTime = System.currentTimeMillis();
				saveResult("TimeOut");
				currentLetter = (int)Math.floor(Math.random() * 26);
				letterSize += 20;
				state++;
				startTime = System.currentTimeMillis();
				timeout.restart();
				if (state == 11) {
					letterSize = 90;
				}
			}
			if (state > 10 && state < 20) {
				if (!animate.isRunning()) {
					animate.start();
				}
			}
			if (state == 20) {
				animate.stop();
				position.x = 0;
				position.y = 0;
			}
		}
		
		this.paint(this.getGraphics());
	}
	
	public void saveResult(String text) {
		totalTest.addResult(new TestResult().setChr(letters[currentLetter]).setContrast(contrast).setDepth(depth).setPosition(position).setRespChr(text).setRespTime(endTime - startTime).setSize(letterSize));
	}

	public static void main(String[] args) {
		LetterTest lt = new LetterTest();
	}
}
