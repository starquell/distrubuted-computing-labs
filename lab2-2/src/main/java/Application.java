import lombok.var;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Application extends JFrame {
    private static JFrame frame;

    private static Department currentDepartment = null;
    private static Employee currentEmployee = null;

    private static boolean editMode = false;
    private static boolean departmentMode = true;

    private static JButton btnAddDepartment = new JButton("Add Department");
    private static JButton btnAddEmployee = new JButton("Add Employee");
    private static JButton btnEdit = new JButton("Edit Data");
    private static JButton btnBack = new JButton("Back");
    private static JButton btnSave = new JButton("Save");
    private static JButton btnDelete = new JButton("Delete");

    private static Box menuPanel = Box.createVerticalBox();
    private static Box actionPanel = Box.createVerticalBox();
    private static Box comboPanel = Box.createVerticalBox();
    private static Box cityPanel = Box.createVerticalBox();
    private static Box departmentPanel = Box.createVerticalBox();

    private static JComboBox comboDepartment = new JComboBox();
    private static JComboBox comboEmployee = new JComboBox();

    private static JTextField textDepartmentName = new JTextField(30);
    private static JTextField textEmployeeName = new JTextField(30);
    private static JTextField textEmployeeDepartmentName = new JTextField(30);
    private static JTextField textEmployeeVelocity = new JTextField(30);

    private Application() {
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
                DBConnection.closeConnection();
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
                cityPanel.setVisible(false);
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
                cityPanel.setVisible(true);
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
                cityPanel.setVisible(false);
                actionPanel.setVisible(true);
                pack();
            }
        });

        // ComboBoxes
        comboPanel.add(new JLabel("Department:"));
        comboPanel.add(comboDepartment);
        comboPanel.add(Box.createVerticalStrut(20));
        comboDepartment.addActionListener(e -> {
            String name = (String) comboDepartment.getSelectedItem();
            currentDepartment = DepartmentDAO.findByName((String) comboDepartment.getSelectedItem());
            departmentMode = true;
            departmentPanel.setVisible(true);
            cityPanel.setVisible(false);
            fillDepartmentFields();
            pack();
        });
        comboPanel.add(new JLabel("Employee:"));
        comboPanel.add(comboEmployee);
        comboPanel.add(Box.createVerticalStrut(20));
        comboEmployee.addActionListener(e -> {
            String name = (String) comboEmployee.getSelectedItem();
            currentEmployee = EmployeeDAO.findByName((String) comboEmployee.getSelectedItem());
            departmentMode = false;
            departmentPanel.setVisible(false);
            cityPanel.setVisible(true);
            fillEmployeeFields();
            pack();
        });
        fillComboBoxes();
        comboPanel.setVisible(false);

        // Employee Fields
        cityPanel.add(new JLabel("Name:"));
        cityPanel.add(textEmployeeName);
        cityPanel.add(Box.createVerticalStrut(20));
        cityPanel.add(new JLabel("Department Id:"));
        cityPanel.add(textEmployeeDepartmentName);
        cityPanel.add(Box.createVerticalStrut(20));
        cityPanel.add(new JLabel("Velocity:"));
        cityPanel.add(textEmployeeVelocity);
        cityPanel.add(Box.createVerticalStrut(20));
        cityPanel.setVisible(false);

        // Department Fields
        departmentPanel.add(new JLabel("Name:"));
        departmentPanel.add(textDepartmentName);
        departmentPanel.add(Box.createVerticalStrut(20));
        departmentPanel.setVisible(false);

        // Action Bar		
        actionPanel.add(btnSave);
        actionPanel.add(Box.createVerticalStrut(20));
        btnSave.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event) {
                save();
            }
        });
        actionPanel.add(btnDelete);
        actionPanel.add(Box.createVerticalStrut(20));
        btnDelete.addMouseListener(new MouseAdapter() {
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
                cityPanel.setVisible(false);
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
        box.add(cityPanel);
        box.add(actionPanel);
        setContentPane(box);
        //pack();
    }

    private static void sizeAllElements() {
        Dimension dimension = new Dimension(300, 50);
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
        cityPanel.setPreferredSize(panelDimension);
        departmentPanel.setPreferredSize(panelDimension);

        comboDepartment.setPreferredSize(dimension);
        comboEmployee.setPreferredSize(dimension);

        textEmployeeDepartmentName.setPreferredSize(dimension);
        textEmployeeName.setPreferredSize(dimension);
        textEmployeeVelocity.setPreferredSize(dimension);
        textDepartmentName.setPreferredSize(dimension);
    }

    private static void save() {
        if (editMode) {
            if (departmentMode) {
                currentDepartment.setName(textDepartmentName.getText());
                if (!DepartmentDAO.update(currentDepartment)) {
                    JOptionPane.showMessageDialog(null, "Error: update failed!");
                }
            } else {
                currentEmployee.setName(textEmployeeName.getText());
                currentEmployee.setVelocity(Float.parseFloat(textEmployeeVelocity.getText()));

                Department department = DepartmentDAO.findByName(textEmployeeDepartmentName.getText());
                if (department == null) {
                    JOptionPane.showMessageDialog(null, "Error: no such department!");
                    return;
                }
                currentEmployee.setDepartmentId(department.getId());

                if (!EmployeeDAO.update(currentEmployee)) {
                    JOptionPane.showMessageDialog(null, "Error: update failed!");
                }
            }
        } else {
            if (departmentMode) {
                Department department = new Department();
                department.setName(textDepartmentName.getText());

                if (!DepartmentDAO.insert(department)) {
                    JOptionPane.showMessageDialog(null, "Error: insertion failed!");
                    return;
                }

                comboDepartment.addItem(department.getName());
            } else {
                Employee employee = new Employee();
                employee.setName(textEmployeeName.getText());
                employee.setVelocity(Float.parseFloat(textEmployeeVelocity.getText()));

                Department department = DepartmentDAO.findByName(textEmployeeDepartmentName.getText());
                if (department == null) {
                    JOptionPane.showMessageDialog(null, "Error: no such department!");
                    return;
                }
                employee.setDepartmentId(department.getId());

                if (!EmployeeDAO.insert(employee)) {
                    JOptionPane.showMessageDialog(null, "Error: insertion failed!");
                    return;
                }

                comboEmployee.addItem(employee.getName());
            }
        }
    }

    private static void delete() {
        if (editMode) {
            if (departmentMode) {
                DepartmentDAO.delete(currentDepartment);
                comboDepartment.removeItem(currentDepartment.getName());

            } else {
                EmployeeDAO.delete(currentEmployee);
                comboEmployee.removeItem(currentEmployee.getName());
            }
        }
    }

    private void fillComboBoxes() {
        comboDepartment.removeAllItems();
        comboEmployee.removeAllItems();
        var departments = DepartmentDAO.findAll();
        var employees = EmployeeDAO.findAll();
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

    private static void fillEmployeeFields() {
        if (currentEmployee == null) {
            return;
        }
        Department department = DepartmentDAO.findById(currentEmployee.getDepartmentId());
        assert department != null;
        textEmployeeDepartmentName.setText(department.getName());
        textEmployeeName.setText(currentEmployee.getName());
        textEmployeeVelocity.setText(String.valueOf(currentEmployee.getVelocity()));
    }

    public static void main(String[] args) {
        JFrame myWindow = new Application();
        myWindow.setVisible(true);
    }
}
