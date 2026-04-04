import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class LoginFrame extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JComboBox<String> roleBox;

    public LoginFrame() {

        setTitle("Student Result Management System");
        setSize(420,280);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(4,2,10,10));

        roleBox = new JComboBox<>(new String[]{"Admin","Faculty","Student"});
        txtUsername = new JTextField();
        txtPassword = new JPasswordField();

        JButton btnLogin = new JButton("Login");
        btnLogin.setBackground(new Color(0,150,0));
        btnLogin.setForeground(Color.WHITE);

        add(new JLabel("Select Role"));
        add(roleBox);

        add(new JLabel("Username"));
        add(txtUsername);

        add(new JLabel("Password"));
        add(txtPassword);

        add(new JLabel(""));
        add(btnLogin);

        btnLogin.addActionListener(e -> loginUser());

        setVisible(true);
    }

    private void loginUser() {

        String username = txtUsername.getText().trim();
        String password = String.valueOf(txtPassword.getPassword()).trim();
        String role = roleBox.getSelectedItem().toString();

        try {

            Connection con = DBConnection.getConnection();

            String query;

            if(role.equals("Admin"))
                query = "SELECT * FROM ADMIN_TABLE WHERE USERNAME=? AND PASSWORD=?";
            else if(role.equals("Faculty"))
                query = "SELECT * FROM FACULTY_TABLE WHERE USERNAME=? AND PASSWORD=?";
            else
                query = "SELECT * FROM STUDENT_TABLE WHERE USERNAME=? AND PASSWORD=?";

            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if(rs.next()){

                JOptionPane.showMessageDialog(this,"Login Successful");

                if(role.equals("Admin")){
                    new AdminFrame();
                }
                else if(role.equals("Faculty")){

                     String dept = rs.getString("DEPARTMENT");
                     String subject = rs.getString("SUBJECT");

                    new FacultyFrame(dept,subject);
                }
                else{
                    new StudentFrame();
                }

                dispose();
            }
            else{
                JOptionPane.showMessageDialog(this,"Invalid Credentials");
            }

            con.close();

        }
        catch(Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,"Database Error");
        }
    }
}

