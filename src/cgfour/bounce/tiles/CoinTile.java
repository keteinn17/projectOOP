package cgfour.bounce.tiles;

import cgfour.bounce.gfx.Asset;

public class CoinTile extends Tile {
	
	public CoinTile(int id) {
		super(Asset.coin, id);
	}
	
	public boolean isCoin() {
		return true;
	}
}