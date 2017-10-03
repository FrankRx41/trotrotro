import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import javax.imageio.*;

/*******************************************************************************
i am not sure this divide is good or bad.
*******************************************************************************/
class GameState{
	public boolean running = false;
	public boolean inOP	   = false;
	public boolean gameOver = true;
}

class GameLevel {
	Font fontBig  	= new Font("MS Gothic", Font.PLAIN, 36);
	Font fontNormal = new Font("MS Gothic", Font.PLAIN, 20);
	Font fontSkill  = new Font("MS Gothic", Font.PLAIN, 24);
	class Table{
		final static int big = 2,normal = 1;
		int x,y,w,h,type;
		String str;
		Color c;
		Table(int x,int y,String str,int type){
			this.x = x;
			this.y = y;
			this.str = str;
			this.type = type;
			Canvas ca = new Canvas();
			BufferedImage t = new BufferedImage(20, 20, BufferedImage.TYPE_INT_RGB);
			FontMetrics fm;
			switch(type){
			case normal:
				fm = ca.getFontMetrics(fontNormal);
				break;
			case big:
				fm = ca.getFontMetrics(fontBig);
				break;
			default:
				fm = ca.getFontMetrics(fontNormal);				
			}
			//FontMetrics fm = t.getGraphics().getFontMetrics();
			w = fm.stringWidth(str);
			h = fm.getAscent() + fm.getDescent() + fm.getLeading();
			
			c = new Color(255,150,0);
			c = Color.white;
		}
		void draw(Graphics g){
			switch(type){
			case normal:
				g.setFont(fontNormal);
				break;
			case big:
				g.setFont(fontBig);
				break;
			default:
				g.setFont(fontNormal);
			}
			FontMetrics fm = g.getFontMetrics();
			Color co = g.getColor();
			g.setColor(c);
			g.drawString(str,x,y + fm.getAscent());
			
			if(ge.go.drawOutline){
				g.setColor(Color.green);
				g.drawRect(x,y,w,h);
			}
			
			g.setColor(co);
			g.setFont(ge.go.fontNormal);
		}
	}
	class ScenceOP{
		Table PLAY = new Table(500,450,"劇情模式",Table.big);
		Table PLAY2 = new Table(500,500,"街機模式",Table.big);
		Table HELP = new Table(200,450,"遊戲幫助",Table.big);
		Table OPTION = new Table(800,450,"設置選項",Table.big);
		boolean showHelp = false;
		boolean showOption = false;

		Table EXIT = new Table(800,450,"  返回  ",Table.big);
		
		Table Option 	= new Table(200,100,"遊戲設置",Table.big);
		Table SOUND,HELPER;
		Table LEFT,RIGHT,UP,DOWN,STOP,TABLE;
		
		Table SKILL1,SKILL2,SKILL3,SKILL4, SKILL5;
		
		Table MSG 		= new Table(200,425,"提示：可以使用下面的快速設置，也可以點擊逐個進行自訂義設置",Table.normal);
		Table SETTING1	= new Table(200,450,"[默認1]",Table.normal);
		Table SETTING2	= new Table(300,450,"[默認2]",Table.normal);
		Table SETTING3	= new Table(400,450,"[默認3]",Table.normal);
		Table Advanced 	= new Table(sw.w/2,100,"高級設置",Table.big);
		Table ANTIALIASING,INTERPOLATION;
		
		Table FPSTABLE;
		Table FPSUP    	= new Table(sw.w/2+150,225,"[增加]",Table.normal);
		Table FPSDOWN  	= new Table(sw.w/2+210,225,"[減少]",Table.normal);
		Table FPSDEF   	= new Table(sw.w/2+270,225,"[默認]",Table.normal);
		
		Table SHOWDEBUGINFO;
		Table SHOWFPS;
		
		Table SELECT;
		Table SELECTUP    	= new Table(sw.w/2+150,350,"[增加]",Table.normal);
		Table SELECTDOWN  	= new Table(sw.w/2+210,350,"[減少]",Table.normal);
		
