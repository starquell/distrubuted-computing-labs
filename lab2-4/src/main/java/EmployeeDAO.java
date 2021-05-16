import DTO.Employee;
import lombok.var;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {
    public static Employee findById(long id) {
        try (Connection connection = DBConnection.getConnection()) {
            String sql =
                    "SELECT * "
                  + "FROM employees "
                  + "WHERE id = ?";
            assert connection != null;
            var preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            var resultSet = preparedStatement.executeQuery();
            Employee employee = null;
            if (resultSet.next()) {
                employee = new Employee();
                employee.setId(resultSet.getLong(1));
                employee.setName(resultSet.getString(2));
                employee.setDepartmentId(resultSet.getLong(3));
                employee.setVelocity(resultSet.getFloat(4));
            }
            preparedStatement.close();
            return employee;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Employee findByName(String name) {
        try (Connection connection = DBConnection.getConnection();) {
            String sql =
                    "SELECT * "
                  + "FROM employees "
                  + "WHERE name = ?";
            assert connection != null;
            var preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            var resultSet = preparedStatement.executeQuery();
            Employee employee = null;
            if (resultSet.next()) {
                employee = new Employee();
                employee.setId(resultSet.getLong(1));
                employee.setName(resultSet.getString(2));
                employee.setDepartmentId(resultSet.getLong(3));
                employee.setVelocity(resultSet.getFloat(4));
            }
            preparedStatement.close();
            return employee;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean update(Employee employee) {
        try (Connection connection = DBConnection.getConnection();) {
            String sql =
                    "UPDATE employees "
                            + "SET name = ?, \"departmentId\" = ?, velocity = ? "
                            + "WHERE id = ?";
            assert connection != null;
            var preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, employee.getName());
            preparedStatement.setLong(2, employee.getDepartmentId());
            preparedStatement.setFloat(3, employee.getVelocity());
            preparedStatement.setLong(4, employee.getId());
            var result = preparedStatement.executeUpdate();
            preparedStatement.close();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean insert(Employee employee) {
        try (Connection connection = DBConnection.getConnection();) {
            String sql =
                    "INSERT INTO employees (\"name\", \"departmentId\", \"velocity\") "
                            + "VALUES (?,?,?) "
                            + "RETURNING id";
            assert connection != null;
            var preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, employee.getName());
            preparedStatement.setLong(2, employee.getDepartmentId());
            preparedStatement.setFloat(3, employee.getVelocity());
            var resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                employee.setId(resultSet.getLong(1));
            } else
                return false;
            preparedStatement.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean delete(Employee employee) {
        try (Connection connection = DBConnection.getConnection();) {
            String sql = "DELETE FROM employees WHERE id = ?";
            assert connection != null;
            var preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, employee.getId());
            var result = preparedStatement.executeUpdate();
            preparedStatement.close();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<Employee> findAll() {
        try (Connection connection = DBConnection.getConnection();) {
            String sql = "SELECT * FROM employees";
            assert connection != null;
            var preparedStatement = connection.prepareStatement(sql);
            var resultSet = preparedStatement.executeQuery();
            List<Employee> list = new ArrayList<>();
            while (resultSet.next()) {
                Employee employee = new Employee();
                employee.setId(resultSet.getLong(1));
                employee.setName(resultSet.getString(2));
                employee.setDepartmentId(resultSet.getLong(3));
                employee.setVelocity(resultSet.getFloat(4));
                list.add(employee);
            }
            preparedStatement.close();
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Employee> findByDepartmentId(Long id) {
        try (Connection connection = DBConnection.getConnection();) {
            String sql =
                    "SELECT * "
                  + "FROM employees "
                  + "WHERE departmentId = ?";
            assert connection != null;
            var preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            var resultSet = preparedStatement.executeQuery();
            List<Employee> list = new ArrayList<>();
            while (resultSet.next()) {
                var employee = new Employee();
                employee.setId(resultSet.getLong(1));
                employee.setName(resultSet.getString(2));
                employee.setDepartmentId(resultSet.getLong(3));
                employee.setVelocity(resultSet.getFloat(4));
                list.add(employee);
            }
            preparedStatement.close();
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
