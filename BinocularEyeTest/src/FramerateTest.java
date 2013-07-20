import java.applet.*;
import java.awt.*;
public class FramerateTest extends Applet implements Runnable
{
	// a Thread for animation
	private Thread animation;
	// the minimum number of milliseconds spent per frame
	private long framerate;
	public void init()
	{
		setBackground(Color.BLACK);
		animation = new Thread(this);
		// set the framerate to 60 frames per second (16.67 ms / frame)
		framerate = 1000/60;
	}
	public void start()
	{
		// start the animation thread
		animation.start();
	}
	public void stop()
	{
		animation = null;
	}
	public void run()
	{
		// time the frame began
		long frameStart;
		// number of frames counted this second
		long frameCount = 0;
		// time elapsed during one frame
		long elapsedTime;
		// accumulates elapsed time over multiple frames
		long totalElapsedTime = 0;
		// the actual calculated framerate reported
		long reportedFramerate;
		Thread t = Thread.currentThread();
		while (t == animation)
		{
			// save the start time
			frameStart = System.currentTimeMillis();
			// paint the frame
			repaint();
			// calculate the time it took to render the frame
			elapsedTime = System.currentTimeMillis() - frameStart;
			// sync the framerate
			try
			{
				// make sure framerate milliseconds have passed this frame
				if(elapsedTime < framerate)
				{
					Thread.sleep(framerate - elapsedTime);
				}
				else
				{
					// don't starve the garbage collector
					Thread.sleep(5);
				}
			}
			catch(InterruptedException e)
			{
				break;
			}
			// update the actual reported framerate
			++ frameCount;
			totalElapsedTime += (System.currentTimeMillis() - frameStart);
			if(totalElapsedTime > 1000)
			{
				reportedFramerate = (long)((double) frameCount /
						(double) totalElapsedTime * 1000.0);
				// show the framerate in the applet status window
				showStatus("fps: " + reportedFramerate);
				frameCount = 0;
				totalElapsedTime = 0;
			}
		}
	} // run
} // FramerateTest