		void drawOption(Graphics g){
                                                             
			SOUND	= new Table(200,150,"播放聲音：" + (ge.go.playSound?"<開>":"<關>"),Table.normal);                                                        
			HELPER	= new Table(200,175,"顯示幫助：" + (ge.go.showHelp?"<開>":"<關>"),Table.normal);                                                        
			                                                                                                               
			UP 		= new Table(200,225,"砲口抬升： "+KeyEvent.getKeyText(il.KEY_UP),Table.normal);                           
			DOWN 	= new Table(200,250,"砲口降低： "+KeyEvent.getKeyText(il.KEY_DOWN),Table.normal);                         
			LEFT 	= new Table(400,225,"戰艦左移： "+KeyEvent.getKeyText(il.KEY_LEFT),Table.normal);                         
			RIGHT 	= new Table(400,250,"戰艦右移： "+KeyEvent.getKeyText(il.KEY_RIGHT),Table.normal);                        
			STOP 	= new Table(200,275,"緊急停止： "+KeyEvent.getKeyText(il.KEY_BRAKE),Table.normal);                        
			TABLE 	= new Table(200,300,"打開情報： "+KeyEvent.getKeyText(il.KEY_SHOW_INFO),Table.normal);                    
			                                                                                                               
			SKILL1 	= new Table(200,325,LIGHTNING_SHOOT.name+"： "+KeyEvent.getKeyText(LIGHTNING_SHOOT.key),Table.normal);
			SKILL2 	= new Table(200,350,NUCLEAR_TURBINE.name+"： "+KeyEvent.getKeyText(NUCLEAR_TURBINE.key),Table.normal);
			SKILL3 	= new Table(200,375,UPDATE_FIRE.name+"： "+KeyEvent.getKeyText(UPDATE_FIRE.key),Table.normal);        
			SKILL4 	= new Table(400,325,RECRUIT_BULLETS.name+"： "+KeyEvent.getKeyText(RECRUIT_BULLETS.key),Table.normal);
			SKILL5 	= new Table(400,350,REPAIR.name+"： "+KeyEvent.getKeyText(REPAIR.key),Table.normal);                  
			                                    
			ANTIALIASING 	= new Table(sw.w/2,150,"抗鋸齒  ：" + (ge.go.ANTIALIAS?"<開>":"<關>"),Table.normal);               
			INTERPOLATION = new Table(sw.w/2,175,"縮放方式："+(ge.go.INTERPOLATION==0?"<BICUBIC>":ge.go.INTERPOLATION==1?"<BILINEAR>":"<NEAREST_NEIGHBOR>"),Table.normal); 
			                                                                              
			FPSTABLE	= new Table(sw.w/2,225,"最大FPS ："+ge.fps.maxFps,Table.normal);                  
              
			SHOWDEBUGINFO = new Table(sw.w/2,300,"DEBUG   ：" + (ge.go.DEBUG_SHOWINFO?"<ON>":"<OFF>"),Table.normal);          
			SHOWFPS = new Table(sw.w/2,250,"顯示FPS ：" + (ge.go.SHOWFPS?"<開>":"<關>"),Table.normal);                    
			if(ge.go.levelSelect>maxLevel)ge.go.levelSelect = 0;
			SELECT = new Table(sw.w/2,350,"關卡選擇： "+(ge.go.levelSelect+1),Table.normal);                 

			int w = 1620;
			int h = 900;
			int x = (sw.w-w)/2;
			int y = (sw.h-h)/2;
			g.drawImage(bg,x,y,w,h,null);
			
			EXIT.draw(g);
			
			Option.draw(g);
			SOUND.draw(g);
			HELPER.draw(g);
			LEFT .draw(g);
			RIGHT.draw(g);
			UP 	 .draw(g);
			DOWN .draw(g);
			STOP .draw(g);
			SKILL1.draw (g);
			SKILL2.draw (g);
			SKILL3.draw (g);
			SKILL4.draw (g);
			SKILL5.draw (g);
			
			MSG.draw(g);
			//MSG2.draw(g);
			TABLE.draw(g);
			
			SETTING1.draw(g);
			SETTING2.draw(g);
			SETTING3.draw(g);
			
			Advanced.draw(g);
			ANTIALIASING.draw(g);
			INTERPOLATION.draw(g);
			FPSTABLE.draw(g);
			FPSUP.draw(g);
			FPSDOWN.draw(g);
			FPSDEF.draw(g);
			SHOWDEBUGINFO.draw(g);
			SHOWFPS.draw(g);
			
			SELECT.draw(g);
			SELECTUP.draw(g);
			SELECTDOWN.draw(g);
		}
		void mouseResponseOption(float second){
			float x = il.mouseX; 
			float y = il.mouseY;
			if(il.MouseDown(MouseEvent.BUTTON1)){
				if(isInButtom(x,y,FPSUP)){
					ge.fps.maxFps += 1;
				}
				if(isInButtom(x,y,FPSDOWN)){
					ge.fps.maxFps -= 1;
					if(ge.fps.maxFps < 2)ge.fps.maxFps = 2;
				}
			}
			if(il.MouseDownOnce(MouseEvent.BUTTON1)){
				if(isInButtom(x,y,SELECTUP)){
					ge.go.levelSelect++;
					if(ge.go.levelSelect>2)ge.go.levelSelect=2;
				}
				if(isInButtom(x,y,SELECTDOWN)){
					ge.go.levelSelect--;
					if(ge.go.levelSelect<0)ge.go.levelSelect=0;
				}
				if(isInButtom(x,y,EXIT)){
					showOption = false;
					bg = Utility.loadImage("src\\op.png");
				}
				if(isInButtom(x,y,SOUND)){
					ge.go.playSound = !ge.go.playSound;
					gm.toggleBGM();
				}
				if(isInButtom(x,y,HELPER)){
					ge.go.showHelp = !ge.go.showHelp;
				}
				
				if(isInButtom(x,y,UP)){
					il.KEY_UP 	= il.getLastDownKey();
				}
				if(isInButtom(x,y,DOWN)){
					il.KEY_DOWN = il.getLastDownKey();
				}
				if(isInButtom(x,y,LEFT)){
					il.KEY_LEFT = il.getLastDownKey();
				}
				if(isInButtom(x,y,RIGHT)){
					il.KEY_RIGHT = il.getLastDownKey();
				}
				if(isInButtom(x,y,STOP)){
					il.KEY_BRAKE = il.getLastDownKey();
				}
				if(isInButtom(x,y,TABLE)){
					il.KEY_SHOW_INFO = il.getLastDownKey();
				}
				if(isInButtom(x,y,SKILL5)){
					REPAIR.key = il.getLastDownKey();
				}
				if(isInButtom(x,y,SKILL1)){
					LIGHTNING_SHOOT.key = il.getLastDownKey();
				}
				if(isInButtom(x,y,SKILL2)){
					NUCLEAR_TURBINE.key = il.getLastDownKey();
				}
				if(isInButtom(x,y,SKILL3)){
					UPDATE_FIRE.key = il.getLastDownKey();
				}
				if(isInButtom(x,y,SKILL4)){
					RECRUIT_BULLETS.key = il.getLastDownKey();
				}				
				if(isInButtom(x,y,ANTIALIASING)){
					ge.go.ANTIALIAS = !ge.go.ANTIALIAS;
				}
				if(isInButtom(x,y,FPSDEF)){
					ge.fps.maxFps = 120;
				}
				if(isInButtom(x,y,INTERPOLATION)){
					ge.go.changeInterpolation();
				}
				if(isInButtom(x,y,SHOWDEBUGINFO)){
					ge.go.DEBUG_SHOWINFO = !ge.go.DEBUG_SHOWINFO;
				}
				if(isInButtom(x,y,SHOWFPS)){
					ge.go.SHOWFPS = !ge.go.SHOWFPS;
				}		
				if(isInButtom(x,y,SETTING1)){
					il.KEY_UP 		= KeyEvent.VK_W;
					il.KEY_DOWN 	= KeyEvent.VK_S;
					il.KEY_LEFT 	= KeyEvent.VK_A;
					il.KEY_RIGHT	= KeyEvent.VK_D;
					il.KEY_BRAKE 	= KeyEvent.VK_X;
					il.KEY_SHOW_INFO = KeyEvent.VK_TAB;
					LIGHTNING_SHOOT.key = KeyEvent.VK_Q;
					NUCLEAR_TURBINE.key = KeyEvent.VK_C;
					UPDATE_FIRE.key 	= KeyEvent.VK_F;
					RECRUIT_BULLETS.key = KeyEvent.VK_E;
					REPAIR.key 			= KeyEvent.VK_R;
				}
				if(isInButtom(x,y,SETTING2)){
					il.KEY_UP 		= KeyEvent.VK_UP;
					il.KEY_DOWN 	= KeyEvent.VK_DOWN;
					il.KEY_LEFT 	= KeyEvent.VK_LEFT;
					il.KEY_RIGHT	= KeyEvent.VK_RIGHT;
					il.KEY_BRAKE 	= KeyEvent.VK_X;
					il.KEY_SHOW_INFO = KeyEvent.VK_TAB;
					LIGHTNING_SHOOT.key = KeyEvent.VK_Q;
					NUCLEAR_TURBINE.key = KeyEvent.VK_W;
					UPDATE_FIRE.key 	= KeyEvent.VK_F;
					RECRUIT_BULLETS.key = KeyEvent.VK_E;
					REPAIR.key 			= KeyEvent.VK_R;           
				}
				if(isInButtom(x,y,SETTING3)){
					il.KEY_UP 		= KeyEvent.VK_UP;
					il.KEY_DOWN 	= KeyEvent.VK_DOWN;
					il.KEY_LEFT 	= KeyEvent.VK_A;
					il.KEY_RIGHT	= KeyEvent.VK_D;
					il.KEY_BRAKE 	= KeyEvent.VK_X;
					il.KEY_SHOW_INFO = KeyEvent.VK_TAB;
					LIGHTNING_SHOOT.key = KeyEvent.VK_Q;
					NUCLEAR_TURBINE.key = KeyEvent.VK_W;
					UPDATE_FIRE.key 	= KeyEvent.VK_F;
					RECRUIT_BULLETS.key = KeyEvent.VK_E;
					REPAIR.key 			= KeyEvent.VK_R;           
				}
			}
			if(isInButtom(x,y,EXIT)){
				EXIT.c = Color.yellow;
			}else{
				EXIT.c = Color.red;
			}
		}
		
