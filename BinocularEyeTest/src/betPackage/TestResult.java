package betPackage;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.*;
public class TestResult {

	private int size = 0;
	private String chr = "";
	private String respChr = "";
	private long respTime = System.currentTimeMillis();
	private int contrast;
	private int depth;
	private Point position = new Point(0, 0);
	
	
	public TestResult () {
		/*
		 * size
		 * character
		 * response character
		 * response time
		 * contrast
		 * depth
		 * position
		 * 
		 */
		
	}
	
	public TestResult setSize (int size) {
		this.size = size;
		return this;
	}
	
	public TestResult setChr (String chr) {
		if (chr.length() >= 1) {
			this.chr = chr.substring(0, 1);
		}	
		return this;
	}
	
	public TestResult setRespChr(String chr) {
		this.respChr = chr;
		return this;
	}
	
	public TestResult setRespTime (long t) {
		this.respTime = t;
		return this;
	}
	
	public TestResult setContrast(int c) {
		this.contrast = c;
		return this;
	}
	
	public TestResult setDepth(int d) {
		this.depth = d;
		return this;
	}
	
	public TestResult setPosition(Point p) {
		this.position.x = p.x;
		this.position.y = p.y;
		return this;
	}
	
	public int getSize () {
		return this.size;
	}
	
	public String getChr () {
		return this.chr;
	}
	
	public String getRespChr () {
		return this.respChr;
	}
	
	public long getRespTime () {
		return this.respTime;
	}
	
	public int getContrast () {
		return this.contrast;
	}
	
	public int getDepth () {
		return this.depth;
	}
	
	public Point getPosition() {
		return this.position;
	}
	public String toString() {
		return getChr() + "," + getSize() + "," + getRespChr() + "," + getRespTime() + "," + getContrast() + "," + getDepth() + "," + getPosition().x + "," + getPosition().y;
	}
}
