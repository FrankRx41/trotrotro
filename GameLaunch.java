/*******************************************************************************
this class create a window and a canvas and add canvas key-listener.
those are all no connect to game-loop.
*******************************************************************************/
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;

import javax.imageio.*;
import javax.swing.*;

public class GameLaunch{
	private JFrame frame;
	private Canvas canvas;
	private InputListener input;
	private WindowAdapter component;
	private GameEngine ge;
	
	public GameLaunch(){
		input = new InputListener();
		component = new WindowAdapter();
		canvas = new Canvas();
		canvas.setSize(1200,640);
		canvas.setBackground(Color.black);
		canvas.addKeyListener(input);
		canvas.addMouseListener(input);
		canvas.addMouseMotionListener(input);
		canvas.addComponentListener(component);
		canvas.setIgnoreRepaint(true);
		
		/* Copy: https://stackoverflow.com/questions/24800417/
		   why-cant-i-get-keyevent-vk-tab-when-i-use-key-binding-for-a-jpanel */
		//use to get VK_TAB event.
		Set<KeyStroke> forwardKeys = new HashSet<KeyStroke>(1);
	    forwardKeys.add(KeyStroke.getKeyStroke(
	        KeyEvent.VK_TAB, InputEvent.CTRL_MASK));
	    canvas.setFocusTraversalKeys(
	        KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, forwardKeys);
		        
		/* Copy: https://stackoverflow.com/questions/3680221/how-can-i-get-screen-resolution-in-java */
	    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int w = dim.getSize().width;
        int h = dim.getSize().height;
        int x = w/2-1200/2;
        int y = h/2-640/2;
        //System.out.println(w+","+h);
	    
		
		frame = new JFrame("tro tro tro (ver 1.0)");
		frame.getContentPane().add(canvas);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		canvas.requestFocusInWindow();
		canvas.requestFocus();
		frame.setLocation(x,y);
		frame.setVisible(true);
		
		ge = new GameEngine(1200,640,canvas,input,component);
		ge.start();
		//System.err.println("starting...");
	}
	
	public static void main(String[] args){
		new GameLaunch();
	}
}