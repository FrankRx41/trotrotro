/*******************************************************************************
Bugs:
001:zoomIn and zoomOut func: if camera x or y is no center, it can center.
002:when zoom is near 23, the screen will be transpose.

Update:
001:x and y not center because it using (cx,cy) as center to zoom, not (0,0).
	i don't thing i should make it more better.
*******************************************************************************/
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;

/*******************************************************************************
this width and height change only when window size changed.
*******************************************************************************/
class ScreenWorld{
	public int w,h;
	public ScreenWorld(int w,int h){
		this.w = w;
		this.h = h;
	}
	public ScreenWorld(){
		this(640,480);
	}
	public ScreenWorld(int w){
		this(w,(int)(w/4.0*3));
	}
}
/*******************************************************************************
this class only setting game world's size.
the value of width and height calc Mode in game world or not.
Notice:
the (0,0) is in the center, so sometime you needed is w/2 and h/2.
*******************************************************************************/
class GameWorld{
	public float w,h;
	public GameWorld(){
		w = 960 * 4;
		h = 720 * 2;
	}
	boolean isInWorld(ModeList.Mode m){
		return !(m.x>w/2 || m.x<-w/2 || m.y>h/2 || m.y<-h/2);
	}
}
/*******************************************************************************
this class divide a rectangle to draw in screen.
so the rate of camera-world and game-world is 1:1. and this class connect 
Screen-world and Game-world.
function to-screen translate game-coord to screen-coord
*******************************************************************************/
class CameraWorld{
	public float x,y;	//position of left-top
	public float h,w;
	public float zoom;	//rate of screen-world and game-world
	//there connect screen-world and game-world
	public ScreenWorld sw;
	public GameWorld gw;
	private void init(){
		x = -320;
		y = +480;
		h = sw.h;
		w = sw.h;
		//this zoom use width value to calc
		zoom = 2.0f;
		reset(sw);
	}
	public CameraWorld(ScreenWorld sw,GameWorld gw){
		this.sw = sw;
		this.gw = gw;
		init();
	}
	boolean isInCamera(ModeList.Mode m){
		return m.x > x && m.x < x+w && m.y < y && m.y > 0;
	}
	//Important: x and y have different zoom function,because coord of y is inv.
	public int toScreenX(float x){
		//out of world
		if(x<-gw.w/2 || x>gw.w/2)
			return -1;
		//translation and scale
		x -= this.x;
		x *= zoom;
		return (int)x;
		//return (int)((x-this.x)*zoom);
	}
	public int toScreenY(float y){
		if(y<-gw.h/2 || y>gw.h/2)
			return -1;
		y *= -1;
		y += this.y;
		y *= zoom;
		return (int)y;
	}
	public int toScreenSize(float x){
		return (int)(x*zoom);
	}
	public float toWorldX(int x){
		float rx = (float)x/zoom;
		rx += this.x;
		return rx;
	}
	public float toWorldY(int y){
		float ry = y/zoom;
		ry -= this.y;
		ry *= -1;
		return ry;
	}
	//zoom camera's size
	public void zoomOut(float size){
		//System.out.println(size);
		//float cx = x+w/2;
		//float cy = y-h/2;
		//w += size;
		//h = w*3/4;
		//zoom = (float)sw.w/(float)w;
		zoom += size/100;
		//x = cx - w/2;
		//y = cy + h/2;
	}
	public void zoomIn(float size){
		//float cx = x+w/2;
		//float cy = y-h/2;
		//w -= size;
		//h = w*3/4;
		//zoom = (float)sw.w/(float)w;
		zoom -= size/100;
		//x = cx - w/2;
		//y = cy + h/2;
	}
	public void zoomOut(){
		zoomOut(10);
	}
	public void zoomIn(){
		zoomIn(10);
	}
	public void reset(ScreenWorld sw){
		//init();
		x = -sw.w/zoom/2;
		y = sw.h/zoom;
	}
	//i don't want to add two float named cx and cy
	//preserve them using to much res
	public float getCx(){
		return x+w/2;
	}
	public float getCy(){
		return y-h/2;
	}
}