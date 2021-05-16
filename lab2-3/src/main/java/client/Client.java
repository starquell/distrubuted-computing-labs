package client;

import DTO.Employee;
import DTO.Department;
import lombok.var;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Client {
    private final Socket socket;
    private final PrintWriter out;
    private final BufferedReader in;
    private static final String split = "&";

    public Client(String ip, int port) throws IOException {
        socket = new Socket(ip, port);
        in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
    }

    public Department departmentFindById(Long id) {
        var query = "DepartmentFindById" + split + id.toString();
        out.println(query);
        String response;
        try {
            response = in.readLine();
            return new Department(id, response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Employee employeeFindByName(String name) {
        var query = "EmployeeFindByName" + split + name;
        out.println(query);
        String response = "";
        try {
            response = in.readLine();
            String[] fields = response.split(split);
            var id = Long.parseLong(fields[0]);
            var departmentId = Long.parseLong(fields[1]);
            var velocity = Float.parseFloat(fields[3]);
            return new Employee(id, departmentId, name, velocity);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Department departmentFindByName(String name) {
        var query = "DepartmentFindByName" + split + name;
        out.println(query);
        try {
            var response = Long.parseLong(in.readLine());
            return new Department(response, name);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean employeeUpdate(Employee employee) {
        var query = "EmployeeUpdate" + split + employee.getId() +
                split + employee.getDepartmentId() + split + employee.getName()
                + split + employee.getVelocity();
        out.println(query);
        try {
            var response = in.readLine();
            return "true".equals(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean departmentUpdate(Department department) {
        var query = "DepartmentUpdate" + split + department.getId() +
                split + department.getName();
        out.println(query);
        try {
            var response = in.readLine();
            return "true".equals(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean employeeInsert(Employee employee) {
        var query = "EmployeeInsert" +
                split + employee.getDepartmentId() + split + employee.getName()
                + split + employee.getVelocity();
        out.println(query);
        try {
            var response = in.readLine();
            return "true".equals(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean departmentInsert(Department department) {
        var query = "DepartmentInsert" +
                split + department.getName();
        out.println(query);
        try {
            var response = in.readLine();
            return "true".equals(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean departmentDelete(Department department) {
        var query = "DepartmentDelete" + split + department.getId();
        out.println(query);
        try {
            var response = in.readLine();
            return "true".equals(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean employeeDelete(Employee employee) {
        var query = "EmployeeDelete" + split + employee.getId();
        out.println(query);
        try {
            var response = in.readLine();
            return "true".equals(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Department> departmentAll() {
        var query = "DepartmentAll";
        out.println(query);
        var list = new ArrayList<Department>();
        try {
            var response = in.readLine();
            String[] fields = response.split(split);
            for (int i = 0; i < fields.length; i += 2) {
                var id = Long.parseLong(fields[i]);
                var name = fields.length == i + 1 ? "" : fields[i + 1];
                list.add(new Department(id, name));
            }
            return list;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Employee> employeeAll() {
        var query = "EmployeeAll";
        return getEmployeeS(query);
    }

    private List<Employee> getEmployeeS(String query) {
        out.println(query);
        var list = new ArrayList<Employee>();
        try {
            var response = in.readLine();
            String[] fields = response.split(split);
            for (int i = 0; i < fields.length; i += 4) {
                var id = Long.parseLong(fields[i]);
                var departmentid = Long.parseLong(fields[i + 1]);
                var name = fields[i + 2];
                var velocity = Float.parseFloat(fields[i + 3]);
                list.add(new Employee(id, departmentid, name, velocity));
            }
            return list;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void disconnect() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
