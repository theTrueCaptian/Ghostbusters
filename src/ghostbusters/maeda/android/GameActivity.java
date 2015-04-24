package ghostbusters.maeda.android;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.util.FPSLogger;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.debug.Debug;

import com.tapfortap.Interstitial;
import com.tapfortap.TapForTap;

import android.graphics.Color;
import android.util.DisplayMetrics;

public class GameActivity extends SimpleBaseGameActivity{
	public int SCORE = 0;
	
	//A reference to the current scene
	public Scene mCurrentScene = null;
	public static GameActivity instance;
	
	//esstential varibles
	public Font mfont;
	public Font gameoverfont, message;
	public Font scorehudFont, loselifefont;	
	
	//camera stuff
	public Camera mCamera;
	//public BoundCamera mCamera;
	
	//screen height and width
	public int height=0, width=0;
	
	//random gen
	Random rand = new Random(1234567890);

	
	
	//creating the engine
	
	@Override
	public EngineOptions onCreateEngineOptions() {
		 instance = this;
		
		 TapForTap.initialize(this, "6317ef63b6618b99c272b1758b3efe46");
		 Interstitial.prepare(this);
		 
		 DisplayMetrics displaymetrics = new DisplayMetrics();
		 getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		 this.height = displaymetrics.heightPixels;
		 this.width = displaymetrics.widthPixels;
		 
	   	 mCamera = new Camera(0, 0, this.width, this.height);
		 //this.mCamera = new BoundCamera(0, 0, this.width	, this.height);
		 //this.mCamera.setBounds(0, 0, 2000, 700);
		 //this.mCamera.setBoundsEnabled(true);
	   	 
	   	 EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(this.width, this.height), mCamera);

	   	 engineOptions.getAudioOptions().setNeedsSound(true);
		   	
	   	 return engineOptions;
	 }

	//the methods here relate to loading scne**************************************************************************
	@Override
	protected Scene onCreateScene() {
		mEngine.registerUpdateHandler(new FPSLogger());
		mCurrentScene = new LoadingScene();
			
		return mCurrentScene;
	}
	
	public static GameActivity getSharedInstance() {
		return instance;
	}
	public Engine getEngine(){
		return this.mEngine;
	}
	
	// to change the current main scene

	public void setCurrentScene(Scene scene) {
	    mCurrentScene = scene;
	    getEngine().setScene(mCurrentScene);
	}
	
	@Override
    protected void onResume(){
	   	super.onResume();
	   	if(this.mCurrentScene!=null)
	   		this.setCurrentScene(new LoadingScene());
    }
	
	@Override
	public void onPauseGame() {
		super.onPauseGame();
	}

	//all methods from here relate to loading resources**********************************************************
	@Override
	protected void onCreateResources() {}
	
	public void loadSounds() {
		SoundFactory.setAssetBasePath("mfx/");
		try {
			this.menuSound = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, GlobalVariables.menuSoundString);
			this.gameover =  SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "gameover.wav");
			this.hotpoint =  SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "nexthotpoint.wav");
			this.knighthurt =  SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, GlobalVariables.knighthurt);
			
			this.killSounds= new ArrayList<Sound>();
			for(int i=0 ; i<GlobalVariables.killSound.length; i++){
				this.killSounds.add(SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, GlobalVariables.killSound[i]));
			}
		} catch (final IOException e) {
			Debug.e(e);
		}
	}

	public void loadFonts() {
		/* Load the font we are going to use. */
		FontFactory.setAssetBasePath("font/");
		this.mfont = FontFactory.createFromAsset(this.getFontManager(), this.getTextureManager(), 512, 512, TextureOptions.BILINEAR, this.getAssets(), "CloisterBlack.ttf", 32, true, Color.GREEN);
		this.mfont.load();
		
		this.scorehudFont = FontFactory.createFromAsset(this.getFontManager(), this.getTextureManager(), 512, 512, TextureOptions.BILINEAR, this.getAssets(), "CloisterBlack.ttf", 30, true, Color.GREEN);
		this.scorehudFont.load();
		
		this.loselifefont = FontFactory.createFromAsset(this.getFontManager(), this.getTextureManager(), 512, 512, TextureOptions.BILINEAR, this.getAssets(), "CloisterBlack.ttf", 75, true, Color.GREEN);
		this.loselifefont.load();
		
		this.message = FontFactory.createFromAsset(this.getFontManager(), this.getTextureManager(), 512, 512, TextureOptions.BILINEAR, this.getAssets(), "CloisterBlack.ttf", 75, true, Color.BLUE);
		this.message.load();
		
		this.gameoverfont = FontFactory.createFromAsset(this.getFontManager(), this.getTextureManager(), 512, 512, TextureOptions.BILINEAR, this.getAssets(), "CloisterBlack.ttf", 50, true, Color.RED);
		gameoverfont.load();
	}

	public void loadGraphics() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

		this.mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 1056, 1056, TextureOptions.BILINEAR);
		this.skybg = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureAtlas, this, "mainbg.png", 0, 0);
		this.mBitmapTextureAtlas.load();
		
		this.mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 1056, 1056, TextureOptions.BILINEAR);
		this.gameoversign = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureAtlas, this, "gameover.png", 0, 0);
		this.mBitmapTextureAtlas.load();
		
		this.mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 1056, 1056, TextureOptions.BILINEAR);
		this.helpsign = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureAtlas, this, "textbubble.png", 0, 0);
		this.mBitmapTextureAtlas.load();
		
		this.mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 256, 256, TextureOptions.BILINEAR);
		//this.mKnightTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "knight.png", 0, 1, 3, 2); // 582x529
		this.reddie = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureAtlas, this, "player.png",0,0); 
		this.mBitmapTextureAtlas.load();
		
		this.enemyball = new ArrayList<ITextureRegion>();
		for(int i=0; i<GlobalVariables.enemyballfiles.length; i++){
			this.mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 256, 256, TextureOptions.BILINEAR);
			this.enemyball.add(BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureAtlas, this, GlobalVariables.enemyballfiles[i], 0, 0)); 
			this.mBitmapTextureAtlas.load();
		}
		
		this.bgtextureatlas = new BitmapTextureAtlas(this.getTextureManager(), 1056, 1056, TextureOptions.BILINEAR);
		background = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.bgtextureatlas, this, GlobalVariables.backgroundfiles[0], 0, 0);
		this.bgtextureatlas.load();
		
		this.mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 256, 256, TextureOptions.BILINEAR);
		this.enemykill=BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureAtlas, this, "enemykilled.png", 0, 0); 
		this.mBitmapTextureAtlas.load();
		
	}
	public BitmapTextureAtlas mBitmapTextureAtlas;
	public ITextureRegion reddie, helpsign;
	public ITextureRegion gameoversign;
	
	public ITextureRegion skybg, enemykill;
	public ArrayList<ITextureRegion> enemyball;
	public BitmapTextureAtlas bgtextureatlas;
	//public ArrayList<ITextureRegion> backgrounds;
	public ITextureRegion background;
	public Sound menuSound, gameover, hotpoint, knighthurt;
	public ArrayList<Sound> killSounds ;
	public int killSoundsCTR=0;
}
