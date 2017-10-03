import java.awt.*;

/* copy: my old file Timer.java */
public class Timer {
	int frameCount;
	long currTime;
	long lastTime;
	long nsPerFrame;
	int maxFps;
	long nsHadPass;
	String frameRate = "99";
	
	private void setFrameRateString(int frameCount){
		frameRate = String.format("%02.0f",frameCount*1.0E9/nsHadPass);
		//System.out.println(frameCount);
	}
	
	public Timer(int max){
		maxFps = max;
		setFrameRateString(maxFps);
	}
	
	public void init(){
		frameCount 	= 0;
		nsLastLeaving 	= 0;
		nsHadPass 	= 0;
		nsPerFrame 	= 0;
		nsLastSleep 	= 0;
		lastTime = System.nanoTime();
	}
	//sleep and calc fps.
	//return how much second pass in this frame, it must small than 0.1
	public float process(){
		currTime = System.nanoTime();
		//this nsPerFrame form last-sleep-begin to this-sleep-begin
		nsPerFrame = currTime - lastTime;
		delayWait(nsPerFrame);
		lastTime = currTime;
		
		//nsPerFrame = System.nanoTime() - currTime;
		
		//measure fps
		nsHadPass += nsPerFrame;
		if(nsHadPass > 1.0E9){
			setFrameRateString(frameCount);
			frameCount = 0;
			nsHadPass = 0;
		}
		
		return (float)(nsPerFrame/1.0E9);
	}
	long nsLastSleep;	//last sleep ns
	void delayWait(long ns){
		ns -= nsLastSleep;
		nsLastSleep = (1000000000/maxFps) - ns;
		sleep(nsLastSleep);
	}
	long nsLastLeaving;
	void sleep(long ns){
		if(ns < 0)return;
		nsLastLeaving += ns;
		long ms = nsLastLeaving/1000000;
		nsLastLeaving -= ms*1000000;
		try{
			Thread.sleep(ms);
		}catch(Exception e){
			System.out.println("Error: "+e);
		}
	}
	//draw once, increase one
	public void increase(){
		frameCount++;
	}
	public void draw(Graphics g){
		Color c = g.getColor();
		g.setColor(Color.yellow);
		g.drawString(frameRate,5,15);
		g.setColor(c);
		//System.out.println(nsPerFrame);
	}
}