		void draw(Graphics2D g){
			
			if(showOption){
			
				drawOption(g);
				
			}else{
		
				int w = 1200;
				int h = 640;
				int x = (sw.w-w)/2;
				int y = (sw.h-h)/2;
				g.drawImage(bg,x,y,w,h,null);
				
				
				g.setColor(Color.white);
				g.drawString("ver 1.03",5,sw.h-5);
				
				//System.out.println("??");
				
				PLAY.draw(g);
				if(ge.go.twicePlay){
					//g.setColor();
					PLAY2.draw(g);
				}
				
				HELP.draw(g);
				OPTION.draw(g);
				
				if(showHelp){
					w = 800;
					h = 420;
					int posx = sw.w/2-w/2;
					int posy = sw.h/2-h/2;
	
					g.setColor(Color.darkGray);
					g.fillRect(posx-10,posy-10,w,h);
					g.setColor(Color.white);
					posy = Utility.drawString(g,posx,posy,"遊戲提示：");
					posy = Utility.drawString(g,posx,posy,"1.小心您的彈藥庫。敵人的子彈如果擊中你的彈藥庫後，你將損失大量護甲");
					posy = Utility.drawString(g,posx,posy,"2.每一個彈藥庫可以提升彈藥存儲上限");
					posy = Utility.drawString(g,posx,posy,"3.在攻擊的時候，請留意剩餘子彈");
					posy = Utility.drawString(g,posx,posy,"4.擁有足夠的資金後，請及時升級您的戰艦");
					posy = Utility.drawString(g,posx,posy,"5.在察看情報時遊戲會自動暫停");
					posy = Utility.drawString(g,posx,posy,"6.你可以自訂義遊戲中幾乎所有的控制鍵");
					posy = Utility.drawString(g,posx,posy,"7.遊戲會自動判斷你正在使用鍵盤還是滑鼠進行操作從而進行不同的響應");
					posy += 15;
					posy = Utility.drawString(g,posx,posy,"設置提示：");
					posy = Utility.drawString(g,posx,posy,"1.你可以在設置中關閉新手提示");
					posy = Utility.drawString(g,posx,posy,"2.高級設置中的選項請不要輕易改動");
					posy = Utility.drawString(g,posx,posy,"3.打開DEBUG模式後，你可以使用[F3][F4][F5]DEBUG鍵");
				}
			}
		}	
		void mouseResponse(float second){
			
			if(showOption){
			
				mouseResponseOption(second);
				
			}else
			if(showHelp){
			
				if(il.MouseDownOnce(MouseEvent.BUTTON1)){
					showHelp = false;
				}
				
			}else{
				float x = il.mouseX;
				float y = il.mouseY;
				if(isInButtom(x,y,PLAY)){
					PLAY.c = Color.yellow;
					if(il.MouseDownOnce(MouseEvent.BUTTON1)){
						//ml.battleShip.cannon.cooldownLeave = ml.battleShip.cannon.cooldownMax;
						gs.running = true;
						gs.inOP = false;
						if(ge.go.levelSelect>maxLevel)ge.go.levelSelect = 0;
						level = ge.go.levelSelect;
						//level = 4;
						//todo 
					}
				}else{
					PLAY.c = Color.red;
				}
				if(isInButtom(x,y,PLAY2) && ge.go.twicePlay){
					PLAY2.c = Color.yellow;
					if(il.MouseDownOnce(MouseEvent.BUTTON1)){
						gs.running = true;
						gs.inOP = false;
						ge.go.levelSelect = 7;
						level = ge.go.levelSelect;
						//ge.go.showHelp = false;
					}
				}else{
					PLAY2.c = Color.red;
				}
				if(isInButtom(x,y,HELP)){
					HELP.c = Color.yellow;
					if(il.MouseDownOnce(MouseEvent.BUTTON1)){
						showHelp = true;
					}
				}else{
					HELP.c = Color.red;
				}	
				if(isInButtom(x,y,OPTION)){
					OPTION.c = Color.yellow;
					if(il.MouseDownOnce(MouseEvent.BUTTON1)){
						showOption = true;
						bg = Utility.loadImage("src\\se.png");
					}
				}else{
					OPTION.c = Color.red;
				}
			}
			
		}
		boolean isInRect(float x,float y,int px,int py,int w,int h){
			return x > px && x < px+w && y > py && y < py+h;
		}
		boolean isInButtom(float x,float y,Table b){
			return isInRect(x,y,b.x,b.y,b.w,b.h);
		}
	}
	
	InputListener il;
	GameState	gs;
	ModeList	ml;
	CameraWorld cw;
	GameEngine	ge;
	GameWorld	gw;
	ScreenWorld	sw;
	GameMusic 	gm;
	
	Helper hp;
	Random rand = new Random(1);
	
	boolean showTabInfo;
	float time;
	float lastUpdateTime;
	float havePassTime;
	int score;
	int enemyNum;
	int enemyKill;
	int totalKill;
	int level;
	int maxLevel = 5;
	String bgm;
	BufferedImage bg,xbg;
	
	ScenceOP op;
	FlowTextList ft;
	LeftMessage lm;
	FlowTextList.FlowText ftPause;
	
	Skill RECRUIT_BULLETS,LIGHTNING_SHOOT,NUCLEAR_TURBINE,REPAIR,UPDATE_FIRE;
	BuffList bfl;
	
