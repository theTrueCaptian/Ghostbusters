package ghostbusters.maeda.android;

import java.util.Random;

import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;

/*
 * individual weapon object thrown at player
 */
public class Weapon{
	private Sprite weapons;
	private GameActivity activity;
	
	private EnemyBalls manager;
	private GameScene scene;
	
	//being a ball yo
	//private int minVelocity=80;
	
	private PhysicsHandler mPhysicsHandler;
	int constantx ;int constanty;
	//id is an int used to identify the weapon when killing it
	private int id;
	
	public Weapon(float pX, float pY, GameActivity activity,  final EnemyBalls manager, final int id, GameScene scene){
		this.activity = activity;
		this.manager= manager;
		this.id = id;
		this.scene = scene;
		
		constantx = activity.rand.nextInt(10)+manager.minVelocity; //random int from minvelocity and 10+
		constanty=activity.rand.nextInt(10)+manager.minVelocity;
		
		addFace(pX,pY);
		
	}
	
	private void addFace(final float pX, final float pY) {
		
		ITextureRegion datface;
		int tile = activity.rand.nextInt(GlobalVariables.enemyballfiles.length);
		datface = activity.enemyball.get(tile);
		weapons = new Sprite(pX, pY, datface, activity.getVertexBufferObjectManager()){
			public boolean onAreaTouched(TouchEvent touchEvent, float X, float Y){
				//if touched then kill this
				System.out.println("kill this weapon:"+id);
				
				activity.killSounds.get(activity.killSoundsCTR).play();
				activity.killSoundsCTR++;
				if(activity.killSoundsCTR>=activity.killSounds.size()){
					activity.killSoundsCTR=0;
				}
				
				final Sprite shadow = new Sprite(weapons.getX(), weapons.getY(), activity.enemykill, activity.getVertexBufferObjectManager());
				shadow.setSize(weapons.getWidth(), weapons.getHeight());
				shadow.registerEntityModifier(new ScaleModifier(2, 1.0f, 0.2f, 1.0f, 0.2f){
					@Override
					protected void onModifierFinished(IEntity pItem) {
					  	
						shadow.setVisible(false);
					}
				});
				activity.mCurrentScene.attachChild(shadow);
				
				manager.killpointsdisp(scene.addplayerkillpoints(), weapons.getX(), weapons.getY());//disp points earned in killing
				manager.removeWeapon(id);
				return true;
				
			}
			//making it ballling
			@Override
			protected void onManagedUpdate(final float pSecondsElapsed) {
				if(this.mX < 0) {
					mPhysicsHandler.setVelocityX(constantx);
				} else if(this.mX + this.getWidth() > activity.width) {
					mPhysicsHandler.setVelocityX(-constantx);
				}

				if(this.mY < 0) {
					mPhysicsHandler.setVelocityY(constanty);
				} else if(this.mY + this.getHeight() > activity.height) {
					mPhysicsHandler.setVelocityY(-constanty);
				}

				super.onManagedUpdate(pSecondsElapsed);
			}
		};
		
		//dont forget to registre your wepon's touch area!!! java grr
		this.scene.registerTouchArea(weapons);
		int lowrange =activity.height/8;
		int newrad = new Random(20).nextInt(20)+lowrange;
		weapons.setHeight(newrad);
		weapons.setWidth(newrad);
		 
		//doing ball physics yo
		this.mPhysicsHandler = new PhysicsHandler(weapons);
		scene.registerUpdateHandler(this.mPhysicsHandler);
		this.mPhysicsHandler.setVelocity(constantx, constanty);
				
	}

	public void removeFace() {
		//this statement makes the exception this statement must be run into the RunnableHandler.postRunnable() which is registered to the Scene or Engine itself
		//this.scene.detach(weapons);
		this.scene.unregisterUpdateHandler(this.mPhysicsHandler);
		this.weapons.setVisible(false);
		//dont forget to unregister the weapons touch are
		this.scene.unregisterTouchArea(weapons);
		this.weapons.dispose();
		System.gc();
		
	}
	
	public Sprite getSprite(){
		return weapons;
	}
	public int getWeaponId(){
		return this.id;
	}
	
	
	
		
	
}
