package ghostbusters.maeda.android;


import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;

public class GameScene extends Scene {
	private GameActivity activity;
	private PlayerSprite player;
	private EnemyBalls enemies;
	private GameWorld world;
	private float newEnemyDelay =  5f;
	
	private Sprite bgsprite;
	private int bgctr = 0;//keeps track which bg to disp
	private int msgctr = 0;//keeps track which message to display
	private int velocityctr=0; //keeps track of velocity inc of enemies
	
	//score hud
	private HUD scorehud;
	private Text scoredisp, playerinfo, earnlife;

	 
	public GameScene(){
		activity = GameActivity.getSharedInstance();
		//attach background pic
		bgsprite = new Sprite(0, 0,activity.background , activity.getVertexBufferObjectManager());
		bgsprite.setSize(activity.width, activity.height);
		this.attachChild(bgsprite );
		
		
		
		//creating and registering the physics and world to the scene
		world = new GameWorld(activity, this);
		this.registerUpdateHandler(world.getWorld());
				
		//setting up a bgcolor
		this.setBackground(new Background(0, 0, 0));	
		
		//player object
		player = new PlayerSprite(activity, world);
		this.attachChild(player.getSprite());
		this.attachChild(player.getHelpIns());
		
		//add hud for player info etc. make sure to call this after player has been init
		//also be sure to call this before calling the controls
		setupScoreLifeHUD();

		
		//set enemies
		enemies = new EnemyBalls(activity, this);		
		this.attachChild(enemies.getLast().getSprite());
		
		//set up game play where the weapons are shot out from enemy side into the air every 0.5 sec
		//this timer also updates the score variable
		this.registerUpdateHandler(new TimerHandler(newEnemyDelay, true, new ITimerCallback() {
			@Override
			public void onTimePassed(final TimerHandler pTimerHandler) {
				//decide how many are going to be thrown into air
				int quan = activity.rand.nextInt(6)+2;
				//gen new enemy
				for(int i=0; i<quan; i++){
					if(enemies.newEnemy()){ //if succesful enemy add, then attach it to screen
						getInstance().attachChild(enemies.getLast().getSprite());
					}
				}	
				
				//throw a dice if a life shud be thrown
				int dice = activity.rand.nextInt(6);
				if(dice==3){
					//throw a new life
					throwlife();
				}
			}
		}));
		
		this.registerUpdateHandler(new IUpdateHandler(){
			@Override
			public void onUpdate(float pSecondsElapsed) {
				//check if any collision occured
				checkCollision();
				updateHud();
				//check if gameover
				if(player.getnumhearts()<=0){
					scorehud.setVisible(false);					
					activity.setCurrentScene(new GameOverScene(activity));
				}
				
			}
			@Override
			public void reset() {}});
		
	}
	
	public void setupScoreLifeHUD(){
		scorehud = new HUD();	
		
		//set text for player num of hearts
		this.playerinfo = new Text(0,0, this.activity.scorehudFont, "Lives:"+player.getnumhearts(),"Lives:xxx".length(), activity.getVertexBufferObjectManager());
		this.playerinfo.setPosition(10, 3);		
		
		//set score text
		this.scoredisp = new Text(0, 0, this.activity.scorehudFont, "Score:"+activity.SCORE,"Score:xxxxxxxxx".length(),activity.getVertexBufferObjectManager());		
		this.scoredisp.setPosition(activity.mCamera.getWidth()/2-this.scoredisp.getWidth(), 3);		
		
		scorehud.attachChild(this.scoredisp);
	    scorehud.attachChild(this.playerinfo);
	    
	    this.activity.mCamera.setHUD(scorehud);
	
	}
	
	public GameScene getInstance(){
		return this;
	}
	public void detach(AnimatedSprite kid){
		this.detachChild(kid);
	}

	public HUD getHud(){
		return this.scorehud;
	}
	
