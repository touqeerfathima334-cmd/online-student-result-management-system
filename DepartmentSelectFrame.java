import javax.swing.*;
import java.awt.*;

public class DepartmentSelectFrame extends JFrame {

    JComboBox<String> deptBox;

    public DepartmentSelectFrame() {

        setTitle("Select Department");
        setSize(300,200);
        setLayout(new GridLayout(3,1,10,10));
        setLocationRelativeTo(null);

        JLabel label = new JLabel("Select Department", SwingConstants.CENTER);
        add(label);

        String departments[] = {"CSE","ECE","EEE","MECH","CIVIL"};
        deptBox = new JComboBox<>(departments);
        add(deptBox);

        JButton btn = new JButton("Continue");

        btn.addActionListener(e -> {

            String dept = deptBox.getSelectedItem().toString();

            dispose();

            new FacultyFrame(dept, dept);   // ✅ FIXED (pass department)
        });

        add(btn);

        setVisible(true);
    }
}