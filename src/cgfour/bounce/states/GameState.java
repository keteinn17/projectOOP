package cgfour.bounce.states;

// def imports
import java.awt.Graphics;

// cgfour imports
import cgfour.bounce.game.Handler;
import cgfour.bounce.world.World;

public class GameState extends State {
	
	private World world; 
	
	public GameState(Handler handler, int levIdx) {
		super(handler);
		if (levIdx == 1) 
			world = new World(handler, "map/Level1.txt");
		else if (levIdx == 2) 
			world = new World(handler, "map/Level2.txt");
		else if (levIdx == 3) 
			world = new World(handler, "map/Level3.txt");
		
		handler.setWorld(world);
	}
	
	public void tick() {
		world.tick();
	}
	
	public void render(Graphics g) {
		world.render(g);
	}
}