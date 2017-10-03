import java.awt.*;
import java.util.*;

class LeftMessage{
	class FlowText{
		float x,y;
		String str;
		float life,disappear;
		boolean alpha = true;
		Color c;
		int size;
		FlowText(float x,float y,String str,float life,Color c,float disappear){
			this.x = x;
			this.y = y;
			this.c = c;
			this.str	= str;
			this.life	= life+0.3f;
			this.disappear = disappear;
			size   = 1;
		}
		void draw(Graphics g){
			if(life <= 0.3)return;
			g.setColor(life<disappear
				?new Color(((int)(life/disappear*255)<<24) + (c.getRGB()) , true)
				:c
			);
			//if(life<3f){
			//System.out.println("FlowText:" + (int)(life/3f*255));
			//}
			//System.out.println(str+life);
			g.drawString(str,cw.toScreenX(x),cw.toScreenY(y));
		}
	}
	ArrayList<FlowText> ftl;
	FlowText fft;
	CameraWorld cw;
	int top,left,width,height;
	int nowShowMsgNum;
	float life;
	String lastStr;
	LeftMessage(CameraWorld cw){
		this.cw  = cw;
		top		= 50;
		left	= 10;
		width	= 0;
		life 	= 2f;
		nowShowMsgNum = 0;
		ftl = new ArrayList<FlowText>();
	}
	void addLeftMessage(String str,float life,Color c){
		//System.out.println(str);
		//System.out.println(lastStr);
		//fuck the "=="
		if(str.equals(lastStr))return;
		ftl.add(new FlowText(0,0,str,life,c,0));
		height += 30;
		nowShowMsgNum++;
		lastStr = new String(str);
		int t = str.length()*20;
		if(t>width)width=t;
	}
	void addLeftMessage(String str,Color c){
		//System.out.println(str);
		//System.out.println(lastStr);
		//fuck the "=="
		if(str.equals(lastStr))return;
		ftl.add(new FlowText(0,0,str,life,c,0));
		height += 30;
		nowShowMsgNum++;
		lastStr = new String(str);
		int t = str.length()*20;
		if(t>width)width=t;
	}
	void draw(Graphics g){
		if(nowShowMsgNum==0)return;
		
		g.setColor(Color.lightGray);
		g.fillRect(left,top,width,height);
		g.setColor(Color.black);
		g.drawRect(left,top,width,height);
		
		int y = top+22;
		for(FlowText ft:ftl){
			if(ft.life <= 0)return;
			g.setColor(ft.life<ft.disappear
				?new Color(((int)(ft.life/ft.disappear*255)<<24) + (ft.c.getRGB()) , true)
				:ft.c
			);
			g.drawString(ft.str,left+8,y);
			y+=30;
		}
	}
	void clearAll(){
		nowShowMsgNum = 0;
		ftl.clear();
		height = 0;
		width = 0;
		lastStr = null;
	}
	void process(float second){
		
		ArrayList<FlowText> r = new ArrayList<FlowText>();
		for(FlowText f : ftl){
			f.life -= second;
			if(f.life < 0){
				r.add(f);
			}
		}
		for(FlowText f : r){
			ftl.remove(f);
			height -= 30;
			nowShowMsgNum--;
		}
		if(ftl.isEmpty()){
			clearAll();
		}
	}
}

class FlowTextList{
	class FlowText{
		float x,y;
		String str;
		float life,disappear;
		boolean alpha = true;
		Color c;
		int size;
		FlowText(float x,float y,String str,float life,Color c,float disappear){
			this.x = x;
			this.y = y;
			this.c = c;
			this.str	= str;
			this.life	= life+0.3f;
			this.disappear = disappear;
			size   = 1;
		}
		void draw(Graphics g){
			if(life <= 0.3)return;
			g.setColor(life<disappear
				?new Color(((int)(life/disappear*255)<<24) + (c.getRGB()) , true)
				:c
			);
			//if(life<3f){
			//System.out.println("FlowText:" + (int)(life/3f*255));
			//}
			//System.out.println(str+life);
			g.drawString(str,cw.toScreenX(x),cw.toScreenY(y));
		}
	}

	ArrayList<FlowText> ftl;
	ArrayList<FlowText> r;
	CameraWorld cw;
	final int Big = 1,Normal = 2;
	Font fontBig = new Font("MS Gothic", Font.BOLD, 30);
	Font fontNormal = new Font("SimSun", Font.PLAIN, 16);
	FlowTextList(CameraWorld cw){
		this.cw = cw;
		ftl = new ArrayList<FlowText>();
	}
	FlowText addFlowText(float x,float y,String str,float life,Color c,float disappear){
		FlowText t = new FlowText(x,y,str,life,c,disappear);
		ftl.add(t);
		return t;
	}
	void addFlowText(int x,int y,String str,float life,Color c,boolean alpha,int speedx,int speedy,int size){
		//ftl.add(new FlowText(x,y,str,life,c));
	}
	void process(float second){
		r = new ArrayList<FlowText>();
		for(FlowText f : ftl){
			f.life -= second;
			if(f.life < 0){
				r.add(f);
			}
		}
		for(FlowText f : r){
			ftl.remove(f);
		}
	}
	void clearAll(){
		r = new ArrayList<FlowText>();
		for(FlowText f : ftl){
			r.add(f);
		}
		for(FlowText f : r){
			ftl.remove(f);
		}
	}
	void removeFlowText(FlowText f){
		if(f == null)return;
		ftl.remove(f);
		//System.out.println("remove");
	}
	void draw(Graphics g){
		Font ft = g.getFont();
		for(FlowText f : ftl){
			switch(f.size){
			case Big:
				g.setFont(fontBig);
				break;
			case Normal:
				g.setFont(fontNormal);
				break;
			default:
				g.setFont(fontNormal);
			}
			f.draw(g);
		}
		g.setFont(ft);
	}
}