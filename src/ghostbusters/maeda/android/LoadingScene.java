package ghostbusters.maeda.android;

import ghostbusters.maeda.android.GameActivity;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;


import android.graphics.Color;


public class LoadingScene extends Scene{
	private GameActivity activity;
	private BitmapTextureAtlas mBitmapTextureAtlas;
	
	public LoadingScene(){
		activity = GameActivity.getSharedInstance();
		
		//this.setBackground(new Background(0.09804f, 0.6274f, 0.8784f));
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		this.mBitmapTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 1056, 1056, TextureOptions.BILINEAR);
		activity.skybg = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureAtlas, activity, "mainbg.png", 0, 0);
		this.mBitmapTextureAtlas.load();
		
		this.mBitmapTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 1056, 1056, TextureOptions.BILINEAR);
		ITextureRegion loadingt = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureAtlas, activity, "loading.png", 0, 0);
		this.mBitmapTextureAtlas.load();
		
		Sprite bgsprite = new Sprite(0, 0,activity.skybg , activity.getVertexBufferObjectManager());
		bgsprite.setSize(activity.width, activity.height);
		this.attachChild(bgsprite );
		
		Sprite loading = new Sprite(0, 0,loadingt , activity.getVertexBufferObjectManager());
		loading.setPosition(activity.mCamera.getWidth()/2-loading.getWidth()/2, activity.mCamera.getHeight()/2-loading.getHeight());		
		this.attachChild(loading );
		
		FontFactory.setAssetBasePath("font/");
			
		Font textfont = FontFactory.createFromAsset(activity.getFontManager(), activity.getTextureManager(), 512, 512, TextureOptions.BILINEAR, activity.getAssets(), "CloisterBlack.ttf", 40, true, Color.RED);
		textfont.load();
		Text ins = new Text(0, 0, textfont, "  Tap on the blue flying  \n objects before they reach \n          Red!", activity.getVertexBufferObjectManager());		
		ins.setPosition(activity.mCamera.getWidth()/2-ins.getWidth()/2, activity.mCamera.getHeight()-ins.getHeight()-10);		
		this.attachChild(ins);
		
		activity.loadGraphics();
		activity.loadFonts();
		activity.loadSounds();
		//activity.setCurrentScene(new MainMenuScene());
		DelayModifier dMod = new DelayModifier(10){
			@Override
			protected void onModifierFinished(IEntity pItem) {
			    activity.setCurrentScene(new GameScene());
			}};
		registerEntityModifier(dMod);
		
	}
	

}
