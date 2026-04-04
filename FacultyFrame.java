import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class FacultyFrame extends JFrame {

    JTable table;
    DefaultTableModel model;

    String department;
    String subject;

    JComboBox<String> examBox;
    JTextField semesterField;

    public FacultyFrame(String dept,String sub) {

        this.department = dept;
        this.subject = sub;

        setTitle("Faculty Dashboard");
        setSize(900,500);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // ⭐ THIS PART OPENS LOGIN PAGE WHEN WINDOW IS CLOSED
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                new LoginFrame();
            }
        });

        JLabel title = new JLabel(
        "Enter Marks  |  Department: "+department+"  |  Subject: "+subject,
        JLabel.CENTER);

        title.setFont(new Font("Arial",Font.BOLD,20));
        add(title,BorderLayout.NORTH);

        model = new DefaultTableModel(
        new String[]{"Roll No","Name","Subject","Marks"},0){

            public boolean isCellEditable(int row,int col){
                return col==3;
            }
        };

        table = new JTable(model);
        table.setRowHeight(28);

        add(new JScrollPane(table),BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();

        controlPanel.add(new JLabel("Exam"));

        String exams[]={"MID1","MID2","INTERNAL","EXTERNAL","SEMESTER"};
        examBox = new JComboBox<>(exams);
        controlPanel.add(examBox);

        controlPanel.add(new JLabel("Semester"));
        semesterField = new JTextField(5);
        controlPanel.add(semesterField);

        JButton loadBtn = new JButton("Load Students");
        JButton submitBtn = new JButton("Submit All Marks");
        JButton updateBtn = new JButton("Update Marks");
        JButton deleteBtn = new JButton("Delete Marks");
        JButton logoutBtn = new JButton("Logout");

        loadBtn.setBackground(new Color(0,120,255));
        loadBtn.setForeground(Color.WHITE);

        submitBtn.setBackground(new Color(0,150,0));
        submitBtn.setForeground(Color.WHITE);

        updateBtn.setBackground(new Color(0,102,204));
        updateBtn.setForeground(Color.WHITE);

        deleteBtn.setBackground(Color.RED);
        deleteBtn.setForeground(Color.WHITE);

        controlPanel.add(loadBtn);
        controlPanel.add(updateBtn);
        controlPanel.add(deleteBtn);
        controlPanel.add(submitBtn);
        controlPanel.add(logoutBtn);

        add(controlPanel,BorderLayout.SOUTH);

        loadBtn.addActionListener(e->loadStudents());
        submitBtn.addActionListener(e->saveMarks());
        updateBtn.addActionListener(e->updateMarks());
        deleteBtn.addActionListener(e->deleteMarks());

        logoutBtn.addActionListener(e->{
            dispose();
            new LoginFrame();
        });

        setVisible(true);
    }

    // LOAD ONLY DEPARTMENT STUDENTS
    void loadStudents(){

        model.setRowCount(0);

        try{

            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(
            "SELECT STUDENT_ID,NAME FROM STUDENT_TABLE WHERE DEPARTMENT=?");

            ps.setString(1,department);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){

                model.addRow(new Object[]{
                        rs.getInt("STUDENT_ID"),
                        rs.getString("NAME"),
                        subject,
                        ""
                });
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    // SAVE MARKS USING BATCH
   void saveMarks(){

    try{

        Connection con = DBConnection.getConnection();

        String exam = examBox.getSelectedItem().toString();
        int semester = Integer.parseInt(semesterField.getText());

        for(int i=0;i<table.getRowCount();i++){

            int studentId = (int)table.getValueAt(i,0);

            String markStr = table.getValueAt(i,3).toString();

            if(markStr.isEmpty()){
                JOptionPane.showMessageDialog(this,"Enter marks for all students");
                return;
            }

            int marks = Integer.parseInt(markStr);

            if(marks<0 || marks>100){
                JOptionPane.showMessageDialog(this,"Marks must be between 0 and 100");
                return;
            }

            String status = (marks>=40) ? "PASS" : "FAIL";

            PreparedStatement check = con.prepareStatement(
            "SELECT * FROM MARKS_TABLE WHERE STUDENT_ID=? AND SUBJECT_NAME=?");

            check.setInt(1,studentId);
            check.setString(2,subject);

            ResultSet rs = check.executeQuery();

            if(rs.next()){

                PreparedStatement update = con.prepareStatement(
                "UPDATE MARKS_TABLE SET MARKS=?,SEMESTER=?,STATUS=?,EXAM_TYPE=? WHERE STUDENT_ID=? AND SUBJECT_NAME=?");

                update.setInt(1,marks);
                update.setInt(2,semester);
                update.setString(3,status);
                update.setString(4,exam);
                update.setInt(5,studentId);
                update.setString(6,subject);

                update.executeUpdate();

            }
            else{

                PreparedStatement insert = con.prepareStatement(
                "INSERT INTO MARKS_TABLE (MARK_ID,STUDENT_ID,SUBJECT_NAME,MARKS,SEMESTER,STATUS,EXAM_TYPE,DEPARTMENT) VALUES(?,?,?,?,?,?,?,?)");
                insert.setInt(1,(int)(Math.random()*100000));
                insert.setInt(2,studentId);
                insert.setString(3,subject);
                insert.setInt(4,marks);
                insert.setInt(5,semester);
                insert.setString(6,status);
                insert.setString(7,exam);
                insert.setString(8,department);

                insert.executeUpdate();
            }

        }

        JOptionPane.showMessageDialog(this,"Marks Saved Successfully");

    }
    catch(Exception e){
        e.printStackTrace();
    }
}
             

    // UPDATE MARK
    
        void updateMarks(){

    int row = table.getSelectedRow();

    if(row==-1){
        JOptionPane.showMessageDialog(this,"Select row first");
        return;
    }

    try{

        Connection con = DBConnection.getConnection();

        int studentId = (int)table.getValueAt(row,0);

        String markStr = table.getValueAt(row,3).toString().trim();

        if(markStr.isEmpty()){
            JOptionPane.showMessageDialog(this,"Enter marks first");
            return;
        }

        int marks = Integer.parseInt(markStr);

        PreparedStatement ps = con.prepareStatement(
        "UPDATE MARKS_TABLE SET MARKS=? WHERE STUDENT_ID=? AND SUBJECT_NAME=?");

        ps.setInt(1,marks);
        ps.setInt(2,studentId);
        ps.setString(3,subject);

        int updated = ps.executeUpdate();

        if(updated==0){
            JOptionPane.showMessageDialog(this,"Marks not inserted yet. Use Submit All Marks first.");
        }
        else{
            JOptionPane.showMessageDialog(this,"Marks Updated Successfully");
        }

    }
    catch(Exception e){
        e.printStackTrace();
    }
}      
    

    // DELETE MARK
   void deleteMarks(){

    int row = table.getSelectedRow();

    if(row==-1){
        JOptionPane.showMessageDialog(this,"Select row first");
        return;
    }

    try{

        Connection con = DBConnection.getConnection();

        int studentId = (int)table.getValueAt(row,0);

        PreparedStatement ps = con.prepareStatement(
        "DELETE FROM MARKS_TABLE WHERE STUDENT_ID=? AND SUBJECT_NAME=?");

        ps.setInt(1,studentId);
        ps.setString(2,subject);

        ps.executeUpdate();

        table.setValueAt("",row,3);

        JOptionPane.showMessageDialog(this,"Marks Deleted");

    }
    catch(Exception e){
        e.printStackTrace();
    }
}
}
            
    