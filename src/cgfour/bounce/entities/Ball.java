package cgfour.bounce.entities;

// def imports
import java.awt.Graphics;

// cgfour imports
import cgfour.bounce.entities.Entity;
import cgfour.bounce.game.Handler;
import cgfour.bounce.gfx.Asset;
import cgfour.bounce.tiles.Tile;

public class Ball extends Entity {
	
	boolean yflag;
	
	public Ball(Handler handler, float x, float y) {
		super(handler, x, y, 70, 70);
		
		bounds.width = 68;
		bounds.height = 70;
	}
	
	public void tick() {
		getInput();
		jump();
		move();
		handler.getFocus().centerOnBall(this);
	}
	
	private void getInput() {
		xMove = 0;
		yMove = 0;
	
		if (handler.getKeyManager().left)
			xMove = -10;
		if (handler.getKeyManager().right)
			xMove = 10;
	}
	
	public void jump() {
		if (handler.getKeyManager().jumping == false) {
			int ty = (int) (y + yMove+ bounds.height) / Tile.TILEHEIGHT;
				if (!collisionWithBox((int) (x) / Tile.TILEWIDTH, ty) &&
					!collisionWithBox((int) (x + bounds.width) / Tile.TILEWIDTH, ty) && !yflag) {
						y += 10;
				}
				else if ((collisionWithBox((int) (x) / Tile.TILEWIDTH, ty) ||
					collisionWithBox((int) (x + bounds.width) / Tile.TILEWIDTH, ty)) && !yflag) {
						if (y < 319) {
							y = ty * Tile.TILEHEIGHT-bounds.height -1;
						} else {
							y = ty * Tile.TILEHEIGHT-bounds.height -1;
							yflag = true;
						}
				}
		}
		
		if (handler.getKeyManager().jumping == true) {
			int ty = (int) (y + yMove) / Tile.TILEHEIGHT;
			
			if (!collisionWithBox((int) (x) / Tile.TILEWIDTH, ty) &&
					!collisionWithBox((int) (x + bounds.width) / Tile.TILEWIDTH, ty)) {
				System.out.println("HAHAHAHAHHAHAHA");
				y -= 15;
				yflag = false;
			}
		}
	}
	
	public void moveX() {
     	if (xMove > 0) { //Moving right
			int tx = (int) (x + xMove + bounds.x + bounds.width) / Tile.TILEWIDTH;
		
			if (!collisionWithBox(tx, (int) (y + bounds.y) / Tile.TILEHEIGHT) &&
					!collisionWithBox(tx, (int) (y + bounds.y + bounds.height) / Tile.TILEHEIGHT)) {
				x += xMove;
			}
		} else if (xMove < 0) { //Moving left
			int tx = (int) (x + xMove + bounds.x) / Tile.TILEWIDTH;
			
			if(!collisionWithBox(tx, (int) (y + bounds.y) / Tile.TILEHEIGHT) &&
					!collisionWithBox(tx, (int) (y + bounds.y + bounds.height) / Tile.TILEHEIGHT)) {
				x += xMove;
			}
		}
	}
	
	public void moveY() {
		if (yMove > 0) {
			int tempy = (int)(y + yMove + bounds.height) / 70;
			if (!collisionWithBox((int)(x + bounds.x) / Tile.TILEHEIGHT, tempy)
				&& !collisionWithBox((int)(x + bounds.x + bounds.width) / Tile.TILEWIDTH, tempy)) {
				y += yMove;
			}
		}
	}
	
	public void render(Graphics g) {
		g.drawImage(Asset.player, (int)(x - handler.getFocus().getxOffset()), 
								  (int)y, width, height, null);
		
		//g.setColor(Color.RED);
		//g.fillRect((int)(x-handler.getFocus().getxOffset()),
					//(int)(y),bounds.width,bounds.height);
	}
}