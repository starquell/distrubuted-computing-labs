import DTO.Employee;
import DTO.Department;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IBackend extends Remote {
    public Department departmentFindById(Long id) throws RemoteException;

    public Employee employeeFindByName(String name) throws RemoteException;

    public Department departmentFindByName(String name) throws RemoteException;

    public boolean employeeUpdate(Employee employee) throws RemoteException;

    public boolean departmentUpdate(Department department) throws RemoteException;

    public boolean employeeInsert(Employee employee) throws RemoteException;

    public boolean departmentInsert(Department department) throws RemoteException;

    public boolean departmentDelete(Department department) throws RemoteException;

    public boolean employeeDelete(Employee employee) throws RemoteException;

    public List<Department> departmentAll() throws RemoteException;

    public List<Employee> employeeAll() throws RemoteException;

    public List<Employee> employeeFindByDepartmentId(Long idc) throws RemoteException;
}
