package cgfour.bounce.entry;

// def imports
import java.io.*;
import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.imageio.*;
import java.awt.event.*;
import javax.swing.border.*;

public class Login extends JFrame implements ActionListener {
    // ATTRIBUTES
    static JPanel logPanel;
    static Player user;
    public static Level level;
    JTextField username;
    JButton login, signUp, guest;
    JPasswordField password;
    JLabel logo, userLabel, passLabel, loginBackground;

    public Login() {
        super("Bounce - Blatant Rip Off");
        this.setSize(700, 480);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        try {
            this.setIconImage(ImageIO.read(new File("res/icon.png")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panelInit();
    }

    private void panelInit() {
        logPanel = new JPanel();
        logPanel.setLayout(null);

        // Game logo
        try {
            logo = new JLabel(new ImageIcon("res/Bounce.png"));
            logo.setBounds(155, 40, 395, 115);
            logPanel.add(logo);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Username & Password
        userLabel = new JLabel("Username:");
        userLabel.setBounds(270, 200, 150, 25);
        userLabel.setFont(new Font("Consolas", Font.BOLD, 20));
        logPanel.add(userLabel);
        username = new JTextField();
        username.setBounds(270, 225, 150, 25);
        username.setFont(new Font("Consolas", Font.BOLD, 15));
        logPanel.add(username);

        passLabel = new JLabel("Password:");
        passLabel.setBounds(270, 260, 150, 25);
        passLabel.setFont(new Font("Consolas", Font.BOLD, 20));
        logPanel.add(passLabel);
        password = new JPasswordField();
        password.setBounds(270, 285, 150, 25);
        logPanel.add(password);

        // Buttons
        login = new JButton("Log In");
        login.addActionListener(this);
        login.setBounds(270, 360, 150, 40);
        login.setBackground(Color.RED);
        login.setForeground(Color.WHITE);
        login.setBorder(new LineBorder(Color.BLACK,3));
        login.setFont(new Font("Berlin Sans FB", Font.BOLD, 25));
        logPanel.add(login);

        signUp = new JButton("Sign Up");
        signUp.addActionListener(this);
        signUp.setBounds(80, 360, 150, 40);
        signUp.setBackground(Color.RED);
        signUp.setForeground(Color.WHITE);
        signUp.setBorder(new LineBorder(Color.BLACK,3));
        signUp.setFont(new Font("Berlin Sans FB", Font.BOLD, 25));
        logPanel.add(signUp);
        
        guest = new JButton("Guest");
        guest.addActionListener(this);
        guest.setBounds(460, 360, 150, 40);
        guest.setBackground(Color.RED);
        guest.setForeground(Color.WHITE);
        guest.setBorder(new LineBorder(Color.BLACK,3));
        guest.setFont(new Font("Berlin Sans FB", Font.BOLD, 25));
        logPanel.add(guest);

        // Frame background
        try {
            //BufferedImage img = ImageIO.read( new File("res/back.png") );
            loginBackground = new JLabel( new ImageIcon("res/back.jpg") );
            loginBackground.setBounds(0, 0, 700, 480);
            logPanel.add(loginBackground);
        } catch(Exception e) {
            e.printStackTrace();
        }

        // Adding panel to frame
        this.add(logPanel);
    }

    public void actionPerformed(ActionEvent ae) {
        String elementText = ae.getActionCommand();

        if (elementText.equals(login.getText())) {
            if (username.getText().equals("") || password.getText().equals("") ) {
                JOptionPane.showMessageDialog(this, "Please enter info! ",
                        "Warning", JOptionPane.YES_NO_CANCEL_OPTION);
            } else {

                //*** DATABASE STUFF
                String query = "select * from user where username='"+username.getText()+"'";
                Connection con = null; // for connection
                Statement st = null; // for query execution
                ResultSet rs = null; // to get row by row result from DB

                try {
                    Class.forName("com.mysql.jdbc.Driver"); // loading driver
                    System.out.println("driver loaded");
                    con = DriverManager.getConnection("jdbc:mysql://localhost:3306/project","root","123456");
                    System.out.println("connection done");  // connection with database established
                    st = con.createStatement();  			// creates statement
                    System.out.println("statement created");
                    rs = st.executeQuery(query); 		    // getting result by executing query
                    System.out.println("results received");

                    if (rs.next()) {
                        String u = rs.getString("username"), p = rs.getString("password");
                        int level = rs.getInt("level");

                        if (p.equals(password.getText())) {
                            int utype = rs.getInt("utype"), approval =  rs.getInt("approval");
                            if(utype == 1) {
                                // Create object for the admin
                                System.out.println("ADMIN");
                                Admin admin = new Admin(this);
                                this.add(admin);
                                logPanel.setVisible(false);
                            }
                            else if (utype == 2 && approval == 1) {
                                // Create object for the player
                                System.out.println("PLAYER");
                                Player player = new Player(this, u, level);
                                this.add(player);
                                logPanel.setVisible(false);

                            } else {
                                JOptionPane.showMessageDialog(this,"Account not approved!",
                                        "Warning", JOptionPane.YES_NO_CANCEL_OPTION);
                            }

                        } else {
                            JOptionPane.showMessageDialog(this,"Invalid password!",
                                    "Warning", JOptionPane.YES_NO_CANCEL_OPTION);
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Username not found! Please sign up.",
                                "Warning", JOptionPane.YES_NO_CANCEL_OPTION);
                    }
                }
                catch (Exception ex) {
                    System.out.println("Exception : " +ex.getMessage());
                }
            }
        }
        else if (elementText.equals(signUp.getText())) {
            SignUp register = new SignUp(this);
            this.add(register);
            logPanel.setVisible(false);
        }
        else if (elementText.equals(guest.getText())) {
        	System.out.println("PLAYER");
            Player player = new Player(this, "guest", 1);
            this.add(player);
            logPanel.setVisible(false);
        }
    }
}