import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;




@SuppressWarnings("serial")
public class LetterTest extends JFrame implements KeyListener{
	
	private BufferedImage backBuffer;
	private String[] letters; 
	private int currentLetter;
	private int letterSize;
	public LetterTest() {
		super("LetterTest");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addKeyListener(this);
		setSize(800, 600);
		setBackground(Color.CYAN);
		init();
		setVisible(true);
	}
	
	public void paint (Graphics g) {
		if (backBuffer == null) {
			backBuffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
		}
		if (backBuffer.getWidth() != getWidth() || backBuffer.getHeight() != getHeight()) {
			backBuffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
		}
		Graphics gBuff = backBuffer.getGraphics();
		g.setColor(getBackground());
		gBuff.fillRect(0,  0, getWidth(), getHeight());
		gBuff.setColor(Color.BLACK);
		Font font = new Font("Monospace", Font.PLAIN, (int)(getHeight() * (letterSize / 100.0)));
		gBuff.setFont(font);
		int ascent = gBuff.getFontMetrics().getAscent();
		Rectangle2D letterBounds = gBuff.getFontMetrics(font).getStringBounds(letters[currentLetter], gBuff);
		gBuff.drawString(letters[currentLetter], getWidth()/2 - (int)(letterBounds.getWidth()/2), getHeight() / 2 + (int)((letterBounds.getHeight()  - (ascent / 2)) / 2));
		g.drawImage(backBuffer, 0, 0, null);
		/*
		g.setColor(Color.MAGENTA);
		g.drawRect(0,0,(int)letterBounds.getWidth(), (int)letterBounds.getHeight());
		g.setColor(Color.RED);
		g.drawLine(getWidth()/2,  0, getWidth()/2, getHeight());
		g.drawLine(0, getHeight() / 2 + (int)((letterBounds.getHeight() - (ascent / 2)) / 2), getWidth(), getHeight() / 2 + (int)((letterBounds.getHeight() - (ascent/2)) / 2));
		*/
	}
	
	public static void main(String[] args) {
		LetterTest lt = new LetterTest();
	}

	public void init() {
		String alpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		letters = new String[26];
		for (int i = 0; i < letters.length; i++) {
			letters[i] = alpha.substring(i, i+1);
		}
		currentLetter = 0;
		letterSize = 90;
		
	}
	
	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		String text = KeyEvent.getKeyText(arg0.getKeyCode());
		System.out.println(text +" L:" + letters[currentLetter] + " S:" + letterSize);
		if(text == "Right") {
			currentLetter++;
			currentLetter = currentLetter % letters.length;
		}
		if(text == "Left") {
			currentLetter--;
			if (currentLetter < 0) {
				currentLetter = letters.length - 1;
			};
		}
		if(text == "Up") {
			letterSize++;
			letterSize = letterSize % 100;
			if (letterSize == 0) {
				letterSize = 1;
			}
		}
		if(text == "Down") {
			letterSize--;
			if (letterSize < 1) {
				letterSize = 99;
			};
		}
		this.paint(this.getGraphics());
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
