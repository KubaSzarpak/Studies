package pjwstk.tpo.DbConnection;

import java.sql.*;

public class DbConnection {
    private Statement statement;

    public void connect(String connectionString, String user, String password) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://" + connectionString + "?user=" + user + "&password=" + password);

            statement = connection.createStatement();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ResultSet executeQuery(String queryString){
        try {
            ResultSet result = statement.executeQuery(queryString);
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
