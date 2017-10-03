import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import javax.imageio.*;

/*******************************************************************************
this is a Mode-Render-List
and all mode
*******************************************************************************/
class ModeList{
	class Mode{
		float x,y;	//this is the coord in left-top
		float w,h;	//use to collision check
		float lifeMax = 1;
		float lifeRecover = 0;
		float lifeCurrent = 1;
		float tempo;	//use to calculate this mode duration, need a better name
		BufferedImage img;
		
		Mode(float x,float y,float w,float h,String img){
			this.x = x;
			this.y = y;
			this.w = w;
			this.h = h;
			this.img = Utility.loadImage(img);
		}
		
		void drawOutline(Graphics2D g){
			g.setColor(Color.green);
			g.setStroke(new BasicStroke(1));
			g.drawRect(cw.toScreenX(x),cw.toScreenY(y),
					   cw.toScreenSize(w),cw.toScreenSize(h));
		}
		void draw(Graphics2D g){
			g.drawImage(img,cw.toScreenX(x),cw.toScreenY(y),
						    cw.toScreenSize(w),cw.toScreenSize(h),null);
			if(go.drawOutline)drawOutline(g);
		}
	  	void update(float second){
	  		//please super this
	  		tempo 		+= second;
	  		//System.out.println("tempo "+tempo);
	  		lifeCurrent += lifeRecover*second;
  			if(lifeCurrent > lifeMax)lifeCurrent = lifeMax;
	  	}
	  	
	  	//boolean isOutOfWorld(){
	  	//	return x>gw.w/2 || x<-gw.w/2 || y>gw.h/2 || y<-gw.h/2;
	  	//}
	  	boolean isInScreen(ScreenWorld sw){
	  		return x > -sw.w/2 && y < sw.h/2;
	  	}
	  	boolean isCollision(Mode m){
	  		return x > m.x && x < m.x+m.w && y < m.y && y >m.y-m.h;
	  	}
	  	boolean isCollision(SubMode m){
	  		if(m == null)return false;
	  		Mode f = m.fm;
	  		if(f == null)return false;
	  		return x > m.x+f.x && x < m.x+m.w+f.x && y < m.y+f.y && y >m.y-m.h+f.y;
	  	}
	}
	class SubMode	extends Mode{
		Mode fm;
		public SubMode(float x, float y, float w, float h, String img,Mode fm) {
			super(x, y, w, h, img);
			this.fm = fm;
		}
		@Override
		void drawOutline(Graphics2D g){
			g.setColor(Color.green);
			g.setStroke(new BasicStroke(1));
			float fx = fm.x,fy = fm.y;
			g.drawRect(cw.toScreenX(fx+x),cw.toScreenY(fy+y),cw.toScreenSize(w),cw.toScreenSize(h));
		}
		@Override
		void draw(Graphics2D g){
			if(go.drawOutline)drawOutline(g);
			float fx = fm.x,fy = fm.y;
			g.drawImage(img,cw.toScreenX(fx+x),cw.toScreenY(fy+y),
						cw.toScreenSize(w),cw.toScreenSize(h),null);
		}
	}
	class Bullet	extends Mode{
		float speedx;
		float speedy;
		float radius;
		int owner;
		
		Bullet(float x,float y,float speedx,float speedy,float radius,int owner)
		{
			super(x, y, radius*2, radius*2, null);
			this.speedx = speedx;
			this.speedy = speedy;
			this.radius = radius;
			this.owner  = owner;
			lifeCurrent = lifeMax = 50-gl.NUCLEAR_TURBINE.level*10;
		}
		@Override
		void draw(Graphics2D g){
			super.draw(g);
			g.setColor(Color.white);
			g.setStroke(new BasicStroke(1.0f*cw.zoom));
			g.drawOval(cw.toScreenX(x),cw.toScreenY(y),
					   cw.toScreenSize(w),cw.toScreenSize(h));
		}
		@Override
		void update(float second){
			super.update(second);
			x += speedx*second;
			y += speedy*second;
			addParticle();
		}
		void addParticle(){
			if((int)(tempo*10)%2==1){
				//System.out.println("Particle "+(int)(tempo*10));
				ModeList.this.addParticle(x,y,0f,0f,0.05f,Color.yellow);
				//maddParticle(x,y,0f,0f,0.15f,Color.red);
			}
		}
	}
	class Cannon	extends SubMode{
		/* copy: my old file Cannon.java */
		float cooldownMax;		//最大冷卻時間
		float cooldownLeave;	//剩餘冷卻時間
		float length;
		float rotate;			//仰角 (angle)
		private float rotateVelocity;
		private float maxElevation;		//俯仰角 (angle)
		private float minElevation;
		private float caliber;			//口径
		private float muzzlevelocity;	//枪口速度 (m/s)
		private SubMode subMode;

