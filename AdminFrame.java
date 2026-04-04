import javax.swing.*;
import java.awt.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class AdminFrame extends JFrame {

    public AdminFrame() {

        setTitle("Admin Dashboard");
        setSize(600,450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                new LoginFrame();
            }
        });

        setLayout(new GridLayout(2,1,20,20));

        JPanel facultyPanel = new JPanel(new GridLayout(2,1,10,10));
        facultyPanel.setBorder(BorderFactory.createTitledBorder("Faculty Management"));

        JButton addFaculty = new JButton("Add Faculty");
        JButton viewFaculty = new JButton("View Faculty");

        viewFaculty.setBackground(new Color(0,180,0));
        viewFaculty.setForeground(Color.WHITE);

        facultyPanel.add(addFaculty);
        facultyPanel.add(viewFaculty);

        JPanel studentPanel = new JPanel(new GridLayout(2,1,10,10));
        studentPanel.setBorder(BorderFactory.createTitledBorder("Student Management"));

        JButton addStudent = new JButton("Add Student");
        JButton viewStudent = new JButton("View Students");

        viewStudent.setBackground(new Color(0,180,0));
        viewStudent.setForeground(Color.WHITE);

        studentPanel.add(addStudent);
        studentPanel.add(viewStudent);

        add(facultyPanel);
        add(studentPanel);

        addFaculty.addActionListener(e -> addFaculty());
        addStudent.addActionListener(e -> addStudent());
        viewFaculty.addActionListener(e -> showFaculty());
        viewStudent.addActionListener(e -> chooseDeptYear());

        setLocationRelativeTo(null);
        setVisible(true);
    }

    // ADD STUDENT
    private void addStudent(){

        JTextField name = new JTextField();
        JTextField dept = new JTextField();
        JTextField year = new JTextField();
        JTextField username = new JTextField();
        JTextField password = new JTextField();

        Object[] fields = {
                "Student Name:",name,
                "Department:",dept,
                "Year:",year,
                "Username:",username,
                "Password:",password
        };

        int option = JOptionPane.showConfirmDialog(this,fields,"Add Student",JOptionPane.OK_CANCEL_OPTION);

        if(option==JOptionPane.OK_OPTION){

            try(Connection con = DBConnection.getConnection()){

                PreparedStatement ps = con.prepareStatement(
                "INSERT INTO STUDENT_TABLE (STUDENT_ID,NAME,DEPARTMENT,YEAR,USERNAME,PASSWORD) VALUES(?,?,?,?,?,?)");

                ps.setInt(1,(int)(Math.random()*10000));
                ps.setString(2,name.getText());
                ps.setString(3,dept.getText().toUpperCase());
                ps.setInt(4,Integer.parseInt(year.getText()));
                ps.setString(5,username.getText());
                ps.setString(6,password.getText());

                ps.executeUpdate();

                JOptionPane.showMessageDialog(this,"Student Added Successfully");

            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    // ADD FACULTY
    private void addFaculty(){

        JTextField name = new JTextField();

        JComboBox<String> dept = new JComboBox<>(new String[]{"CSE","ECE","IT","MECH"});
        JComboBox<String> subject = new JComboBox<>(new String[]{"DBMS","JAVA","OS","CN","AI","ML","Add New Subject"});

        JTextField newSubject = new JTextField();
        JTextField username = new JTextField();
        JTextField password = new JTextField();

        Object[] fields = {
                "Faculty Name:",name,
                "Department:",dept,
                "Subject:",subject,
                "New Subject (if needed):",newSubject,
                "Username:",username,
                "Password:",password
        };

        int option = JOptionPane.showConfirmDialog(this,fields,"Add Faculty",JOptionPane.OK_CANCEL_OPTION);

        if(option==JOptionPane.OK_OPTION){

            try(Connection con = DBConnection.getConnection()){

                String selectedSubject = subject.getSelectedItem().toString();

                if(selectedSubject.equals("Add New Subject")){
                    selectedSubject = newSubject.getText().toUpperCase();
                }

                PreparedStatement ps = con.prepareStatement(
                "INSERT INTO FACULTY_TABLE (FACULTY_ID,NAME,DEPARTMENT,SUBJECT,USERNAME,PASSWORD) VALUES(?,?,?,?,?,?)");

                ps.setInt(1,(int)(Math.random()*10000));
                ps.setString(2,name.getText());
                ps.setString(3,dept.getSelectedItem().toString());
                ps.setString(4,selectedSubject);
                ps.setString(5,username.getText());
                ps.setString(6,password.getText());

                ps.executeUpdate();

                JOptionPane.showMessageDialog(this,"Faculty Added Successfully");

            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    // VIEW FACULTY
    private void showFaculty(){

    JFrame frame = new JFrame("Faculty List");
    frame.setSize(850,420);
    frame.setLayout(new BorderLayout());

    String[] columns = {"ID","Name","Department","Subject","Username","Password"};
    DefaultTableModel model = new DefaultTableModel(columns,0);
    JTable table = new JTable(model);

    loadFacultyData(model,null);

    JPanel topPanel = new JPanel();

    String[] depts={"CSE","ECE","IT","MECH","ALL"};

    for(String d:depts){

        JButton btn=new JButton(d);

        btn.addActionListener(e->{

            if(d.equals("ALL"))
                loadFacultyData(model,null);
            else
                loadFacultyData(model,d);

        });

        topPanel.add(btn);
    }

    JButton editBtn = new JButton("Edit");
    JButton deleteBtn = new JButton("Delete");

    editBtn.setBackground(new Color(0,102,204));
    editBtn.setForeground(Color.WHITE);

    deleteBtn.setBackground(Color.RED);
    deleteBtn.setForeground(Color.WHITE);

    editBtn.addActionListener(e -> editFaculty(table));
    deleteBtn.addActionListener(e -> deleteFaculty(table,model));

    topPanel.add(editBtn);
    topPanel.add(deleteBtn);

    frame.add(topPanel,BorderLayout.NORTH);
    frame.add(new JScrollPane(table),BorderLayout.CENTER);

    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
}
private void loadFacultyData(DefaultTableModel model,String dept){

    model.setRowCount(0);

    try{

        Connection con=DBConnection.getConnection();

        PreparedStatement ps;

        if(dept==null)
            ps=con.prepareStatement("SELECT * FROM FACULTY_TABLE");
        else{
            ps=con.prepareStatement("SELECT * FROM FACULTY_TABLE WHERE DEPARTMENT=?");
            ps.setString(1,dept);
        }

        ResultSet rs=ps.executeQuery();

        while(rs.next()){

            model.addRow(new Object[]{
                    rs.getInt("faculty_id"),
                    rs.getString("name"),
                    rs.getString("department"),
                    rs.getString("subject"),
                    rs.getString("username"),
                    rs.getString("password")
            });
        }

    }catch(Exception e){
        e.printStackTrace();
    }
}
private void editFaculty(JTable table){

    int row = table.getSelectedRow();

    if(row==-1){
        JOptionPane.showMessageDialog(this,"Select Faculty First");
        return;
    }

    int id = (int)table.getValueAt(row,0);

    JTextField name = new JTextField(table.getValueAt(row,1).toString());
    JTextField dept = new JTextField(table.getValueAt(row,2).toString());
    JTextField subject = new JTextField(table.getValueAt(row,3).toString());
    JTextField username = new JTextField(table.getValueAt(row,4).toString());
    JTextField password = new JTextField(table.getValueAt(row,5).toString());

    Object[] fields = {
        "Name:",name,
        "Department:",dept,
        "Subject:",subject,
        "Username:",username,
        "Password:",password
    };

    int option = JOptionPane.showConfirmDialog(this,fields,"Edit Faculty",JOptionPane.OK_CANCEL_OPTION);

    if(option==JOptionPane.OK_OPTION){

        try(Connection con = DBConnection.getConnection()){

            PreparedStatement ps = con.prepareStatement(
            "UPDATE FACULTY_TABLE SET NAME=?,DEPARTMENT=?,SUBJECT=?,USERNAME=?,PASSWORD=? WHERE FACULTY_ID=?");

            ps.setString(1,name.getText());
            ps.setString(2,dept.getText());
            ps.setString(3,subject.getText());
            ps.setString(4,username.getText());
            ps.setString(5,password.getText());
            ps.setInt(6,id);

            ps.executeUpdate();

            JOptionPane.showMessageDialog(this,"Faculty Updated");

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
private void deleteFaculty(JTable table,DefaultTableModel model){

    int row = table.getSelectedRow();

    if(row==-1){
        JOptionPane.showMessageDialog(this,"Select Faculty First");
        return;
    }

    int id = (int)model.getValueAt(row,0);

    int confirm = JOptionPane.showConfirmDialog(this,"Delete this faculty?");

    if(confirm==JOptionPane.YES_OPTION){

        try(Connection con = DBConnection.getConnection()){

            PreparedStatement ps = con.prepareStatement(
            "DELETE FROM FACULTY_TABLE WHERE FACULTY_ID=?");

            ps.setInt(1,id);
            ps.executeUpdate();

            model.removeRow(row);

            JOptionPane.showMessageDialog(this,"Faculty Deleted");

        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
}                   

    // STEP 1 : CHOOSE DEPARTMENT AND YEAR
    private void chooseDeptYear(){

        JComboBox<String> deptBox = new JComboBox<>(new String[]{"CSE","ECE","IT","MECH"});
        JComboBox<String> yearBox = new JComboBox<>(new String[]{"1","2","3","4"});

        Object[] fields = {
                "Department:",deptBox,
                "Year:",yearBox
        };

        int option = JOptionPane.showConfirmDialog(this,fields,"Select Class",JOptionPane.OK_CANCEL_OPTION);

        if(option==JOptionPane.OK_OPTION){

            String dept = deptBox.getSelectedItem().toString();
            int year = Integer.parseInt(yearBox.getSelectedItem().toString());

            showStudents(dept,year);
        }
    }

    // STEP 2 : SHOW STUDENTS
    private void showStudents(String dept,int year){

        JFrame frame = new JFrame("Student List");
        frame.setSize(850,420);
        frame.setLayout(new BorderLayout());

        String[] columns={"ID","Name","Department","Year","Username","Password"};
        DefaultTableModel model=new DefaultTableModel(columns,0);
        JTable table=new JTable(model);

        try{

            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(
            "SELECT * FROM STUDENT_TABLE WHERE DEPARTMENT=? AND YEAR=?");

            ps.setString(1,dept);
            ps.setInt(2,year);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){

                model.addRow(new Object[]{
                        rs.getInt("student_id"),
                        rs.getString("name"),
                        rs.getString("department"),
                        rs.getInt("year"),
                        rs.getString("username"),
                        rs.getString("password")
                });
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        JPanel topPanel = new JPanel();

        JButton editBtn = new JButton("Edit");
        JButton deleteBtn = new JButton("Delete");

        editBtn.setBackground(new Color(0,102,204));
        editBtn.setForeground(Color.WHITE);

        deleteBtn.setBackground(Color.RED);
        deleteBtn.setForeground(Color.WHITE);

        topPanel.add(editBtn);
        topPanel.add(deleteBtn);

        frame.add(topPanel,BorderLayout.NORTH);
        frame.add(new JScrollPane(table),BorderLayout.CENTER);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
