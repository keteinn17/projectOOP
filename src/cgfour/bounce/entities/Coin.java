package cgfour.bounce.entities;

// def imports
import java.awt.Graphics;

// cgfour imports
import cgfour.bounce.game.Handler;
import cgfour.bounce.gfx.Asset;

public class Coin extends Entity {
	
	private boolean pointNoted = false;
	
	public Coin(Handler handler, float x, float y, int width, int height) {
		super(handler, x, y, width, height);
		//bounds.x = 10;
		//bounds.y = (int) (height / 1.5f);
		bounds.width = 68;
		bounds.height = 70;
	}
	
	public void moveX() {
		
	}
	
	public void moveY() {
		
	}
	
	public void tick() {
		
	}
	
	public boolean isPoint() {
		return true;
	}
	
	public void setPicked(boolean x) {
		this.picked = true;
		if (!pointNoted) {
			handler.getGame().getPlayer().incScore(handler.getGame().getLevelIdx());
			pointNoted = true;
		}
	}
	
	public void render(Graphics g) {
		if (isPicked()) {
			//g.drawImage(Asset.back, (int)(x - handler.getFocus().getxOffset()), 
								  //(int)y, width, height, null);
		}
		else {
			g.drawImage(Asset.coin2, (int)(x - handler.getFocus().getxOffset()), 
								  (int)y, width, height, null);
								  
			//g.setColor(Color.RED);
			//g.fillOval((int)(x-handler.getFocus().getxOffset()), (int)(y), 70, 70);
			//g.fillRect((int)(x-handler.getFocus().getxOffset()),
					//(int)(y),bounds.width,bounds.height);
		}
	}
}