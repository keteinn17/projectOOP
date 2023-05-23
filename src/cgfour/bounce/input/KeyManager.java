package cgfour.bounce.input;

import java.awt.event.*; 

public class KeyManager implements KeyListener {
	
	private boolean[] keys;
	public long jumpingTime = 300;
	public boolean up, down, left, right, jumping;
	
	public KeyManager(){
		keys = new boolean[256];
	}
	
	public void tick() {
		left = keys[KeyEvent.VK_LEFT];
		right = keys[KeyEvent.VK_RIGHT];
/*		up = keys[KeyEvent.VK_UP];
		down = keys[KeyEvent.VK_DOWN];*/
	}

	public void keyPressed(KeyEvent e) {
		keys[e.getKeyCode()] = true;
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			jumping = true;
			new Thread(new BabyThread()).start();
		} 
	}

	public void keyReleased(KeyEvent e) {
		keys[e.getKeyCode()] = false;
	}
	
	public void keyTyped(KeyEvent e) {
		
	}
	
	public class BabyThread implements Runnable {
		
		public void run() {
			try {
				Thread.sleep(jumpingTime);
				jumping = false;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}