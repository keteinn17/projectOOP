package cgfour.bounce.tiles;

//def imports
import java.awt.*;
import java.awt.image.*;

public class Tile {
	
	public static Tile[] tiles = new Tile[256];
	public static Tile coinTile = new CoinTile(0);
	public static Tile planeTile = new PlaneTile(1);
	public static Tile backTile = new BackTile(2);
	public static Tile spikeTile = new SpikeTile(3);
	
	public static final int TILEWIDTH = 70, TILEHEIGHT = 70;
		
	protected BufferedImage texture;
	protected final int id;
	
	public Tile(BufferedImage texture, int id) {
		this.texture = texture;
		this.id = id;
		tiles[id] = this;
	}
	
	public void tick() {
		
	}
	
	public void render(Graphics g, int x, int y) {
		g.drawImage(texture, x, y, TILEWIDTH, TILEHEIGHT, null);
	}
	
	public int getId() {
		return id;
	}
	
	public boolean isSolid() {
		return false;
	}
	
	public boolean isCoin() {
		return false;
	}
}