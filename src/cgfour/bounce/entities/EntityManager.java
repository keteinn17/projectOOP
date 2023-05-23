package cgfour.bounce.entities;

// cgfour imports
import cgfour.bounce.game.Handler;

// def imports
import java.util.ArrayList;
import java.awt.Graphics;

public class EntityManager {
	
	private Handler handler;
	private Ball ball;
	private ArrayList<Entity> entities;
	
	public EntityManager(Handler handler, Ball ball) {
		this.handler = handler;
		this.ball = ball;
		entities = new ArrayList<Entity>();
		addEntity(ball);
	}
	
	public void tick() {
		for(int i = 0;i < entities.size();i++) {
			Entity e = entities.get(i);
			e.tick();
		}
	}
	
	public void render(Graphics g) {
		for(Entity e : entities){
			e.render(g);
		}
	}
	
	public void addEntity(Entity e) {
		entities.add(e);
	}
	
	//GETTERS SETTERS

	public Handler getHandler() {
		return handler;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	public Ball getBall() {
		return ball;
	}

	public void setBall(Ball ball) {
		this.ball = ball;
	}

	public ArrayList<Entity> getEntities() {
		return entities;
	}

	public void setEntities(ArrayList<Entity> entities) {
		this.entities = entities;
	}
}