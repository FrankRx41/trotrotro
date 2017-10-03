/*******************************************************************************
Bugs:
001:key-down-once of "F10" is out of time
*******************************************************************************/
import java.awt.event.*;
import java.util.*;

class WindowAdapter implements ComponentListener{
	ScreenWorld sw;
	public void init(ScreenWorld sw){
		this.sw = sw;
	}
	public void componentResized(ComponentEvent e) {
        //System.out.println(e.getComponent().getBounds().getMaxX());
        //System.out.println(e.getComponent().getBounds().getMaxY());
        //System.out.println(e.getComponent().getBounds().getMinX());
        //System.out.println(e.getComponent().getBounds().getMinY());
        sw.w = (int) e.getComponent().getBounds().getMaxX();
        sw.h = (int) e.getComponent().getBounds().getMaxY();
     }
     public void componentMoved(ComponentEvent e) {}
     public void componentShown(ComponentEvent e) {}
     public void componentHidden(ComponentEvent e) {}

}

class InputListener implements KeyListener, MouseListener,MouseMotionListener{
	public int
		//game control
		KEY_UP 		= KeyEvent.VK_UP,
		KEY_DOWN	= KeyEvent.VK_DOWN,
		KEY_LEFT	= KeyEvent.VK_A,
		KEY_RIGHT	= KeyEvent.VK_D,
		KEY_BRAKE	= KeyEvent.VK_X,
		KEY_FIRE	= KeyEvent.VK_SPACE,
		//option control
		KEY_OK		= KeyEvent.VK_ENTER,
		//KEY_SKILL_RECRUIT_BULLETS	= KeyEvent.VK_Q,
		//KEY_SKILL_LIGHTNING_SHOOT	= KeyEvent.VK_E,
		//KEY_SKILL_NUCLEAR_TURBINE 	= KeyEvent.VK_C,
		//KEY_SKILL_REPAIR 		= KeyEvent.VK_R,
		//KEY_SKILL_UPDATE_FIRE	= KeyEvent.VK_F,

		KEY_SHOW_INFO	= KeyEvent.VK_TAB,
		KEY_PAUSE		= KeyEvent.VK_ESCAPE,
		KEY_RESTART		= KeyEvent.VK_F12,
						
		KEY_SOUND_ON	= KeyEvent.VK_F11,
		KEY_SOUND_TOGGLE	= KeyEvent.VK_F10,
		KEY_FPS_HIDE,
		
		KEY_MAXFPS_INC	= KeyEvent.VK_NUMPAD9,
		KEY_MAXFPS_SUB	= KeyEvent.VK_NUMPAD3,
		
		KEY_FONT_SIZE_INC	= KeyEvent.VK_NUMPAD7,
		KEY_FONT_SIZE_SUB	= KeyEvent.VK_NUMPAD1,
		
		KEY_ANTIALIASING_TOGGLE = KeyEvent.VK_F7,
		KEY_INTERPOLATION_TOGGLE= KeyEvent.VK_F8,
		KEY_SHOW_FPS_TOGGL	= KeyEvent.VK_F9,
		
		KEY_CAMERA_ZOOM_OUT	= KeyEvent.VK_ADD,
		KEY_CAMERA_ZOOM_IN	= KeyEvent.VK_SUBTRACT,
		KEY_CAMERA_MOVE_UP		= KeyEvent.VK_NUMPAD8,
		KEY_CAMERA_MOVE_DOWN	= KeyEvent.VK_NUMPAD2,
		KEY_CAMERA_MOVE_LEFT	= KeyEvent.VK_NUMPAD4,
		KEY_CAMERA_MOVE_RIGHT	= KeyEvent.VK_NUMPAD6,
		KEY_CAMERA_RESET		= KeyEvent.VK_NUMPAD5,
		
		KEY_DEBUG_KILLSELF	= KeyEvent.VK_F4,
		KEY_DEBUG_DRAWOUTLINE 	= KeyEvent.VK_F2,
		KEY_DEBUG_BEST_STATE	= KeyEvent.VK_F6,
		KEY_DEBUG_SHOWINFO	= KeyEvent.VK_F3,
		KEY_DEBUG_ADD_SCORE = KeyEvent.VK_F5
		
