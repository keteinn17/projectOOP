package cgfour.bounce.entry;

//def imports
import java.awt.*;
import java.sql.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.border.*;

public class Highscore extends JPanel implements ActionListener  {
	// ATTRIBUTES
    JTable table;
    JButton back;
    JLabel logo, background;
    JScrollPane scrollPane;
    
    public Highscore() {
    	this.setLayout(null);
    	panelInit();
    }
    
    void panelInit() {
    	// Game logo
    	logo = new JLabel(new ImageIcon("res/Bounce.png"));
   		logo.setBounds(155, 40, 395, 115);
  		this.add(logo);
  		
  		// Highscore Table
  		tableInit();
  		
  		// Back button
  		back =  new JButton("Back");
        back.addActionListener(this);
        back.setBounds(280, 350, 150, 40);
        back.setBackground(Color.RED);
        back.setForeground(Color.WHITE);
        back.setBorder(new LineBorder(Color.BLACK,3));
        back.setFont(new Font("Berlin Sans FB", Font.BOLD, 25));
        this.add(back);
  		
  		// Background
        try {
            background  =  new JLabel( new ImageIcon("res/back.png") );
            background.setBounds(0, 0, 700, 500);
            this.add(background);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    void tableInit() {
    	String query = "SELECT `username`, `score` FROM `leaderboard` ORDER BY `score` DESC;";
        Connection con = null; //for connection
        Statement st = null;   //for query execution
        ResultSet rs = null;   //to get row by row result from DB
        String [][]row = new String[100][2];
        try {
            Class.forName("com.mysql.jdbc.Driver"); //load driver
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/project", "root", "");
            st = con.createStatement();  //create statement
            rs = st.executeQuery(query); //getting result

            int i = 0;
            while(rs.next()) {
                row[i][0] = rs.getString("userName");
                row[i][1] = rs.getString("score");
                i++;
            }
            st.close();
            con.close();
        } catch(Exception ex) {
            System.out.println("Exception : " +ex.getMessage());
        }

        String []col = {"USERNAME", "SCORE"};

        table = new JTable(row, col);
        table.setEnabled(false);
        table.setFont(new Font("Consolas", Font.BOLD, 16));
        scrollPane = new JScrollPane(table);
        scrollPane.setBounds(155, 185, 395, 115);
        this.add(scrollPane);
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		String elementText = e.getActionCommand();
		
		if (elementText.equals(back.getText())) {
			this.setVisible(false);
			Login.user.setVisible(true);
		}
	}
}
