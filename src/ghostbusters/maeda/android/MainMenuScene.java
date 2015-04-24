package ghostbusters.maeda.android;

import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;

import android.content.Intent;

public class MainMenuScene extends MenuScene implements IOnMenuItemClickListener{
	MainActivity activity;
	int play=0;
	int ins =1;
	
	public MainMenuScene(){
		super(MainActivity.getSharedInstance().mCamera);
		activity = MainActivity.getSharedInstance();
		
		//setBackground(new Background(0.20f, 0.6274f, 0.8784f));
		Sprite bgsprite = new Sprite(0, 0,activity.skybg , activity.getVertexBufferObjectManager());
		bgsprite.setSize(activity.width, activity.height);
		this.attachChild(bgsprite );
		
		Text title1 = new Text(0, 0, activity.mFont, "Ghost Busters", activity.getVertexBufferObjectManager());		
		title1.setPosition(activity.mCamera.getWidth()/2-title1.getWidth()/2, 30);		
		title1.setColor(1, 0, 0);
		attachChild(title1);
		
		/*IMenuItem startButton = new TextMenuItem(play, activity.mFont, ("Begin"), activity.getVertexBufferObjectManager());
		startButton.setPosition(mCamera.getWidth()/2-startButton.getWidth()/2, mCamera.getHeight()/2-startButton.getHeight()/2);
		startButton.setColor(1,0,0);
		addMenuItem(startButton);	*/	
		
		SpriteMenuItem begin = new SpriteMenuItem(play, activity.startbutton, activity.getVertexBufferObjectManager());
		begin.setPosition(mCamera.getWidth()/2-begin.getWidth()/2, mCamera.getHeight()/2-begin.getHeight()/2);
		addMenuItem(begin);
		
		setOnMenuItemClickListener(this);
	}
	
	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem, float pMenuItemLocalX, float pMenuItemLocalY) {

		switch(pMenuItem.getID()){
			case 0:
				activity.menuSound.play();
				Intent myIntent1 = new Intent(activity.getBaseContext(), GameActivity.class);
				activity.startActivityForResult(myIntent1, 0);
				//activity.setCurrentScene(new GameScene());
			
				return true;
			/*case 1:
				Intent myIntent2 = new Intent(activity.getBaseContext(), GameActivity.class);
				activity.startActivityForResult(myIntent2, 0);
				//activity.setCurrentScene(new GameActivity());
				return true;*/
			default:
				break;
		}
		
		return false;
	}

}
