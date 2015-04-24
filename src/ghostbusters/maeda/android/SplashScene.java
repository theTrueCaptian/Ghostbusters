package ghostbusters.maeda.android;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;


public class SplashScene extends Scene{
	MainActivity activity;
	//private BitmapTextureAtlas mBitmapTextureAtlas;
	public BitmapTextureAtlas bgtextureatlas;

	public SplashScene(){
		activity = MainActivity.getSharedInstance();
		
		
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		this.bgtextureatlas = new BitmapTextureAtlas(activity.getTextureManager(), 1056, 1056, TextureOptions.BILINEAR);
		ITextureRegion background = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.bgtextureatlas, activity, "splashbg.png", 0, 0);
		this.bgtextureatlas.load();
		//attach background pic
		Sprite bgsprite = new Sprite(0, 0,background , activity.getVertexBufferObjectManager());
		bgsprite.setSize(activity.width, activity.height);
		this.attachChild(bgsprite );
		
		//setBackground(new Background(0.20f, 0.6274f, 0.8784f));
		
		
		Text title1 = new Text(0, 0, activity.plok, "Captian", activity.getVertexBufferObjectManager());
		Text title2 = new Text(0, 0, activity.plok, "Coder", activity.getVertexBufferObjectManager());
		
		title1.setPosition(-title1.getWidth(), activity.mCamera.getHeight()/2);
		title2.setPosition(activity.mCamera.getWidth(), activity.mCamera.getHeight()/2);
		
		if(!GlobalVariables.SPLASHSHOWN){
			GlobalVariables.SPLASHSHOWN = true;
		}else{
			 activity.setCurrentScene(new MainMenuScene());
		}
		
		attachChild(title1);
		attachChild(title2);
		
		title1.registerEntityModifier( new MoveXModifier(1, title1.getX(), activity.mCamera.getWidth()/2 - title1.getWidth()));
		title2.registerEntityModifier( new MoveXModifier(1, title2.getX(), activity.mCamera.getWidth()/2));
		
		loadResources();
	}
	
	public void loadResources(){
		DelayModifier dMod = new DelayModifier(2){
			@Override
			protected void onModifierFinished(IEntity pItem) {
			        activity.setCurrentScene(new MainMenuScene());
			}};
		registerEntityModifier(dMod);

	}

}
