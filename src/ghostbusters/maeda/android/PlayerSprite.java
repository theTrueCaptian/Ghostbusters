package ghostbusters.maeda.android;


import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;

import com.badlogic.gdx.physics.box2d.Body;


/**
 * 
 * @author Maeda
 * This class contains all info related to player:
 * sprite and sprite physics
 *
 */
public class PlayerSprite{
	private GameActivity activity;
	private Sprite face;
	//private GameWorld world;
	Body body;
	private Text loselifesign;
	private int hearts;
	private Sprite helpsign;
	
	public PlayerSprite(GameActivity activity, GameWorld world){
		this.activity = activity;
		//this.world = world;

		this.hearts = 5;
		
		final float centerX = activity.width/2;
		final float centerY = activity.height/2;
		face = new Sprite(centerX, centerY, activity.reddie, activity.getVertexBufferObjectManager());
		helpsign = new Sprite(0,0, activity.helpsign, activity.getVertexBufferObjectManager());
		helpsign.setPosition(face.getX()-helpsign.getWidth()/2, face.getY()-activity.helpsign.getHeight()-5);
		
		this.loselifesign = new Text(0, 0, this.activity.loselifefont, "Life Lost!",activity.getVertexBufferObjectManager());		
		this.loselifesign.setPosition(activity.mCamera.getWidth()/2-this.loselifesign.getWidth()/2, activity.height/2-loselifesign.getHeight()/2 );		
		
	
		//add world physics to this body		
		//this setup player's desity, elasticity, friction
		/*final FixtureDef objectFixtureDef = PhysicsFactory.createFixtureDef(2, 0, 0f);
		body = PhysicsFactory.createBoxBody(this.world.getWorld(), face, BodyType.DynamicBody, objectFixtureDef);
		//to make sure physics worlds is aware of player's physics: parms sprite, body, update position?, and rotation?
		this.world.getWorld().registerPhysicsConnector(new PhysicsConnector(face, body, true, false));
		face.setUserData(body);
		body.setUserData("player");*/
		

	}
	public Sprite getHelpIns(){
		return helpsign;
	}
	public Sprite getSprite(){
		return face;
	}
	
	public Body getPlayerBody(){
		return body;
	}
	
	public void lostpoints(int pointslost){
		if(!loselifesign.hasParent())
			activity.mCurrentScene.attachChild(loselifesign);
		this.loselifesign.setVisible(true);
		final Text loselife = new Text(this.face.getX(), this.face.getY(),  activity.scorehudFont, "- "+pointslost+" pts", activity.getVertexBufferObjectManager());
		loselife.setPosition(face.getX()-10, face.getY()-10);
		
		activity.mCurrentScene.attachChild(loselife);
		loselife.registerEntityModifier(  new MoveYModifier(3, face.getY()-10, 0){ 
			@Override
			protected void onModifierFinished(IEntity pItem) {
				loselifesign.setVisible(false);
			    loselife.setVisible(false);
			}
		});
		
		
	}
	
	public void losealife(){
		hearts=hearts -1;
	}
	public void earnalife(){
		hearts=hearts+1;
	}
	public int getnumhearts(){
		return hearts;
	}
}
