package server;

import lombok.var;
import server.DBConnection;

import DTO.Employee;
import DTO.Department;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDAO {
    public static Department findById(long id) {
        try (Connection connection = DBConnection.getConnection()) {
            String sql = "SELECT * FROM departments WHERE id = ?";
            assert connection != null;
            var preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            var resultSet = preparedStatement.executeQuery();
            Department department = null;
            if (resultSet.next()) {
                department = new Department();
                department.setId(resultSet.getLong(1));
                department.setName(resultSet.getString(2));
            }
            preparedStatement.close();
            return department;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Department findByName(String name) {
        try (Connection connection = DBConnection.getConnection()) {
            String sql = "SELECT * FROM departments WHERE name = ?";

            assert connection != null;
            var preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            var resultSet = preparedStatement.executeQuery();
            Department department = null;
            if (resultSet.next()) {
                department = new Department();
                department.setId(resultSet.getLong(1));
                department.setName(resultSet.getString(2));
            }
            preparedStatement.close();
            return department;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean update(Department department) {
        try (Connection connection = DBConnection.getConnection()) {
            String sql =
                    "UPDATE departments "
                            + "SET name = ? "
                            + "WHERE id = ?";
            assert connection != null;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, department.getName());
            preparedStatement.setLong(2, department.getId());
            var result = preparedStatement.executeUpdate();
            preparedStatement.close();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean insert(Department department) {
        try (Connection connection = DBConnection.getConnection()) {
            String sql =
                    "INSERT INTO departments (name) "
                            + "VALUES (?) "
                            + "RETURNING id";
            assert connection != null;
            var preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, department.getName());
            var resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                department.setId(resultSet.getLong(1));
            } else
                return false;
            preparedStatement.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean delete(Department department) {
        try (Connection connection = DBConnection.getConnection()) {
            String sql = "DELETE FROM departments WHERE id = ?";
            assert connection != null;
            var preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, department.getId());
            var result = preparedStatement.executeUpdate();
            preparedStatement.close();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<Department> findAll() {
        try (Connection connection = DBConnection.getConnection()) {
            String sql = "SELECT * FROM departments";
            assert connection != null;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Department> list = new ArrayList<>();
            while (resultSet.next()) {
                Department department = new Department();
                department.setId(resultSet.getLong(1));
                department.setName(resultSet.getString(2));
                list.add(department);
            }
            preparedStatement.close();
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
