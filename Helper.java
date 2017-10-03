import java.awt.*;
import java.awt.event.*;

class Helper{
	GameLevel gl;
	InputListener il;
	LeftMessage lm;
	GameEngine.GameOption go;
	int estop[] = new int[100];
	final int msg = 0,war = 1,err = 2,dia = 3;
	final int
		ArmoryBeDestory = 1,
		WhitePlane		= 2,
		DonotCareBoard	= 3,
		FirstBlood		= 4,
		HaveManyGold	= 5,
		HaveTooManyGold	= 6,
		BulletRunOut	= 7,
		BulletWillRunOut= 8,
		LowHP			= 9,
		WhitePlaneMoreSocre = 10,
		LossArmoy		= 11,
		OpenInfo		= 12,
		Move			= 13,
		LastLevel		= 14,
		STAFF1			= 21,
		STAFF2			= 22,
		STAFF3			= 23,
		STAFF4			= 24,
		STAFF5			= 25,
		STAFF6			= 26,
		STAFF7			= 27,
		STAFF8			= 28,
		STAFF9			= 29,
		STAFF10			= 30,
		STAFF11			= 31,
		STAFF12			= 32,
		STAFF13			= 33,
		STAFF14			= 34,
		STAFF15			= 35,
		STAFF16			= 36,
		STAFF17			= 37,
		STAFF18			= 38,
		STAFF19			= 39,
		STAFF20			= 40,
		DIALOG1_1		= 41,
		DIALOG2_1		= 42,
		DIALOG2_2 		= 43,
		DIALOG3_1		= 44,
		DIALOG3_2		= 45,
		DIALOG4_1		= 46,
		DIALOG4_2		= 47
		
	;
	void notice(int id){
		if(estop[id]==1)return;
		estop[id]=1;
		
		if(gl.level == 5){
			switch(id){
			case STAFF1:
				addFlowText("總監督       ",war);
				addFlowText("符传锐",msg);
				break;
			case STAFF2:
				addFlowText("美工        ",war);
				addFlowText("張昊辰&劉孝芳",msg);
				break;     
			case STAFF3:  
				addFlowText("音效        ",war);
				addFlowText("劉孝芳&楊揚",msg);
				break;     
			case STAFF4:  
				addFlowText("程序        ",war);
				addFlowText("符传锐&姜昊",msg);
				break;     
			case STAFF5: 
				addFlowText("腳本        ",war);
				addFlowText("楊揚&張昊辰",msg);
				break;     
			case STAFF6:  
				addFlowText("關卡設計      ",war);
				addFlowText("符传锐&楊揚 ",msg);
				break;     
			case STAFF7:  
				addFlowText("EiSnow    ",war);
				addFlowText("2017.6.5",msg);
				break;     
			//case STAFF8:
			//	gl.ft.addFlowText(-35,150,"EiSnow",1000f,Color.red,0);
			//	gl.ft.addFlowText(-43,125,"2017.6.5",1000f,Color.white,0);
			//	break;     
			default:
				//addFlowText2("",msg);
			//case STAFF10:  
			//	
			//	break;     
			//case STAFF9:   
			//	addFlowText2("alpha版製作完成日期",war);
			//	break;     
			//case STAFF8:   
			//	addFlowText2("Beta 版製作完成日期",war);
			//	break;     
			//case STAFF2:   
			//	addFlowText2("EiSnow",err);
			//	break;     
			//case STAFF1:   
			//	addFlowText2("",msg);
			//	break;     
			}
		}else{
			if(!go.showHelp)return;
			if(gl.level==1){
				switch(id){
				case ArmoryBeDestory:
					addFlowText("小心！彈藥庫爆炸後會對戰艦甲板造成巨大傷害",err);
					break;
				case WhitePlane:
					addFlowText("小心！白色的飛機擁有更高的移動速度，並且可以同時發射多發子彈",err);
					break;
				case DonotCareBoard:
					addFlowText("戰艦甲板擁有極高的耐久，你不需要擔心這些子彈就可以把它擊沉",msg);
					break;
				case FirstBlood:
					addFlowText("做的很好，攔截住他們",dia);
					break;
				case HaveManyGold:
					addFlowText("你所擁有的金幣可以在商店中購買物品",msg);
					break;
				case HaveTooManyGold:
					addFlowText("你有很多金幣",msg);
					break;
				}
				
			}
			switch(id){
			case DIALOG1_1:
				addFlowText("很好，你通過了初級訓練。現在，讓我們開始真正的戰鬥！",dia);
				break;
			case DIALOG2_1:
				addFlowText("前方就是日本海域，我們盡量隱藏，然後將日軍一舉擊潰",dia);
				break;
			case DIALOG2_2:
				addFlowText("很好，我們進入了日軍的軍事基地了。不過請小心，裏面的防守飛機將會更多",dia);
				break;
			case DIALOG3_1:
				addFlowText("警告！發現大量飛機正在朝我們方向飛行！",dia);
				break;
			case DIALOG3_2:
				addFlowText("很好，之後就是最危險的地方了，請小心",dia);
				break;
			case DIALOG4_1:
				addFlowText("警告！我們正在出於包圍之中！",dia);
				break;
			case DIALOG4_2:
				addFlowText("很好，你出色的完成了任務，我們向你致敬！",dia);
				break;
				
				


			case BulletRunOut:
				estop[id]=0;
				addFlowText("你沒有多餘的彈藥！按["+KeyEvent.getKeyText(gl.RECRUIT_BULLETS.key)+"]補充彈藥",err);
				break;
			case BulletWillRunOut:
				estop[id]=0;
				addFlowText("按["+KeyEvent.getKeyText(gl.RECRUIT_BULLETS.key)+"]補充彈藥",err);
				break;
			case LowHP:
				estop[id]=0;
				addFlowText("按["+KeyEvent.getKeyText(gl.REPAIR.key)+"]修復戰艦",err);
				break;
			case OpenInfo:
				addFlowText("按下["+KeyEvent.getKeyText(il.KEY_SHOW_INFO)+"]鍵打開狀態表，查看情報時遊戲會自動暫停",msg);
				break;
			case Move:
				addFlowText("使用鍵盤移動戰艦，使用滑鼠攻擊",msg);
				break;
			case LastLevel:
				//addFlowText("恭喜進入隱藏關卡",msg);
				break;
			}
		}
	}
	void reset(){
		for(int i=0;i<estop.length;i++){
			estop[i] = 0;
		}
	}
	Helper(LeftMessage lm,CameraWorld cw,ScreenWorld sw,GameEngine.GameOption go,GameLevel gl){
		this.lm = lm;
		this.go = go;
		this.gl = gl;
		this.il = gl.il;
		reset();
	}
	void addFlowText(String str,int degree){
		Color c;
		switch(degree){
		case msg:c = Color.white;	break;
		case war:c = Color.yellow;	break;
		case err:c = Color.red;		break;
		case dia:c = Color.blue;	break;
		default: c = Color.black;
		}
		lm.addLeftMessage(str,c);		
		//System.out.println("add");
	}
}