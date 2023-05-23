package cgfour.bounce.entry;

//def imports
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.border.*;

//cgfour imports
import cgfour.bounce.game.Game;

public class Level extends JPanel implements ActionListener {
	
	JFrame frame;
	int unlocked;
	JButton one, two, three, back;
	JLabel logo, background;
	
	public Level(JFrame frame, int unlocked) {
		this.setLayout(null);
		this.frame = frame;
		this.unlocked = unlocked;
		Login.level = this;
		panelGo();
	}
	
	public void setUnlocked(int x) {
		this.unlocked = x;
	}
	
	public void refreshLevel() {
		for (int i = 0; i < unlocked; ++i) {
			if (i == 0) one.setEnabled(true);
			else if (i == 1) two.setEnabled(true);
			else three.setEnabled(true);
		}
	}
	
	public void panelGo() {
		logo = new JLabel(new ImageIcon("JProject/res/Bounce.png"));
		logo.setBounds(155, 40, 395, 115);
		this.add(logo);

		// Buttons
		one = new JButton("1");
		one.addActionListener(this);
		one.setBounds(150, 200, 100, 40);
		one.setBackground(Color.RED);
		one.setForeground(Color.WHITE);
		one.setBorder(new LineBorder(Color.BLACK, 3));
		one.setFont(new Font("Berlin Sans FB", Font.BOLD, 25));
		one.setEnabled(false);
		this.setEnabled(false);
		this.add(one);
		
		two = new JButton("2");
		two.addActionListener(this);
		two.setBounds(300, 200, 100, 40);
		two.setBackground(Color.RED);
		two.setForeground(Color.WHITE);
		two.setBorder(new LineBorder(Color.BLACK, 3));
		two.setFont(new Font("Berlin Sans FB", Font.BOLD, 25));
		two.setEnabled(false);
		this.add(two);
		
		three = new JButton("3");
		three.addActionListener(this);
		three.setBounds(450, 200, 100, 40);
		three.setBackground(Color.RED);
		three.setForeground(Color.WHITE);
		three.setBorder(new LineBorder(Color.BLACK, 3));
		three.setFont(new Font("Berlin Sans FB", Font.BOLD, 25));
		three.setEnabled(false);
		this.add(three);
		
		back = new JButton("Back");
		back.addActionListener(this);
		back.setBounds(270, 300, 150, 40);
		back.setBackground(Color.RED);
		back.setForeground(Color.WHITE);
		back.setBorder(new LineBorder(Color.BLACK, 3));
		back.setFont(new Font("Berlin Sans FB", Font.BOLD, 25));
		this.add(back);
		
		for (int i = 0; i < unlocked; ++i) {
			if (i == 0) one.setEnabled(true);
			else if (i == 1) two.setEnabled(true);
			else three.setEnabled(true);
		}
		
		// Frame background
		try {
			background = new JLabel( new ImageIcon("JProject/res/back.png") );
			background.setBounds(0, 0, 700, 500);
			this.add(background);	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		String elementText = e.getActionCommand();
		
		if (elementText.equals(one.getText())) {
			Game game = new Game(frame, Login.user, 1);
			Login.user.setScore(1);
			Login.level.setVisible(false);
			game.start();
		}
		else if (elementText.equals(two.getText())) {
			Game game = new Game(frame, Login.user, 2);
			Login.user.setScore(2);
			Login.level.setVisible(false);
			game.start();  
		}
		else if (elementText.equals(three.getText())) {
			Game game = new Game(frame, Login.user, 3);
			Login.user.setScore(3);
			Login.level.setVisible(false);
			game.start(); 
		}
		else if (elementText.equals(back.getText())) {
			Login.level.setVisible(false);
			Login.user.setVisible(true);
		}
	}
}
