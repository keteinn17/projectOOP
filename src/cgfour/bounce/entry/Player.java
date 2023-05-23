package cgfour.bounce.entry;

// def imports
import java.sql.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.border.*;

// cgfour imports
import cgfour.bounce.game.Game;

public class Player extends JPanel implements ActionListener {
	// ATTRIBUTES
	String name;
	int score1, score2, score3, unlocked;

	JFrame frame;
	JLabel logo, background;
	JButton play, highScore, signOut;

	public Player(JFrame frame, String name, int level) {
		this.setLayout(null);
		this.name = name;
		System.out.println(this.name);
		this.unlocked = level;
		this.frame = frame;
		Login.user = this;
		panelInit();
	}

	void panelInit() {
		// Game logo
		logo = new JLabel(new ImageIcon("res/Bounce.png"));
		logo.setBounds(155, 40, 395, 115);
		this.add(logo);

		// Buttons
		play = new JButton("Play");
		play.addActionListener(this);
		play.setBounds(270, 200, 150, 40);
		play.setBackground(Color.RED);
		play.setForeground(Color.WHITE);
		play.setBorder(new LineBorder(Color.BLACK, 3));
		play.setFont(new Font("Berlin Sans FB", Font.BOLD, 25));
		this.add(play);

		highScore = new JButton("High Score");
		highScore.addActionListener(this);
		highScore.setBounds(270, 270, 150, 40);
		highScore.setBackground(Color.RED);
		highScore.setForeground(Color.WHITE);
		highScore.setBorder(new LineBorder(Color.BLACK,3));
		highScore.setFont(new Font("Berlin Sans FB", Font.BOLD, 25));
		this.add(highScore);

		signOut = new JButton("Log Out");
	    signOut.addActionListener(this);
	    signOut.setBounds(270, 340, 150, 40);
        signOut.setBackground(Color.RED);
        signOut.setForeground(Color.WHITE);
        signOut.setBorder(new LineBorder(Color.BLACK,3));
        signOut.setFont(new Font("Berlin Sans FB", Font.BOLD, 25));
        this.add(signOut);

		// Frame background
		try {
			background = new JLabel( new ImageIcon("res/back.png") );
			background.setBounds(0, 0, 700, 500);
			this.add(background);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void actionPerformed(ActionEvent ae)  {
		String elementText = ae.getActionCommand();

		if (elementText.equals(play.getText())) {
			Level level = new Level(frame, unlocked);
			frame.add(level);
			Login.user.setVisible(false);
		}
		else if (elementText.equals(signOut.getText())) {	
			Login.logPanel.setVisible(true);
			Login.user.setVisible(false);
		}
		else if (elementText.equals(highScore.getText())) {
            // Highscore Panel is added here
			Highscore hg = new Highscore();
			frame.add(hg);
			Login.user.setVisible(false);
		}
	}

	public void incScore(int levIdx) {
		if (levIdx == 1) {
			score1+=10;
			System.out.println("Score: " +score1);
		}
		else if (levIdx == 2) {
			score2+=10;
			System.out.println("Score: " +score2);
		}
		else {
			score3+=10;
			System.out.println("Score: " +score3);
		}
	}

	public int getScore(int levIdx) {
		if (levIdx == 1) {
			return score1;
		}
		else if (levIdx == 2) {
			return score2;
		}
		else {
			return score3;
		}
	}

	public void setScore(int levIdx) {
		if (levIdx == 1) score1 = 0;
		else if (levIdx == 2) score2 = 0;
		else score3 = 0;
	}
	
	public String getName() {
		return name;
	}
}
