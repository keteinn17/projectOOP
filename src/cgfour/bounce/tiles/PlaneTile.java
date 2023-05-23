package cgfour.bounce.tiles;

import cgfour.bounce.gfx.Asset;

public class PlaneTile extends Tile {
	
	public PlaneTile(int id) {
		super(Asset.plane, id);
	}
	
	public boolean isSolid() {
		return true;
	}
}