	GameLevel(GameEngine	ge,
			  GameState		gs,
			  CameraWorld	cw,
			  GameMusic		gm,
			  GameWorld		gw,
			  ScreenWorld	sw,
			  InputListener il)
	{
		this.ge = ge;
		this.gs = gs;
		this.cw = cw;
		this.gm = gm;
		this.il = il;
		this.sw = sw;
		showTabInfo = false;
		bfl = new BuffList();
		ft = new FlowTextList(cw);
		lm = new LeftMessage(cw);
		hp = new Helper(lm,cw,sw,ge.go,this);
		ml = new ModeList(cw,gw,ge.go,this,gm,sw,ft,hp);
	}
	int getRandomInt(int min,int max){
		int x = rand.nextInt(max-min+1) + min;
		//System.out.println(x);
		return x;
    }

	void init(){
		LIGHTNING_SHOOT = new Skill("閃電射擊","src\\LIGHTNING_SHOOT.png",KeyEvent.VK_Q,"在1秒種內發射大量的子彈，且不消耗彈藥  [被動]減少0.1秒槍口冷卻",1000,4,20);
		NUCLEAR_TURBINE = new Skill("核能渦輪","src\\NUCLEAR_TURBINE.png",KeyEvent.VK_W,"使用後在之後03秒鐘內提升戰艦200%移動速度[被動]增加10點護甲",1000,4,20);
		UPDATE_FIRE		= new Skill("槍林彈雨","src\\UPDATE_FIRE.png",KeyEvent.VK_F,"[被動]強化你的砲台，使得能在一次射击中发射更多的子弹，不過這會消耗更多的彈藥",2500,4,-1);
		RECRUIT_BULLETS = new Skill("補充彈藥","src\\RECRUIT_BULLETS.png",KeyEvent.VK_E,"使用後在之後的5秒種內補充50發子彈",500,0,5);
		REPAIR			= new Skill("修復戰艦","src\\REPAIR.png",KeyEvent.VK_R,"使用後在之後的5秒種內恢復100點耐久",500,0,5);
		bfl.clear();
		
		op = new ScenceOP();
		hp.reset();
		ft.clearAll();
		ml.clearAll();
		if(ml.battleShip == null){
			ml.addBattleship();
		}else{
			ml.battleShip.reset();
		}
		gs.running = false;
		gs.inOP = true;
		score = 0;
		//todo
		//System.out.println(ge.go.levelSelect);
		totalKill = 0;
		enemyKill = 0;
		time = 0;
		bg = Utility.loadImage("src\\op.png");
		gm.stopBGM();
		bgm = "src\\bgm3.wav";
		//System.out.println("Init..."+gs.running);
		gm.playBGM(bgm);
		xbg = null;
		lm.clearAll();
	}
	void levelUp(){
		gs.gameOver = false;
		//ml.battleShip.bulletNum = ml.battleShip.bulletMax;
		lm.clearAll();
			
		switch(level){
		case 1:
			bg = Utility.loadImage("src\\bg1.png");
			gm.stopBGM();
			bgm = "src\\bgm1.wav";
			time = 30;
			gm.playBGM(bgm);
			ml.clear();
			break;
		case 2:
			bg = Utility.loadImage("src\\bg2.png");
			gm.stopBGM();
			bgm = "src\\bgm2.wav";
			time = 60;
			gm.playBGM(bgm);
			ml.clear();
			break;
		case 3:
			bg = Utility.loadImage("src\\bg3.png");
			gm.stopBGM();
			bgm = "src\\bgm3.wav";
			time = 60;
			gm.playBGM(bgm);
			ml.clear();
			break;
		case 4:
			bg = Utility.loadImage("src\\bg3.png");
			time = 60;
			ml.clear();
			break;
		case 5:
			bg = Utility.loadImage("src\\bg3.png");
			time = 22;
			ml.clear();
			break;
		case 8:
			bg = Utility.loadImage("src\\bg3.png");
			gm.stopBGM();
			bgm = "src\\bgm2.wav";
			time = 60;
			gm.playBGM(bgm);
		case 9:
			time = 60;
		}
		
		totalKill += enemyKill;
		enemyKill = 0;
		
		lastUpdateTime  = time;
		havePassTime    = 0;
		
	}
	void levelUpdate(float x,int per){
		if(havePassTime > x && level >= maxLevel){
			if(per == -1){
				ml.addPlane3(getRandomInt(-480,-320),getRandomInt(130,220));
			}else{
				ml.addPlane(getRandomInt(-480,-320),getRandomInt(130,220),per);
			}
			havePassTime -= x;
			//System.out.println("new plane");
		}else
		if(havePassTime > x && time > 10){
			ml.addPlane(getRandomInt(-480,-320),getRandomInt(130,220),per);
			havePassTime -= x;
		}
	}
	void levelProcess(){
		havePassTime += lastUpdateTime - time;		
		if(time <= 0){
			//System.out.println("LEVEL UP");
			if(level <= maxLevel && ml.battleShip.board!=null){
				level++;
				levelUp();
			}else 
			if(level == 7 && ml.battleShip.board!=null){
				level++;
				levelUp();
			}else 
			if(level == 8 && ml.battleShip.board!=null){
				level++;
				levelUp();
			}else
			if(level == 9 && ml.battleShip.board!=null){
				levelUp();
			}
		}
		
		switch(level){
		case 1:
			if(time>25){
				levelUpdate(2,100);
				if(time<28.5)hp.notice(hp.Move);
				if(time<26)hp.notice(hp.OpenInfo);
			}else if(time>20){
				levelUpdate(3f,0);
			}else if(time>10){
				levelUpdate(1.5f,90);
			}else{
				if(time<5)hp.notice(hp.DIALOG1_1);
				//todo: add plot
			}
			break;
		case 2:
			if(time>50){
				levelUpdate(1.5f,80);
				if(time<58.5)hp.notice(hp.DIALOG2_1);
			}else if(time>40){
				levelUpdate(1.8f,90);
			}else if(time>30){
				levelUpdate(1.0f,100);
			}else if(time>20){
				levelUpdate(1.5f,40);
			}else{
				levelUpdate(1.0f,80);
				if(time<5)hp.notice(hp.DIALOG2_2);
			}
			break;
		case 3:
			if(time>50){
				levelUpdate(0.5f,90);
				if(time<58.5)hp.notice(hp.DIALOG3_1);
			}else if(time>40){
				levelUpdate(0.8f,80);
			}else if(time>30){
				levelUpdate(0.4f,100);
			}else if(time>20){
				levelUpdate(0.9f,90);
			}else{
				levelUpdate(0.5f,80);
				if(time<5)hp.notice(hp.DIALOG3_2);
			}
			break;
		case 4:
			if(time>50){
				if(time<58.5)hp.notice(hp.DIALOG4_1);
				hp.notice(hp.LastLevel);
				levelUpdate(0.3f,50);
			}else if(time>40){
				levelUpdate(0.8f,100);
			}else if(time>30){
				levelUpdate(0.8f,0);
			}else if(time>20){
				levelUpdate(0.2f,100);
			}else{
				levelUpdate(1.0f,0);
				if(time<5)hp.notice(hp.DIALOG4_2);
			}
			break;
		case 5:
			//System.out.println(havePassTime);
			if(time>19){
				//levelUpdate(1f,-1);
				hp.notice(hp.STAFF1);
			}else if(time>16.5){
				hp.notice(hp.STAFF2);
			}else if(time>14){
				hp.notice(hp.STAFF3);
			}else if(time>11.5){
				//levelUpdate(2.0f,100);
				hp.notice(hp.STAFF4);
			}else if(time>9){
				hp.notice(hp.STAFF5);
			}else if(time>6.5){
				levelUpdate(13f,-1);
				hp.notice(hp.STAFF6);
			}else if(time>4){
				hp.notice(hp.STAFF7);
			}else if(time>3){
				//gs.running = false;
				gs.gameOver = true;
				xbg = Utility.loadImage("src\\ed.png");
				gs.inOP = true;
				hp.notice(hp.STAFF3);
			}else if(time>2){
				hp.notice(hp.STAFF2);
			}else if(time>1){
				hp.notice(hp.STAFF1);
			}
			break;
		case 8:
			if(time>50){
				levelUpdate(0.6f,90);
			}else if(time>40){
				levelUpdate(0.9f,20);
			}else if(time>30){
				levelUpdate(0.5f,90);
			}else if(time>20){
				levelUpdate(0.7f,30);
			}else{
				levelUpdate(0.6f,80);
			}
			break;
		case 9:
			if(time>50){
				levelUpdate(0.5f,10);
			}else if(time>40){
				levelUpdate(0.8f,80);
			}else if(time>30){
				levelUpdate(0.4f,20);
			}else if(time>20){
				levelUpdate(0.6f,90);
			}else{
				//levelUpdate(0.5f,10);
				time = 60;
				lastUpdateTime  = time;
				havePassTime    = 0;
			}
			break;
		default:
			gs.running = false;
			gs.gameOver = true;
			this.init();
		}
		
		
		if(ml.battleShip.board!=null && ml.battleShip.board.lifeCurrent < ml.battleShip.board.lifeMax/3 && REPAIR.cooldownCur==0){
			hp.notice(hp.LowHP);
		}else
		//if(ml.battleShip.board!=null && ml.battleShip.board.lifeCurrent>0 && ml.battleShip.bulletNum < 1){
		//	hp.notice(hp.BulletRunOut);
		//}else
		if(ml.battleShip.board!=null && ml.battleShip.board.lifeCurrent>0 && ml.battleShip.bulletNum < ml.battleShip.bulletMax/2 && RECRUIT_BULLETS.cooldownCur==0){
			//System.out.println("??");
			hp.notice(hp.BulletWillRunOut);
		}else
		if(ml.battleShip.board!=null && ml.battleShip.board.lifeCurrent>0 && score > 4000){
			hp.notice(hp.HaveManyGold);
		}else
		if(ml.battleShip.board!=null && ml.battleShip.board.lifeCurrent>0 && score > 9000){
			hp.notice(hp.HaveTooManyGold);
		}
		
		lastUpdateTime = time;	
	}
	