	public void updateHud(){
		try{
			playerinfo.setText("Lives:"+player.getnumhearts());
			//update score variable
			scoredisp.setText("Score:"+activity.SCORE);
		}catch(ArrayIndexOutOfBoundsException ex){
			System.out.println("score to high niggay");
		}
	}
	
	
	public void checkCollision(){
		for(int i=0; i<this.enemies.getAllWeapons().size(); i++){
			if(this.enemies.getAllWeapons().get(i).getSprite().collidesWith(player.getSprite())){
				//if this weapon collides with the player then player loses a few life points and the wweapon dies
				int pointslost = activity.rand.nextInt(20);
				player.lostpoints(pointslost);//this is for displaying the points lost
				player.losealife();//player must lose a life 
				this.playerlosepoints(pointslost);//deduct a few points from score
				this.enemies.removeWeapon(this.enemies.getAllWeapons().get(i).getWeaponId());
				activity.knighthurt.play();
			}
		}
		
	}
	
	public void playerlosepoints(int pts){
		this.activity.SCORE -=pts;
	}
	public int addplayerkillpoints(){
		this.activity.SCORE +=10;
		if(player.getHelpIns().isVisible()){//remove help ins if needed
			player.getHelpIns().setVisible(false);
		}
		changebackground();//see if we need to change bg
		spawnmessage();//see if we need to spawn messages
		incvelocity();//see if we need to inc veocity of enemies and inc limit of enemies
		return 10;
		//this.bgctr++;
	}
	
	public void incvelocity(){
		if(activity.SCORE>=GlobalVariables.hotPoints[this.velocityctr]){//check if player surpassed a hotpoint
			this.enemies.incvelocity();
			
			this.velocityctr+=1;
			if(this.velocityctr>=GlobalVariables.hotPoints.length ){
				this.velocityctr=0;
			}
		}
	}
	
	public void spawnmessage(){//messages of encouragement
		if(activity.SCORE>=GlobalVariables.hotPoints[this.msgctr]){//check if player surpassed a hotpoint
			final Text msg = new Text(0, activity.height/2,  activity.message, GlobalVariables.messages[this.msgctr], activity.getVertexBufferObjectManager());
			msg.setPosition(0, activity.height/2-msg.getHeight()/2);
			//msg.setColor(GlobalVariables.dispcolors[activity.rand.nextInt(GlobalVariables.dispcolors.length)]);
			activity.mCurrentScene.attachChild(msg);
			activity.hotpoint.play();
			msg.registerEntityModifier(  new MoveXModifier(5, 0-msg.getWidth(), activity.width+msg.getWidth()){ 
				@Override
				protected void onModifierFinished(IEntity pItem) {	msg.setVisible(false);	}
			});
			this.msgctr+=1;
			if(this.msgctr>=GlobalVariables.hotPoints.length || this.msgctr>=GlobalVariables.messages.length ){
				this.msgctr=0;
			}
		}
	}
	public void changebackground(){
		if(activity.SCORE>=GlobalVariables.hotPoints[this.bgctr]){//check if player surpassed a hotpoint
			this.bgctr+=1;
			if(this.bgctr>=GlobalVariables.hotPoints.length || this.bgctr>=GlobalVariables.backgroundfiles.length ){
				this.bgctr=0;
			}
			activity.bgtextureatlas.clearTextureAtlasSources();
			activity.background = BitmapTextureAtlasTextureRegionFactory.createFromAsset(activity.bgtextureatlas, activity, GlobalVariables.backgroundfiles[this.bgctr], 0, 0); 
			activity.bgtextureatlas.load();
			activity.background.setTextureSize(activity.width, activity.height);
			
		}
		
	}
	
	//free life chance
	public void throwlife(){
		earnlife = new Text(activity.rand.nextInt(activity.width-10)+10,0,  activity.loselifefont, "1 life", activity.getVertexBufferObjectManager()){
			
			public boolean onAreaTouched(TouchEvent touchEvent, float X, float Y){
				//if touched then kill this
				earnlife.setVisible(false);
				player.earnalife();
				unregisterTouchArea(earnlife);
				return true;
				
			}
		};
		this.registerTouchArea(earnlife);
		activity.mCurrentScene.attachChild(earnlife);
		earnlife.registerEntityModifier(  new MoveYModifier(2, 0-earnlife.getHeight(), activity.height+earnlife.getHeight()){ 
			@Override
			protected void onModifierFinished(IEntity pItem) {	earnlife.setVisible(false);	}
		});
	}
}