		//todo: connect this to skill class
		//int level;
		
		Cannon(float x, float y, float w, float h,Mode fm) {
			super(x, y, w, h, null,fm);
			rotate 			= -135f;
			maxElevation	= -60;
			minElevation	= -170f;
			rotateVelocity	= 100f;
			caliber			= 1f;
			muzzlevelocity	= 300f;
			cooldownMax		= 0.7f;
			cooldownLeave	= cooldownMax;
			length = 15f;
			subMode = new SubMode(36,5-5,8,5,"src//battership_cannon.png",fm);
			//level = 1;
		}
		void launchBullet(int level){
			float fx = fm.x,fy = fm.y;
			float l1 = 2f,l2 = 2.5f;
			switch(level){
			case 0:
				addMode(new Bullet(
					(float)(fx+x+length*l1*Math.cos(Math.toRadians(rotate))),
					(float)(fy+y-length*l1*Math.sin(Math.toRadians(rotate))),
					(float)(muzzlevelocity*Math.cos(Math.toRadians(rotate))),
					(float)(-muzzlevelocity*Math.sin(Math.toRadians(rotate))),caliber,1));
					//System.out.println(muzzlevelocity*Math.cos(Math.toRadians(rotate)));
					//System.out.println(muzzlevelocity*Math.sin(Math.toRadians(rotate)));
				break;
			case 1:
				addMode(new Bullet(
						(float)(fx+x+2+length*l1*Math.cos(Math.toRadians(rotate))),
						(float)(fy+y+2-length*l1*Math.sin(Math.toRadians(rotate))),
						(float)(muzzlevelocity*1*Math.cos(Math.toRadians(rotate))),
						(float)(-muzzlevelocity*1*Math.sin(Math.toRadians(rotate))),caliber,1));
				addMode(new Bullet(
						(float)(fx+x-2+length*l1*Math.cos(Math.toRadians(rotate))),
						(float)(fy+y-2-length*l1*Math.sin(Math.toRadians(rotate))),
						(float)(muzzlevelocity*1*Math.cos(Math.toRadians(rotate))),
						(float)(-muzzlevelocity*1*Math.sin(Math.toRadians(rotate))),caliber,1));
				break;
			case 2:
				addMode(new Bullet(
						(float)(fx+x+length*l2*Math.cos(Math.toRadians(rotate))),
						(float)(fy+y-length*l2*Math.sin(Math.toRadians(rotate))),
						(float)(muzzlevelocity*Math.cos(Math.toRadians(rotate))),
						(float)(-muzzlevelocity*Math.sin(Math.toRadians(rotate))),caliber,1));
				addMode(new Bullet(
						(float)(fx+x+3+length*l1*Math.cos(Math.toRadians(rotate))),
						(float)(fy+y+3-length*l1*Math.sin(Math.toRadians(rotate))),
						(float)(muzzlevelocity*1*Math.cos(Math.toRadians(rotate))),
						(float)(-muzzlevelocity*1*Math.sin(Math.toRadians(rotate))),caliber,1));
				addMode(new Bullet(
						(float)(fx+x-3+length*l1*Math.cos(Math.toRadians(rotate))),
						(float)(fy+y-3-length*l1*Math.sin(Math.toRadians(rotate))),
						(float)(muzzlevelocity*1*Math.cos(Math.toRadians(rotate))),
						(float)(-muzzlevelocity*1*Math.sin(Math.toRadians(rotate))),caliber,1));
				break;
			case 3:
				addMode(new Bullet(
						(float)(fx+x+2+length*l1*Math.cos(Math.toRadians(rotate))),
						(float)(fy+y+2-length*l1*Math.sin(Math.toRadians(rotate))),
						(float)(muzzlevelocity*1*Math.cos(Math.toRadians(rotate))),
						(float)(-muzzlevelocity*1*Math.sin(Math.toRadians(rotate))),caliber,1));
				addMode(new Bullet(
						(float)(fx+x-2+length*l1*Math.cos(Math.toRadians(rotate))),
						(float)(fy+y-2-length*l1*Math.sin(Math.toRadians(rotate))),
						(float)(muzzlevelocity*1*Math.cos(Math.toRadians(rotate))),
						(float)(-muzzlevelocity*1*Math.sin(Math.toRadians(rotate))),caliber,1));
				addMode(new Bullet(
						(float)(fx+x+length*l1*Math.cos(Math.toRadians(rotate))),
						(float)(fy+y-length*l1*Math.sin(Math.toRadians(rotate))),
						(float)(muzzlevelocity*Math.cos(Math.toRadians(rotate+8))),
						(float)(-muzzlevelocity*Math.sin(Math.toRadians(rotate+8))),caliber,1));
				addMode(new Bullet(
						(float)(fx+x+length*l1*Math.cos(Math.toRadians(rotate))),
						(float)(fy+y-length*l1*Math.sin(Math.toRadians(rotate))),
						(float)(muzzlevelocity*Math.cos(Math.toRadians(rotate-8))),
						(float)(-muzzlevelocity*Math.sin(Math.toRadians(rotate-8))),caliber,1));
				break;
			case 4:
				addMode(new Bullet(
						(float)(fx+x+length*l1*Math.cos(Math.toRadians(rotate))),
						(float)(fy+y-length*l1*Math.sin(Math.toRadians(rotate))),
						(float)(muzzlevelocity*Math.cos(Math.toRadians(rotate))),
						(float)(-muzzlevelocity*Math.sin(Math.toRadians(rotate))),caliber,1));
				addMode(new Bullet(
						(float)(fx+x+length*l1*Math.cos(Math.toRadians(rotate))),
						(float)(fy+y-length*l1*Math.sin(Math.toRadians(rotate))),
						(float)(muzzlevelocity*Math.cos(Math.toRadians(rotate+5))),
						(float)(-muzzlevelocity*Math.sin(Math.toRadians(rotate+5))),caliber,1));
				addMode(new Bullet(
						(float)(fx+x+length*l1*Math.cos(Math.toRadians(rotate))),
						(float)(fy+y-length*l1*Math.sin(Math.toRadians(rotate))),
						(float)(muzzlevelocity*Math.cos(Math.toRadians(rotate-5))),
						(float)(-muzzlevelocity*Math.sin(Math.toRadians(rotate-5))),caliber,1));
				addMode(new Bullet(
						(float)(fx+x+length*l1*Math.cos(Math.toRadians(rotate))),
						(float)(fy+y-length*l1*Math.sin(Math.toRadians(rotate))),
						(float)(muzzlevelocity*Math.cos(Math.toRadians(rotate+10))),
						(float)(-muzzlevelocity*Math.sin(Math.toRadians(rotate+10))),caliber,1));
				addMode(new Bullet(
						(float)(fx+x+length*l1*Math.cos(Math.toRadians(rotate))),
						(float)(fy+y-length*l1*Math.sin(Math.toRadians(rotate))),
						(float)(muzzlevelocity*Math.cos(Math.toRadians(rotate-10))),
						(float)(-muzzlevelocity*Math.sin(Math.toRadians(rotate-10))),caliber,1));
				break;
			}
		}
		boolean fire(float fx,float fy){
			if(cooldownLeave != 0)return false;
			cooldownLeave = cooldownMax;
			if(bfl.haveBuff("BUFF_LIGHTNING_SHOOT"))cooldownLeave = 0.1f;
			//launch bullet
			launchBullet(gl.UPDATE_FIRE.level);
			return true;
		}
		void lay(float second){
			rotate -= second * rotateVelocity;
			//what is this fucking algorithm?
			//if( rotate>-90 && rotate > minElevation ||
			//	rotate<-90 && rotate < minElevation
			//)rotate = minElevation;
			checkRotate();
		}
		void raise(float second){
			rotate += second * rotateVelocity;
			checkRotate();
		}
		void checkRotate(){
			if(rotate > maxElevation)
				rotate = maxElevation;
			if(rotate < minElevation)
				rotate = minElevation;
		}
		@Override
		void update(float second){
			super.update(second);
			if(cooldownLeave > 0){
				cooldownLeave -= second;
				if(cooldownLeave < 0)cooldownLeave = 0;
			}
			//System.out.println(rotate);
			//System.out.println(cooldownLeave);
		}
		//@Override
		void draw(Graphics2D g,CameraWorld cw,float fx,float fy){
			g.setColor(new Color(157,157,157));
			g.setStroke(new BasicStroke(1.0f*cw.zoom)); 
			int xx=3,yy=-3;
			g.drawLine(cw.toScreenX(fx+x+xx),cw.toScreenY(fy+y+yy),
				(int)(cw.toScreenX(fx+x+xx)+cw.toScreenSize(length)*Math.cos(Math.toRadians(rotate))),
				(int)(cw.toScreenY(fy+y+yy)+cw.toScreenSize(length)*Math.sin(Math.toRadians(rotate)))
			);
			subMode.draw(g);
		}
	}
	class BattleShip extends Mode{
		private float accel;	//加速度(廢棄)
		private float friction;	//摩擦力
		float speedCur;
		float speedMax;
		float bulletNum;
		float bulletMax;
		Cannon cannon;
		SubMode board,bilge,room,tower,armory1,armory2,armory3,armory4;
		
