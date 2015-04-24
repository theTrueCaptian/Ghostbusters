package ghostbusters.maeda.android;

import java.util.ArrayList;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.text.Text;

/*
 * Array of all the weapons throw at player 
 * 
 */
public class EnemyBalls {
	private ArrayList<Weapon> enemies;
	private GameActivity activity;
	private GameScene scene;
	
	public int LIMIT = 10;//limit on number of enemies spawned
	public int minVelocity=80;//min velocity of enemies
	
	public EnemyBalls(GameActivity activity, GameScene scene){
		this.activity = activity;
		this.scene = scene;
		
		
		enemies = new ArrayList<Weapon>();
		newEnemy();
	}
	
	
	
	public boolean newEnemy(){
		if(this.enemies.size()<LIMIT){ //continure to add new weapons as long as it doesn't exceed limits
			int index = enemies.size();
			
			int x = activity.rand.nextInt(activity.width);
			int y = activity.rand.nextInt(activity.height);
			if(index%2==0){
				x=0;
			}else if(index%3==0){
				y=0;
			}else if(index%5==0){
				y=activity.height;
			}else{
				x=activity.width;
			}
			enemies.add(new Weapon(x+activity.rand.nextInt(10)-5, y-activity.rand.nextInt(10), activity, this, index, scene));
			
			//x has a +5 tp -5 range addition
			//y has a 10 range minus
			//enemies.add(new Weapon(x+activity.rand.nextInt(10)-5, y-enemyfort.getHeight() - activity.rand.nextInt(10), activity, this, index, scene));
			return true;
		}
		return false;
	}
	
	public void removeWeapon(int weaponIndex){
		//search for index==weapon.id 
		
		try{
			for(int i=0; i<enemies.size(); i++){
				if(enemies.get(i).getWeaponId()==weaponIndex){
						
						//remove physics and sprite
						enemies.get(i).removeFace();
						
						//remove from array
						 enemies.remove(i);
						
						break;
				}
			}
		}catch(NullPointerException ex){
			System.out.println(" null ptr EXCEPTION NIGGA!");
		}catch(IndexOutOfBoundsException ex){
			System.out.println("Array EXCEPTION NIGGA!");
		}
	}
	
	public void killpointsdisp(int pointsearned, float x, float y){
		
		final Text earnpt = new Text(x, y,  activity.scorehudFont, "+ "+pointsearned+" pts", activity.getVertexBufferObjectManager());
		earnpt.setPosition(x,y);
		earnpt.setColor(GlobalVariables.dispcolors[activity.rand.nextInt(GlobalVariables.dispcolors.length)]);
		activity.mCurrentScene.attachChild(earnpt);
		earnpt.registerEntityModifier(  new MoveYModifier(3, y, 0){ 
			@Override
			protected void onModifierFinished(IEntity pItem) {
			  	
				earnpt.setVisible(false);
			}
		});
		
		
	}
	public Weapon getLast(){
		return enemies.get(enemies.size()-1);
	}
	

	public ArrayList<Weapon> getAllWeapons(){
		return this.enemies;
	}
	
	public void incvelocity(){
		//inc velocity
		minVelocity=minVelocity+20;
		//inc limit
		LIMIT=LIMIT+5;
	}

}
