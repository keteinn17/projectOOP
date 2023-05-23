package cgfour.bounce.gfx;

// cgfour imports
import cgfour.bounce.game.Handler;
import cgfour.bounce.tiles.Tile;

import javax.swing.JOptionPane;

import cgfour.bounce.entities.Entity;

public class Focus {
	private float xoffset;
	protected Handler handler;
	
	public Focus(Handler handler, float xoffset) {
		this.handler = handler;
		this.xoffset = xoffset;
	}
	
	public void checkBlankSpace(){
		if (xoffset < 0) {
			xoffset = 0;
		}
		else if (xoffset>handler.getWorld().getWidth()*Tile.TILEWIDTH-handler.getWidth()) {
			xoffset=handler.getWorld().getWidth()*Tile.TILEWIDTH-handler.getWidth();
		}
	}
	
	public void win(Entity e) {
		if(e.getX()>((handler.getWorld().getWidth()*70)-60)) {
			handler.getGame().setRunning(false);
			handler.getGame().setWin(true);
		}
	}
	
	public void move(float xAmnt) {
		xoffset += xAmnt;
		checkBlankSpace();
	}
	
	public void centerOnBall(Entity e) {
		xoffset = ((e.getX() - handler.getWidth() / 2) + 250);
		checkBlankSpace();
		win(e);
	}
	
	public float getxOffset() {
		return xoffset;
	}
	
	public void setxOffset(float xoffset) {
		this.xoffset = xoffset;
	}
}