		//todo: finish this idea
		int skillLevel[];
		void reset(){
			x = 0;
			y = 48;
			speedCur = 0f;
			speedMax = 25f;
			friction = 50f;
			cannon	= new Cannon(36,0,8,5,this);
			board 	= new SubMode(0,0,199,16,"src\\battership_main.png",this);
			bilge 	= new SubMode(14,-16,168,7,"src\\battership_main2.png",this);
			room	= new SubMode(58,15-5,40,15,"src\\battership_building_1.png",this);
			tower	= new SubMode(88,29-4,12,29,"src\\battership_building_2.png",this);
			armory1	= new SubMode(98,6-6,12,6,"src\\battership_house1.png",this);
			armory2	= new SubMode(111,9-6,13,9,"src\\battership_house2.png",this);
			armory3	= new SubMode(125,9-6,13,9,"src\\battership_house3.png",this);
			armory4	= new SubMode(139,9-6,13,9,"src\\battership_house4.png",this);
			bulletNum = 100;
			bulletMax = 100;
			
			cannon.lifeCurrent	= cannon.lifeMax = 300;
			board.lifeCurrent	= board.lifeMax	= 1000;
			bilge.lifeCurrent	= bilge.lifeMax	= 1000;
			room.lifeCurrent	= room.lifeMax	= 200;
			tower.lifeCurrent	= tower.lifeMax	= 100;
			armory1.lifeCurrent = armory1.lifeMax = 10;
			armory2.lifeCurrent = armory2.lifeMax = 10;
			armory3.lifeCurrent = armory3.lifeMax = 10;
			armory4.lifeCurrent = armory4.lifeMax = 10;
		}
		BattleShip(){
			super(0,48,199,16,null);
			
			reset();
			
			skillLevel = new int[4];
			for(int i=0;i<4;i++)skillLevel[i] = 0;
		}
		@Override
		void update(float second){
			super.update(second);
			if(speedCur < -speedMax)speedCur = -speedMax;
			if(speedCur >  speedMax)speedCur =  speedMax;
			if(bfl.haveBuff("BUFF_NUCLEAR_TURBINE")){
				speedCur *= 3;
			}
			//System.out.println(speedCur);
			x += speedCur * second;
			//lock in screen
			if(x<-(cw.w/2+w)/cw.zoom) x=-(cw.w/2+w)/cw.zoom;
			if(x>(cw.w/2-w)/cw.zoom) x=(cw.w/2-w)/cw.zoom;
			//friction emulate
			if(speedCur > 0)speedCur -= second*friction;
			if(speedCur < 0)speedCur += second*friction;
			//System.out.println("speed: " + speed);
			if(cannon!=null)cannon.update(second);
			
			if(bfl.haveBuff("BUFF_REPAIR") && board!=null){
				//System.out.println("666");
				board.lifeCurrent+=second*20;
				if(board.lifeCurrent > board.lifeMax)board.lifeCurrent = board.lifeMax;
				//System.out.println("BUFF_REPAIR");
			}
			if(bfl.haveBuff("BUFF_RECRUIT_BULLETS")){
				bulletNum += second*10;
				if(bulletNum > bulletMax)bulletNum = bulletMax;
				//System.out.println("BUFF_RECRUIT_BULLETS");
			}
		}
		@Override
		void draw(Graphics2D g){
			super.draw(g);
			if(tower != null)	tower.draw(g);
			if(room != null)	room.draw(g);
			if(armory1 != null)	armory1.draw(g);
			if(armory2 != null)	armory2.draw(g);
			if(armory3 != null)	armory3.draw(g);
			if(armory4 != null)	armory4.draw(g);
			if(cannon != null)	cannon.draw(g,cw,x,y);
			if(board != null)	board.draw(g);
		}
		//launch bullet
		void fire(){
			if(bfl.haveBuff("BUFF_LIGHTNING_SHOOT")){
				if(cannon.fire(x,y)){
					gm.play("src\\fire.wav");
				}
			}else{
				if(cannon == null || bulletNum < gl.UPDATE_FIRE.level+1)return;
				if(cannon.fire(x,y)){
					bulletNum -= gl.UPDATE_FIRE.level+1;
					gm.play("src\\fire.wav");
				}
			}
		}
		void speedUp(float second){
			speedCur = -speedMax;
			addSpray(x+12,y,w,h,speedCur);
		}
		void speedDown(float second){
			speedCur = speedMax*2.0f;
			addSpray(x+w-2,y,w,h,speedCur);
		}
		void brake(){
			speedCur = 0;
		}
		void CannonRaise(float second){
			cannon.raise(second);
		}
		void CannonLay(float second){
			cannon.lay(second);
		}
		//void addBullet(float second){
		//	//float ex = 0;
		//	//if(armory1 != null)ex += 0.25f;
		//	//if(armory2 != null)ex += 0.25f;
		//	//if(armory3 != null)ex += 0.25f;
		//	//if(armory4 != null)ex += 0.25f;
		//	bulletNum += second*(20);
		//	if(bulletNum > bulletMax)bulletNum = bulletMax;
		//}
		void kill(){
			//System.out.println("die");
			board 	= null;
			cannon	= null;
			board 	= null;
			bilge 	= null;
			room	= null;
			tower	= null;
			armory1	= null;
			armory2	= null;
			armory3	= null;
			armory4	= null;
			//add boom
			for(int i=0;i<10;i++){
				addBoom(getRanomFloat(battleShip.x,battleShip.x+battleShip.w),
						getRanomFloat(battleShip.y-battleShip.h,battleShip.y));
			}
			//battleShip = null;
			gl.gs.gameOver = true;
			battleShip.bulletNum = 0.0f;
		}
		void killHouse(){
			board.lifeCurrent -= 250;
			board.lifeMax -= 150;
			bulletMax -= 15;
			if(bulletNum > bulletMax)bulletNum = bulletMax;
			//System.out.println(battleShip.board.lifeCurrent);
			if(battleShip.board.lifeCurrent <= 0){
				kill();
			}
		}
		//todo
		void skillLevelUp(int id){
			//if(id)??
			if(skillLevel[id] < 10){
				skillLevel[id]++;
			}
		}
	}
	class Plane		extends Mode{
		float speedx;
		int bulletNum;
		int type;
		Plane(float x, float y, float w, float h,int type) {
			//"Constructor call must be the first statement in a constructor"
			//I hate this fucking illegal, I have to do this to avoid it.
			super(x, y, w, h, null);
			String imgStr = null;
			float speed	  = 0;
			switch(type){
			case 1:
				imgStr = "src\\plane1.png";
				speed = getRanomFloat(80,120);
				bulletNum = getRandomInt(2,5);
				break;
			case 2:
				imgStr = "src\\plane2.png";
				speed = getRanomFloat(80,110);
				bulletNum = 1;
				break;
			case 3:
				imgStr = "src\\plane3.png";
				speed = getRanomFloat(130,150);
				bulletNum = 0;
				break;
			}
			this.img	= Utility.loadImage(imgStr);
			this.type	= type;
			this.speedx	= speed;
		}
		