	;
	
	class Event{
		KeyEvent keyEvent;
		boolean keyIsDown;
		Event(KeyEvent key,boolean down){
			keyEvent = key;
			keyIsDown = down;
		}
	}
	
	//all key event will poll here
	//to read them, just need a while to cycle them.
	LinkedList<Event> keyEventList = new LinkedList<Event>();
	LinkedList<Event> tepEventList = new LinkedList<Event>();
	Event e;
	int maxKeyID = 256;
	//this array save press times in one frame
	int[] vKeyPress = new int[maxKeyID];
	
	//this func return last input-key
	//if null, return -1
	public synchronized int readNextKey(){
		e = tepEventList.poll();
		if(e != null){
			int id = e.keyEvent.getKeyCode();
			if( id>=0 && id<vKeyPress.length ){
				if(e.keyIsDown == true){
					vKeyPress[id]++;
					//Increase had undertake by keyPressTimeIncrease()
				}else{
					vKeyPress[id] = 0;
				}
			}
			return id;
		}else{
			return -1;
		}
	}
	
	int getLastDownKey(){
		while(keyEventList.isEmpty())
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		;
		return keyEventList.poll().keyEvent.getKeyCode();
	}
	
	boolean keyDown(int id){
		return vKeyPress[id] > 0;
	}
	boolean keyDownTime(int id,int time){
		return vKeyPress[id] == time;
	}
	boolean keyDownOnce(int id){
		if(e == null)return false;
		return e.keyEvent.getKeyCode() == id && keyDownTime(id,1);
	}
	boolean keyDownTwice(int id){
		if(e == null)return false;
		return e.keyEvent.getKeyCode() == id && keyDownTime(id,2);
	}
	
	void keyPressTimeIncrease(){
		//auto increase, make sure key-down-time work
		for(int i=0; i<vKeyPress.length; i++){
			if(vKeyPress[i] != 0) vKeyPress[i]++;
		}
	}
	@Override
	public void keyPressed(KeyEvent e){
		keyEventList.add( new Event(e,true) );
	}
	@Override
	public void keyReleased(KeyEvent e){
		keyEventList.add( new Event(e,false) );
	}
	@Override
	public void keyTyped(KeyEvent e){}


	int mouseX,mouseY;
	int mouseKeyPress[] = new int[10];
	int mouseKeyDown[]  = new int[10];
	float getMouseX(CameraWorld cw){
		return cw.toWorldX(mouseX);
	}
	float getMouseY(CameraWorld cw){
		return cw.toWorldY(mouseY);
	}
	boolean MouseDownOnce(int id){
		return mouseKeyPress[id] == 1;
	}
	boolean MouseDown(int id){
		return mouseKeyPress[id] > 0;
	}
	
	void mousePressTimeIncrease(){
		//auto increase, make sure key-down-time work
		for(int i=0; i<mouseKeyPress.length; i++){
			if(mouseKeyDown[i] != 0) mouseKeyPress[i]++;
			else mouseKeyPress[i] = 0;
		}
	}

	public void mouseClicked(MouseEvent e){}

	public void mouseEntered(MouseEvent e){
		mouseMoved(e);
	}

	public void mouseExited(MouseEvent e){
		mouseMoved(e);
	}

	public void mousePressed(MouseEvent e){
		mouseKeyDown[e.getButton()] = 1;
	}

	public void mouseReleased(MouseEvent e) {
		mouseKeyDown[e.getButton()] = 0;
	}

	public void mouseDragged(MouseEvent e){
		mouseX = e.getX();
		mouseY = e.getY();
	}

	public void mouseMoved(MouseEvent e){
		mouseX = e.getX();
		mouseY = e.getY();
	}
	
	void process(float second){
		//needn't second but for style
		LinkedList<Event> swap = keyEventList;
		keyEventList = tepEventList;
		tepEventList = swap;
		//keyPressTimeIncrease();
		
		mousePressTimeIncrease();
	}
}