	void mouseResponse(float second){
		
		if(gs.inOP){
			op.mouseResponse(second);
			return;
		}
	
		//Cannon following mouse
		if(ml.battleShip.cannon!=null && ge.go.mouseControl == true && gs.running){
			float x = il.getMouseX(cw), y = il.getMouseY(cw);
			//System.out.println(String.format("(%.2f,%.2f)",x,y));
			float r = (float)((float)Math.atan2(
				(x-ml.battleShip.x-ml.battleShip.cannon.x)
				,
				(y-ml.battleShip.y-ml.battleShip.cannon.y)
			)/Math.PI*180-90);
			ml.battleShip.cannon.rotate = r;
			ml.battleShip.cannon.checkRotate();
			//System.out.println(r);		
		}
		
		if(il.MouseDown(MouseEvent.BUTTON1) && gs.running){
			//debug
			//for(int i=0;i<3;i++)
			//ml.addParticle(il.getMouseX(cw),il.getMouseY(cw),
			//	getRanomFloat(-0.5f,0.5f),getRanomFloat(-0.5f,0.5f)
			//);
			ml.battleShip.fire();
			ge.go.mouseControl = true;
		}	
		if(il.MouseDownOnce(MouseEvent.BUTTON3) && gs.running){
			//ml.battleShip.fire2();
		}
	}
	void keyResponse(float second){
	
		if(gs.inOP){
			ge.keyResponse(second);
			return;
		}
	
		//todo:
		//here you just need to deal the key which you're interested in.
		//Notice:
		//if you use keyDown on keyDownOnce, you must tell it the second
		
		//answer keyDownTime and keyDown
		ge.keyResponse(second);

		if(il.keyDown(il.KEY_BRAKE) && !gs.gameOver && gs.running){
			ml.battleShip.brake();
		}
		if(il.keyDown(il.KEY_FIRE) || bfl.haveBuff("BUFF_LIGHTNING_SHOOT") && !gs.gameOver && gs.running){
			ml.battleShip.fire();
		}
		if(il.keyDown(il.KEY_UP) && !gs.gameOver && gs.running){
			ml.battleShip.CannonRaise(second);
			ge.go.mouseControl = false;
		}
		if(il.keyDown(il.KEY_DOWN) && !gs.gameOver && gs.running){
			//battleShip.brake();
			ml.battleShip.CannonLay(second);
			ge.go.mouseControl = false;
		}
		if(il.keyDown(il.KEY_LEFT) && !gs.gameOver && gs.running){
			ml.battleShip.speedUp(second);
		}
		if(il.keyDown(il.KEY_RIGHT) && !gs.gameOver && gs.running){
			ml.battleShip.speedDown(second);
		}
		
		if(il.keyDownOnce(il.KEY_PAUSE)){
			gs.running = !gs.running;
			if(!gs.running){
				ftPause = ft.addFlowText(-35,150,"暫停遊戲",1000f,Color.red,0);
				//ftPause = ft.addFlowText(-35,150,"",1000f,Color.red,0);
				gm.bgm.pause();
			}else{
				ft.removeFlowText(ftPause);
				if(ge.go.playSound)
					gm.bgm.resume();
			}
		}
		if(il.keyDown(KeyEvent.VK_CONTROL)){
			//System.out.println("Ctrl");
		
		}
		//todo
		if(!gs.gameOver && gs.running && ml.battleShip.board!=null){
			if(showTabInfo || il.keyDown(KeyEvent.VK_CONTROL)){
				//Skill update here
				if(il.keyDownOnce(LIGHTNING_SHOOT.key)){
					if(LIGHTNING_SHOOT.level < LIGHTNING_SHOOT.levelMax && score >= LIGHTNING_SHOOT.cost && ml.battleShip.cannon.cooldownMax > 0.1){
						ml.battleShip.cannon.cooldownMax -= 0.1;
						score -= LIGHTNING_SHOOT.cost;
						LIGHTNING_SHOOT.level++;
						//LIGHTNING_SHOOT.cost+=500;
						LIGHTNING_SHOOT.cooldownMax -= 3.5;
					}
				}
				if(il.keyDownOnce(NUCLEAR_TURBINE.key)){
					if(NUCLEAR_TURBINE.level < NUCLEAR_TURBINE.levelMax && score >= NUCLEAR_TURBINE.cost){
						//ml.battleShip.speedMax += 5;
						score -= NUCLEAR_TURBINE.cost;
						NUCLEAR_TURBINE.level++;
						NUCLEAR_TURBINE.describe="使用後在之後"+String.format("%02d",3*NUCLEAR_TURBINE.level+3)+"秒鐘內提升戰艦100%移動速度[被動]增加10點護甲";
						//NUCLEAR_TURBINE.cost+=1000;
					}
				}
				if(il.keyDownOnce(UPDATE_FIRE.key)){
					if(UPDATE_FIRE.level < UPDATE_FIRE.levelMax && score >= UPDATE_FIRE.cost){
						//ml.battleShip.cannon.level += 1;
						score -= UPDATE_FIRE.cost;
						UPDATE_FIRE.level++;
						UPDATE_FIRE.cost+=1000;
					}
				}
			}else{
				//Skill using
				if(il.keyDownOnce(LIGHTNING_SHOOT.key) && LIGHTNING_SHOOT.cooldownCur==0){
					if(LIGHTNING_SHOOT.level > 0){
						bfl.add("BUFF_LIGHTNING_SHOOT",1);
						LIGHTNING_SHOOT.setCooldown();
					}
				}
				if(il.keyDownOnce(NUCLEAR_TURBINE.key) && NUCLEAR_TURBINE.cooldownCur==0){
					if(NUCLEAR_TURBINE.level > 0){
						bfl.add("BUFF_NUCLEAR_TURBINE",3*NUCLEAR_TURBINE.level);
						NUCLEAR_TURBINE.setCooldown();
					}
				}
				if(il.keyDown(RECRUIT_BULLETS.key) && RECRUIT_BULLETS.cooldownCur==0){
					if(score >= RECRUIT_BULLETS.cost && ml.battleShip.bulletNum < ml.battleShip.bulletMax){
						//ml.battleShip.addBullet(second);
						bfl.add("BUFF_RECRUIT_BULLETS",5);
						score -= RECRUIT_BULLETS.cost;
						RECRUIT_BULLETS.setCooldown();
						//RECRUIT_BULLETS.cost+=1;
					}
				}
				if(il.keyDownOnce(REPAIR.key) && REPAIR.cooldownCur==0){
					if(score >= 500 && ml.battleShip.board.lifeCurrent < ml.battleShip.board.lifeMax){
						bfl.add("BUFF_REPAIR",5);
						score -= REPAIR.cost;
						REPAIR.setCooldown();
					}
				}
			}
		}

		if(il.keyDown(il.KEY_SHOW_INFO)){
			//gs.running = false;
			showTabInfo = true;
		}else{
			//gs.running = true;
			showTabInfo = false;
		}
		if(il.keyDownOnce(il.KEY_DEBUG_ADD_SCORE) && ge.go.DEBUG_SHOWINFO){
			score += 10000;
		}
		if(il.keyDownOnce(il.KEY_DEBUG_KILLSELF)  && !gs.gameOver && ge.go.DEBUG_SHOWINFO){
			ml.battleShip.board.lifeCurrent = 1;
		}
		if(il.keyDownOnce(il.KEY_DEBUG_BEST_STATE)  && !gs.gameOver && ge.go.DEBUG_SHOWINFO){
			//ml.battleShip.cannon.cooldownMax = 0.3f;
			//UPDATE_FIRE.level	 = 3;
			LIGHTNING_SHOOT.cooldownCur = 0;
			NUCLEAR_TURBINE.cooldownCur = 0;
			UPDATE_FIRE.cooldownCur = 0;
			//RECRUIT_BULLETS.cooldownCur = 0;
			//REPAIR.cooldownCur = 0;
			ml.battleShip.bulletNum = ml.battleShip.bulletMax;
			ml.battleShip.board.lifeCurrent = ml.battleShip.board.lifeMax;
		}
	}
	
