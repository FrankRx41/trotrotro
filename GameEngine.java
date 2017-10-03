/*******************************************************************************
*******************************************************************************/
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

public class GameEngine implements Runnable{
	class GameOption{
		Font fontNormal = new Font("SimSun", Font.PLAIN, 18);
		Font fontGothic = new Font("MS Gothic", Font.PLAIN, 26);
		Font fontSkill  = new Font("YouYuan", Font.BOLD, 20);
		
		boolean playSound   = true;
		boolean SHOWFPS		= true;
		boolean ANTIALIAS	= false;
		boolean DEBUG_SHOWINFO = false;
		boolean showHelp	= true;
		boolean twicePlay	= false;
		int INTERPOLATION	= 2;
		
		int levelSelect = 0;
		
		boolean drawOutline	= false;
		boolean mouseControl	= true;
		
		public void FontSizeInc(){
			//font = new Font(font.getFamily(),font.getStyle(),font.getSize() + 2);
		}
		public void FontSizeSub(){
			//font = new Font(font.getFamily(),font.getStyle(),font.getSize() - 2);
		}
		public void maxFpsInc(float second){
			fps.maxFps += 1000*second;
		}
		public void maxFpsSub(float second){
			fps.maxFps /= 2; fps.maxFps+=1;
		}
		public void changeInterpolation(){
			INTERPOLATION++;
			if(INTERPOLATION == 3)INTERPOLATION = 0;
		}
	}
	class GameRender{
		Graphics2D graphics2dInit(Graphics g){
			g = bs.getDrawGraphics();
			g.clearRect(0,0,gc.getWidth(),gc.getHeight());
			
			Graphics2D g2d = (Graphics2D)g;
			if(go.ANTIALIAS){
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
									 RenderingHints.VALUE_ANTIALIAS_ON);
			}else{
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
						 RenderingHints.VALUE_ANTIALIAS_OFF);
			}
			switch(go.INTERPOLATION){
			case 0:
				g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
									 RenderingHints.VALUE_INTERPOLATION_BICUBIC);
				break;
			case 1:
				g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
									 RenderingHints.VALUE_INTERPOLATION_BILINEAR);
				break;
			case 2:
				g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
									 RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
				break;
			}
			g2d.setFont(go.fontNormal);
			return g2d;
		}
		public void draw() {
			/* copy: http://Fundamental-2D-Game-Programming-With-Java-master
			 \CH01\src\javagames\render\ActiveRenderingExample.java */
			do{
				do{
					Graphics g = null;
					try{
						fps.increase();
						//init g2d
						Graphics2D g2d = graphics2dInit(g);
						
						render(g2d);
					}finally{
						if( g!=null) g.dispose();
					}
				}while( bs.contentsRestored() );
				bs.show();
			}while( bs.contentsLost() );
			
		}
		void render(Graphics2D g){
			gl.draw(g);
			drawInfo(g);
		}
	}
	private ScreenWorld sw;
	private CameraWorld cw;
	private GameMusic	gm;
	private GameRender	gr;
	private GameWorld	gw;
	private GameLevel	gl;
	private GameState	gs;
	private Thread		gt;
	GameOption go;
	Timer fps;

	private Canvas gc;
	private BufferStrategy bs;
	private InputListener il;
	private WindowAdapter wa;
	
	public GameEngine(int w,int h,Canvas canvas,InputListener input,WindowAdapter component){
		gc = canvas;
		il = input;
		wa = component;
		
		sw = new ScreenWorld(w,h);
		gw = new GameWorld();
		cw = new CameraWorld(sw,gw);
		go = new GameOption();
		gr = new GameRender();
		gm = new GameMusic(go);
		
		gs = new GameState();
		gl = new GameLevel(this,gs,cw,gm,gw,sw,il);
		
		gt = new Thread(this);
		fps = new Timer(120);
		
		gc.createBufferStrategy(2);
		bs = gc.getBufferStrategy();
		
		wa.init(sw);
	}
	
	public void start(){
		gt.start();
	}
	
	public void run(){
		fps.init();
		gl.init();
		while(true){
			float second = fps.process();
			il.process(second);
			gl.process(second);
			this.process();
		}
	}
	
	void keyResponse(float second){
		//camera
		int doubled = 100;
		if(go.DEBUG_SHOWINFO){
			if(il.keyDown(il.KEY_CAMERA_MOVE_DOWN))	cw.y -= second * doubled*10;
			if(il.keyDown(il.KEY_CAMERA_MOVE_UP))	cw.y += second * doubled*10;
			if(il.keyDown(il.KEY_CAMERA_MOVE_LEFT))	cw.x -= second * doubled*10;
			if(il.keyDown(il.KEY_CAMERA_MOVE_RIGHT))cw.x += second * doubled*10;
			if(il.keyDown(il.KEY_CAMERA_ZOOM_OUT))	cw.zoomOut(second * doubled);
			if(il.keyDown(il.KEY_CAMERA_ZOOM_IN))	cw.zoomIn(second * doubled);
			if(il.keyDownOnce(il.KEY_CAMERA_RESET))	cw.reset(sw);
		}
		//
		//if(il.keyDown(il.KEY_MAXFPS_INC))		go.maxFpsInc(second);
		//if(il.keyDownOnce(il.KEY_MAXFPS_SUB))	go.maxFpsSub(second);
		//
		//if(il.keyDownOnce(il.KEY_FONT_SIZE_INC))go.FontSizeInc();
		//if(il.keyDownOnce(il.KEY_FONT_SIZE_SUB))go.FontSizeSub();
		//
		//if(il.keyDownOnce(il.KEY_SHOW_FPS_TOGGL)){
		//	go.SHOWFPS = !go.SHOWFPS;
		//}
		//if(il.keyDownOnce(il.KEY_ANTIALIASING_TOGGLE)){
		//	go.ANTIALIAS = !go.ANTIALIAS;
		//}
		//if(il.keyDownOnce(il.KEY_INTERPOLATION_TOGGLE)){
		//	go.changeInterpolation();
		//}
		if(il.keyDownOnce(il.KEY_DEBUG_SHOWINFO)){
			go.DEBUG_SHOWINFO = !go.DEBUG_SHOWINFO;
		}
		if(il.keyDownOnce(il.KEY_DEBUG_DRAWOUTLINE)){
			go.drawOutline = !go.drawOutline;
		}
		if(il.keyDownOnce(il.KEY_SOUND_TOGGLE)){
			go.playSound = !go.playSound;
			gm.toggleBGM();
		}
		if(il.keyDownOnce(il.KEY_RESTART)){
			gl.init();
		}
		
		//test only
		//if(il.keyDownOnce(KeyEvent.VK_A))System.out.print("A");
		//if(il.keyDownOnce(KeyEvent.VK_S))System.out.print("S");
		//if(il.keyDownOnce(KeyEvent.VK_D))System.out.print("D");
		//if(il.keyDownOnce(KeyEvent.VK_W))System.out.print("W");
		//if(il.keyDownOnce(KeyEvent.VK_ENTER))System.out.println("  >");
		//System.out.println(String.format("%s",gs.running));
	}
	
	void drawInfo(Graphics g){
		//draw fps;
		int pos = 30;
		//fps.draw(g2d);
		if(go.SHOWFPS){
			g.setColor(Color.yellow);
			pos = Utility.drawString(g,0,0,fps.frameRate);
			//System.out.println(String.format("%.8f",second));			
		}
		//gameworld coord to screen coord through camera 
		int tx = 300, ty = 300;
		int x = cw.toScreenX(0);
		int tox = cw.toScreenX(tx);
		int y = cw.toScreenY(0);
		int toy = cw.toScreenY(ty);

		//draw coordinate
		g.setColor(Color.red);
		g.setColor(Color.white);
		//g.drawLine((int)(x-gw.w*cw.zoom/2),y,(int)(x+gw.w*cw.zoom/2),y);
		//g.drawLine(x,(int)(y-gw.h*cw.zoom/2),x,(int)(y+gw.h*cw.zoom/2));
		
		
		//debug info
		if(go.DEBUG_SHOWINFO){
		g.setColor(Color.green);
			pos = 400;
			pos = Utility.drawString(g,0,pos,"[WORLD]");
			pos = Utility.drawString(g,10,pos,String.format("zoom: %.2f",cw.zoom));
			pos = Utility.drawString(g,10,pos,String.format("camera (%+5.2f,%+5.2f)",cw.x,cw.y));
			pos = Utility.drawString(g,10,pos,String.format("w:  %5.2f  h:  %5.2f",cw.w,cw.h));
			
			pos = Utility.drawString(g,0,pos,"[SETTING]");
			pos = Utility.drawString(g,10,pos,String.format("maxFps: %d",fps.maxFps));
			pos = Utility.drawString(g,10,pos,String.format("font-size: %d",g.getFont().getSize()));
			pos = Utility.drawString(g,10,pos,"ANTIALIAS: " + go.ANTIALIAS);
			pos = Utility.drawString(g,10,pos,"INTERPOLATION: " + (go.INTERPOLATION==0?"BICUBIC":go.INTERPOLATION==1?"BILINEAR":"NEAREST_NEIGHBOR"));
		}
	}
	
	void process(){

		gr.draw();
		
	}	
}