package cgfour.bounce.entry;

// def imports
import java.awt.*;
import java.sql.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.border.*;

public class Admin extends JPanel implements ActionListener {
    // ATTRIBUTES
    Login login;
    JTable table;
    JLabel logo, background;
    JTextField searchBar;
    JScrollPane scrollPane;

    private JButton delete, signOut, approve, refresh;

    public Admin(Login login) {
        this.setLayout(null);
        this.login = login;
        panelInit();
    }

    private void panelInit() {
        // Buttons
        signOut = new JButton("Log Out");
        signOut.addActionListener(this);
        signOut.setBounds(460, 350, 150, 40);
        signOut.setBackground(Color.RED);
        signOut.setForeground(Color.WHITE);
        signOut.setBorder(new LineBorder(Color.BLACK,3));
        signOut.setFont(new Font("Berlin Sans FB", Font.BOLD, 25));
        this.add(signOut);

        searchBar = new JTextField("");
        searchBar.setBounds(230,180,250,50);
        searchBar.setBackground(Color.RED);
        searchBar.setForeground(Color.WHITE);
        searchBar.setFont(new Font("Consolas", Font.BOLD, 25));
        this.add(searchBar);

        approve  =  new JButton("Approve");
        approve.addActionListener(this);
        approve.setBounds(280, 250, 150, 40);
        approve.setBackground(Color.RED);
        approve.setForeground(Color.WHITE);
        approve.setBorder(new LineBorder(Color.BLACK,3));
        approve.setFont(new Font("Berlin Sans FB", Font.BOLD, 25));
        this.add(approve);

        showUsers();

        refresh  =  new JButton("Refresh");
        refresh.addActionListener(this);
        refresh.setBounds(280, 350, 150, 40);
        refresh.setBackground(Color.RED);
        refresh.setForeground(Color.WHITE);
        refresh.setBorder(new LineBorder(Color.BLACK,3));
        refresh.setFont(new Font("Berlin Sans FB", Font.BOLD, 25));
        this.add(refresh);

        delete  =  new JButton("Delete");
        delete.addActionListener(this);
        delete.setBounds(100, 350, 150, 40);
        delete.setBackground(Color.RED);
        delete.setForeground(Color.WHITE);
        delete.setBorder(new LineBorder(Color.BLACK,3));
        delete.setFont(new Font("Berlin Sans FB", Font.BOLD, 25));
        this.add(delete);

        // login background
        try {
            background  =  new JLabel( new ImageIcon("JProject/res/back.png") );
            background.setBounds(0, 0, 700, 500);
            this.add(background);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showUsers() {
        String query = "SELECT `username`, `password`, `approval` FROM `user`;";
        Connection con = null; //for connection
        Statement st = null;   //for query execution
        ResultSet rs = null;   //to get row by row result from DB
        String [][]row = new String[100][3];
        try {
            Class.forName("com.mysql.jdbc.Driver"); //load driver
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/project", "root", "123456");
            st = con.createStatement();  //create statement
            rs = st.executeQuery(query); //getting result

            int i = 0;
            while(rs.next()) {
                row[i][0] = rs.getString("username");
                row[i][1] = rs.getString("password");
                row[i][2] = rs.getString("approval");
                i++;
            }
            st.close();
            con.close();
        } catch(Exception ex) {
            System.out.println("Exception : " +ex.getMessage());
        }

        String []col = {"USERNAME", "PASSWORD", "VALID"};

        table = new JTable(row, col);
        table.setEnabled(false);
       // table.setBackground(Color.RED);
       // table.setForeground(Color.WHITE);
        table.setFont(new Font("Consolas", Font.BOLD, 16));
        scrollPane = new JScrollPane(table);
        scrollPane.setBounds(155, 40, 395, 130);
        this.add(scrollPane);
    }

    public void actionPerformed(ActionEvent ae)  {
        String elementText = ae.getActionCommand();

        if (elementText.equals(signOut.getText())) {
            login.username.setText("");
            login.password.setText("");
            login.logPanel.setVisible(true);
            login.remove(this);
        }
        else if (elementText.equals(approve.getText())) {

            if ((searchBar.getText().equals(""))) {
                JOptionPane.showMessageDialog(this, "No User Selected",
                        "Warning", JOptionPane.YES_NO_CANCEL_OPTION);
            }
            else {
                if (searchBar.getText().equals("admin")) {
                    JOptionPane.showMessageDialog(this, "YOU MAD BRO???",
                            "Warning", JOptionPane.YES_NO_CANCEL_OPTION);
                    return;
                }
                try {
                    String query = "select * from user where username='"+searchBar.getText()+"'";
                    Class.forName("com.mysql.jdbc.Driver");         // loading driver
                    System.out.println("driver loaded");
                    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/project","root","123456");
                    System.out.println("connection done");          // connection with database established
                    Statement st = con.createStatement();  			// creates statement
                    System.out.println("statement created");
                    ResultSet rs  =  st.executeQuery(query); 		// getting result by executing query
                    System.out.println("results received");

                    if (rs.next()) {
                        System.out.println(searchBar.getText());
                        query = "UPDATE `user` SET `approval` = '1' WHERE `username`  = '"+searchBar.getText()+"'";
                        st.execute(query);
                        st.close();
                        con.close();
                        JOptionPane.showMessageDialog(this, "User Approved!",
                                "Successful", JOptionPane.YES_NO_CANCEL_OPTION);
                    } else {
                        JOptionPane.showMessageDialog(this, "Username not found!",
                                "Warning", JOptionPane.YES_NO_CANCEL_OPTION);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        else if (elementText.equals(refresh.getText())) {
            showUsers();
            searchBar.setText("");
        }
        else if (elementText.equals(delete.getText())) {
            if ((searchBar.getText().equals(""))) {
                JOptionPane.showMessageDialog(this, "No User Selected",
                        "Warning", JOptionPane.YES_NO_CANCEL_OPTION);
            }
            else{
                if (searchBar.getText().equals("admin")) {
                    JOptionPane.showMessageDialog(this, "YOU MAD BRO???",
                            "Warning", JOptionPane.YES_NO_CANCEL_OPTION);
                    return;
                }
                try {
                    String query  =  "select * from user where username='"+searchBar.getText()+"'";
                    Class.forName("com.mysql.jdbc.Driver");  // loading driver
                    System.out.println("driver loaded");
                    Connection con  =  DriverManager.getConnection("jdbc:mysql://localhost:3306/project","root","123456");
                    System.out.println("connection done");   // connection with database established
                    Statement st  =  con.createStatement();  // creates statement
                    System.out.println("statement created");
                    ResultSet rs  =  st.executeQuery(query); // getting result by executing query
                    System.out.println("results received");

                    if (rs.next()) {
                        query  =  "DELETE from user where username='"+searchBar.getText()+"'";
                        st.execute(query);
                        st.close();
                        con.close();
                        JOptionPane.showMessageDialog(this, "User Deleted!",
                                "Successful", JOptionPane.YES_NO_CANCEL_OPTION);
                    } else {
                        JOptionPane.showMessageDialog(this, "Username not found!",
                                "Warning", JOptionPane.YES_NO_CANCEL_OPTION);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}