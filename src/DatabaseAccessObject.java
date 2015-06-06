import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by Thomas on 6-6-2015.
 */
public class DatabaseAccessObject {

    private String url = "";
    private String dbName = "";
    private String driver = "com.mysql.jdbc.Driver";
    private String userName = "";
    private String password = "";


    public Connection connect() {
        try {
            Class.forName(driver).newInstance();
            Connection connection;
            connection = DriverManager.getConnection(url + dbName, userName, password);
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
            return connection;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void disconnect(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(String sql, Connection connection) {
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.execute();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void commit(Connection connection) {
        try {
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