		@Override
		void update(float second) {
			super.update(second);
			x += speedx * second;
			switch(type){
			case 1:speedx+=second*30;break;
			case 3:
				speedx+=second*75;
				y += (float) (Math.cos(x/100))*second*100;
				y += 10*second;
				break;
			}
			//fire
			if(isFireTime()){
				fire();
			}
		}
		
		boolean isFireTime(){
			switch(type){
			case 1:return battleShip.x - x < 150 && battleShip.x - x >0 && 
						  getRandomInt(0,100) > 90;
			case 2:return battleShip.x - x < 100 && battleShip.x - x >0 && 
					  	  getRandomInt(0,100) > 90;
			}
			return false;
		}
		void fire(){
			if(bulletNum <= 0)return;
			bulletNum -= 1;
			
			switch(type){
			case 2:
				addMode(new Bullet(
						(float)(x+w),
						(float)(y-h),
						(float)(speedx),
						(float)(-speedx),1,2));
				if(bulletNum <= 0)
					speedx += 50;
				break;
			case 1:
				addMode(new Bullet(
						(float)(x+w),
						(float)(y-h),
						(float)(speedx*1.5),
						(float)(-speedx),1,2));
				break;
			}
		}
	}
	class Particle	extends Mode{
		float vx,vy;
		Color c;
		Particle(float x, float y,float vx,float vy){
			this(x,y,vx,vy,0.5f,rand.nextInt()%2==0?Color.white:Color.red);
		}
		Particle(float x, float y,float vx,float vy,float second,Color c){
			super(x, y, 1, 1, null);
			this.vx = vx;
			this.vy = vy;
			this.c 	= c;
			lifeMax 	= second;
			lifeRecover = -1f;
			lifeCurrent = second;
		}
		@Override
		void draw(Graphics2D g){
			if(lifeCurrent <= 0.005)return;
			g.setColor(
				new Color(((int)(lifeCurrent/lifeMax*255)<<24)+(c.getRGB()),
				true)
			);
			g.setStroke(new BasicStroke(1.0f*cw.zoom));
			g.drawOval(cw.toScreenX(x),cw.toScreenY(y),
					   cw.toScreenSize(w),cw.toScreenSize(h));
		}
		@Override
		void update(float second){
			super.update(second);
			x += vx*second;
			y += vy*second;
		}
	}
/*******************************************************************************
there is the ModeList begin
todo: just mark
*******************************************************************************/
	CameraWorld	cw;
	GameWorld	gw;
	GameLevel	gl;
	ScreenWorld sw;
	GameMusic	gm;
	FlowTextList ft;
	BuffList bfl;
	GameEngine.GameOption go;
	
