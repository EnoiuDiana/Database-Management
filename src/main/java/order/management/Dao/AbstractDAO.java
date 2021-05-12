package order.management.Dao;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import order.management.Connection.ConnectionFactory;


/**
 * The abstract data access class that inserts, deletes or updates the data from the database
 * @param <T>
 */
public class AbstractDAO<T> {
    protected static final Logger LOGGER = Logger.getLogger(AbstractDAO.class.getName());

    private final Class<T> type;

    @SuppressWarnings("unchecked")
    public AbstractDAO() {
        this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    /**
     * Method that uses reflection techniques to create the objects received from the result set
     * @param resultSet
     * @deprecated
     * @return
     */
    private List<T> createObjects(ResultSet resultSet) {
        List<T> list = new ArrayList<T>();

        try {
            while (resultSet.next()) {
                T instance = type.newInstance();
                for (Field field : type.getDeclaredFields()) {
                    Object value = resultSet.getObject(field.getName());
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), type);
                    Method method = propertyDescriptor.getWriteMethod();
                    method.invoke(instance, value);
                }
                list.add(instance);
            }
        } catch (InstantiationException | IllegalAccessException | SecurityException | IllegalArgumentException |
                InvocationTargetException | SQLException | IntrospectionException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Generic method to create the select query
     * @param field
     * @return string query
     */
    private String createSelectQuery(String field) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" * ");
        sb.append(" FROM ");
        sb.append(type.getSimpleName());
        sb.append(" WHERE ").append(field).append(" =?");
        return sb.toString();
    }

    /**
     * Generic method to create the select all query
     * @return string query
     */
    private String createSelectAllQuery() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" * ");
        sb.append(" FROM ");
        sb.append(type.getSimpleName());
        return sb.toString();
    }

    /**
     * Generic method to create the insert query
     * @return string query
     */
    private String createInsertQuery(T object) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO ");
        sb.append(type.getSimpleName());
        sb.append(" (");
        boolean isId = true;
        for(Field field : object.getClass().getDeclaredFields()) {
            if(!isId) {
                sb.append(field.getName()).append(", ");
            } else {
                isId = false;
            }
        }
        sb.replace(sb.lastIndexOf(", "), sb.length(), ")");
        sb.append(" VALUES (");

        isId = true;
        for(Field ignored : object.getClass().getDeclaredFields()) {
            if(!isId) {
                sb.append("?").append(", ");
            } else {
                isId = false;
            }
        }
        sb.replace(sb.lastIndexOf(", "), sb.length(), ")");
        return sb.toString();
    }

    /**
     * Generic method to create the update query
     * @return string query
     */
    private String createUpdateQuery(T object) {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE ");
        sb.append(type.getSimpleName());
        sb.append(" SET ");
        boolean isId = true;
        Field idField = object.getClass().getDeclaredFields()[0];
        for(Field field : object.getClass().getDeclaredFields()) {
            if(!isId) {
                sb.append(field.getName()).append(" = ");
                sb.append("?").append(", ");
            } else {
                isId = false;
            }
        }
        sb.replace(sb.lastIndexOf(", "), sb.length(), "");
        sb.append(" WHERE ").append(idField.getName()).append(" =?");

        return sb.toString();
    }

    /**
     * Generic method to create the delete query
     * @return string query
     */
    private String createDeleteQuery(String idField) {
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM ");
        sb.append(type.getSimpleName());
        sb.append(" WHERE ");
        sb.append(idField).append(" =?");
        return sb.toString();
    }

    /**
     * Method to execute the find all query
     * @return the list of objects that were found
     */
    public List<T> findAll() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createSelectAllQuery();
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            return createObjects(resultSet);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findById " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }

        return null;
    }

    /**
     * Method to execute the find by id query
     * @return the list of objects that were found
     */
    public T findById(int id) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createSelectQuery("id");
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();
            return createObjects(resultSet).get(0);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findById " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    /**
     * Method to execute the insert query
     * @return
     */
    public T insert(T t) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createInsertQuery(t);
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS );
            int parameterIndex = 0;
            for(Field field : t.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                if(parameterIndex != 0) {
                    statement.setObject(parameterIndex, field.get(t));
                }
                parameterIndex++;
            }
            statement.executeUpdate();
            resultSet = statement.getGeneratedKeys();
            while (resultSet.next()) {
                java.math.BigDecimal idColVar = resultSet.getBigDecimal(1);
                // Get automatically generated key
                System.out.println("automatically generated key value = " + idColVar);
            }
            return t;
        } catch (SQLException | IllegalAccessException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findById " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    /**
     * Method to execute the update query
     * @return
     */
    public T update(T t) {
        Connection connection = null;
        PreparedStatement statement = null;
        String query = createUpdateQuery(t);
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            int parameterIndex = 0;
            Field idField = t.getClass().getDeclaredFields()[0];
            idField.setAccessible(true);
            for(Field field : t.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                if(parameterIndex != 0) { //jumps over the id field
                    statement.setObject(parameterIndex, field.get(t));
                }
                parameterIndex++;
            }
            statement.setObject(parameterIndex, idField.get(t)); // sets WHERE id = ?
            statement.executeUpdate();
            return t;
        } catch (SQLException | IllegalAccessException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findById " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    /**
     * Method to execute the delete query
     * @return
     */
    public T delete(int id) {
        Connection connection = null;
        PreparedStatement statement = null;
        String query = createDeleteQuery("id");
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setObject(1, id); // sets WHERE id = ?
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findById " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }
}

