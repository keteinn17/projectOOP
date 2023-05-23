package cgfour.bounce.gfx;

// def imports
import java.io.File;
import java.awt.image.*;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Asset {
	
	public static BufferedImage player, plane, plane2, back, coin, coin2, spike, spike2;
	
	public static void init(int levIdx) {
		try {
			BufferedImage img = ImageIO.read( new File("res/sprite.png") );
			SpriteSheet sheet = new SpriteSheet(img);
			
			if (levIdx == 1) 
				player = sheet.crop(246, 1811, 70, 70);
			else if (levIdx == 2) 
				player = sheet.crop(219, 1969, 70, 70);
			else
				player = sheet.crop(0, 1969, 70, 70);
			
			plane  = sheet.crop(1802, 497, 70, 70);
			coin2  = ImageIO.read( new File("res/coin.png") );
			coin   = ImageIO.read( new File("res/sky.png") );
			back   = ImageIO.read( new File("res/sky.png") );
			spike2 = ImageIO.read( new File("res/spike up.png") );
			spike  = ImageIO.read( new File("res/sky.png") );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}