	Helper hp;
	Random rand = new Random(1);
	int score = 0;
	int kill = 0;
	
	BattleShip	battleShip;
	ArrayList<Mode> modeList 	= new ArrayList<Mode>();
	ArrayList<Mode> enemyList 	= new ArrayList<Mode>();
	ArrayList<Mode> particleList= new ArrayList<Mode>();
	ArrayList<Mode> removeList 	= new ArrayList<Mode>();
	//change his <Bullet> to <Mode>?
	ArrayList<Bullet> bulletList	= new ArrayList<Bullet>();
	
	public ModeList(CameraWorld cw,
					GameWorld 	gw,
					GameEngine.GameOption go,
					GameLevel 	gl,
					GameMusic 	gm,
					ScreenWorld sw,
					FlowTextList ft,
					Helper 		hp
					)
	{
		this.cw = cw;
		this.gw = gw;
		this.go = go;
		this.gl = gl;
		this.gm = gm;
		this.sw = sw;
		this.ft = ft;
		this.hp = hp;
		this.bfl = gl.bfl;
	}
	
	void addParticle(float x,float y,float vx,float vy){
		this.addMode(new Particle(x,y,vx,vy));
	}
	void addParticle(float x,float y,float vx,float vy,float second,Color c){
		this.addMode(new Particle(x,y,vx,vy,second,c));
	}
	void addSpray(float x,float y,float w,float h,float speed){
		//System.out.println("SPRAY");
		addParticle(x,y-h,-getRanomFloat(0.1f,0.9f)*speed,getRanomFloat(0.1f,10f),0.5f,new Color(76,188,200));
	}
	
