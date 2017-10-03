import java.awt.image.*;
import java.util.*;

class Skill{
	int key;
	String name;
	String describe;
	BufferedImage img;
	BufferedImage imgun;
	int cost;
	float cooldownCur;
	float cooldownMax;
	int level,levelMax;
	Skill(String name,String img,int key,String describe,int cost,int levelMax,float cooldownMax){
		this.key	= key;
		this.name	= name;
		this.cost	= cost;
		this.describe = describe;
		this.levelMax = levelMax;
		this.cooldownCur = 0;
		this.cooldownMax = cooldownMax;
		//System.out.println(img);
		this.img = Utility.loadImage(img);
		this.imgun = Utility.loadImage(img.substring(0,img.length()-4)+"_un"+".png");
	}
	void setCooldown(){
		cooldownCur = cooldownMax;
	}
	void process(float second){
		cooldownCur -= second;
		if(cooldownCur < 0)cooldownCur = 0;
	}
}

class Buff{
	float leaving;
	int owner;
	String name;
	
	Buff(String name,float time,int owner){
		leaving = time;
		this.name	= name;
		this.owner  = owner;
	}
	
}

class BuffList{
	ArrayList<Buff> bfl = new ArrayList<Buff>();
	
	void process(float second){
		ArrayList<Buff> r = new ArrayList<Buff>();
		
		for(Buff b : bfl){
			b.leaving -= second;
			if(b.leaving < 0)r.add(b);
		}
		for(Buff b : r){
			bfl.remove(b);
		}
	}
	void clear(){
		bfl.clear();
	}
	
	void add(String name,float life){
		bfl.add(new Buff(name,life,1));
	}
	boolean haveBuff(String name){
		int owner = 1;
		for(Buff b : bfl){
			if(b.name == name && b.owner == owner){
				return true;
			}
		}
		return false;
	}
}