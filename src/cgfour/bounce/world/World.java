package cgfour.bounce.world;

// def imports
import java.awt.*;

// cgfour imports
import cgfour.bounce.game.Handler;
import cgfour.bounce.tiles.Tile;
import cgfour.bounce.util.UtilShiz;
import cgfour.bounce.entities.Ball;
import cgfour.bounce.entities.Coin;
import cgfour.bounce.entities.EntityManager;
import cgfour.bounce.entities.Spike;

public class World {
	
	private Handler handler;
	private int width, height;
	private int spawnX, spawnY;
	private int[][] tiles;
	private boolean[][] check;
	
	// MANAGER OF ENTITIES
	private EntityManager entityManager;
	
	public World(Handler handler, String path) {
		this.handler = handler;
		entityManager = new EntityManager(handler, new Ball(handler, 100, 100)); 
		loadWorld(path);
		
		check = new boolean[200][200];
		
		entityManager.getBall().setX(spawnX);
		entityManager.getBall().setY(spawnY);
	}
	
	public void tick() {
		/// EMPTY ( :P )
		entityManager.tick();  
	}
	
	public void render(Graphics g) {
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (getTile(x, y).getId() == 0) {
					if(!check[x][y]) {
						//System.out.println(+x+" "+y);
						Coin coin = new Coin(handler, (int)(x*Tile.TILEWIDTH - handler.getFocus().getxOffset()), (int)(y*Tile.TILEHEIGHT), 70, 70);
						entityManager.addEntity(coin);
						check[x][y] = true;
					}
					else {
						getTile(x, y).render(g, (int)(x*Tile.TILEWIDTH - handler.getFocus().getxOffset()), (int)(y*Tile.TILEHEIGHT));
						continue;
					}
				}
				else if (getTile(x, y).getId() == 3) {
					if(!check[x][y]) {
						//System.out.println(+x+" "+y);
						Spike spikey = new Spike(handler, (int)(x*Tile.TILEWIDTH - handler.getFocus().getxOffset()), (int)(y*Tile.TILEHEIGHT), 70, 70);
						entityManager.addEntity(spikey);
						check[x][y] = true;
					}
					else {
						continue;
						//getTile(x, y).render(g, (int)(x*Tile.TILEWIDTH - handler.getFocus().getxOffset()), (int)(y*Tile.TILEHEIGHT));
					}
				}
				getTile(x, y).render(g, (int)(x*Tile.TILEWIDTH - handler.getFocus().getxOffset()), (int)(y*Tile.TILEHEIGHT));
			}
		}
		entityManager.render(g);
		
		g.setColor(Color.gray);
		g.setFont(new Font("Verdana",Font.BOLD,25));
		int xoxo = handler.getGame().getPlayer().getScore(handler.getGame().getLevelIdx());
		g.drawString(String.valueOf(xoxo), 350, 100);
	}
	
	public Tile getTile(int x, int y) {
		/// THIS CONDITION IS TO CHECK WHETHER THE BALL IS OUT OF BOUNDS OR NOT ... BUJJCHEN?? AR BUJHA LAGBE NA
		if (x < 0 || y < 0 || x >= width || y >= height) {
			return Tile.backTile;
		}
		
		Tile t = Tile.tiles[tiles[x][y]];
		if (t == null)
			return Tile.backTile;
		return t;
	}

	private void loadWorld(String path) {
		String file = UtilShiz.loadFileAsString(path);
		String[] tokens = file.split("\\s+");
		width  = UtilShiz.parseInt(tokens[0]);
		height = UtilShiz.parseInt(tokens[1]); 
		spawnX = UtilShiz.parseInt(tokens[2]);
		spawnY = UtilShiz.parseInt(tokens[3]);
		
		tiles = new int[width][height]; // This is height and width from the level file ( row x column )
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				tiles[x][y] = UtilShiz.parseInt(tokens[ (x + y * width) + 4 ]);
			}
		}
	} 
	
	 public int getWidth(){
		return width;
	}
	
	public int getHeight(){
		return height;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public Handler getHandler() {
		return handler;
	}
}