	void addMode(Mode m){
		if(m instanceof Bullet){
			bulletList.add((Bullet)m);
		}else if(m instanceof Plane){
			enemyList.add(m);
		}else if(m instanceof Particle){
			particleList.add(m);
		}else{
			modeList.add(m);
		}
	}
	void addPlane3(float x,float y){
		this.addMode(new Plane(x,y,32,10,3));
	}
	void addPlane(float x, float y,int per){
		if(getRanomFloat(0,100) > per){
			hp.notice(hp.WhitePlane);
			y-=15;
			//System.out.println(y);
			this.addMode(new Plane(x,y,42,17,1));
		}else{
			y+=15;
			this.addMode(new Plane(x,y,42,12,2));
		}
	}
	void addBattleship(){
		modeList.add(battleShip = new BattleShip());
	}
	void addBoom(float x,float y){
		for(int i=0;i<30;i++)                                  
			addParticle(x,y,
				getRanomFloat(-50,50),getRanomFloat(-50,50)
			);
		//System.out.println("boom");
		gm.play("src\\explosion.wav");
	}
	public void draw(Graphics2D g) {
		for(Mode m:modeList){
            m.draw(g);
        }
		for(Mode m:bulletList){
            m.draw(g);
        }
		for(Mode m:enemyList){
            m.draw(g);
        }
		for(Mode m:particleList){
            m.draw(g);
        }
	}
	public void update(float second){	
		//Notice:don't change the list order when you visiting it
		for(Mode m:modeList){
			m.update(second);
			if(m.lifeCurrent <= 0 || !gw.isInWorld(m)){
				removeList.add(m);
			}
		}
		for(Mode m:enemyList){
			m.update(second);
			if(m.lifeCurrent <= 0 || !gw.isInWorld(m)){
				removeList.add(m);
			}
		}
		for(Mode m:particleList){
			m.update(second);
			if(m.lifeCurrent <= 0 || !gw.isInWorld(m)){
				removeList.add(m);
			}                               
		}
		for(Bullet m:bulletList){
			m.update(second);
			//todo: for and calc and make it better
			if(m.isCollision(battleShip.board)){
				hp.notice(hp.DonotCareBoard);
				addBoom(m.x,m.y);
				removeList.add(m);
				battleShip.board.lifeCurrent -= m.lifeCurrent;
				//System.out.println(battleShip.board.lifeCurrent);
				if(battleShip.board.lifeCurrent <= 0){
					battleShip.kill();
				}
			}
			if(m.isCollision(battleShip.room)){
				addBoom(m.x,m.y);
				removeList.add(m);
				battleShip.room.lifeCurrent -= m.lifeCurrent;
				if(battleShip.room.lifeCurrent <= 0)battleShip.room = null;
			}
			if(m.isCollision(battleShip.tower)){
				addBoom(m.x,m.y);
				removeList.add(m);
				battleShip.tower.lifeCurrent -= m.lifeCurrent;
				if(battleShip.tower.lifeCurrent <= 0)battleShip.tower = null;
			}
			if(m.isCollision(battleShip.armory1)){
				hp.notice(hp.ArmoryBeDestory);
				for(int i=0;i<3;i++)addBoom(m.x,m.y);
				removeList.add(m);
				battleShip.armory1.lifeCurrent -= m.lifeCurrent;
				if(battleShip.armory1.lifeCurrent <= 0)battleShip.armory1 = null;
				battleShip.killHouse();
			}
			if(m.isCollision(battleShip.armory2)){
				hp.notice(hp.ArmoryBeDestory);
				for(int i=0;i<3;i++)addBoom(m.x,m.y);
				removeList.add(m);
				battleShip.armory2.lifeCurrent -= m.lifeCurrent;
				if(battleShip.armory2.lifeCurrent <= 0)battleShip.armory2 = null;
				battleShip.killHouse();
			}
			if(m.isCollision(battleShip.armory3)){
				hp.notice(hp.ArmoryBeDestory);
				for(int i=0;i<3;i++)addBoom(m.x,m.y);
				removeList.add(m);
				battleShip.armory3.lifeCurrent -= m.lifeCurrent;
				if(battleShip.armory3.lifeCurrent <= 0)battleShip.armory3 = null;
				battleShip.killHouse();
			}
			if(m.isCollision(battleShip.armory4)){
				hp.notice(hp.ArmoryBeDestory);
				for(int i=0;i<3;i++)addBoom(m.x,m.y);
				removeList.add(m);
				battleShip.armory4.lifeCurrent -= m.lifeCurrent;
				if(battleShip.armory4.lifeCurrent <= 0)battleShip.armory4 = null;
				battleShip.killHouse();
			}
			if(m.isCollision(battleShip.cannon)){
				addBoom(m.x,m.y);
				removeList.add(m);
				battleShip.cannon.lifeCurrent -= m.lifeCurrent;
				if(battleShip.cannon.lifeCurrent <= 0)battleShip.cannon = null;
			}
			
			for(Mode mx:enemyList){
				if(mx.isInScreen(sw) && m.isCollision(mx)){
					//System.out.println("delete");
					removeList.add(m);
					removeList.add(mx);
					//score inc
					//now it will check the bullet's father
					if(m.owner == 1){
						kill++;
						hp.notice(hp.FirstBlood);
						if(((Plane)mx).type == 1){
							score += 500;
							ft.addFlowText(mx.x,mx.y,"+500",1f,Color.red,1);
						}else{
							score += 100;
							ft.addFlowText(mx.x,mx.y,"+100",1f,Color.yellow,1);
						}
					}
				}
			}
			if(m.lifeCurrent <= 0 || !gw.isInWorld(m)){
			//m.isOutOfWorld()){
				removeList.add(m);
			}
		}
		//System.out.println(enemyList.size());
		//System.out.println(bulletList.size());
		//System.out.println(particleList.size());
		//System.out.println(removeList.size());
		//System.out.println();
		
		//Remove-list
		for(Mode m:removeList){
			//System.out.println("remove");
			modeList.remove(m);
			enemyList.remove(m);
			bulletList.remove(m);
			particleList.remove(m);
			//Notice again: you can't order the modelist when you visit it
			if(m instanceof Plane && cw.isInCamera(m))addBoom(m.x,m.y);
			if(m instanceof Plane && cw.isInCamera(m) && ((Plane) m).type == 3)go.twicePlay = true;
		}
		removeList.clear();
	}
	public void clearScoreAndKill(){
		kill = 0;
		score = 0;
	}
	public void clear(){
		for(Mode m:modeList){
			if(m!=battleShip)
				removeList.add(m);
		}
		for(Mode m:bulletList){
			//I don't want to clear them, so I comment this
			//removeList.add(m);
		}
		//for(Mode m:enemyList){
		//	removeList.add(m);
		//}
		enemyList.clear();
		
		for(Mode m:removeList){
			modeList.remove(m);
			bulletList.remove(m);
			enemyList.remove(m);
		}
		clearScoreAndKill();
	}
	public void clearAll(){
	
		//for(Mode m:bulletList){
		//	removeList.add(m);
		//}
		//for(Mode m:particleList){
		//	removeList.add(m);
		//}
		bulletList.clear();
		particleList.clear();
		clear();
		
	}
	
    float getRanomFloat(float min,float max){
    	float x = rand.nextFloat();
    	//System.out.println(x);
    	return x*(max-min)+min;
    }
	int getRandomInt(int min,int max){
		int x = rand.nextInt(max-min+1) + min;
		//System.out.println(x);
		return x;
    }
}