	void readKeyAndResponse(float second){
		int keyID;
		while( (keyID = il.readNextKey()) != -1){
			keyResponse(second);
		}
		il.keyPressTimeIncrease();
		//those response just answer key-down-no-time only
		keyResponse(second);
		mouseResponse(second);
	}
	
	//here processing all mode update, key-input
	public void process(float second){
		//battleship will be update here
		readKeyAndResponse(second);
		//and others will be update here
		//but battleship also be update here
		
		//for OP, I have to do this
		if(!gs.running || showTabInfo){
			
			return;
		}
		
		ml.update(second);
		score += ml.score;
		enemyKill += ml.kill;
		ml.clearScoreAndKill();
		//if(enemyKill == enemyNum && time > 3){
		//	time = 1.2f;
		//}
		time -= second;
		
		levelProcess();
		ft.process(second);
		lm.process(second);
		bfl.process(second);
		LIGHTNING_SHOOT.process(second);
		NUCLEAR_TURBINE.process(second);
		RECRUIT_BULLETS.process(second);
		REPAIR.process(second);
	}
	
	public void draw(Graphics2D g){
		if(!gs.inOP)
			drawLevel(g);
		else
			op.draw(g);
		ft.draw(g);
		lm.draw(g);
		if(xbg!=null)g.drawImage(xbg,0,0,sw.w,sw.h,null);
	}

