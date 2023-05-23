package cgfour.bounce.entities;

// def imports
import java.awt.*;

// cgfour imports
import cgfour.bounce.game.Handler;

public abstract class Entity {
	
	protected Handler handler;
	protected float x, y; // float because for smooth gaming
	protected float xMove,yMove;
	protected int width, height;
	protected Rectangle bounds;
	
	// To check if entity taken by player or not
	protected boolean picked = false;

	public Entity(Handler handler, float x, float y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.handler = handler;
		
		bounds = new Rectangle(0, 0, width, height);
	}
	
	public void move() {
		if (!checkEntityCollisions(xMove, 0f)) 
			moveX();
		if (!checkEntityCollisions(0f, yMove)) 
			moveY();
	}
	
	public abstract void moveX();
	
	public abstract void moveY();
	 
	protected boolean collisionWithBox(int x,int y) {
		return handler.getWorld().getTile(x,y).isSolid();
	}
	
	/// SETTER AND GETTERS
	
	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	public boolean isPicked() {
		return picked;
	}
	
	public void setPicked(boolean x) {
		this.picked = true;
	}
	
	public boolean checkEntityCollisions(float xOffset, float yOffset){
		for (Entity e : handler.getWorld().getEntityManager().getEntities()) {
			if (e.equals(this)) continue; // We do this so that the particular entity does not check itself
			if (e.getCollisionBounds(0f, 0f).intersects(getCollisionBounds(xOffset, yOffset))) {
				e.setPicked(true);
				return false;
			}	
		}
		return false;
	}
	
	public Rectangle getCollisionBounds(float xOffset, float yOffset){
		return new Rectangle((int) (x + bounds.x + xOffset), (int) (y + bounds.y + yOffset), bounds.width, bounds.height);
	}
	
	public abstract void tick(); // Where the entity move itself
	public abstract void render(Graphics g); // Where the entity will draw itself
}