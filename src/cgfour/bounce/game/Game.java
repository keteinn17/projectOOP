package cgfour.bounce.game;

// def imports
import java.awt.*;
import javax.swing.*;
import java.awt.image.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

// cgfour imports
import cgfour.bounce.entry.Login;
import cgfour.bounce.entry.Player;
import cgfour.bounce.gfx.Asset;
import cgfour.bounce.gfx.Focus;
import cgfour.bounce.input.KeyManager;
import cgfour.bounce.states.GameState;
import cgfour.bounce.states.State;

public class Game implements Runnable {

	private Graphics g;
	private Thread thread;
	private Canvas canvas;
	private JFrame frame;
	private Player player;
	private BufferStrategy bs;
	private int width, height, levIdx;
	private boolean running = false, death = false, win = false;
	
	// State
	private State gameState;

	// Input - Keyboard
	private KeyManager keyManager;
	
	// Camera
	private Focus focus;
	
	//Handler
	private Handler handler;
	
	public Game(JFrame frame, Player player, int levIdx) {
		this.width = 700;
		this.height = 480;
		this.frame = frame;
		this.player = player;
		this.levIdx = levIdx;
		keyManager = new KeyManager();
	}
	
	private void init() {
		canvas = new Canvas();
		canvas.setPreferredSize(new Dimension(700, 480));
		canvas.setMaximumSize(new Dimension(700, 480));
		canvas.setMinimumSize(new Dimension(700, 480));
		canvas.setFocusable(false);
		frame.add(canvas);
		frame.pack();

		// Adding key listener to frame and initializing my sprites through the Asset class
		frame.addKeyListener(keyManager);
		frame.requestFocusInWindow();   /// IF YOU DONT DO THIS YOU WONT BE ABLE TO USE KEY LISTENER 
		Asset.init(levIdx);
		
		// Focus object
		handler = new Handler(this);
		focus = new Focus(handler, 0); // making the default offset 0
		
		gameState = new GameState(handler, levIdx);
		State.setState(gameState);
	}
	
	private void tick() {
		keyManager.tick();
		if (State.getState() != null) 
			State.getState().tick();
	}
	
	private void render() {
		bs = canvas.getBufferStrategy();
		if (bs == null) {
			canvas.createBufferStrategy(3);
			return;
		}
		
		g = bs.getDrawGraphics();  // PaintBrush that draws stuff on the canvas
		g.clearRect(0, 0, 700, 480); // clears screen
		// Start Drawing

		if (State.getState() != null) 
			State.getState().render(g);
		 
		// End Drawing
		bs.show();
		g.dispose();
	}
	
	public void run() {
		init();
		
		int fps = 70; // This means the amount of time the tick and render method will be called which is 60 times every second
		double timePerTick = 1000000000 / fps; // Maximum amount of time we need to execute tick and render methods 10^9 nanosec = 1sec
		double delta = 0;
		long now;
		long lastTime = System.nanoTime(); // Returns the current time in nanosec
		long timer = 0;
		int ticks = 0;
		
		while (running) {
			now = System.nanoTime();
			delta += (now - lastTime) / timePerTick;
			timer += now - lastTime;
			lastTime = now;
			
			if (delta >= 1) {
				tick();
				render();
				ticks++;
				delta--;
			}
			
			if (timer >= 1000000000) {
				//System.out.println("Ticks and Frames: " + ticks);
				ticks = 0;
				timer = 0;
			}
		}
		stop();
		frame.remove(canvas);
		frame.setSize(700, 480);
		Login.level.setUnlocked(this.levIdx);
		Login.level.refreshLevel();
		Login.level.setVisible(true);
		
	}
	
	public KeyManager getKeyManager() {
		return keyManager;
	}

	public Focus getFocus() {
		return focus;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void setRunning(boolean x) {
		running = x;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public void setDeath(boolean x) {
		this.death = x;
	}
	
	public void setWin(boolean x) {
		this.win = x;
	}
	
	public int getLevelIdx() {
		return levIdx;
	}
	
	public void setLevelIdx() {
		this.levIdx++;
	}
	
	public synchronized void start() {
		if (running) return;
		else {
			running = true;
			thread = new Thread(this);
			thread.start();
		}
	}
	
	public synchronized void stop() {
		if(!running) {
			if (death) {
				JOptionPane.showMessageDialog(frame, "YOU DEAD BRO? YOUR SCORE: "+handler.getGame().getPlayer().getScore(levIdx),
						"xDEATHx", JOptionPane.YES_NO_CANCEL_OPTION);
				if (player.getName() == "guest") return;
				// Updating database for highscore
				try {
	                String query = "select * from leaderboard where username='"+player.getName()+"'";
	                Class.forName("com.mysql.jdbc.Driver");         // loading driver
	                System.out.println("driver loaded");
	                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/project","root","");
	                System.out.println("connection done");          // connection with database established
	                Statement st = con.createStatement();  			// creates statement
	                System.out.println("statement created");
	                ResultSet rs  =  st.executeQuery(query); 		// getting result by executing query
	                System.out.println("results received");

	                if (rs.next()) {
	                	int pt = player.getScore(1) + player.getScore(2) + player.getScore(3);
	                	if (rs.getInt("score") < pt) {
	                		query = "UPDATE `leaderboard` SET `score` = '"+pt+"' WHERE `username` = '"+player.getName()+"'";
	                        st.execute(query);
	                	}
	                	st.close();
	                    con.close();
	                } else 
	                	System.out.println("NOT FOUND!");
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
			}  
			else if (win) {
				JOptionPane.showMessageDialog(frame, "LEVEL COMPLETE! YOUR SCORE: "+handler.getGame().getPlayer().getScore(levIdx),
						"xDEATHx", JOptionPane.YES_NO_CANCEL_OPTION);
				
				if (player.getName() == "guest") {
					if (this.levIdx < 3) {
						setLevelIdx();
					}
					return;
				}
				
				// Updating database for highscore && level 
				try {
	                String query = "select * from leaderboard where username='"+player.getName()+"'";
	                Class.forName("com.mysql.jdbc.Driver");         // loading driver
	                System.out.println("driver loaded");
	                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/project","root","");
	                System.out.println("connection done");          // connection with database established
	                Statement st = con.createStatement();  			// creates statement
	                System.out.println("statement created");
	                ResultSet rs  =  st.executeQuery(query); 		// getting result by executing query
	                System.out.println("results received");

	                if (rs.next()) {
	                	int pt = player.getScore(1) + player.getScore(2) + player.getScore(3);
	                	if (rs.getInt("score") < pt) {
	                		query = "UPDATE `leaderboard` SET `score` = '"+pt+"' WHERE `username` = '"+player.getName()+"'";
	                        st.execute(query);
	                	}
	                	
	                	query = "select * from user where username='"+player.getName()+"'";
	                	if (getLevelIdx() == 2) {
	                		query = "UPDATE `user` SET `level` = '3' WHERE `username` = '"+player.getName()+"'";
	                		st.execute(query);
	                		setLevelIdx();
	                	}
	                	else if (getLevelIdx() == 1) {
	                		query = "UPDATE `user` SET `level` = '2' WHERE `username` = '"+player.getName()+"'";
	                		st.execute(query);
	                		setLevelIdx();
	                	}
	                	
	                	st.close();
	                    con.close();
	                } else 
	                	System.out.println("NOT FOUND!");
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
			}
			return;
		}
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}