import DTO.Department;
import DTO.Employee;

import lombok.SneakyThrows;
import lombok.var;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Application extends JFrame {
    private static JFrame frame;

    private static Department currentDepartment = null;
    private static Employee currentEmployee = null;

    private static boolean editMode = false;
    private static boolean departmentMode = true;

    private static JButton btnAddDepartment = new JButton("Add department");
    private static JButton btnAddEmployee = new JButton("Add employee");
    private static JButton btnEdit = new JButton("Edit data");
    private static JButton btnBack = new JButton("Back");
    private static JButton btnSave = new JButton("Save");
    private static JButton btnDelete = new JButton("Delete");

    private static Box menuPanel = Box.createVerticalBox();
    private static Box actionPanel = Box.createVerticalBox();
    private static Box comboPanel = Box.createVerticalBox();
    private static Box employeePanel = Box.createVerticalBox();
    private static Box departmentPanel = Box.createVerticalBox();

    private static JComboBox comboDepartment = new JComboBox();
    private static JComboBox comboEmployee = new JComboBox();

    private static JTextField textDepartmentName = new JTextField(30);
    private static JTextField textEmployeeName = new JTextField(30);
    private static JTextField textEmployeeDepartmentName = new JTextField(30);
    private static JTextField textEmployeeVelocity = new JTextField(30);

    private static IBackend source;

    private Application() throws RemoteException {
        super("Human resources");
        frame = this;
        frame.setMinimumSize(new Dimension(400, 500));
        frame.setPreferredSize(new Dimension(400, 500));
        frame.setMaximumSize(new Dimension(400, 500));
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                frame.dispose();
                System.exit(0);
            }
        });
        Box box = Box.createVerticalBox();
        sizeAllElements();
        frame.setLayout(new FlowLayout());

        // Menu
        menuPanel.add(btnAddDepartment);
        menuPanel.add(Box.createVerticalStrut(20));
        btnAddDepartment.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event) {
                editMode = false;
                departmentMode = true;
                menuPanel.setVisible(false);
                comboPanel.setVisible(false);
                departmentPanel.setVisible(true);
                employeePanel.setVisible(false);
                actionPanel.setVisible(true);
                pack();
            }
        });
        menuPanel.add(btnAddEmployee);
        menuPanel.add(Box.createVerticalStrut(20));
        btnAddEmployee.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event) {
                editMode = false;
                departmentMode = false;
                menuPanel.setVisible(false);
                comboPanel.setVisible(false);
                departmentPanel.setVisible(false);
                employeePanel.setVisible(true);
                actionPanel.setVisible(true);
                pack();
            }
        });
        menuPanel.add(btnEdit);
        menuPanel.add(Box.createVerticalStrut(20));
        btnEdit.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event) {
                editMode = true;
                menuPanel.setVisible(false);
                comboPanel.setVisible(true);
                departmentPanel.setVisible(false);
                employeePanel.setVisible(false);
                actionPanel.setVisible(true);
                pack();
            }
        });

        // ComboBoxes
        comboPanel.add(new JLabel("Department:"));
        comboPanel.add(comboDepartment);
        comboPanel.add(Box.createVerticalStrut(20));
        comboDepartment.addActionListener(e -> {
            try {
                currentDepartment = source.departmentFindByName((String) comboDepartment.getSelectedItem());
            } catch (RemoteException remoteException) {
                remoteException.printStackTrace();
            }
            departmentMode = true;
            departmentPanel.setVisible(true);
            employeePanel.setVisible(false);
            fillDepartmentFields();
            pack();
        });
        comboPanel.add(new JLabel("Employee:"));
        comboPanel.add(comboEmployee);
        comboPanel.add(Box.createVerticalStrut(20));
        comboEmployee.addActionListener(e -> {
            try {
                currentEmployee = source.employeeFindByName((String) comboEmployee.getSelectedItem());
            } catch (RemoteException remoteException) {
                remoteException.printStackTrace();
            }

            departmentMode = false;
            departmentPanel.setVisible(false);
            employeePanel.setVisible(true);
            try {
                fillEmployeeFields();
            } catch (RemoteException remoteException) {
                remoteException.printStackTrace();
            }
            pack();
        });
        fillComboBoxes();
        comboPanel.setVisible(false);

        // DTO.Employee Fields
        employeePanel.add(new JLabel("Name:"));
        employeePanel.add(textEmployeeName);
        employeePanel.add(Box.createVerticalStrut(20));
        employeePanel.add(new JLabel("Department id:"));
        employeePanel.add(textEmployeeDepartmentName);
        employeePanel.add(Box.createVerticalStrut(20));
        employeePanel.add(new JLabel("Velocity:"));
        employeePanel.add(textEmployeeVelocity);
        employeePanel.add(Box.createVerticalStrut(20));
        employeePanel.setVisible(false);

        // DTO.Department Fields
        departmentPanel.add(new JLabel("Name:"));
        departmentPanel.add(textDepartmentName);
        departmentPanel.add(Box.createVerticalStrut(20));
        departmentPanel.setVisible(false);

        // Action Bar		
        actionPanel.add(btnSave);
        actionPanel.add(Box.createVerticalStrut(20));
        btnSave.addMouseListener(new MouseAdapter() {
            @SneakyThrows
            public void mouseClicked(MouseEvent event) {
                save();
            }
        });
        actionPanel.add(btnDelete);
        actionPanel.add(Box.createVerticalStrut(20));
        btnDelete.addMouseListener(new MouseAdapter() {
            @SneakyThrows
            public void mouseClicked(MouseEvent event) {
                delete();
            }
        });
        actionPanel.add(btnBack);
        actionPanel.add(Box.createVerticalStrut(20));
        btnBack.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event) {
                clearFields();
                menuPanel.setVisible(true);
                comboPanel.setVisible(false);
                departmentPanel.setVisible(false);
                employeePanel.setVisible(false);
                actionPanel.setVisible(false);
                pack();
            }
        });
        actionPanel.setVisible(false);

        clearFields();
        box.setPreferredSize(new Dimension(300, 500));
        box.add(menuPanel);
        box.add(comboPanel);
        box.add(departmentPanel);
        box.add(employeePanel);
        box.add(actionPanel);
        setContentPane(box);
        //pack();
    }

    private static void sizeAllElements() {
        var dimension = new Dimension(300, 50);
        btnAddDepartment.setMaximumSize(dimension);
        btnAddEmployee.setMaximumSize(dimension);
        btnEdit.setMaximumSize(dimension);
        btnBack.setMaximumSize(dimension);
        btnSave.setMaximumSize(dimension);
        btnDelete.setMaximumSize(dimension);

        btnAddDepartment.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnAddEmployee.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSave.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnBack.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnEdit.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnDelete.setAlignmentX(Component.CENTER_ALIGNMENT);

        Dimension panelDimension = new Dimension(300, 300);
        menuPanel.setMaximumSize(panelDimension);
        comboPanel.setPreferredSize(panelDimension);
        actionPanel.setPreferredSize(panelDimension);
        employeePanel.setPreferredSize(panelDimension);
        departmentPanel.setPreferredSize(panelDimension);

        comboDepartment.setPreferredSize(dimension);
        comboEmployee.setPreferredSize(dimension);

        textEmployeeDepartmentName.setPreferredSize(dimension);
        textEmployeeName.setPreferredSize(dimension);
        textEmployeeVelocity.setPreferredSize(dimension);
        textDepartmentName.setPreferredSize(dimension);
    }

    private void save() throws RemoteException {
        if (editMode) {
            if (departmentMode) {
                currentDepartment.setName(textDepartmentName.getText());
                if (!source.departmentUpdate(currentDepartment)) {
                    JOptionPane.showMessageDialog(null, "Error: update failed!");
                }
            } else {
                currentEmployee.setName(textEmployeeName.getText());
                currentEmployee.setVelocity(Float.parseFloat(textEmployeeVelocity.getText()));

                var department = source.departmentFindByName(textEmployeeDepartmentName.getText());
                if (department == null) {
                    JOptionPane.showMessageDialog(null, "Error: no such department!");
                    return;
                }
                currentEmployee.setDepartmentId(department.getId());

                if (!source.employeeUpdate(currentEmployee)) {
                    JOptionPane.showMessageDialog(null, "Error: update failed!");
                }
            }
        } else {
            if (departmentMode) {
                var department = new Department();
                department.setName(textDepartmentName.getText());

                if (!source.departmentInsert(department)) {
                    JOptionPane.showMessageDialog(null, "Error: insertion failed!");
                    return;
                }
                comboDepartment.addItem(department.getName());
            } else {
                var employee = new Employee();
                employee.setName(textEmployeeName.getText());
                employee.setVelocity(Float.parseFloat(textEmployeeVelocity.getText()));

                Department department = source.departmentFindByName(textEmployeeDepartmentName.getText());
                if (department == null) {
                    JOptionPane.showMessageDialog(null, "Error: no such department!");
                    return;
                }
                employee.setDepartmentId(department.getId());

                if (!source.employeeInsert(employee)) {
                    JOptionPane.showMessageDialog(null, "Error: insertion failed!");
                    return;
                }

                comboEmployee.addItem(employee.getName());
            }
        }
    }

    private static void delete() throws RemoteException {
        if (editMode) {
            if (departmentMode) {
                source.departmentDelete(currentDepartment);
                comboDepartment.removeItem(currentDepartment.getName());

            } else {
                source.employeeDelete(currentEmployee);
                comboEmployee.removeItem(currentEmployee.getName());
            }
        }
    }

    private void fillComboBoxes() throws RemoteException {
        comboDepartment.removeAllItems();
        comboEmployee.removeAllItems();
        var departments = source.departmentAll();
        var employees = source.employeeAll();
        for (var department : departments) {
            comboDepartment.addItem(department.getName());
        }
        for (var employee : employees) {
            comboEmployee.addItem(employee.getName());
        }
    }

    private static void clearFields() {
        textDepartmentName.setText("");
        textEmployeeName.setText("");
        textEmployeeDepartmentName.setText("");
        textEmployeeVelocity.setText("");
        currentDepartment = null;
        currentEmployee = null;
    }

    private static void fillDepartmentFields() {
        if (currentDepartment == null) {
            return;
        }
        textDepartmentName.setText(currentDepartment.getName());
    }

    private static void fillEmployeeFields() throws RemoteException {
        if (currentEmployee == null) {
            return;
        }
        Department department = source.departmentFindById(currentEmployee.getDepartmentId());
        assert department != null;
        textEmployeeDepartmentName.setText(department.getName());
        textEmployeeName.setText(currentEmployee.getName());
        textEmployeeVelocity.setText(String.valueOf(currentEmployee.getVelocity()));
    }

    public static void main(String[] args) throws IOException, NotBoundException {
        var url = "//localhost:8085/hr";
        source = (IBackend) Naming.lookup(url);
        JFrame myWindow = new Application();
        myWindow.setVisible(true);
    }
}
