package ghostbusters.maeda.android;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.TextMenuItem;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;

import com.tapfortap.Interstitial;

import android.content.Intent;

public class GameOverScene extends Scene implements IOnMenuItemClickListener {
	private GameActivity activity;
	private Text GameOverText;
	private int replayid=0, menu=1;
	private MenuScene menuscene;
	
	public GameOverScene(final GameActivity activity){
		this.activity = activity;		
		Sprite bgsprite = new Sprite(0, 0,activity.skybg , activity.getVertexBufferObjectManager());
		bgsprite.setSize(activity.width, activity.height);
		this.attachChild(bgsprite );
		
		Sprite gameover = new Sprite(0, 0,activity.gameoversign , activity.getVertexBufferObjectManager());
		gameover.setPosition(activity.width/2-gameover.getWidth()/2, 0);		
		this.attachChild(gameover );
		
		GameOverText = new Text(0, 0, activity.gameoverfont, "Score:"+activity.SCORE, activity.getVertexBufferObjectManager());		
		GameOverText.setPosition(activity.width/2-GameOverText.getWidth()/2, gameover.getHeight()+5);		
		this.attachChild(GameOverText);
		
		this.menuscene = this.createMenuScene();
		
		activity.gameover.play();
		
		gameover.registerEntityModifier( new MoveYModifier(1, 0, activity.height/3-gameover.getHeight()/2));
		GameOverText.registerEntityModifier( new MoveYModifier(1, 0, activity.height/3-GameOverText.getHeight()/2+ gameover.getHeight()+5));
		System.out.println("game text set alrady");
		
		DelayModifier dMod = new DelayModifier(1){
			@Override
			protected void onModifierFinished(IEntity pItem) {
				/* Attach the menu. */
				// Later when you want to display the interstitial
				Interstitial.show(activity);
				setChildScene(menuscene, false, true, true);
			}};
		registerEntityModifier(dMod);
		
	}
	
	private MenuScene createMenuScene() {
		MenuScene menuScene = new MenuScene(activity.mCamera);
		IMenuItem replay = new TextMenuItem(replayid, activity.scorehudFont, ("Replay!"), activity.getVertexBufferObjectManager());
		replay.setPosition(activity.width/3-replay.getWidth()/2, 2*activity.height/3-replay.getHeight()/2+GameOverText.getHeight()/2);
		menuScene.addMenuItem(replay);		
				
		IMenuItem menumenuscene = new TextMenuItem(menu, activity.scorehudFont, ("Back to Menu"), activity.getVertexBufferObjectManager());
		menumenuscene.setPosition(2*activity.width/3-menumenuscene.getWidth()/2, 2*activity.height/3-menumenuscene.getHeight()/2+GameOverText.getHeight()/2);
		menuScene.addMenuItem(menumenuscene);
		
		menuScene.setBackgroundEnabled(false);
		
		menuScene.setOnMenuItemClickListener(this);
		return menuScene;
	}

	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem,
			float pMenuItemLocalX, float pMenuItemLocalY) {
			System.out.println("touch detected");
			switch(pMenuItem.getID()){
				case 0:
					activity.menuSound.play();
					Intent myIntent1 = new Intent(activity.getBaseContext(), GameActivity.class);
					activity.startActivityForResult(myIntent1, 0);
					//activity.setCurrentScene(new GameScene());
				
					return true;
				case 1:
					activity.menuSound.play();
					
					Intent myIntent2 = new Intent(activity.getBaseContext(), MainActivity.class);
					activity.startActivityForResult(myIntent2, 0);
					//activity.setCurrentScene(new GameActivity());
					return true;
				default:
					break;
			}
			return false;
	}
}
