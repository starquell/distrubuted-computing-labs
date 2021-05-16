import DTO.Employee;
import DTO.Department;
import lombok.var;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class Backend extends UnicastRemoteObject implements IBackend {

    protected Backend() throws RemoteException {
        super();
    }

    @Override
    public Department departmentFindById(Long id) throws RemoteException {
        return DepartmentDAO.findById(id);
    }

    @Override
    public Employee employeeFindByName(String name) throws RemoteException {
        return EmployeeDAO.findByName(name);
    }

    @Override
    public Department departmentFindByName(String name) throws RemoteException {
        return DepartmentDAO.findByName(name);
    }

    @Override
    public boolean employeeUpdate(Employee employee) throws RemoteException {
        return EmployeeDAO.update(employee);
    }

    @Override
    public boolean departmentUpdate(Department department) throws RemoteException {
        return DepartmentDAO.update(department);
    }

    @Override
    public boolean employeeInsert(Employee employee) throws RemoteException {
        return EmployeeDAO.insert(employee);
    }

    @Override
    public boolean departmentInsert(Department department) throws RemoteException {
        return DepartmentDAO.insert(department);
    }

    @Override
    public boolean departmentDelete(Department department) throws RemoteException {
        return DepartmentDAO.delete(department);
    }

    @Override
    public boolean employeeDelete(Employee employee) throws RemoteException {
        return EmployeeDAO.delete(employee);
    }

    @Override
    public List<Department> departmentAll() throws RemoteException {
        return DepartmentDAO.findAll();
    }

    @Override
    public List<Employee> employeeAll() throws RemoteException {
        return EmployeeDAO.findAll();
    }

    @Override
    public List<Employee> employeeFindByDepartmentId(Long departmentId) throws RemoteException {
        return EmployeeDAO.findByDepartmentId(departmentId);
    }

    public static void main(String[] args) throws RemoteException {
        var backend = new Backend();
        Registry r = LocateRegistry.createRegistry(8085);
        r.rebind("hr", backend);
    }
}
