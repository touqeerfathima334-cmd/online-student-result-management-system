import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;

public class StudentFrame extends JFrame {

    JTextField rollField;
    JTable table;
    DefaultTableModel model;
    JLabel totalLabel, percentLabel, cgpaLabel;

    public StudentFrame() {

        setTitle("Student Result Portal");
        setSize(750,450);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                new LoginFrame();
            }
        });

        JLabel title = new JLabel("Student Result Management System", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        title.setOpaque(true);
        title.setBackground(new Color(0,102,204));
        title.setPreferredSize(new Dimension(750,60));

        add(title, BorderLayout.NORTH);

        JPanel searchPanel = new JPanel();
        searchPanel.setBackground(Color.WHITE);

        searchPanel.add(new JLabel("Enter Roll Number:"));

        rollField = new JTextField(10);
        searchPanel.add(rollField);

        JButton checkBtn = new JButton("Check Result");
        checkBtn.setBackground(new Color(0,150,0));
        checkBtn.setForeground(Color.WHITE);

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setBackground(Color.RED);
        logoutBtn.setForeground(Color.WHITE);

        searchPanel.add(checkBtn);
        searchPanel.add(logoutBtn);

        add(searchPanel, BorderLayout.SOUTH);

        model = new DefaultTableModel(
        new String[]{"Subject","Internal","External","Total","Grade","Credits","Status"},0);

        table = new JTable(model);
        table.setRowHeight(28);
        table.setFont(new Font("Arial",Font.PLAIN,14));

        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(0,102,204));
        header.setForeground(Color.WHITE);

        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel resultPanel = new JPanel(new GridLayout(3,1));
        resultPanel.setBorder(BorderFactory.createTitledBorder("Summary"));

        totalLabel = new JLabel("Total Marks : ");
        percentLabel = new JLabel("SGPA : ");
        cgpaLabel = new JLabel("CGPA : ");

        resultPanel.add(totalLabel);
        resultPanel.add(percentLabel);
        resultPanel.add(cgpaLabel);

        add(resultPanel, BorderLayout.EAST);

        checkBtn.addActionListener(e -> loadResults());

        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });

        setVisible(true);
    }

    void loadResults(){

        model.setRowCount(0);

        try{

            if(rollField.getText().trim().isEmpty()){
                JOptionPane.showMessageDialog(this,"Enter Roll Number");
                return;
            }

            Connection con = DBConnection.getConnection();

            // FIXED TABLE NAME
           String sql = "SELECT SUBJECT_NAME,MARKS,STATUS FROM MARKS_TABLE WHERE STUDENT_ID=?";

            PreparedStatement ps = con.prepareStatement(sql);

            int id = Integer.parseInt(rollField.getText());
            ps.setInt(1,id);

            ResultSet rs = ps.executeQuery();

            int totalMarks = 0;
            int subjectCount = 0;

            double totalGradePoints = 0;
            int totalCredits = 0;

           

                            while(rs.next()){

                int marks = rs.getInt("MARKS");
                String subject = rs.getString("SUBJECT_NAME");
                String status = rs.getString("STATUS");

                int internal = marks/2;
                int external = marks/2;
                int total = marks;

                String grade;

                if(total>=90) grade="A+";
                else if(total>=80) grade="A";
                else if(total>=70) grade="B";
                else if(total>=60) grade="C";
                else if(total>=50) grade="D";
                else grade="F";

                int credits = 4;

                totalMarks += total;
                subjectCount++;

                double gp;

                if(total>=90) gp=10;
                else if(total>=80) gp=9;
                else if(total>=70) gp=8;
                else if(total>=60) gp=7;
                else if(total>=50) gp=6;
                else gp=0;

                totalGradePoints += gp * credits;
                totalCredits += credits;

                model.addRow(new Object[]{
                        subject,
                        internal,
                        external,
                        total,
                        grade,
                        credits,
                        status
                });
            }

            if(subjectCount==0){
                JOptionPane.showMessageDialog(this,"No results found");
                return;
            }

            double sgpa = totalGradePoints / totalCredits;
            double cgpa = sgpa;

            totalLabel.setText("Total Marks : " + totalMarks);
            percentLabel.setText("SGPA : " + String.format("%.2f",sgpa));
            cgpaLabel.setText("CGPA : " + String.format("%.2f",cgpa));

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}