package cgfour.bounce.entry;

// def imports
import java.awt.*;
import java.sql.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.border.*;

public class SignUp extends JPanel implements ActionListener {
    // ATTRIBUTES
    Login log;
    JTextField username;
    JButton cancel, confirm;
    JPasswordField password, password2;
    JLabel logo, userLabel, passLabel, passLabel2, background;

    public SignUp(Login log) {
        this.setLayout(null);
        this.log = log;
        panelInit();
    }

    private void panelInit() {
        // Game logo
        logo = new JLabel(new ImageIcon("JProject/res/Bounce.png"));
        logo.setBounds(155, 40, 395, 115);
        this.add(logo);

        // Username & Password
        userLabel = new JLabel("Username:");
        userLabel.setBounds(270, 160, 150, 25);
        userLabel.setFont(new Font("Consolas", Font.BOLD, 18));
        this.add(userLabel);
        username = new JTextField();
        username.setBounds(270, 185, 150, 25);
        username.setFont(new Font("Consolas", Font.BOLD, 15));
        this.add(username);

        passLabel = new JLabel("Password:");
        passLabel.setBounds(270, 220, 150, 25);
        passLabel.setFont(new Font("Consolas", Font.BOLD, 18));
        this.add(passLabel);
        password = new JPasswordField();
        password.setBounds(270, 245, 150, 25);
        this.add(password);

        passLabel2 = new JLabel("Re-Enter:");
        passLabel2.setBounds(270, 280, 200, 25);
        passLabel2.setFont(new Font("Consolas", Font.BOLD, 18));
        this.add(passLabel2);
        password2 = new JPasswordField();
        password2.setBounds(270, 305, 150, 25);
        this.add(password2);

        // Buttons
        confirm=new JButton("Confirm");
        confirm.addActionListener(this);
        confirm.setBounds(180, 360, 150, 40);
        confirm.setBackground(Color.RED);
        confirm.setForeground(Color.WHITE);
        confirm.setBorder(new LineBorder(Color.BLACK, 3));
        confirm.setFont(new Font("Berlin Sans FB", Font.BOLD, 25));
        this.add(confirm);

        cancel=new JButton("Cancel");
        cancel.setBounds(380, 360, 150, 40);
        cancel.setBackground(Color.RED);
        cancel.setForeground(Color.WHITE);
        cancel.setBorder(new LineBorder(Color.BLACK, 3));
        cancel.setFont(new Font("Berlin Sans FB", Font.BOLD, 25));
        cancel.addActionListener(this);
        this.add(cancel);

        // Frame background
        try {
            background = new JLabel( new ImageIcon("res/back.jpg") );
            background.setBounds(0, 0, 700, 500);
            this.add(background);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent ae)  {
        String elementText = ae.getActionCommand();

        if (elementText.equals(cancel.getText())) {
            log.logPanel.setVisible(true);
            log.remove(this);
        }
        else if (elementText.equals(confirm.getText())) {
            if (username.getText().equals("") || password.getText().equals("") || password2.getText().equals("")) {
                JOptionPane.showMessageDialog(this, "Some fields are empty!",
                        "Warning", JOptionPane.YES_NO_CANCEL_OPTION);
            }
            else if (password.getText().equals(password2.getText())) {
                /** DATABASE STUFF */
            	String query = "INSERT INTO `user` VALUES ('"+username.getText()+"', '"+password.getText()+"', 2, 0, 1)";
            	Connection con=null; // for connection
                Statement st = null; // for query execution
                ResultSet rs = null; // to get row by row result from DB

                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    con = DriverManager.getConnection("jdbc:mysql://localhost:3306/project", "root", "123456");
                    st = con.createStatement();
                    st.execute(query);
                    query = "INSERT INTO `leaderboard` VALUES ('"+username.getText()+"', 0)";
                    st.execute(query);
                    st.close();
                    con.close();
                    JOptionPane.showMessageDialog(this, "User Added!",
                            "Successful", JOptionPane.YES_NO_CANCEL_OPTION);
                    log.username.setText("");
                    log.password.setText("");
                    log.logPanel.setVisible(true);
                    log.remove(this);

                }
                catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Username already exists! ",
                            "Warning", JOptionPane.YES_NO_CANCEL_OPTION);
                    ex.printStackTrace();
                }
            }
            else {
                JOptionPane.showMessageDialog(this, "Password Mismatch! ",
                        "Warning", JOptionPane.YES_NO_CANCEL_OPTION);
            }
        }
    }
}