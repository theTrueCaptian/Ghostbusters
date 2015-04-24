package ghostbusters.maeda.android;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class GameWorld {
	//physics world
	private PhysicsWorld mPhysicsWorld;
	//private GameActivity activity;
	//private Sprite ground;
	
	public GameWorld(GameActivity activity, GameScene scene){
		//this.activity = activity;
		
		//set world physics
		this.mPhysicsWorld = new PhysicsWorld(new Vector2(0, 0), false);
		
		final VertexBufferObjectManager vertexBufferObjectManager = activity.getVertexBufferObjectManager();
		final Rectangle ground = new Rectangle(0, activity.height - 2, activity.width, 2, vertexBufferObjectManager);
		/*ground = new Sprite(0, activity.height - 2, activity.grassRegion, vertexBufferObjectManager);
		ground.setSize(activity.WORLDWIDTH, 20);
		scene.attachChild(ground);
		*/
		
		final Rectangle roof = new Rectangle(0, 0, activity.width, 2, vertexBufferObjectManager);
		final Rectangle left = new Rectangle(0, 0, 2, activity.height, vertexBufferObjectManager);
		final Rectangle right = new Rectangle(activity.width - 2, 0, 2, activity.height, vertexBufferObjectManager);

		final FixtureDef wallFixtureDef = PhysicsFactory.createFixtureDef(0, 0.5f, 0.5f);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, ground, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, roof, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, left, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, right, BodyType.StaticBody, wallFixtureDef);
		
		
	}
	
	/*public Sprite getGround(){
		return this.ground;
	}*/
	
	public PhysicsWorld getWorld(){
		return mPhysicsWorld;
	}
	
	
			
}