	void drawSkillUI(Graphics g,Skill skill,int num){
		if(skill.level == 0 && skill.levelMax != 0)return;
		int w=64;
		int posx = (int)(sw.w/2-w*2.5f);
		int posy = sw.h-50;
		if(skill.cooldownCur == 0){
			g.drawImage(skill.img,posx+num*w+8,posy,48,48,null);
		}else{
			g.drawImage(skill.imgun,posx+num*w+8,posy,48,48,null);
		}
		Font f = g.getFont();
		g.setColor(Color.white);
		g.setFont(fontSkill);
		if(skill.cooldownMax>0){
			g.drawString(KeyEvent.getKeyText(skill.key),posx+num*w+8,posy+22);
			g.setFont(fontNormal);
			g.drawString(String.format("%.2f",skill.cooldownCur),posx+num*w+8,posy+48);
		}
		g.setFont(f);
	}
	private void drawLevel(Graphics2D g){
		//if(battleShip!=null)battleShip.draw(g);
		FontMetrics fm = g.getFontMetrics();
		String s;
		
		//draw background
		drawBackGround(g,bg,ml.battleShip.x,ml.battleShip.y);
		
		if(enemyKill == enemyNum && level!=16){
			g.setColor(Color.yellow);
			s = String.format("read? %.2f",time);
			//Utility.drawString(g,sw.w/2-fm.stringWidth(s)/2,sw.h/2,s);
		}
		
		
		g.setFont(ge.go.fontGothic);
		g.setColor(Color.yellow);
		s = String.format("軍資: %-6d",score);
		Utility.drawString(g,sw.w-150,0,s);
		
		//g.setColor(Color.green);
		g.setColor(Color.red);
		s = String.format("殺敵: %-6d",totalKill+enemyKill);
		Utility.drawString(g,sw.w-150,30,s);
		g.setFont(ge.go.fontNormal);
		
		//g.setColor(Color.white);
		//s = String.format("level : %d",level);
		//Utility.drawString(g,sw.w/2-fm.stringWidth(s)/2,20,s);
		g.setColor(Color.white);
			
		int posx = 0;
		if(ml.battleShip.cannon != null)
		drawProcess(g,0,0,ml.battleShip.cannon.cooldownMax-ml.battleShip.cannon.cooldownLeave,ml.battleShip.cannon.cooldownMax);
		else drawProcess(g,posx,0,0,1);
		Utility.drawString(g,posx+100,0,String.format("槍口冷卻: %1.2f (s)",ml.battleShip.cannon!=null?ml.battleShip.cannon.cooldownLeave:0));
		posx = sw.w/4;
		drawProcess(g,posx,0,ml.battleShip.board);
		Utility.drawString(g,posx+100,0,String.format("甲板耐久: %.0f/%.0f",ml.battleShip.board!=null?ml.battleShip.board.lifeCurrent:0,ml.battleShip.board!=null?ml.battleShip.board.lifeMax:0));
		posx = sw.w/4*2;
		drawProcess(g,posx,0,ml.battleShip.bulletNum,100);
		Utility.drawString(g,posx+100,0,String.format("剩餘彈藥: %-3.0f/%.0f",ml.battleShip.bulletNum+0.4,ml.battleShip.bulletMax));
		
		g.setColor(Color.red);
		int pos = 20;
		//pos = Utility.drawString(g,0,pos,"Press {TAB} open info");
		//g.setFont(ge.go.fontGothic);
		//Utility.drawString(g,sw.w-200,40,"按住{TAB鍵}查看情報");
		//g.setFont(ge.go.fontNormal);
		
		//draw SKILL UI
		g.setColor(Color.gray);
		g.fillRect(sw.w/2-200,sw.h-55,400,60);
		g.setColor(Color.red);
		g.drawRect(sw.w/2-200,sw.h-55,400,60);
		drawSkillUI(g,LIGHTNING_SHOOT,0);
		drawSkillUI(g,NUCLEAR_TURBINE,1);
		drawSkillUI(g,RECRUIT_BULLETS,2);
		drawSkillUI(g,REPAIR,3);
		drawSkillUI(g,UPDATE_FIRE,4);

		if(ge.go.DEBUG_SHOWINFO){
			pos = 200;
			int px = 0;
			g.setColor(Color.green);
			pos = Utility.drawString(g,px,pos,"[GAME]");
			s = String.format("enemyNum:%2d level:%d",totalKill+enemyNum,level);
			pos = Utility.drawString(g,px,pos,s);
			s = String.format("time: %.2f",time);
			pos = Utility.drawString(g,px,pos,s);
			s = String.format("battleShip x: %.2f y: %.2f",ml.battleShip.x,ml.battleShip.y);
			pos = Utility.drawString(g,px,pos,s);
		}
		
		
		ml.draw(g);
		
		if(ml.battleShip.board==null){
			pos = sw.h/2;
			g.setColor(Color.red);
			Font f = g.getFont();
			g.setFont(fontBig);
			fm = g.getFontMetrics();
			s = "You Loss";
			Utility.drawString(g,sw.w/2-fm.stringWidth(s)/2,sw.h/2-50,s);
			g.setFont(f);
			fm = g.getFontMetrics();
			s = "press F12 back to menu";
			Utility.drawString(g,sw.w/2-fm.stringWidth(s)/2,sw.h/2,s);
		}
		if(showTabInfo){
			drawTable(g);
		}
		
		//if(level > maxLevel){
		//	g.setColor(Color.gray);
		//	g.fillRect(0,0,sw.w,sw.h);
		//	pos = sw.h/2;
		//	g.setColor(Color.yellow);
		//	s = "You Win";
		//	Utility.drawString(g,sw.w/2-fm.stringWidth(s)/2,sw.h/2-50,s);
		//	s = "High Score:";
		//	pos = Utility.drawString(g,sw.w/2-fm.stringWidth(s)/2,pos,s);
		//	s = String.format("%d",score);
		//	pos = Utility.drawString(g,sw.w/2-fm.stringWidth(s)/2,pos,s);
		//}
		//debug
		//float x = il.getMouseX(cw), y = il.getMouseY(cw);
		//g.drawLine(cw.toScreenX(x),cw.toScreenY(y),
		//	cw.toScreenX(ml.battleShip.x+ml.battleShip.cannon.x),
		//	cw.toScreenY(ml.battleShip.y+ml.battleShip.cannon.y));		
	}
	void drawBackGround(Graphics g,BufferedImage bg,float x,float y){
		int w = 1800,h = 1000;
		int posx = (int)((sw.w-w)/2+x+100);
		int posy = -200;
		g.drawImage(bg,posx,posy,w,h,null);
	}
	void drawTable(Graphics g){
		int w = 800,h = 470;
		int posy = (sw.h - h)/2;
		int posx = (sw.w - w)/2;

		g.setColor(Color.darkGray);
		g.fillRect(posx-10,posy-10,sw.w-posx*2,sw.h-posy*2);
		
		g.setColor(Color.black);
		g.drawRect(posx-10,posy-10,sw.w-posx*2,sw.h-posy*2);
		
		//todo: change them to picture
		
		g.setColor(Color.orange);
		g.setFont(ge.go.fontGothic);
		posy = Utility.drawString(g,posx,posy,"戰艦情報(Info)");
		g.setFont(ge.go.fontNormal);
		posy += 10;
		
		g.setColor(Color.white);
		//drawProcess(g,posx,posy,ml.battleShip.bulletNum,100);
		Utility.drawString(g,posx,posy,String.format("戰艦移速: %2.0f (m/s)",ml.battleShip.speedMax));
		posy = Utility.drawString(g,posx+w/4,posy,String.format("剩餘彈藥: %-3.0f/%.0f",ml.battleShip.bulletNum,ml.battleShip.bulletMax));
		Utility.drawString(g,posx,posy,String.format("槍口冷卻: %1.2f (s)",ml.battleShip.cannon!=null?ml.battleShip.cannon.cooldownMax:0));
		Utility.drawString(g,posx+w/4,posy,String.format("耐久上限: %1.0f",ml.battleShip.board!=null?ml.battleShip.board.lifeMax:0));
		//posy = Utility.drawString(g,posx+w/4,posy,String.format("砲台等級: %3d/5",ml.battleShip.cannon!=null?ml.battleShip.cannon.level:0));

		posy += 30;
		g.setColor(Color.orange);
		g.setFont(ge.go.fontGothic);
		posy = Utility.drawString(g,posx,posy,"戰艦狀態(State)");
		g.setFont(ge.go.fontNormal);
		posy += 10;

		g.setColor(Color.white);
		drawProcess(g,posx,posy,ml.battleShip.board);
		posy = Utility.drawString(g,posx,posy,String.format("甲板      %.0f",ml.battleShip.board!=null?ml.battleShip.board.lifeCurrent:0));
		
		drawProcess(g,posx,posy,ml.battleShip.cannon);
		posy = Utility.drawString(g,posx,posy,String.format("炮台      %.0f",ml.battleShip.cannon!=null?ml.battleShip.cannon.lifeCurrent:0));
		
		drawProcess(g,posx,posy,ml.battleShip.room);
		posy = Utility.drawString(g,posx,posy,String.format("司令塔    %.0f",ml.battleShip.room!=null?ml.battleShip.room.lifeCurrent:0));
		
		drawProcess(g,posx,posy,ml.battleShip.tower);
		posy = Utility.drawString(g,posx,posy,String.format("哨塔      %.0f",ml.battleShip.tower!=null?ml.battleShip.tower.lifeCurrent:0));
		
		drawProcess(g,posx,posy,ml.battleShip.armory1);
		posy = Utility.drawString(g,posx,posy,String.format("武器庫1   %.0f",ml.battleShip.armory1!=null?ml.battleShip.armory1.lifeCurrent:0));
		drawProcess(g,posx,posy,ml.battleShip.armory2);
		posy = Utility.drawString(g,posx,posy,String.format("武器庫2   %.0f",ml.battleShip.armory2!=null?ml.battleShip.armory2.lifeCurrent:0));
		drawProcess(g,posx,posy,ml.battleShip.armory3);
		posy = Utility.drawString(g,posx,posy,String.format("武器庫3   %.0f",ml.battleShip.armory3!=null?ml.battleShip.armory3.lifeCurrent:0));
		drawProcess(g,posx,posy,ml.battleShip.armory4);
		posy = Utility.drawString(g,posx,posy,String.format("武器庫4   %.0f",ml.battleShip.armory4!=null?ml.battleShip.armory4.lifeCurrent:0));
		
		
		
		posy = (sw.h - h)/2;
		posx = sw.w/2;
		g.setColor(Color.orange);
		g.setFont(ge.go.fontGothic);
		posy = Utility.drawString(g,posx,posy,"戰艦升級(Upgrade)");
		g.setFont(ge.go.fontNormal);
		posy += 10;
		
		posy = drawSkill(g,posx,posy,LIGHTNING_SHOOT);		
		posy = drawSkill(g,posx,posy,NUCLEAR_TURBINE);
		posy = drawSkill(g,posx,posy,UPDATE_FIRE);
					
		posy = drawSkill(g,posx,posy,RECRUIT_BULLETS);	
		posy = drawSkill(g,posx,posy,REPAIR);
		
		//posy = Utility.drawString(g,posx,posy,"[F5]+10000 score (debug only)");
		//posy = Utility.drawString(g,posx,posy,"[F6]in best state (debug only)");
		
		//posy = 100;
		//posx = sw.w/2;
	}
	//int drawSkill(Graphics g,int posx,int posy,String skill,String shortcut,int cost){
	//	FontMetrics fm = g.getFontMetrics();
	//	g.setFont(ge.go.fontSkill);
	//	g.setColor(Color.red);
	//	g.drawString(shortcut, posx+13, posy + fm.getAscent());
	//	g.setColor(Color.yellow);
	//	g.drawString("  "+cost, posx+170, posy + fm.getAscent());
	//	g.setFont(ge.go.fontNormal);
	//	
    //
	//	g.setColor(Color.white);
	//	posy = Utility.drawString(g,posx,posy,"[  ]"+" "+skill+"  "+"費用：");
	//	posy += 10;
	//	return posy;
	//}
	int drawSkill(Graphics g,int posx,int posy,Skill skill){
		FontMetrics fm = g.getFontMetrics();
		g.setFont(ge.go.fontSkill);
		g.setColor(Color.red);
		//g.drawString(KeyEvent.getKeyText(skill.key), posx+13+32, posy + fm.getAscent());
		g.setColor(Color.yellow);
		g.drawString("     "+skill.cost, posx, posy + fm.getAscent() + fm.getDescent() + fm.getLeading() + fm.getAscent());
		g.setFont(ge.go.fontNormal);
		g.drawImage(skill.img,posx+6,posy-4,24,24,null);
	
		int y = posy;
		g.setColor(Color.white);
		posy = Utility.drawString(g,posx+32+2,posy,skill.name+"["+KeyEvent.getKeyText(skill.key)+"]"+" "+(skill.levelMax==0?"":"  LV:"+skill.level+"/"+skill.levelMax)+(skill.cooldownMax>0?"   冷卻："+skill.cooldownMax:""));
		posy = Utility.drawString(g,posx,posy,"費用：");
		if(skill.describe!=""){
			posy = Utility.drawString(g,posx,posy,skill.describe,350);
		}
		//g.drawRect(posx,y-10,w,h+10);
		posy += 10;
		return posy;
	}
	void drawProcess(Graphics g,int x,int y,float now,float max){
		int offset = 90;
		int h = 17;
		int maxw = 250;
		int w = (int)(maxw*now/max);
		y+=2;
		Color c;
		c = g.getColor();
		g.setColor(Color.gray);
		g.fillRect(x+offset,y,maxw,h);
		g.setColor(Color.red);
		g.fillRect(x+offset,y,(int)w,h);
		g.setColor(Color.white);
		g.drawRect(x+offset,y,maxw,h);
		g.setColor(c);
	}
	void drawProcess(Graphics g,int x,int y,ModeList.Mode m){
		if(m == null){
			drawProcess(g,x,y,0,1);
		}else{
			drawProcess(g,x,y,m.lifeCurrent,m.lifeMax);
		}
	}
}
