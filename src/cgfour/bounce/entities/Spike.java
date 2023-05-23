package cgfour.bounce.entities;

// def imports
import java.awt.Graphics;

import javax.swing.JOptionPane;

// cgfour imports 
import cgfour.bounce.game.Handler;
import cgfour.bounce.gfx.Asset;

public class Spike extends Entity {
	
	public Spike(Handler handler, float x, float y, int width, int height) {
		super(handler, x, y, width, height);
		//bounds.x = 10;
		//bounds.y = (int) (height / 1.5f);
		bounds.width = 70;
		bounds.height = 70;
	}
	
	public void moveX() {
		
	}
	
	public void moveY() {
		
	}
	
	public void tick() {
		
	}
	
	public void render(Graphics g) {
		if (isPicked()) {
			handler.getGame().setRunning(false);
			handler.getGame().setDeath(true);
		}
		else {
			g.drawImage(Asset.spike2, (int)(x - handler.getFocus().getxOffset()), 
								  (int)y, width, height, null);
		}
	}
}