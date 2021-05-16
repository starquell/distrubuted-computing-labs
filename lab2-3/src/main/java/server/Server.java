package server;

import DTO.Employee;
import DTO.Department;
import lombok.var;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class Server {
    private PrintWriter out = null;
    private BufferedReader in = null;
    private static final String split = "&";

    public void start(int port) throws IOException {
        var server = new ServerSocket(port);
        Socket socket = null;
        while (true) {
            socket = server.accept();
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            while (processQuery()) ;
        }
    }

    private boolean processQuery() {
        String response;
        try {
            String query = in.readLine();
            if (query == null) {
                return false;
            }

            String[] fields = query.split(split);
            if (fields.length == 0) {
                return true;
            } else {
                var action = fields[0];
                Department department;
                Employee employee;

                switch (action) {
                    case "DepartmentFindById":
                        var id = Long.parseLong(fields[1]);
                        department = DepartmentDAO.findById(id);
                        response = department.getName();
                        out.println(response);
                        break;
                    case "EmployeeFindByDepartmentId":
                        id = Long.parseLong(fields[1]);
                        var list = EmployeeDAO.findByDepartmentId(id);
                        var str = new StringBuilder();
                        assert list != null;
                        for (Employee currEmployee : list) {
                            str.append(currEmployee.getId());
                            str.append(split);
                            str.append(currEmployee.getDepartmentId());
                            str.append(split);
                            str.append(currEmployee.getName());
                            str.append(split);
                            str.append(currEmployee.getVelocity());
                            str.append(split);
                        }
                        response = str.toString();
                        out.println(response);
                        break;
                    case "EmployeeFindByName":
                        var name = fields[1];
                        employee = EmployeeDAO.findByName(name);
                        if (employee != null) {
                            response = employee.getId() + split + employee.getDepartmentId() + split + employee.getName() + split + employee.getVelocity();
                            out.println(response);
                        } else {
                            out.println(-1);
                        }
                        break;
                    case "DepartmentFindByName":
                        name = fields[1];
                        department = DepartmentDAO.findByName(name);
                        if (department != null) {
                            response = department.getId() + "";
                            out.println(response);
                        }
                        else {
                            out.println(-1);
                        }
                        break;
                    case "EmployeeUpdate":
                        id = Long.parseLong(fields[1]);
                        var departmentId = Long.parseLong(fields[2]);
                        name = fields[3];
                        var velocity = Float.parseFloat(fields[4]);
                        employee = new Employee(id, departmentId, name, velocity);
                        response = EmployeeDAO.insert(employee) ? "true" : "false";
                        out.println(response);
                        break;
                    case "DepartmentUpdate":
                        id = Long.parseLong(fields[1]);
                        name = fields[2];
                        department = new Department(id, name);
                        response = DepartmentDAO.update(department) ? "true" : "false";
                        out.println(response);
                        break;
                    case "EmployeeInsert":
                        departmentId = Long.parseLong(fields[1]);
                        name = fields[2];
                        velocity = Float.parseFloat(fields[3]);
                        employee = new Employee(0, departmentId, name, velocity);

                        response = EmployeeDAO.insert(employee) ? "true" : "false";
                        out.println(response);
                        break;
                    case "DepartmentInsert":
                        name = fields[1];
                        department = new Department();
                        department.setName(name);

                        response = DepartmentDAO.insert(department) ? "true" : "false";
                        out.println(response);
                        break;
                    case "EmployeeDelete":
                        id = Long.parseLong(fields[1]);
                        employee = new Employee();
                        employee.setId(id);
                        if (EmployeeDAO.delete(employee)) {
                            response = "true";
                        } else {
                            response = "false";
                        }
                        out.println(response);
                        break;
                    case "DepartmentDelete":
                        id = Long.parseLong(fields[1]);
                        department = new Department();
                        department.setId(id);
                        if (DepartmentDAO.delete(department)) {
                            response = "true";
                        } else {
                            response = "false";
                        }
                        out.println(response);
                        break;
                    case "EmployeeAll":
                        var employees = EmployeeDAO.findAll();
                        str = new StringBuilder();
                        assert employees != null;
                        for (var currEmployee : employees) {
                            str.append(currEmployee.getId());
                            str.append(split);
                            str.append(currEmployee.getDepartmentId());
                            str.append(split);
                            str.append(currEmployee.getName());
                            str.append(split);
                            str.append(currEmployee.getVelocity());
                            str.append(split);
                        }
                        response = str.toString();
                        out.println(response);
                        break;
                    case "DepartmentAll":
                        var departments = DepartmentDAO.findAll();
                        str = new StringBuilder();
                        for (var currDepartment : departments) {
                            str.append(currDepartment.getId());
                            str.append(split);
                            str.append(currDepartment.getName());
                            str.append(split);
                        }
                        response = str.toString();
                        out.println(response);
                        break;
                }
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static void main(String[] args) {
        try {
            Server server = new Server();
            server.start(8082);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
