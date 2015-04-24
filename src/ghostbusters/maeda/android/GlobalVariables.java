package ghostbusters.maeda.android;

import org.andengine.util.color.Color;

public class GlobalVariables {
	public static boolean SPLASHSHOWN = false;
	public static String[] enemyballfiles = {"enemyball.png"};
	public static String[] backgroundfiles = {"mainbg.png"};
	public static Color[] dispcolors ={Color.GREEN};
	public static String[] messages = {"Proceed!", "Faster!", "Accelerate!", "Hurry!", "Quick!!", "Argh!!!", "Fantastic!", "No Way!", "Impossible!!!!!"};
	public static int[] hotPoints = {1000, 2000, 3000, 4000, 5000, 7500, 10000,20000,40000,100000};
	//public static int[] hotPoints = {20, 50, 100, 200, 5000, 10000,20000,40000,100000};
	
	public static String menuSoundString = "buttonmenu.wav";
	public static String killSound[] = {"objecttouch.wav", "gameobject2.wav","killobject.wav"};
	public static String knighthurt = "objecttouch2.wav";
	public static int topScore = 0;
}
