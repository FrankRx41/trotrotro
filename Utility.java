/*******************************************************************************

*******************************************************************************/
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import java.util.List;

public class Utility {

	/* copy: Fundamental-2D-Game-Programming-With-Java-master\CH11\src\javagames\Util\Utility.java */
	public static int drawString(Graphics g, int x, int y, String str) {
		return drawString(g, x, y, new String[] { str });
	}
	public static int drawString(Graphics g, int x, int y, String str,int width) {
		FontMetrics fm = g.getFontMetrics();
		int height = fm.getAscent() + fm.getDescent() + fm.getLeading();
		String[] arrStr;
		int len = str.length();
		int totalWidth = fm.stringWidth(str);
		int onecharwidth = fm.charWidth('我');
		if(totalWidth > width)
			arrStr = new String[(int)(totalWidth*1.0/width+0.99)];
		else
			arrStr = new String[1];
		
		int j=0;
		int k=0;
		for(int i=0;i<(int)(totalWidth*1.0/width+0.99);i++){
		
			arrStr[i] = str.substring(j,k);
			while(fm.stringWidth(arrStr[i]) < width && k<len){
				//System.out.println(str);
				k++;
				//System.out.println(str.substring(j,k));
				arrStr[i] = str.substring(j,k);
			}
			j=k;
		}
		
		for (String s : arrStr) {
			if(s==null)break;
			g.drawString(s, x, y + fm.getAscent());
			y += height;
		}
		return y;
	}
	public static int drawString(Graphics g, int x, int y, List<String> str) {
		return drawString(g, x, y, str.toArray(new String[0]));
	}

	public static int drawString(Graphics g, int x, int y, String... str) {
		FontMetrics fm = g.getFontMetrics();
		int height = fm.getAscent() + fm.getDescent() + fm.getLeading();
		for (String s : str) {
			g.drawString(s, x, y + fm.getAscent());
			y += height;
		}
		return y;
	}
	public static float max(float a,float b){
		return a>b?a:b;
	}
	public static BufferedImage loadImage(String name){
	/* copy: http://www.geocities.jp/inu_poti/java/meida/image01.html */
		if(name == null)return null;
	    try{
	    	FileInputStream in = new FileInputStream(name);	//FileInputStreamを作る
	      	BufferedImage rv = ImageIO.read(in);	//イメージを取り込む
	      	in.close();								//閉じる
	      	return rv;								//戻り値に読み込んだイメージをセット
	    }catch(IOException e){
	      	//エラー時の処理（エラーを表示）しnullを返す
	      	System.out.println("Err e: "+e);		//エラーを表示
	      	return null;							//null　を返す
	    }
  	}
}